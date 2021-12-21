package decorator;

public interface coffee1 {
  // The interface Coffee defines the functionality of Coffee implemented by decorator
  interface Coffee {
    long cost(); // Returns the cost of the coffee, in cents
    String ingredients(); // Returns the ingredients of the coffee
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
    Coffee coffee = new SimpleCoffee(100);
    Coffee coffeeWithMilk = new WithMilk(coffee);
    Coffee coffeeWithMilkAndSprinkles = new WithSprinkles(coffeeWithMilk);
    System.out.println("ingredients: " + coffeeWithMilkAndSprinkles.ingredients());
    System.out.println("cost: " + coffeeWithMilkAndSprinkles.cost() + " cents");
  }
}
