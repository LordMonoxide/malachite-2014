package malachite.gfx.gui.parser;

import malachite.gfx.gui.Control;

import java.util.Map;

public interface GUIEvents {
  public void registerControls(Map<String, Control<?>> controls);
}