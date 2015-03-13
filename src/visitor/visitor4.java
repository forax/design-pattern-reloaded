package visitor;

import java.util.HashMap;
import java.util.function.Function;

public interface visitor4 {
  public interface Vehicle { /* empty */ }
  public class Car implements Vehicle { /* empty */}
  public class Moto implements Vehicle { /* empty */ }
  
  public class Visitor<R> {
    private final HashMap<Class<?>, Function<Object, ? extends R>> map = new HashMap<>();
    
    public <T> Visitor<R> when(Class<? extends T> type, Function<? super T, ? extends R> fun) {
      map.put(type, obj -> fun.apply(type.cast(obj)));
      return this;
    }
    public R call(Object receiver) {
      return map.getOrDefault(receiver.getClass(),
              obj -> { throw new IllegalArgumentException("invalid " + obj); })
          .apply(receiver);
    }
  }

  public static void main(String[] args) {
    Visitor<String> visitor = new Visitor<>();
    visitor.when(Car.class, car -> "car")
           .when(Moto.class, moto -> "moto");

    Vehicle vehicle = new Car();
    String text = visitor.call(vehicle); 
    System.out.println(text);
  }
}
