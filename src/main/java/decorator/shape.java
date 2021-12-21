package decorator;

import java.util.Objects;

public interface shape {
  interface Shape {
    String description();
  }

  record Circle(double radius) implements Shape {
    @Override
    public String description() {
      return "a circle with a radius of " + radius + " cm";
    }
  }

  record ColorDecorator(Shape shape, String color) implements Shape {
    @Override
    public String description() {
      return shape.description() + " with the color " + color;
    }
  }

  static void main(String[] args){
    Shape circle = new Circle(5.2);
    Shape coloredCircle = new ColorDecorator(circle, "red");
    System.out.println(coloredCircle.description());
  }
}
