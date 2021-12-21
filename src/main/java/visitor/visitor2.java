package visitor;

import java.util.HashMap;
import java.util.function.Function;

public interface visitor2 {
  interface Vehicle { }
  record Car() implements Vehicle { }
  record Bus() implements Vehicle { }
  
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

  static void main(String[] args) {
    var visitor = new Visitor<String>();
    visitor.when(Bus.class, bus -> "bus")
           .when(Car.class, car -> "car");

    Vehicle vehicle = new Car();
    var text = visitor.call(vehicle);
    System.out.println(text);
  }
}
