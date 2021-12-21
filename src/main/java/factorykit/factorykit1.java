package factorykit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface factorykit1 {
  interface Vehicle { }
  record Car() implements Vehicle { }
  record Bus() implements Vehicle { }

  class FactoryKit {
    static class Builder {
      private final HashMap<String, Supplier<Vehicle>> map = new HashMap<>();

      private Builder() {}

      public Builder register(String name, Supplier<Vehicle> supplier) {
        map.put(name, supplier);
        return this;
      }

      public FactoryKit build() {
        return new FactoryKit(Map.copyOf(map));
      }
    }

    public static Builder builder() {
      return new Builder();
    }

    private final Map<String, Supplier<Vehicle>> map;

    private FactoryKit(Map<String, Supplier<Vehicle>> map) {
      this.map = map;
    }

    public Supplier<Vehicle> create(String name) {
      return map.computeIfAbsent(name, n -> { throw new IllegalArgumentException("unknown name " + n); });
    }
  }
  
  static void main(String[] args) {
    var factoryKit = FactoryKit.builder()
        .register("car", Car::new)
        .register("bus", Bus::new)
        .build();

    var vehicle1 = factoryKit.create("car").get();
    System.out.println(vehicle1);
    var vehicle2 = factoryKit.create("bus").get();
    System.out.println(vehicle2);
  }
}
