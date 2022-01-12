package observer;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface observer2 {
  record Order(Kind kind, int quantity, String tick, int accountId) {
    enum Kind { BUY, SELL }
  }

  interface BalanceObserver {
    void balanceChanged(int newValue);
  }

  class StockExchange {
    private final BalanceObserver balanceObserver;
    private final TreeMap<String, Integer> stockMap = new TreeMap<>();
    private int balance;

    public StockExchange(BalanceObserver balanceObserver) {
      this.balanceObserver = balanceObserver;
    }

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

  static void main(String[] args) {
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
    stockExchange.setBalance(5_000);
    stockExchange.setStockQuantity("FOOGL", 1_000);
    stockExchange.setStockQuantity("PAPL", 2_000);
    System.out.println(stockExchange);

    var orders = List.of(
        new Order(Order.Kind.SELL, 200, "FOOGL", 12),
        new Order(Order.Kind.BUY, 1_500, "PAPL", 12),
        new Order(Order.Kind.BUY, 3_000, "FOOGL", 666)
        );

    var rejectedOrderList = stockExchange.process(orders);
    var rejectedOrders = rejectedOrderList.stream()
        .collect(Collectors.groupingBy(Order::accountId));

    System.out.println(stockExchange);
    System.out.println("rejected orders " + rejectedOrders);
  }
}
