package malachite.gfx.gui;

import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.gfx.Context;
import malachite.gfx.ContextEvents.DrawEventData;
import malachite.gfx.ContextEvents.MouseButtonEventData;
import malachite.gfx.ContextEvents.MouseMoveEventData;
import malachite.gfx.ContextEvents.MouseWheelEventData;

public class GUIManager {
  protected ConcurrentLinkedDeque<GUI> _gui = new ConcurrentLinkedDeque<>();
  
  public GUIManager(Context ctx) {
    ctx.events
      .onDraw      (ev -> { draw(ev);       })
      .onMouseMove (ev -> { mouseMove(ev);  })
      .onMouseDown (ev -> { mouseDown(ev);  })
      .onMouseUp   (ev -> { mouseUp(ev);    })
      .onMouseWheel(ev -> { mouseWheel(ev); });
  }
  
  public void push(GUI gui) {
    _gui.push(gui);
  }
  
  public void pop() {
    _gui.pop();
  }
  
  public void pop(GUI gui) {
    _gui.remove(gui);
  }
  
  public void clear() {
    _gui.clear();
  }
  
  //TODO
  public void destroy() {
    for(GUI gui : _gui) {
      gui.destroy();
    }
    
    clear();
  }
  
  private void draw(DrawEventData ev) {
    GUI[] g = new GUI[_gui.size()];
    g = _gui.toArray(g);
    
    for(int i = _gui.size(); --i >= 0;) {
      if(g[i].isLoaded()) {
        g[i].drawGUI(ev);
      }
    }
  }
  
  //TODO
  private void logic() {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.logicGUI()) {
          break;
        }
      }
    }
  }
  
  //TODO
  private void resize() {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        gui.resize();
      }
    }
  }
  
  private void mouseMove(MouseMoveEventData ev) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.mouseMove(ev)) {
          break;
        }
      }
    }
  }
  
  private void mouseDown(MouseButtonEventData ev) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.mouseDown(ev)) {
          break;
        }
      }
    }
  }
  
  private void mouseUp(MouseButtonEventData ev) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.mouseUp(ev)) {
          break;
        }
      }
    }
  }
  
  private void mouseWheel(MouseWheelEventData ev) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.mouseWheel(ev)) {
          break;
        }
      }
    }
  }
  
  //TODO
  private void keyDown(int key, boolean repeat) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.keyDown(key, repeat)) {
          break;
        }
      }
    }
  }
  
  //TODO
  private void keyUp(int key) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.keyUp(key)) {
          break;
        }
      }
    }
  }
  
  //TODO
  private void charDown(char c) {
    for(GUI gui : _gui) {
      if(gui.isLoaded()) {
        if(gui.charDown(c)) {
          break;
        }
      }
    }
  }
}