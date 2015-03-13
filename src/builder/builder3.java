package builder;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    public void register(String name, Supplier<Vehicle> supplier);
  }
  
  public interface VehicleFactory {
    public Vehicle create(String name);
    
    public static VehicleFactory create(Consumer<Builder> consumer) {
      HashMap<String, Supplier<Vehicle>> map = new HashMap<>();
      consumer.accept((name, supplier) -> map.put(name, supplier));
      return name -> {
        return map.getOrDefault(name,
            () -> { throw new IllegalArgumentException("Unknown " + name); })
          .get();
      };
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
