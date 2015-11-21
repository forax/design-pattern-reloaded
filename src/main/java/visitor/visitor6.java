package visitor;

import java.util.HashMap;
import java.util.function.Function;

public interface visitor6 {
  public interface Vehicle { /* empty */ }
  public class Car implements Vehicle { /* empty */}
  public class Moto implements Vehicle { /* empty */ }

  public class Fruit { /* empty */ }
  
  public class Visitor<U, R> {
    private final HashMap<Class<? extends U>, Function<U, ? extends R>> map = new HashMap<>();
    
    public <T extends U> Visitor<U, R> when(Class<? extends T> type, Function<? super T, ? extends R> fun) {
      map.put(type, fun.compose(type::cast));
      return this;
    }
    public R call(U receiver) {
      return map.getOrDefault(receiver.getClass(),
              obj -> { throw new IllegalArgumentException("invalid " + obj); })
          .apply(receiver);
    }
  }

  public static void main(String[] args) {
    Visitor<Vehicle, String> visitor = new Visitor<>();
    visitor.when(Car.class, car -> "car")
           .when(Moto.class, moto -> "moto")
           .when(Fruit.class, fruit -> "fruit"); // doesn't compile :)

    Vehicle vehicle = new Car();
    String text = visitor.call(vehicle);
    visitor.call(new Fruit()); // doesn't compile :)
    System.out.println(text);
  }
}
