package factory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface factory1 {
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
  
  static List<Vehicle> create5(Supplier<Vehicle> factory) {
    return Stream.generate(factory).limit(5).toList();
  }
  
  static void main(String[] args) {
    var redCarFactory = (Supplier<Vehicle>) () -> new Car(Color.RED);
    var blueMotoFactory = (Supplier<Vehicle>) () -> new Moto(Color.BLUE);
    
    System.out.println(create5(redCarFactory));
    System.out.println(create5(blueMotoFactory));
  }
}
