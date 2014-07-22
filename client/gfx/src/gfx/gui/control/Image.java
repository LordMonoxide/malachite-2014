package gfx.gui.control;

import gfx.Context;
import gfx.Drawable;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.gui.GUI;
import gfx.textures.Texture;

public class Image extends Control<ControlEvents> {
  private Drawable _image;

  public Image(InitFlags... flags) {
    super(flags);
    _image = Context.newDrawable();
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
  }

  public void setTexture(Texture texture) {
    if(texture != null) {
      _image.setTexture(texture);
      _image.createQuad();
      setWH(texture.getW(), texture.getH());
    } else {
      setWH(0, 0);
    }
  }

  public Texture getTexture() {
    return _image.getTexture();
  }

  @Override protected void resize() {

  }

  @Override public void draw() {
    if(drawBegin()) {
      _image.draw();
    }

    drawEnd();
    drawNext();
  }
}
