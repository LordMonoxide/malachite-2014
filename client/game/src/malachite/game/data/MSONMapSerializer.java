package malachite.game.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class MSONMapSerializer implements SerializerInterface<JSONObject, Map> {
  private final Map _map;
  
  public MSONMapSerializer(Map map) {
    _map = map;
  }
  
  @Override public JSONObject serialize() {
    JSONObject json = new JSONObject();
    
    json.put("x", _map.x);
    json.put("y", _map.y);
    
    JSONArray layers = new JSONArray();
    
    json.put("layers", layers);
    
    for(Map.Layer layer : _map.layer) {
      JSONArray tiles1 = new JSONArray();
      
      layers.put(tiles1);
      
      for(Map.Tile[] ta : layer.tile) {
        JSONArray tiles2 = new JSONArray();
        
        tiles1.put(tiles2);
        
        for(Map.Tile tile : ta) {
          JSONObject jsonTile = new JSONObject();
          jsonTile.put("a", tile.alpha);
          jsonTile.put("x", tile.x);
          jsonTile.put("y", tile.y);
          jsonTile.put("s", tile.set);
          tiles2.put(jsonTile);
        }
      }
    }
    
    return json;
  }
  
  @Override public Map deserialize(JSONObject data) {
    return null;
  }
}