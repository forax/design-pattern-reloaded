package abstractfactory;

public interface abstractfactory1 {
  sealed interface Vehicle {
    static Vehicle create(String name) {
      switch(name) {
        case "bus":
          return new Bus();
        case "car":
          return new Car();
        default:
          throw new IllegalArgumentException("unknown " + name);
      }
    }
  }
  record Bus() implements Vehicle {
    @Override
    public String toString() {
      return "Bus";
    }
  }
  record Car() implements Vehicle {
    @Override
    public String toString() {
      return "Car";
    }
  }
  
  static void main(String[] args) {
    var vehicle1 = Vehicle.create("bus");
    System.out.println(vehicle1);
    var vehicle2 = Vehicle.create("car");
    System.out.println(vehicle2);
  }
}
