package visitor;

import java.util.List;

public interface visitor3 {
  sealed interface Vehicle { }
  record Car() implements Vehicle { }
  record CarHauler(List<Car> cars) implements Vehicle {}

  static int count(Vehicle vehicle) {
    return switch (vehicle) {
      case Car() -> 1;
      case CarHauler(List<Car> cars) -> 1 + cars.stream().mapToInt(car -> count(car)).sum();
    };
  }

  static void main(String[] args) {
    var vehicle = new CarHauler(List.of(new Car(), new Car()));
    var count = count(vehicle);
    System.out.println(count);
  }
}
