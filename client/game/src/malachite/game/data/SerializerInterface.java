package malachite.game.data;

public interface SerializerInterface<SerializedType, DeserializedType> {
  public SerializedType   serialize();
  public DeserializedType deserialize(SerializedType data);
}