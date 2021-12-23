package command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface command3 {
  class Config {
    boolean showHidden = false;
    boolean longForm = false;
    boolean showInode = false;
    boolean showHelp = false;

    @Override
    public String toString() {
      return "Config[showHidden: %s, longForm: %s, showInode: %s, showHelp: %s]"
          .formatted(showHidden, longForm, showInode, showHelp);
    }
  }

  record Command(String name, Consumer<Config> action) {}

  record CommandRegistry(Map<String, Command> commandMap, String help) {
    public static class Builder {
      private final HashMap<String, Command> map = new HashMap<>();
      private final StringBuilder help = new StringBuilder();

      public Builder registerOptions(List<String> options, String description, Consumer<Config> action) {
        var command = new Command(options.get(0), action);
        options.forEach(option -> map.put(option, command));
        help.append(String.join(", ", options)).append(": ").append(description).append("\n");
        return this;
      }

      public CommandRegistry toRegistry() {
        return new CommandRegistry(Map.copyOf(map), help.toString());
      }
    }

    public Command command(String option) {
      return commandMap.get(option);
    }
  }

  static Config config(CommandRegistry registry, List<String> args) {
    var config = new Config();
    var commandSet = new HashSet<String>();
    for(var arg: args) {
      var command = registry.command(arg);
      if (command == null) {
        continue;  // ignore
      }
      if (!commandSet.add(command.name)) {
        throw new IllegalStateException(command.name + " specified twice");
      }
      command.action.accept(config);
    }
    return config;
  }

  static CommandRegistry commandRegistry() {
    return new CommandRegistry.Builder()
        .registerOptions(List.of("--all", "-a"), "show hidden files", c -> c.showHidden = true)
        .registerOptions(List.of("--long", "-l"), "long form", c -> c.longForm = true)
        .registerOptions(List.of("--inode", "-i"), "show inodes", c -> c.showInode = true)
        .registerOptions(List.of("--help", "-h"), "show this help", c -> c.showHelp = true)
        .toRegistry();
  }

  static void main(String[] args) {
    args = new String[] { "--all", "foo", "-i", "--help" }; // DEBUG
    var registry = commandRegistry();
    var config = config(registry, List.of(args));
    System.out.println(config);
    if (config.showHelp) {
      System.out.println(registry.help());
    }
  }
}
