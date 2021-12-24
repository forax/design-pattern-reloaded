package visitor;

import java.util.List;

public interface visitor1 {
  sealed interface Vehicle {
    <R> R accept(Visitor<? extends R> visitor);
  }
  record Car() implements Vehicle {
    @Override
    public <R> R accept(Visitor<? extends R> visitor) {
      return visitor.visitCar(this);
    }
  }
  record CarHauler(List<Car> cars) implements Vehicle {
    @Override
    public <R> R accept(Visitor<? extends R> visitor) {
      return visitor.visitCarHauler(this);
    }
  }
  
  interface Visitor<R> {
    R visitCar(Car car);
    R visitCarHauler(CarHauler carHauler);
  }

  static int count(Vehicle vehicle) {
    var visitor = new Visitor<Integer>() {
      @Override
      public Integer visitCar(Car car) {
        return 1;
      }
      @Override
      public Integer visitCarHauler(CarHauler carHauler) {
        return 1 + carHauler.cars().stream().mapToInt(car -> car.accept(this)).sum();
      }
    };
    return vehicle.accept(visitor);
  }

  static void main(String[] args) {
    var vehicle = new CarHauler(List.of(new Car(), new Car()));
    var count = count(vehicle);
    System.out.println(count);
  }
}
