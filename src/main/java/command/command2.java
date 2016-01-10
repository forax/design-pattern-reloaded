package command;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;

public interface command2 {
  enum Option {
    ALL("a", "print all info", () -> System.out.println("see -a")),
    HELP("help", "print this help", () -> stream(values()).forEach(System.out::println));
    
    private final String name;
    private final String description;
    private final Runnable action;
    
    private Option(String name, String description, Runnable action) {
      this.name = requireNonNull(name);
      this.description = requireNonNull(description);
      this.action = requireNonNull(action);
    }
    
    @Override
    public String toString() {
      return name + ": " + description;
    }
    
    public static void parse(String[] args) {
      for(String arg: args) {
        if (arg.startsWith("-")) {
          Option option = OPTION_MAP.getOrDefault(arg, HELP);
          if (option == null) {
            HELP.action.run();
            return;
          }
          option.action.run();
        }
      }
    }
    
    private static final Map<String, Option> OPTION_MAP =
        stream(values()).collect(toMap(opt -> '-' + opt.name, identity()));
  }
  
  public static void main(String[] args) {
    Option.parse(args);
  }
}
