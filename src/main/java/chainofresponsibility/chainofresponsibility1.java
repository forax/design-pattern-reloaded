package chainofresponsibility;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import chainofresponsibility.chainofresponsibility1.BinaryOp.Operator;

public interface chainofresponsibility1 {
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
      return "(" + left + ' ' + operator.name + ' ' + right + ')';
    }
  }
  
  public interface Expr {
    public static Expr parse(Iterator<String> it) {
      String token = it.next();
      Optional<Operator> operator = Operator.parse(token);
      if (operator.isPresent()) {
        Expr left = parse(it);
        Expr right = parse(it);
        return new BinaryOp(operator.get(), left, right);
      }
      try {
        return new Value(Double.parseDouble(token));
      } catch(NumberFormatException e) {
        return new Variable(token);
      }
    }
  }
  
  public static void main(String[] args) {
    Expr expr = Expr.parse(Arrays.asList("+ 2 * a 3".split(" ")).iterator());
    System.out.println(expr);
  }
}
