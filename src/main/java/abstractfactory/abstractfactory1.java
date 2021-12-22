package abstractfactory;

public interface abstractfactory1 {
  sealed interface Vehicle {
    static Vehicle create(String name) {
      return switch(name) {
        case "bus" -> new Bus("yellow");
        case "car" -> new Car();
        default -> throw new IllegalArgumentException("unknown " + name);
      };
    }
  }
  record Bus(String color) implements Vehicle { }
  record Car() implements Vehicle { }
  
  static void main(String[] args) {
    var vehicle1 = Vehicle.create("bus");
    System.out.println(vehicle1);
    var vehicle2 = Vehicle.create("car");
    System.out.println(vehicle2);
  }
}
