package decorator;

public interface decorator1 {
  interface Coffee {
    long cost();
    String ingredients();
  }

  record SimpleCoffee(long cost) implements Coffee {
    @Override
    public String ingredients() {
      return "Coffee";
    }
  }

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
