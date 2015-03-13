package factory;

import static factory.factory1.Color.BLUE;
import static factory.factory1.Color.RED;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;

public interface factory1 {
  public enum Color { RED, GREEN, BLUE }
  
  public interface Vehicle { /* empty */ }
  public class Car implements Vehicle {
    private final Color color;
    public Car(Color color) {
      this.color = color;
    }
    
    @Override
    public String toString() {
      return "Car " + color; 
    }
  }
  public class Moto implements Vehicle {
    private final Color color;
    public Moto(Color color) {
      this.color = color;
    }
    
    @Override
    public String toString() {
      return "Moto " + color; 
    }
  }
  
  public interface VehicleFactory {
    public Vehicle create();
  }
  
  public static List<Vehicle> create5(VehicleFactory factory) {
    return range(0, 5).mapToObj(i -> factory.create()).collect(toList());
  }
  
  public static void main(String[] args) {
    VehicleFactory redCarFactory =
      () -> new Car(RED);
    VehicleFactory blueMotoFactory =  
      () -> new Moto(BLUE);
    
    System.out.println(create5(redCarFactory));
    System.out.println(create5(blueMotoFactory));
  }
}
