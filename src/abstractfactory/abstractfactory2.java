package abstractfactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface abstractfactory2 {
  public interface Vehicle { /* empty */ }
  public class Car implements Vehicle {
    @Override
    public String toString() {
      return "Car "; 
    }
  }
  public class Moto implements Vehicle {
    @Override
    public String toString() {
      return "Moto "; 
    }
  }
  
  public class VehicleFactory {
    private final Map<String, Supplier<? extends Vehicle>> map = new HashMap<>();
    
    public void register(String name, Supplier<? extends Vehicle> supplier) {
      map.put(name, supplier);
    }
    public Vehicle create(String name) {
      return map.getOrDefault(name,
            () -> { throw new IllegalArgumentException("Unknown " + name); })
          .get();
    }
  }
  
  public static void main(String[] args) {
    VehicleFactory factory = new VehicleFactory();
    factory.register("car", () -> new Car());
    factory.register("moto", () -> new Moto());
    
    Vehicle vehicle1 = factory.create("car");
    System.out.println(vehicle1);
    Vehicle vehicle2 = factory.create("moto");
    System.out.println(vehicle2);
  }
}
