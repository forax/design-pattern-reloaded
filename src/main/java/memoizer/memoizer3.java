package memoizer;

import static java.util.stream.IntStream.range;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface memoizer3 {
  class Memoizer<V, R> {
    private final BiFunction<? super V, Function<? super V, ? extends R>, ? extends R> function;
    private final HashMap<V, R> map = new HashMap<>();
    
    public Memoizer(BiFunction<? super V, Function<? super V, ? extends R>, ? extends R> function) {
      this.function = Objects.requireNonNull(function);
    }

    public final R memoize(V value) {
      return map.computeIfAbsent(value, v -> function.apply(v, this::memoize));
    }
  }
  
  public static void main(String[] args) {
    Memoizer<Integer, Integer> memoizer = new Memoizer<>((n, memoize) -> {
      if (n < 2) {
        return 1;
      }
      return memoize.apply(n - 1) + memoize.apply(n - 2);
    });
    
    range(0, 20).map(memoizer::memoize).forEach(System.out::println);
  }
}
