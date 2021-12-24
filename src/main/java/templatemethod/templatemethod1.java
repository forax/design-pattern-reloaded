package templatemethod;

public interface templatemethod1 {

  abstract class DecoratedString {
    protected abstract String plainString();

    public String loveString() {
      return "❤️ " + plainString() + " ❤️";
    }
  }

  static void main(String[] args) {
    var decoratedString = new DecoratedString() {
      @Override
      protected String plainString() {
        return "hello";
      }
    };
    System.out.println(decoratedString.loveString());
  }
}
