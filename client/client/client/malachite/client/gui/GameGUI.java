package malachite.client.gui;

import java.util.HashMap;

import malachite.api.Settings;
import malachite.client.game.Game;
import malachite.client.gfx.RegionRenderer;
import malachite.engine.world.Region;
import malachite.gfx.fonts.Font;
import malachite.gfx.fonts.FontBuilder;
import malachite.gfx.fonts.TextStream;
import malachite.gfx.gui.GUI;

public class GameGUI extends GUI {
  private static final Font _font = FontBuilder.getInstance().getDefault();
  
  public final Game.GameInterface gameInterface;
  
  private HashMap<String, RegionRenderer> _region = new HashMap<>();
  
  public GameGUI(Game.GameInterface gi) {
    gameInterface = gi;
    ready();
  }
  
  @Override protected void load() {
    _context.camera.bind(gameInterface.me().loc);
  }
  
  @Override public void destroy() {
    
  }
  
  @Override protected void resize() {
    
  }
  
  private RegionRenderer getRegion(Region r) {
    RegionRenderer rr = _region.get(r.name);
    
    if(rr == null) {
      rr = new RegionRenderer(r);
      _region.put(r.name, rr);
    }
    
    return rr;
  }
  
  @Override protected void draw() {
    for(int z = 0; z < Settings.Map.Depth; z++) {
      getRegion(gameInterface.me().getRegion()).draw(z);
    }
    
    _font.draw(4, 4, new TextStream(String.valueOf(Math.round(_context.getFPS())) + " FPS")); //$NON-NLS-1$
  }
  
  @Override protected boolean logic() {
    return false;
  }
}