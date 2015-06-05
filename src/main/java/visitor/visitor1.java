package visitor;

import java.util.function.Function;

public interface visitor1 {
  public interface Vehicle { /* empty */ }
  public class Car implements Vehicle { /* empty */}
  public class Moto implements Vehicle { /* empty */ }
  
  public class Visitor<R> {
    public <T> Visitor<R> when(Class<? extends T> type, Function<? super T, ? extends R> fun) {
      throw new UnsupportedOperationException("TODO");
    }
    public R call(Object receiver) {
      throw new UnsupportedOperationException("TODO");
    }
  }

  public static void main(String[] args) {
    Visitor<String> visitor = new Visitor<>();
    visitor.when(Car.class, car -> "car")
           .when(Moto.class, moto -> "moto");

    Vehicle vehicle = new Car();
    String text = visitor.call(vehicle); 
    System.out.println(text);
  }
}
