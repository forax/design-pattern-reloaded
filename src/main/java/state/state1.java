package state;

import java.util.ArrayList;
import java.util.List;

public interface state1 {
  record Article(String name, long price) {}
  record CreditCard(String name, String id) {}
  record Address(String address, String country) {}

  class Cart {
    private enum State { CREATED, PAYED, SHIPPED }

    private List<Article> articles = new ArrayList<>();
    private Address address;
    private State state = State.CREATED;

    public void add(Article article) {
      if (state != State.CREATED) {
        throw new IllegalStateException();
      }
      articles.add(article);
    }

    public void buy(CreditCard creditCard) {
      if (state != State.CREATED) {
        throw new IllegalStateException();
      }
      state = State.PAYED;
      articles = List.copyOf(articles);
    }

    public void ship(Address address) {
      if (state != State.PAYED) {
        throw new IllegalStateException();
      }
      this.address = address;
      state = State.SHIPPED;
    }

    public String info() {
      return switch (state) {
        case CREATED -> "created articles " + articles;
        case PAYED -> "payed articles " + articles;
        case SHIPPED -> "shipped articles " + articles + " to " + address;
      };
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
