package builder;

import java.util.function.Supplier;

public interface builder1 {
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
    public void register(String name, Supplier<? extends Vehicle> supplier) {
      throw new UnsupportedOperationException("TODO");
    }
    
    public VehicleFactory toFactory() {
      throw new UnsupportedOperationException("TODO");
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
