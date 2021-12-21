package factory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface factory2 {
  enum Color { RED, BLUE }

  interface Vehicle { }
  record Car(Color color) implements Vehicle {
    @Override
    public String toString() {
      return "Car " + color;
    }
  }
  record Moto(Color color) implements Vehicle {
    @Override
    public String toString() {
      return "Moto " + color;
    }
  }

  interface VehicleFactory {
    Vehicle create(Color color);

    default Supplier<Vehicle> curry(Color color) {
      return () -> create(color);
    }
  }

  static List<Vehicle> create5(Supplier<Vehicle> factory) {
    return Stream.generate(factory).limit(5).toList();
  }
  
  static void main(String[] args) {
    var carFactory = (VehicleFactory) Car::new;
    var motoFactory = (VehicleFactory) Car::new;

    System.out.println(create5(carFactory.curry(Color.RED)));
    System.out.println(create5(motoFactory.curry(Color.BLUE)));
  }
}
