package factory;

import static factory.factory3.Color.BLUE;
import static factory.factory3.Color.RED;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface factory3 {
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
  
  public static <T, R> Supplier<R> partial(Function<? super T, ? extends R> function, T value) {
    return () -> function.apply(value);
  }
  
  public static List<Vehicle> create5(Supplier<? extends Vehicle> factory) {
    return Collections.nCopies(5, factory.get());
  }
  
  public static void main(String[] args) {
    System.out.println(create5(partial(Car::new, RED)));
    System.out.println(create5(partial(Moto::new, BLUE)));
  }
}
