package command;

import java.util.List;

public interface command1 {
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

  static Config config(List<String> args) {
    var config = new Config();
    for(var arg: args) {
      switch (arg) {
        case "-a", "--all" -> {
          if (config.showHidden) {
            throw new IllegalStateException("showHidden specified twice");
          }
          config.showHidden = true;
        }
        case "-l", "--long" -> {
          if (config.longForm) {
            throw new IllegalStateException("longForm specified twice");
          }
          config.longForm = true;
        }
        case "-i", "--inode" -> {
          if (config.showInode) {
            throw new IllegalStateException("showInode specified twice");
          }
          config.showInode = true;
        }
        case "-h", "--help" -> {
          if (config.showHelp) {
            throw new IllegalStateException("showHelp specified twice");
          }
          config.showHelp = true;
        }
        default -> {}  // ignore
      }
    }
    return config;
  }

  static void main(String[] args){
    args = new String[] { "--all", "foo", "-i", "--help" }; // DEBUG
    var config = config(List.of(args));
    System.out.println(config);
    if (config.showHelp) {
      System.out.println("""
          -a, --all: show hidden files
          -l, --long: long form
          -i, --inode: show inodes
          -h, --help: show this help
          """);
    }
  }
}
