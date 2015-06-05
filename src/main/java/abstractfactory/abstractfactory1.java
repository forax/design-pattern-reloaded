package abstractfactory;

public interface abstractfactory1 {
  public interface Vehicle {
    public static Vehicle create(String name) {
      switch(name) {
        case "car":
          return new Car();
        case "moto":
          return new Moto();
        default:
          throw new IllegalArgumentException("unknown " + name);
      }
    }
  }
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
  
  public static void main(String[] args) {
    Vehicle vehicle1 = Vehicle.create("car");
    System.out.println(vehicle1);
    Vehicle vehicle2 = Vehicle.create("moto");
    System.out.println(vehicle2);
  }
}
