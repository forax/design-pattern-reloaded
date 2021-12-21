package decorator;

import decorator.coffee1.Coffee;

public interface coffee2 {
  // The interface Coffee defines the functionality of Coffee implemented by decorator
  sealed interface Coffee {
    long cost(); // Returns the cost of the coffee, in cents
    String ingredients(); // Returns the ingredients of the coffee

    static Coffee simple(long cost) {
      return new SimpleCoffee(cost);
    }
    default Coffee withMilk() {
      return new WithMilk(this);
    }
    default Coffee withSprinkles() {
      return new WithSprinkles(this);
    }
  }

  // Extension of a simple coffee without any extra ingredients
  record SimpleCoffee(long cost) implements Coffee {
    @Override
    public String ingredients() {
      return "Coffee";
    }
  }

  // Decorator WithMilk mixes milk into coffee.
  record WithMilk(Coffee coffee) implements Coffee {
    @Override
    public long cost() {
      return coffee.cost() + 50;
    }
    @Override
    public String ingredients() {
      return coffee.ingredients() + ", Milk";
    }
  }

  // Decorator WithSprinkles mixes sprinkles onto coffee.
  record WithSprinkles(Coffee coffee) implements Coffee {
    @Override
    public long cost() {
      return coffee.cost() + 20;
    }
    @Override
    public String ingredients() {
      return coffee.ingredients() + ", Sprinkles";
    }
  }

  static void main(String[] args){
    Coffee coffeeWithMilkAndSprinkles = Coffee.simple(100)
            .withMilk()
            .withSprinkles();
    System.out.println("ingredients: " + coffeeWithMilkAndSprinkles.ingredients());
    System.out.println("cost: " + coffeeWithMilkAndSprinkles.cost() + " cents");
  }
}
