package curry;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface curry {
  enum Color { MAGENTA }
  interface Vehicle { }
  record Car(Color color, boolean chrome) implements Vehicle { }
  record Bus(Color color, boolean chrome) implements Vehicle { }

  static Vehicle createVehicle(String kind, Color color, boolean chrome) {
    return switch (kind) {
      case "car" -> new Car(color, chrome);
      case "bus" -> new Bus(color, chrome);
      default -> throw new IllegalArgumentException("unknown kind " + kind);
    };
  }

  static <T, U, V> Function<T, Function<U, V>> curry(BiFunction<? super T, ? super U, ? extends V> fun) {
    return t -> u -> fun.apply(t, u);
  }

  static void main(String[] args) {
    record Customization(Color color, boolean chrome) { }
    var curriedFactory = curry(
        (Customization customization, String kind) -> createVehicle(kind, customization.color, customization.chrome));
    var curriedFactory2 = curry(
        (Boolean chrome, Color color) -> curriedFactory.apply(new Customization(color, chrome)));
        
    var vehicle = curriedFactory2.apply(true).apply(Color.MAGENTA).apply("bus");
    System.out.println(vehicle);
  }
}
