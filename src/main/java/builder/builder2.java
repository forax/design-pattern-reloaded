package builder;

import java.util.HashMap;
import java.util.function.Supplier;

public interface builder2 {
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
  
  public class Builder {
    private final HashMap<String, Supplier<? extends Vehicle>> map = new HashMap<>();
    
    public void register(String name, Supplier<? extends Vehicle> supplier) {
      map.put(name, supplier);
    }
    
    public VehicleFactory toFactory() {
      return name -> map.getOrDefault(name, unknown(name)).get();
    }
    
    private static Supplier<Vehicle> unknown(String name) {
      return () -> { throw new IllegalArgumentException("Unknown " + name); };
    }
  }
  
  public interface VehicleFactory {
    public Vehicle create(String name);
  }
  
  public static void main(String[] args) {
    Builder builder = new Builder();
    builder.register("car", Car::new);
    builder.register("moto", Moto::new);
    VehicleFactory factory = builder.toFactory();
    
    Vehicle vehicle1 = factory.create("car");
    System.out.println(vehicle1);
    Vehicle vehicle2 = factory.create("moto");
    System.out.println(vehicle2);
  }
}
