package malachite.gfx.gui.control;

import malachite.gfx.Context;
import malachite.gfx.Scalable;
import malachite.gfx.textures.TextureBuilder;

public class Frame extends Image {
  public Frame() {
    super(
      InitFlags.WITH_DEFAULT_EVENTS,
      InitFlags.REGISTER
    );
    
    Scalable s = Context.newScalable();
    s.setTexture(TextureBuilder.getInstance().getTexture("gui/textbox.png"));
    s.setXY(-5, -5);
    s.setSize(
        new float[] {12, 12, 12, 12},
        new float[] {12, 12, 12, 12},
        25, 25, 1
    );

    _background = s;
  }
}
