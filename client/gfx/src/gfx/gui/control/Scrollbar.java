package gfx.gui.control;

import gfx.gui.Control;
import gfx.gui.ControlEvents;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Scrollbar extends Control<Scrollbar.Events> {
  private Button _inc, _dec;
  private int _min, _max, _val;
  
  public Scrollbar() {
    _events = new Events(this);
    
    _inc = new Button();
    _inc.setText(">");
    _inc.events().onClick(e -> {
      setVal(_val + 1);
    });
    
    _dec = new Button();
    _dec.setText("<");
    _dec.events().onClick(e -> {
      setVal(_val - 1);
    });
    
    controls().add(_inc);
    controls().add(_dec);
  }
  
  public int getMin() { return _min; }
  public int getMax() { return _max; }
  public int getVal() { return _val; }
  
  public void setMin(int min) { _min = Math.min(min, _max); setVal(_val); }
  public void setMax(int max) { _max = Math.max(max, _min); setVal(_val); }
  public void setVal(int val) {
    int v = Math.max(_min, Math.min(val, _max));
    if(v != _val) {
      _val = v;
      events().onChange(_val);
    }
  }
  
  @Override protected void resize() {
    if(size.getX() >= size.getY()) {
      _dec.size.set(size.getX() / 2, size.getY());
      
      _inc.pos .set(_dec.size.getX(), 0);
      _inc.size.set(size.getX() - _dec.size.getX(), size.getY());
    } else {
      _dec.size.set(size.getX(), size.getY() / 2);
      
      _inc.pos. set(0, _dec.size.getY());
      _inc.size.set(size.getX(), size.getY() - _dec.size.getY());
    }
  }
  
  public static class Events extends ControlEvents {
    private Deque<ChangeEvent> _change = new ConcurrentLinkedDeque<>();
    
    public Events onChange(ChangeEvent e) { _change.add(e); return this; }
    
    protected Events(Control<? extends ControlEvents> c) {
      super(c);
    }
    
    public void onChange(int val) {
      for(ChangeEvent e : _change) {
        e.event(new ChangeEventData(_control, val));
      }
    }
    
    public interface ChangeEvent extends Event<ChangeEventData> { }
    
    public class ChangeEventData extends EventData {
      public final int val;
      
      private ChangeEventData(Control<? extends ControlEvents> control, int val) {
        super(control);
        this.val = val;
      }
    }
  }
}