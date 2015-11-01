package curry;

import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface curry5 {
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
  
  static <K, T> Function<K, T> factory(Consumer<BiConsumer<K, T>> consumer, Function<? super K, ? extends T> ifAbsent) {
    HashMap<K, T> map = new HashMap<>();
    consumer.accept(map::put);
    return key -> map.computeIfAbsent(key, ifAbsent);
  }
  
  static void main(String[] args) {
    Function<String, Color> colorFactory = factory(builder -> {
      builder.accept("blue", Color.BLUE);
      builder.accept("violet", Color.MAGENTA);
    }, __ -> Color.BLACK);
    
    Function<String, Function<Color, Vehicle>> vehicleFactoryFactory = factory(builder -> {
      builder.accept("car", Car::new);
      builder.accept("moto", Moto::new);
    }, key -> __ -> { throw new IllegalStateException("unknow vehicle " + key); });
    
    Function<String, Function<String, Vehicle>> vehicleFactory =
        kind -> vehicleFactoryFactory.apply(kind).compose(colorFactory);
        
    Vehicle vehicle = vehicleFactory.apply("car").apply("violet");
    System.out.println(vehicle);
  }
}
