package abstractfactory;

import java.util.HashMap;
import java.util.function.Supplier;

public interface abstractfactory2 {
  interface Vehicle { }
  record Bus(String color) implements Vehicle { }
  record Car() implements Vehicle { }
  
  class Registry {
    private final HashMap<String, Supplier<? extends Vehicle>> map = new HashMap<>();
    
    public void register(String name, Supplier<? extends Vehicle> supplier) {
      map.put(name, supplier);
    }

    public Vehicle create(String name) {
      return map.computeIfAbsent(name, n -> { throw new IllegalArgumentException("Unknown " + n); })
          .get();
    }
  }
  
  static void main(String[] args) {
    var registry = new Registry();
    registry.register("car", Car::new);

    // as a singleton
    var yellowBus = new Bus("yellow");
    registry.register("bus", () -> yellowBus);
    
    var vehicle1 = registry.create("bus");
    System.out.println(vehicle1);
    var vehicle2 = registry.create("car");
    System.out.println(vehicle2);
  }
}
