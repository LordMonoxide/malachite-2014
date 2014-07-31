package malachite.gfx.gui.control;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;

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
    if(bounds.getW() >= bounds.getH()) {
      //TODO:
      _dec.bounds.wh.set(bounds.getW() / 2, bounds.getH());
      
      _inc.bounds.xy.set(_dec.bounds.getW(), 0);
      _inc.bounds.wh.set(bounds.getW() - _dec.bounds.getW(), bounds.getH());
    } else {
      _dec.bounds.wh.set(bounds.getW(), bounds.getH() / 2);
      
      _inc.bounds.xy.set(0, _dec.bounds.getH());
      _inc.bounds.wh.set(bounds.getW(), bounds.getH() - _dec.bounds.getH());
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