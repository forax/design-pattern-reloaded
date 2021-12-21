package builder;

import java.util.Objects;

public interface builder1 {
  record Spaceship(String name, String captain, int torpedoes, int length) {}

  class SpaceshipBuilder {
    private String name;
    private String captain;
    private int torpedoes = -1;
    private int length = -1;

    public SpaceshipBuilder name(String name) {
      this.name = name;
      return this;
    }
    public SpaceshipBuilder captain(String captain) {
      this.captain = captain;
      return this;
    }
    public SpaceshipBuilder torpedoes(int torpedoes) {
      this.torpedoes = torpedoes;
      return this;
    }
    public SpaceshipBuilder length(int length) {
      this.length = length;
      return this;
    }
    
    public Spaceship build() {
      if (name == null || captain == null || torpedoes == -1 || length == -1) {
        throw new IllegalStateException("name, captain, torpedoes or length not initialized");
      }
      return new Spaceship(name, captain, torpedoes, length);
    }
  }

  static void printSpaceship(Spaceship spaceship) {
    System.out.println(spaceship);
  }

  static void main(String[] args) {
    printSpaceship(
        new SpaceshipBuilder()
            .name("USS Enterprise")
            .captain("Kirk")
            .torpedoes(10_000)
            .length(288_646)
            .build());
  }
}
