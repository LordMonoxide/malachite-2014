package malachite.game.data;

public interface LoadableInterface<ID> {
  public String type();
  public ID     id();
}