# Observer Pattern

The point of the observer pattern is to decouple two pieces of code by using an interface in the middle.

Let say we want to model a `StockExchange` that have a balance and contains several quantities of stocks.
By example, here the balance is 5 000 and wa have 1 000 stocks of FOOGL and 2 000 stocks of PAPL.
```java
  static void main(String[] args) {
    var stockExchange = new StockExchange();
    stockExchange.setBalance(5_000);
    stockExchange.setStockQuantity("FOOGL", 1_000);
    stockExchange.setStockQuantity("PAPL", 2_000);
    System.out.println(stockExchange);
```

An exchange is able to process orders and group the rejected orders by `accountId`.
An order is rejected if the exchange has no enough stock to process a BUY order,
here the last BUY order can not be processed because the exchange has not 3 000 stocks of FOOGL.
```java
   record Order(Kind kind, int quantity, String tick, int accountId) {
    enum Kind { BUY, SELL }
  }

  static void main(String[] args) {
    ...
    var orders = List.of(
        new Order(Order.Kind.SELL, 200, "FOOGL", 12),
        new Order(Order.Kind.BUY, 1_500, "PAPL", 12),
        new Order(Order.Kind.BUY, 3_000, "FOOGL", 666)
        );

    var rejectedOrderList = stockExchange.process(orders);
    var rejectedOrders = rejectedOrderList.stream()
        .collect(Collectors.groupingBy(Order::accountId));
```

This is the code of the `StockExchange`
```java
  class StockExchange {
    private final TreeMap<String, Integer> stockMap = new TreeMap<>();
    private int balance;

    @Override
    public String toString() {
      return "{stockMap: " + stockMap + ", balance: " + balance + "}";
    }

    public void setBalance(int balance) {
      this.balance = balance;
    }

    public void setStockQuantity(String tick, int quantity) {
      stockMap.put(tick, quantity);
    }

    public List<Order> process(List<? extends Order> orders) {
      var rejectedOrders = new ArrayList<Order>();
      for (var order : orders) {
        switch (order.kind()) {
          case BUY -> {
            var stockQuantity = stockMap.getOrDefault(order.tick(), 0);
            if (order.quantity() > stockQuantity) {
              rejectedOrders.add(order);
              continue;
            }
            stockMap.put(order.tick(), stockQuantity - order.quantity());
            balance += order.quantity();
          }
          case SELL -> {
            stockMap.merge(order.tick(), order.quantity(), Integer::sum);
            balance -= order.quantity();
          }
        }
      }
      return rejectedOrders;
    }
  }
```


We now want to add a code to log if the balance is less than 0 or more than 6 000, because
having a negative balance is always bad and having too much money in a hot wallet is bad too.

We can patch the code of `process` to in case of a BUY check if the balance does not grow over 6 000
or in case of a SELL if the balance does not go below 0, but it will make the code hard to maintain
because we will be mixed the processing algorithm with other concerns.
It's better to decouple those thing by introducing an observer.

## Enter the observer

An observer is an interface used to publish the state of an object so a code can react to it.

In our example, let's define a `BalanceObserver` that will be called each time the balance change
```java
interface BalanceObserver {
  void balanceChanged(int newValue);
}
```

We take the `BalanceObserver` at creation and called it each time the balance changed
```java
class StockExchange {
    private final BalanceObserver balanceObserver;
    private final TreeMap<String, Integer> stockMap = new TreeMap<>();
    private int balance;

    public StockExchange(BalanceObserver balanceObserver) {
      this.balanceObserver = balanceObserver;
    }
  ...

    public List<Order> process(List<? extends Order> orders) {
      var rejectedOrders = new ArrayList<Order>();
      for (var order : orders) {
        switch (order.kind()) {
          case BUY -> {
            var stockQuantity = stockMap.getOrDefault(order.tick(), 0);
            if (order.quantity() > stockQuantity) {
              rejectedOrders.add(order);
              continue;
            }
            stockMap.put(order.tick(), stockQuantity - order.quantity());
            balance += order.quantity();
            balanceObserver.balanceChanged(balance);
          }
          case SELL -> {
            stockMap.merge(order.tick(), order.quantity(), Integer::sum);
            balance -= order.quantity();
            balanceObserver.balanceChanged(balance);
          }
        }
      }
      return rejectedOrders;
    }
  }
```

We can now implement the observer with the correct semantics
```java
    BalanceObserver balanceObserver = newValue -> {
      if (newValue < 0) {
        System.out.println("balance negative !!!");
        return;
      }
      if (newValue >= 6_000) {
        System.out.println("balance too high !!!");
      }
    };
    var stockExchange = new StockExchange(balanceObserver);
    ...
```

As you can see, the code that reacts to the value of the balance being changed  is separated from
the code that process the orders thanks to the observer pattern.

Note: historically, the observer pattern was called the observable/observer pattern and was able
to have manage several observers instead of one like in the example above.
We now prefer to have only one observer and to use the design __pattern composite__ in case
we have several observers.

Here is an example of such composite
```java
  record CompositeObserver(List<Observer> observers) implements Observer {
    public void balanceChanged(int newValue) {
      observers.forEach(observer -> observer.balanceChanged(value))
    }
  }
```

## More on observers

If we take a look to the initial code, there was already an observer hidden between the lines,
instead of using a list to collect the rejected orders, we should also apply the same
principle and declare an observer of the rejected orders.

```java
interface OrderObserver {
  void rejected(Order order);
}

...
  public void process(List<? extends Order> orders, OrderObserver orderObserver) {
    for (var order : orders) {
      switch (order.kind()) {
        case BUY -> {
          var stockQuantity = stockMap.getOrDefault(order.tick(), 0);
          if (order.quantity() > stockQuantity) {
            orderObserver.rejected(order);
            continue;
          }
          stockMap.put(order.tick(), stockQuantity - order.quantity());
          balance += order.quantity();
          balanceObserver.balanceChanged(balance);
        }
        case SELL -> {
          stockMap.merge(order.tick(), order.quantity(), Integer::sum);
          balance -= order.quantity();
          balanceObserver.balanceChanged(balance);
        }
      }
    }
  }
...
```

You can note that this observer does not have to be stored in a field if it is only useful for a treatment.

And in the `main()`, we can create the list that will store all rejected orders
```java
  ...
  var rejectedOrderList = new ArrayList<Order>();
  stockExchange.process(orders, rejectedOrderList::add);
  var rejectedOrders = rejectedOrderList.stream()
      .collect(Collectors.groupingBy(Order::accountId));
  ...
```

or using a stream + mapMulti(), avoid the creation of the intermediary list
```java
  ...
  var rejectedOrders = Stream.of(stockExchange)
      .<Order>mapMulti((exchange, consumer) -> exchange.process(orders, consumer::accept))
      .collect(Collectors.groupingBy(Order::accountId));
  ...
```

The Observer Pattern allow to decouple/untangle a code to create an on the shelf class that can be reused
because it depends on an interface.

