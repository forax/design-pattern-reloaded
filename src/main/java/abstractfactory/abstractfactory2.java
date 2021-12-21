package abstractfactory;

import java.util.HashMap;
import java.util.function.Supplier;

public interface abstractfactory2 {
  interface Vehicle { }
  record Bus(String color) implements Vehicle {
    @Override
    public String toString() {
      return "Bus " + color;
    }
  }
  record Car() implements Vehicle {
    @Override
    public String toString() {
      return "Car";
    }
  }
  
  class VehicleFactory {
    private final HashMap<String, Supplier<? extends Vehicle>> map = new HashMap<>();
    
    public void register(String name, Supplier<? extends Vehicle> supplier) {
      map.put(name, supplier);
    }
    public Vehicle create(String name) {
      return map.getOrDefault(name,
            () -> { throw new IllegalArgumentException("Unknown " + name); })
          .get();
    }
  }
  
  static void main(String[] args) {
    var factory = new VehicleFactory();
    factory.register("car", Car::new);

    // as a singleton
    var yellowBus = new Bus("yellow");
    factory.register("bus", () -> yellowBus);
    
    var vehicle1 = factory.create("bus");
    System.out.println(vehicle1);
    var vehicle2 = factory.create("car");
    System.out.println(vehicle2);
  }
}
