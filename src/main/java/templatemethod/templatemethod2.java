package templatemethod;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface templatemethod2 {

  class DecoratedString {
    private final Supplier<String> supplier;

    public DecoratedString(Supplier<String> supplier) {
      this.supplier = supplier;
    }

    public String loveString() {
      return "❤️ " + supplier.get() + " ❤️";
    }
  }

  static void main(String[] args) {
    var decoratedString = new DecoratedString(() -> "hello");
    System.out.println(decoratedString.loveString());
  }
}
