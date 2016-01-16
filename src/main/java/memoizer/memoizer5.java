package memoizer;

import static java.util.stream.IntStream.range;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface memoizer5 {
  static <V, R> Function<V, R> memoize(BiFunction<? super V, Function<? super V, ? extends R>, ? extends R> fun) {
    HashMap<V, R> map = new HashMap<>();
    return new Object() {
      Function<V, R> memoizer = value -> map.computeIfAbsent(value, v -> fun.apply(v, this.memoizer));
    }.memoizer;
  }
  
  public static void main(String[] args) {
    Function<Integer, Integer> fibo = memoize((n, fib) -> {
      if (n < 2) {
        return 1;
      }
      return fib.apply(n - 1) + fib.apply(n - 2);
    });
    
    range(0, 20).map(fibo::apply).forEach(System.out::println);
  }
}
