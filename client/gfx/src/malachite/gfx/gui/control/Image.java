package malachite.gfx.gui.control;

import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.GUI;
import malachite.gfx.textures.Texture;

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
      bounds.wh.set(texture.getW(), texture.getH());
    } else {
      bounds.wh.set(0, 0);
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
