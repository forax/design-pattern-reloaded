package state;

import java.util.ArrayList;
import java.util.List;

public interface state2 {
  record Article(String name, long price) {}
  record CreditCard(String name, String id) {}
  record Address(String address, String country) {}

  class Cart {
    private sealed interface State {
      default State add(Article article) {
        throw new IllegalStateException();
      }
      default State buy(CreditCard creditCard) {
        throw new IllegalStateException();
      }
      default State ship(Address address) {
        throw new IllegalStateException();
      }
    }
    private record Created(ArrayList<Article> articles) implements State {
      @Override
      public State add(Article article) {
        articles.add(article);
        return this;
      }

      @Override
      public State buy(CreditCard creditCard) {
        return new Payed(List.copyOf(articles));
      }
    }
    private record Payed(List<Article> articles) implements State {
      @Override
      public State ship(Address address) {
        return new Shipped(articles, address);
      }
    }
    private record Shipped(List<Article> articles, Address address) implements State { }

    private State state = new Created(new ArrayList<>());

    public void add(Article article) {
      state = state.add(article);
    }
    public void buy(CreditCard creditCard) {
      state = state.buy(creditCard);
    }
    public void ship(Address address) {
      state = state.ship(address);
    }
    public String info() {
      return state.toString();
    }
  }

  static void main(String[] args){
    var cart = new Cart();
    cart.add(new Article("Lego Kit", 9999));
    System.out.println(cart.info());
    cart.buy(new CreditCard("Mr Nobody", "1212_2121_1212_2121"));
    System.out.println(cart.info());
    cart.ship(new Address("12 Nice Street, London", "England"));
    System.out.println(cart.info());
  }
}
