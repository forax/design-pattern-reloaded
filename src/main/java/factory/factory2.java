package factory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface factory2 {
  enum Color { RED, BLUE }

  interface Vehicle { }
  record Car(Color color) implements Vehicle { }
  record Bus(Color color) implements Vehicle { }

  interface VehicleFactory {
    Vehicle create(Color color);

    default Supplier<Vehicle> bind(Color color) {
      return () -> create(color);
    }
  }

  static List<Vehicle> create5(Supplier<Vehicle> factory) {
    return Stream.generate(factory).limit(5).toList();
  }
  
  static void main(String[] args) {
    VehicleFactory carFactory = Car::new;
    VehicleFactory busFactory = Bus::new;

    System.out.println(create5(carFactory.bind(Color.RED)));
    System.out.println(create5(busFactory.bind(Color.BLUE)));
  }
}
