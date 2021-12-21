package visitor;

public interface visitor1 {
  interface Vehicle {
    <R> R accept(Visitor<? extends R> visitor);
  }
  record Car() implements Vehicle {
    @Override
    public <R> R accept(Visitor<? extends R> visitor) {
      return visitor.visitCar(this);
    }
  }
  record Bus() implements Vehicle {
    @Override
    public <R> R accept(Visitor<? extends R> visitor) {
      return visitor.visitBus(this);
    }
  }
  
  interface Visitor<R> {
    R visitBus(Bus bus);
    R visitCar(Car car);
  }

  static void main(String[] args) {
    var visitor = new Visitor<String>() {
      @Override
      public String visitBus(Bus bus) {
        return "bus";
      }
      @Override
      public String visitCar(Car car) {
        return "car";
      }
    };
   
    Vehicle vehicle = new Car();
    String text = vehicle.accept(visitor); 
    System.out.println(text);
  }
}
