package malachite.gfx.fonts;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.fonts.TextStream.Colour;
import malachite.gfx.textures.Texture;

public class Font {
  private static Colour white = new Colour(new float[] {1, 1, 1, 1});
  
  private final Context _ctx;
  
  private boolean _loaded;
  
  public final Events events = new Events(this);
  public final Face regular, bold, italic;
  
  Font(Context ctx) {
    _ctx = ctx;
    
    this.regular = new Face(this);
    this.bold    = new Face(this);
    this.italic  = new Face(this);
  }
  
  public boolean loaded() { return _loaded; }
  
  void load() {
    regular.load();
    bold   .load();
    italic .load();
    
    _loaded = true;
    events.raiseLoad();
  }
  
  public void draw(float x, float y, TextStream text) {
    draw(x, y, 0, 0, text, 0);
  }
  
  public void draw(float x, float y, TextStream text, int mask) {
    draw(x, y, 0, 0, text, mask);
  }
  
  public void draw(float x, float y, int w, int h, TextStream text) {
    draw(x, y, w, h, text, 0);
  }
  
  public void draw(float x, float y, int w, int h, TextStream text, int mask) {
    if(text == null)    { return; }
    if(regular == null) { return; }
    
    _ctx.matrix.push(() -> {
      _ctx.matrix.translate(x, y);
      
      _ctx.matrix.push(() -> {
        FontRenderState state = new FontRenderState(regular, 0, 0, w, h, mask, _ctx.matrix);
        
        white.render(state);
        
        for(TextStreamable s : text) {
          s.render(state);
        }
      });
    });
  }
  
  public class Face {
    public final Font font;
    
    Texture _texture;
    Glyph[] _glyph;
    int     _h;
    
    private Face(Font font) {
      this.font = font;
    }
    
    void load() {
      for(Glyph glyph : _glyph) {
        if(glyph != null) {
          glyph.create(_ctx, _texture);
        }
      }
    }
    
    public int getW(TextStream text) {
      if(text == null) { return 0; }
      
      int w = 0;
      for(TextStreamable ts : text) {
        if(ts instanceof TextStream.Text) {
          w += getW(((TextStream.Text)ts).getText());
        }
      }
      
      return w;
    }
    
    public int getW(String text) { return getW(text, 0); }
    public int getW(String text, int mask) {
      if(text == null) { return 0; }
      
      int w = 0;
      for(int i = 0; i < text.length(); i++) {
        int n = mask == 0 ? text.codePointAt(i) : mask;
        if(_glyph[n] != null) {
          w += _glyph[n].w;
        }
      }
      
      return w;
    }
    
    public int getH() {
      return _h;
    }
    
    public int getCharAtX(String text, int x) { return getCharAtX(text, 0, x); }
    public int getCharAtX(String text, int mask, int x) {
      if(text == null) { return 0; }
      
      int w = 0;
      for(int i = 0; i < text.length(); i++) {
        int n = mask == 0 ? text.codePointAt(i) : mask;
        if(_glyph[n] != null) {
          if(x <= w + _glyph[n].w / 2) {
            return i;
          }
          
          w += _glyph[n].w;
        }
      }
      
      return text.length();
    }
  }
  
  protected static class Glyph {
    protected Drawable sprite;
    protected int code;
    protected int  w,  h;
    protected int tx, ty;
    protected int tw, th;
    
    protected void create(Context ctx, Texture texture) {
      sprite = ctx.drawable()
        .texture(texture)
        .wh(tw, th)
        .st(tx, ty)
        .uv(tw, th)
        .buildQuad();
    }
    
    public void draw() {
      sprite.draw();
    }
  }
  
  public static class Events {
    private Deque<Runnable> _load = new ConcurrentLinkedDeque<>();
    
    private Font _this;
    
    public Events(Font font) {
      _this = font;
    }
    
    public void addLoadHandler(Runnable e) {
      _load.add(e);
      
      if(_this._loaded) {
        raiseLoad();
      }
    }
    
    public void raiseLoad() {
      Runnable e = null;
      while((e = _load.poll()) != null) {
        e.run();
      }
    }
  }
  
  public enum FONT_FACE {
    REGULAR, BOLD, ITALIC;
  }
}