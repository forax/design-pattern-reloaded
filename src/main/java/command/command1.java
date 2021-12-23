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
            throw new IllegalStateException("--all specified twice");
          }
          config.showHidden = true;
        }
        case "-l", "--long" -> {
          if (config.longForm) {
            throw new IllegalStateException("--long specified twice");
          }
          config.longForm = true;
        }
        case "-i", "--inode" -> {
          if (config.showInode) {
            throw new IllegalStateException("--inode specified twice");
          }
          config.showInode = true;
        }
        case "-h", "--help" -> {
          if (config.showHelp) {
            throw new IllegalStateException("--help specified twice");
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
          --all, -a: show hidden files
          --long, -l: long form
          --inode, -i: show inodes
          --help, -h: show this help
          """);
    }
  }
}
