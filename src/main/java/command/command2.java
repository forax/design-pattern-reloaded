package command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public interface command2 {
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

  class CommandManager {
    private final HashMap<String, Command> map = new HashMap<>();
    private final StringBuilder help = new StringBuilder();

    public void register(String name, List<String> options, String description, Consumer<Config> action) {
      var command = new Command(name, action);
      options.forEach(option -> map.put(option, command));
      help.append(String.join(", ", options)).append(": ").append(description).append("\n");
    }

    public Command command(String option) {
      return map.get(option);
    }

    public String help() {
      return help.toString();
    }
  }

  static Config config(CommandManager manager, List<String> args) {
    var config = new Config();
    var actionSet = new HashSet<String>();
    for(var arg: args) {
      var command = manager.command(arg);
      if (command == null) {
        continue;  // ignore
      }
      if (!actionSet.add(command.name)) {
        throw new IllegalStateException(command.name + " specified twice");
      }
      command.action.accept(config);
    }
    return config;
  }

  static CommandManager commandManager() {
    var manager = new CommandManager();
    manager.register("showHidden", List.of("-a", "--all"), "show hidden files", c -> c.showHidden = true);
    manager.register("longForm", List.of("-l", "--long"), "long form", c -> c.longForm = true);
    manager.register("showInode", List.of("-i", "--inode"), "show inodes", c -> c.showInode = true);
    manager.register("help", List.of("-h", "--help"), "show this help", c -> c.showHelp = true);
    return manager;
  }

  static void main(String[] args) {
    args = new String[] { "--all", "foo", "-i", "--help" }; // DEBUG
    var manager = commandManager();
    var config = config(manager, List.of(args));
    System.out.println(config);
    if (config.showHelp) {
      System.out.println(manager.help());
    }
  }
}
