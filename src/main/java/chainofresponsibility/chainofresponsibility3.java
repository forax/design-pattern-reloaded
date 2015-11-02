package chainofresponsibility;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface chainofresponsibility3 {
  public static class Value implements Expr {
    private final double value;

    public Value(double value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return Double.toString(value);
    }
  }

  public static class Variable implements Expr {
    private final String name;

    public Variable(String name) {
      this.name = Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static class BinaryOp implements Expr {
    private final Operator operator;
    private final Expr left;
    private final Expr right;

    public enum Operator {
      ADD("+"), SUB("-"), MUL("*");

      final String name;

      private Operator(String name) {
        this.name = name;
      }

      public static Optional<Operator> parse(String token) {
        return Optional.ofNullable(MAP.get(token));
      }
      
      private static final Map<String, Operator> MAP =
          Arrays.stream(values()).collect(toMap(op -> op.name, op -> op));
    }

    public BinaryOp(Operator operator, Expr left, Expr right) {
      this.operator = Objects.requireNonNull(operator);
      this.left = Objects.requireNonNull(left);
      this.right = Objects.requireNonNull(right);
    }

    @Override
    public String toString() {
      return left + " " + operator.name + ' ' + right;
    }
  }

  public interface Expr {
    public static Optional<Expr> parseValue(String token) {
      try {
        return Optional.of(new Value(Double.parseDouble(token)));
      } catch(NumberFormatException e) {
        return Optional.empty();
      }
    }
    public static Optional<Expr> parseVariable(String token) {
      return Optional.of(new Variable(token));
    }
    public static Optional<Expr> parseBinaryOp(String token, Supplier<? extends Expr> supplier) {
      return BinaryOp.Operator.parse(token).map(op -> new BinaryOp(op, supplier.get(), supplier.get()));
    }
    
    public static Expr parse(Iterator<String> it, Function<? super String, ? extends Optional<Expr>> factory) {
      String token = it.next();
      return factory.apply(token).orElseThrow(() -> new IllegalStateException("illegal token " + token));
    }
  }

  static <T> Optional<T> or(Optional<T> opt, Supplier<? extends Optional<T>> supplier) {
    return opt.isPresent()? opt: supplier.get();
  }
  
  public static Expr parse(Iterator<String> it) {
    return Expr.parse(it, token ->
        or(Expr.parseBinaryOp(token, () -> parse(it)), () ->
            or(Expr.parseValue(token), () ->
               Expr.parseVariable(token))));
    
    // with JDK9 !
    //return parse(it, token -> Expr.parseBinaryOp(token, () -> parse(it))
    //    .or(() -> Expr.parseValue(token)
    //        .or(() -> Expr.parseVariable(token))));
  }
  
  public static void main(String[] args) {
    Expr expr = parse(Arrays.asList("+ 2 * a 3".split(" ")).iterator());
    System.out.println(expr);
  }
}
