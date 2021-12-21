package builder;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.invoke.MethodType.methodType;

public interface builder2 {
  record Spaceship(String name, String captain, int torpedoes, int length) {}

  class Builder<T extends Record> {
    public interface Accessor<T, V> extends Serializable {
      V apply(T t);
    }

    @SuppressWarnings("unchecked")   // very wrong but works
    private static <T extends Throwable> AssertionError rethrow(Throwable cause) throws T {
      throw (T) cause;
    }

    private final Lookup lookup;
    private final Class<T> type;
    private final HashMap<String, Object> map = new HashMap<>();

    public Builder(Lookup lookup, Class<T> type) {
      this.lookup = lookup;
      this.type = type;
    }

    public <K> Builder<T> with(Accessor<T, K> accessor, K value) {
      SerializedLambda lambda;
      try {
        var mh = lookup.findVirtual(accessor.getClass(), "writeReplace", methodType(Object.class));
        lambda = (SerializedLambda) (Object) mh.invoke(accessor);
      } catch(NoSuchMethodException | IllegalAccessException e) {
        throw (LinkageError) new LinkageError().initCause(e);
      } catch (Throwable e) {
        throw rethrow(e);
      }
      map.put(lambda.getImplMethodName(), value);
      return this;
    }
    
    public T build() {
      var components = type.getRecordComponents();
      var array = new Object[components.length];
      for (var i = 0; i < components.length; i++) {
        var component = components[i];
        array[i] = map.computeIfAbsent(component.getName(), k -> { throw new IllegalStateException("no value '" + k + "' defined"); });
      }
      try {
        var constructor = lookup.findConstructor(type,
            methodType(void.class, Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new)));
        return type.cast(constructor.invokeWithArguments(array));
      } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
        throw (LinkageError) new LinkageError().initCause(e);
      } catch(Throwable e) {
        throw rethrow(e);
      }
    }
  }

  static void printSpaceship(Spaceship spaceship) {
    System.out.println(spaceship);
  }

  static void main(String[] args) {
    printSpaceship(
        new Builder<>(MethodHandles.lookup(), Spaceship.class)
            .with(Spaceship::name, "USS Enterprise")
            .with(Spaceship::captain, "Kirk")
            .with(Spaceship::torpedoes, 10_000)
            .with(Spaceship::length, 288_646)
            .build());
  }
}
