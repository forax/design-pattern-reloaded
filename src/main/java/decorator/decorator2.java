package decorator;

public interface decorator2 {
  sealed interface Coffee {
    long cost();
    String ingredients();

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
    Coffee coffeeWithMilkAndSprinkles = Coffee.simple(100)
            .withMilk()
            .withSprinkles();
    System.out.println("ingredients: " + coffeeWithMilkAndSprinkles.ingredients());
    System.out.println("cost: " + coffeeWithMilkAndSprinkles.cost() + " cents");
  }
}
