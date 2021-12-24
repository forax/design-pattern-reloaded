# Memoizer Pattern

The result of pure function with one parameter, i.e. a function that has its return value only depending on the value
of the parameter with no side effect, can be stored in a cache to avoid re-computation of this value.

The Memoizer Pattern provided a reusable class `Memoizer` allowing add a cache to the computation
of a pure function.

## The memoizer pattern using inheritance

We can use the [Template Method](../templatemethod) pattern and inheritance to create a class that let users
overriding the method `compute()` while providing a method `memoize` that cache the result
of a computation.

```java
abstract class Memoizer<V, R> {
  private final HashMap<V, R> map = new HashMap<>();
    
  public final R memoize(V value) {
    return map.computeIfAbsent(value, this::compute);
  }
    
  protected abstract R compute(V value);
}
```

This is how it can be used:
```java
var memoizer = new Memoizer<Integer, Integer>() {
  @Override
  protected Integer compute(Integer n) {
    if (n < 2) {
      return 1;
    }
    return memoize(n - 1) + memoize(n - 2);
  }
};
```

But as usual with inheritance, it's easy to mis-use that class because the API
contains both the méthod `compute()` and `memoize()` leading to two frequent mistake,
either the computation in `compute` calling recursively the method `compute()` instead of
`memoize()` or a user using the method `compute()` directly instead of the method `memoize()`.
This second mistake is less frequent because `compute` is declared protected an not public.


## The Memoizer Pattern using delegation

As usual the solution is to avoid inheritance and use delegation instead. 

So the class `Memoizer` can be written that way
```java
final class Memoizer<V, R> {
  private final Function<? super V, ? extends R> function;
  private final HashMap<V, R> map = new HashMap<>();
    
  public Memoizer(Function<? super V, ? extends R> function) {
    this.function = function;
  }

  public R memoize(V value) {
    return map.computeIfAbsent(value, function);
  }
}
```

but in that case, the method `memoize()` is not available inside the lambda
```java
var memoizer = new Memoizer<Integer, Integer>(n -> {
      if (n < 2) {
        return 1;
      }
      return memoize(n - 1) + memoize(n - 2);   //FIXME !!
    });
```


To solve that, we need to introduce a second parameter able to do recursive call, like this
```java
var fibo = new Memoizer<Integer, Integer>((n, fib) -> {
      if (n < 2) {
        return 1;
      }
      return fib.apply(n - 1) + fib.apply(n - 2);
    });
```

this leads to the following code for the class `Memoizer`
```java
final class Memoizer<V, R> {
  private final BiFunction<? super V, Function<? super V, ? extends R>, ? extends R> bifunction;
  private final HashMap<V, R> map = new HashMap<>();
    
  public Memoizer(BiFunction<? super V, Function<? super V, ? extends R>, ? extends R> bifunction) {
    this.bifunction = bifunction;
  }

  public R memoize(V value) {
    return map.computeIfAbsent(value, v -> bifunction.apply(v, this::memoize));
  }
}
```

