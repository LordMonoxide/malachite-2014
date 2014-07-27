package gfx.gui.control;

import gfx.Context;
import gfx.Scalable;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.textures.Texture;
import gfx.textures.TextureBuilder;

public class List<T> extends Control<ControlEvents> {
  private Texture _itemBG = TextureBuilder.getInstance().getTexture("gui/listitem.png");
  
  private Frame _inner;
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private Item _selected;
  
  public List() {
    super(
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER,
      InitFlags.WITH_DEFAULT_EVENTS
    );
    
    events().onFocusGot(e -> {
      _border.setColour(_hoverBorder);
      _border.createBorder();
    }).onFocusLost(e -> {
      _border.setColour(_normalBorder);
      _border.createBorder();
    });
    
    _border.setColour(_normalBorder);
    
    _inner = new Frame();
    controls().add(_inner);
  }
  
  public void clear() {
    Control<? extends ControlEvents> c = _inner.controls().first();
    while(c != null) {
      _inner.controls().remove(c);
      c = c.controlPrev();
    }
  }
  
  public Item add(String name, T data) {
    Item i = new Item(name, data);
    i.setY(_inner.controls().size() * 20);
    i.setWH(_w, 20);
    _inner.controls().add(i);
    return i;
  }
  
  public Item selected() {
    return _selected;
  }
  
  @Override protected void resize() {
    _inner.setWH(_w, _h);
    
    Control<? extends ControlEvents> c = _inner.controls().first();
    while(c != null) {
      c.setW(_w);
      c = c.controlPrev();
    }
  }
  
  public class Item extends Control<ControlEvents> {
    private Label  _text;
    private T      _data;
    
    private Item(String text, T data) {
      super(
        InitFlags.REGISTER,
        InitFlags.ACCEPTS_FOCUS,
        InitFlags.WITH_BORDER,
        InitFlags.WITH_DEFAULT_EVENTS
      );
      
      _text = new Label();
      _text.setTextColour(65f / 255, 52f / 255, 8f / 255, 1);
      _text.setText(text);
      _text.setX(4);
      
      Scalable s = Context.newScalable();
      _background = s;
      
      s.setTexture(_itemBG);
      s.setXY(-1, -1);
      s.setSize(
        new float[] {5, 5, 5, 5},
        new float[] {5, 5, 5, 5},
        11, 11, 1
      );
      
      _border.setColour(_normalBorder);
      
      _data = data;
      
      controls().add(_text);
      
      events().onFocusGot(e -> {
        _border.setColour(_hoverBorder);
        _border.createBorder();
        _selected = (List<T>.Item)e.control;
      }).onFocusLost(e -> {
        _border.setColour(_normalBorder);
        _border.createBorder();
      });
    }
    
    public String getText() { return _text.getText(); }
    public T      getData() { return _data; }
    
    public void setText(String text) { _text.setText(text); }
    public void setData(T      data) { _data = data; }
    
    @Override protected void resize() {
      _text.setY(_h / 2);
    }
  }
}