package factory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface factory1 {
  enum Color { RED, BLUE }
  interface Vehicle { }
  record Car(Color color) implements Vehicle { }
  record Bus(Color color) implements Vehicle { }
  
  static List<Vehicle> create5(Supplier<Vehicle> factory) {
    return Stream.generate(factory).limit(5).toList();
  }
  
  static void main(String[] args) {
    Supplier<Vehicle> redCarFactory = () -> new Car(Color.RED);
    Supplier<Vehicle> blueBusFactory = () -> new Bus(Color.BLUE);
    
    System.out.println(create5(redCarFactory));
    System.out.println(create5(blueBusFactory));
  }
}
