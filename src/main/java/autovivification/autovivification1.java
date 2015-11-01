package autovivification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface autovivification1 {
  interface Dict<K,V> {
    V get(K key);
    
    public static <K,V> Dict<K, V> asDict(Map<? super K, V> map, Supplier<? extends V> factory) {
      return key -> map.computeIfAbsent(key, k -> factory.get());
    }
  }

  public static void main(String[] args) {
    HashMap<String, List<String>> map = new HashMap<>();
    Dict<String, List<String>> dict = Dict.asDict(map, ArrayList::new);
    dict.get("foo").add("bar");
    dict.get("foo").add("baz");
    System.out.println(map);
  }
}
