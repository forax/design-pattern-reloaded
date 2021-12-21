package builder;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;

import static java.lang.invoke.MethodType.methodType;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

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

    private record HiddenClass(MethodType constructorType, Map<String, Integer> slotMap) { }

    private static final ClassValue<HiddenClass> HIDDEN_CLASS_VALUE =
        new ClassValue<>() {
          @Override
          protected HiddenClass computeValue(Class<?> type) {
            var components = type.getRecordComponents();
            var slotMap = range(0, components.length)
                .boxed()
                .collect(toMap(i -> components[i].getName(), i -> i));
            var  constructorType= methodType(void.class,
                Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new));
            return new HiddenClass(constructorType, slotMap);
          }
        };

    private final Lookup lookup;
    private final Class<T> type;
    private final HiddenClass hiddenClass;
    private final Object[] values;

    public Builder(Lookup lookup, Class<T> type) {
      this.lookup = lookup;
      this.type = type;
      var hiddenClass = HIDDEN_CLASS_VALUE.get(type);
      this.hiddenClass = hiddenClass;
      values = new Object[hiddenClass.slotMap.size()];
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
      var slot = hiddenClass.slotMap.get(lambda.getImplMethodName());
      if (slot == null) {
        throw new IllegalArgumentException("invalid method reference " + accessor);
      }
      values[slot] = value;
      return this;
    }
    
    public T build() {
      try {
        var constructor = lookup.findConstructor(type, hiddenClass.constructorType);
        return type.cast(constructor.invokeWithArguments(values));
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
