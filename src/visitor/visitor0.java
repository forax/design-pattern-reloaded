package visitor;

public interface visitor0 {
  public interface Vehicle { 
    public <R> R accept(Visitor<? extends R> visitor);
  }
  public class Car implements Vehicle {
    @Override
    public <R> R accept(Visitor<? extends R> visitor) {
      return visitor.visitCar(this);
    }
  }
  public class Moto implements Vehicle {
    @Override
    public <R> R accept(Visitor<? extends R> visitor) {
      return visitor.visitMoto(this);
    }
  }
  
  public class Visitor<R> {
    public R visitMoto(Moto moto) {
      throw new NoSuchMethodError();
    }
    public R visitCar(Car car) {
      throw new NoSuchMethodError();
    }
  }

  public static void main(String[] args) {
    Visitor<String> visitor = new Visitor<String>() {
      @Override
      public String visitCar(Car car) {
        return "car";
      }
      @Override
      public String visitMoto(Moto moto) {
        return "moto";
      }
    };
   
    Vehicle vehicle = new Car();
    String text = vehicle.accept(visitor); 
    System.out.println(text);
  }
}
