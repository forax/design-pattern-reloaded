package curry;

import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public interface curry1 {
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
  
  static Color getColor(String name) {
    return COLOR_MAP.getOrDefault(name, Color.BLACK);
  }
  
  static final HashMap<String, Color> COLOR_MAP = createColorMap();
  
  static HashMap<String, Color> createColorMap() {
    HashMap<String, Color> colorMap = new HashMap<>();
    colorMap.put("blue", Color.BLUE);
    colorMap.put("violet", Color.MAGENTA);
    return colorMap;
  }
  
  static Function<Color, Vehicle> getVehicleFactory(String kind) {
    return VEHICLE_FACTORY_MAP.getOrDefault(kind, k -> { throw new IllegalStateException("unkonwn kind " + k); });
  }
  
  static final HashMap<String, Function<Color, Vehicle>> VEHICLE_FACTORY_MAP = createVehicleFactoryMap();
  
  static HashMap<String, Function<Color, Vehicle>> createVehicleFactoryMap() {
    HashMap<String, Function<Color, Vehicle>> factoryMap = new HashMap<>();
    factoryMap.put("car", Car::new);
    factoryMap.put("moto", Moto::new);
    return factoryMap;
  }
  
  static Vehicle createVehicle(String kind, String colorName) {
    Function<Color, Vehicle> vehiceFactory = getVehicleFactory(kind);
    Color color = getColor(colorName);
    return vehiceFactory.apply(color);
  }
  
  static void main(String[] args) {
    Vehicle vehicle = createVehicle("car", "violet");
    System.out.println(vehicle);
  }
}
