package curry;

import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface curry2 {
  public interface Vehicle { /* empty */ }
  public class Car implements Vehicle {
    private final Color color;
    public Car(Color color) {
      this.color = Objects.requireNonNull(color);
    }
    @Override
    public String toString() {
      return "Car " + color; 
    }
  }
  public class Moto implements Vehicle {
    private final Color color;
    public Moto(Color color) {
      this.color = Objects.requireNonNull(color);
    }
    @Override
    public String toString() {
      return "Moto " + color; 
    }
  }
  
  static void main(String[] args) {
    HashMap<String, Color> colorMap = new HashMap<>();
    colorMap.put("blue", Color.BLUE);
    colorMap.put("violet", Color.MAGENTA);
    Function<String, Color> colorFactory = name -> colorMap.getOrDefault(name, Color.BLACK);
    
    HashMap<String, Function<Color, Vehicle>> shapeMap = new HashMap<>();
    shapeMap.put("car", Car::new);
    shapeMap.put("moto", Moto::new);
    Function<String, Function<Color, Vehicle>> vehicleFactoryFactory =
        kind -> shapeMap.getOrDefault(kind, k -> { throw new IllegalStateException("unkonwn kind " + k); });
    
    BiFunction<String, String, Vehicle> createVehicle =
        (kind, colorName) -> vehicleFactoryFactory.apply(kind).apply(colorFactory.apply(colorName));
    
    Vehicle vehicle = createVehicle.apply("car", "violet");
    System.out.println(vehicle);
  }
}
