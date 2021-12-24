package visitor;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public interface visitor2 {
  /*not sealed*/ interface Vehicle { }
  record Car() implements Vehicle { }
  record CarHauler(List<Car> cars) implements Vehicle {}
  
  class Visitor<R> {
    private final HashMap<Class<?>, Function<Object, ? extends R>> map = new HashMap<>();
    
    public <T> Visitor<R> when(Class<? extends T> type, Function<? super T, ? extends R> fun) {
      map.put(type, fun.compose(type::cast));
      return this;
    }
    public R call(Object receiver) {
      var receiverClass = receiver.getClass();
      return map.computeIfAbsent(receiverClass, k -> { throw new IllegalArgumentException("invalid " + k.getName()); })
          .apply(receiver);
    }
  }

  static int count(Vehicle vehicle) {
    var visitor = new Visitor<Integer>();
    visitor.when(Car.class, car -> 1)
        .when(CarHauler.class, carHauler -> 1 + carHauler.cars().stream().mapToInt(visitor::call).sum());
    return visitor.call(vehicle);
  }

  static void main(String[] args) {
    var vehicle = new CarHauler(List.of(new Car(), new Car()));
    var count = count(vehicle);
    System.out.println(count);
  }
}
