package command;

import static java.util.Arrays.stream;

import java.util.HashMap;
import java.util.function.BiConsumer;

public interface command3 {
  interface OptionBuilder {
    void register(String name, String description, Runnable action);
  }
  
  interface Context {
    void help();
  }
  
  interface CommandLineParser {
    void parse(String[] args);
    
    public static CommandLineParser create(BiConsumer<OptionBuilder, Context> consumer) {
      StringBuilder help = new StringBuilder();
      Context context = () -> System.out.println(help);
      HashMap<String, Runnable> actionMap = new HashMap<>();
      consumer.accept((name, description, action) -> {
        actionMap.put('-' + name, action);
        help.append(name).append(": ").append(description).append('\n');
      }, context);
      return args ->
          stream(args)
            .filter(arg -> arg.startsWith("-"))
            .map(arg -> actionMap.getOrDefault(arg, () -> { context.help(); System.exit(1); }))
            .forEach(Runnable::run);
    }
  }
  
  public static void main(String[] args) {
    CommandLineParser.create((opt, ctx) -> {
      opt.register("a", "print all info",
          () -> System.out.println("see -a"));
      opt.register("help", "print this help", ctx::help);
    }).parse(args);
  }
}
