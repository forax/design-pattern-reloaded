package memoizer;

import static java.util.stream.IntStream.range;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

public interface memoizer2 {
  class Memoizer<V, R> {
    private final Function<? super V, ? extends R> function;
    private final HashMap<V, R> map = new HashMap<>();
    
    public Memoizer(Function<? super V, ? extends R> function) {
      this.function = Objects.requireNonNull(function);
    }

    public final R memoize(V value) {
      return map.computeIfAbsent(value, function);
    }
  }
  
  public static void main(String[] args) {
    Memoizer<Integer, Integer> memoizer = new Memoizer<>(
        n -> {
          if (n < 2) {
            return 1;
          }
          
          //return memoize(n - 1) + memoize(n - 2);   //FIXME !!
          return 0;
        });
    
    range(0, 20).map(memoizer::memoize).forEach(System.out::println);
  }
}
