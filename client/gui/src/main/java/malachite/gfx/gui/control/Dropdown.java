package malachite.gfx.gui.control;

import java.util.ArrayList;
import java.util.List;

import malachite.gfx.Context;
import malachite.gfx.Scalable;
import malachite.gfx.fonts.Font;
import malachite.gfx.fonts.FontBuilder;
import malachite.gfx.fonts.TextStream;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.GUI;
import malachite.gfx.textures.Texture;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.Bounds;

public class Dropdown<T> extends Control<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  
  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Texture _textureNormal = _textures.getTexture("gui/textbox.png");
  private Texture _textureHover = _textures.getTexture("gui/textbox_hover.png");
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  
  private List<Item> _items = new ArrayList<>();
  private int _selected = -1;
  
  private Drop _drop;
  
  public Dropdown() {
    super(
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER,
      InitFlags.WITH_DEFAULT_EVENTS
    );
    
    _drop = new Drop();
    
    _events.onFocusGot(e -> {
      _border.setColour(_hoverBorder);
      _border.createBorder();
    }).onFocusLost(e -> {
      _border.setColour(_normalBorder);
      _border.createBorder();
    });
    
    _events.onHoverIn(e -> {
      _background.setTexture(_textureHover);
    }).onHoverOut(e -> {
      _background.setTexture(_textureNormal);
    });
    
    _events.onClick(e -> {
      _drop.push();
      _drop.resize();
      _drop.bounds.xy.set(calculateTotalX(), calculateTotalY());
    });
    
    _border.setColour(_normalBorder);
    
    Scalable s = Context.newScalable();
    _background = s;
    
    s.setTexture(_textureNormal);
    s.setXY(-5, -5);
    s.setSize(
      new float[] {12, 12, 12, 12},
      new float[] {12, 12, 12, 12},
      25, 25, 1
    );
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
  }
  
  @Override protected void resize() {
    
  }
  
  public void clear() {
    _selected = -1;
    _items = new ArrayList<>();
  }
  
  public Item add(String text, T data) {
    Item item = new Item(text, data);
    _items.add(item);
    
    if(_selected == -1) {
      setSelected(0);
    }
    
    return item;
  }
  
  public int getSelected() {
    return _selected;
  }
  
  public void setSelected(int index) {
    _selected = Math.max(0, Math.min(index, _items.size() - 1));
  }
  
  public String getText() {
    if(_selected != -1) {
      return _items.get(_selected)._text.getText();
    }
    
    return null;
  }
  
  public T getData() {
    if(_selected != -1) {
      return _items.get(_selected)._data;
    }
    
    return null;
  }
  
  @Override public void draw() {
    if(drawBegin()) {
      if(_selected != -1) {
        _font.draw(0, 0, _items.get(_selected)._textStream);
      }
    }
    
    drawEnd();
    drawNext();
  }
  
  public class Item {
    private TextStream _textStream = new TextStream();
    private TextStream.Text _text = new TextStream.Text();
    private T      _data;
    
    private Item(String text, T data) {
      _textStream.insert(_textColour, _text);
      _text.setText(text);
      _data = data;
    }
    
    public String getText() { return _text.getText(); }
    public T      getData() { return _data; }
    
    public void setText(String text) { _text.setText(text); }
    public void setData(T      data) { _data = data; }
  }
  
  private class Drop extends GUI {
    private Frame _frame;
    public Bounds bounds;
    
    @Override protected void load() {
      _frame = new Frame();
      _frame.events().onDraw(e -> {
        int y = 0;
        
        for(Item item : _items) {
          _font.draw(0, y, item._textStream);
          y += _font.regular().getH();
        }
      });
      
      _frame.events().onMouseDown(e -> {
        setSelected(e.y / _font.regular().getH());
      });
      
      bounds = _frame.bounds;
      
      controls().add(_frame);
    }
    
    @Override public void destroy() {
      
    }
    
    @Override protected void resize() {
      bounds.setH(_items.size() * _font.regular().getH());
    }
    
    @Override protected void draw() {
      
    }
    
    @Override protected boolean logic() {
      return false;
    }
    
    @Override protected boolean handleMouseDown(int x, int y, int button) {
      pop();
      return true;
    }
  }
}