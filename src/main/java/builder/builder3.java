package builder;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import builder.builder2.Vehicle;

public interface builder3 {
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
  
  public interface Builder {
    public void register(String name, Supplier<? extends Vehicle> supplier);
  }
  
  public interface VehicleFactory {
    public Vehicle create(String name);
    
    public static VehicleFactory create(Consumer<Builder> consumer) {
      HashMap<String, Supplier<? extends Vehicle>> map = new HashMap<>();
      consumer.accept(map::put);
      return name -> map.getOrDefault(name, unknown(name)).get();
    }
    
    static Supplier<Vehicle> unknown(String name) {
      return () -> { throw new IllegalArgumentException("Unknown " + name); };
    }
  }
  
  public static void main(String[] args) {
    VehicleFactory factory = VehicleFactory.create(builder -> {
      builder.register("car", Car::new);
      builder.register("moto", Moto::new);  
    });
    
    Vehicle vehicle1 = factory.create("car");
    System.out.println(vehicle1);
    Vehicle vehicle2 = factory.create("moto");
    System.out.println(vehicle2);
  }
}
