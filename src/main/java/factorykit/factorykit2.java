package factorykit;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface factorykit2 {
  interface Vehicle { }
  record Car() implements Vehicle { }
  record Bus() implements Vehicle { }
  
  static <K, T> Function<K, T> factoryKit(Consumer<BiConsumer<K, T>> consumer) {
    var map = new HashMap<K, T>();
    consumer.accept(map::put);
    return key -> map.computeIfAbsent(key, k -> { throw new IllegalArgumentException("unknown key " + k); });
  }
  
  static void main(String[] args) {
    Function<String, Supplier<Vehicle>> factoryKit = factoryKit(builder -> {
      builder.accept("car", Car::new);
      builder.accept("bus", Bus::new);
    });
    
    var vehicle1 = factoryKit.apply("car").get();
    System.out.println(vehicle1);
    var vehicle2 = factoryKit.apply("bus").get();
    System.out.println(vehicle2);
  }
}
