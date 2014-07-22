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
    if(_w >= _h) {
      _dec.setXYWH(0, 0, _w / 2, _h);
      _inc.setXYWH(_dec.getW(), 0, _w - _dec.getW(), _h);
    } else {
      _dec.setXYWH(0, 0, _w, _h / 2);
      _inc.setXYWH(0, _dec.getH(), _w, _h - _dec.getH());
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