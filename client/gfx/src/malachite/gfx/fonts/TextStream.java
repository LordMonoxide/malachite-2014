package malachite.gfx.fonts;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.vector.Vector4f;

public class TextStream implements Iterable<TextStreamable> {
  private final ArrayList<TextStreamable> _stream = new ArrayList<>();
  
  public TextStream() { }
  
  public TextStream(String... data) {
    insert(data);
  }
  
  public TextStream(TextStreamable... data) {
    insert(data);
  }
  
  public void insert(TextStreamable... data) {
    for(TextStreamable ts : data) {
      insert(ts);
    }
  }
  
  public void insert(String data) {
    insert(new Text(data));
  }
  
  public void insert(String... data) {
    for(String s : data) {
      insert(s);
    }
  }
  
  public void insert(TextStreamable data) {
    _stream.add(data);
  }
  
  public Text text(String data) {
    return new Text(data);
  }
  
  public NewLine newLine() {
    return new NewLine();
  }
  
  public Face face(Font.FONT_FACE face) {
    return new Face(face);
  }
  
  public Colour colour(float r, float g, float b, float a) {
    return new Colour(r, g, b, a);
  }
  
  public Colour colour(Vector4f c) {
    return new Colour(c);
  }
  
  public static class Text implements TextStreamable {
    String _text;
    
    public Text() { }
    
    public Text(String text) {
      _text = text;
    }
    
    public String getText() {
      return _text;
    }
    
    public void setText(String text) {
      _text = text;
    }
    
    @Override public void render(FontRenderState state) {
      if(_text == null) { return; }
      for(int i = 0; i < _text.length(); i++) {
        Font.Glyph glyph = state.face._glyph[state.mask == 0 ? _text.codePointAt(i) : state.mask];
        
        switch(glyph.code) {
          case '\n':
            state.newLine();
            break;
          
          case ' ':
            state.x += 4;
            if(state.x >= state.w && state.w > 0) {
              state.newLine();
            } else {
              state.matrix.translate(4, 0);
            }
            
            break;
          
          default:
            state.x += glyph.w;
            if(state.x >= state.w && state.w > 0) {
              state.newLine();
            }
            
            //TODO: This is just a hack to temporarily get font colour working
            //XXX
            //glyph.setColour(state.c);
            glyph.draw();
            state.matrix.translate(glyph.w, 0);
        }
        
        //TODO: Don't think this is necessary
        /*if(state.x >= state.w && state.w != 0) {
          state.matrix.pop();
          state.matrix.push();
        }*/
      }
    }
  }
  
  public static class NewLine implements TextStreamable {
    @Override public void render(FontRenderState state) {
      state.newLine();
    }
  }
  
  public static class Face implements TextStreamable {
    Font.FONT_FACE _face;
    
    public Face(Font.FONT_FACE face) {
      _face = face;
    }
    
    public Font.FONT_FACE getFace() {
      return _face;
    }
    
    public void setFace(Font.FONT_FACE face) {
      _face = face;
    }
    
    @Override public void render(FontRenderState state) {
      switch(_face) {
        case REGULAR:
          state.face = state.face.font.regular;
          break;
          
        case BOLD:
          state.face = state.face.font.bold;
          break;
          
        case ITALIC:
          state.face = state.face.font.italic;
          break;
      }
    }
  }
  
  public static class Colour implements TextStreamable {
    private Vector4f _c = new Vector4f(1, 1, 1, 1);
    
    public Colour(Vector4f c) {
      setColour(c);
    }
    
    public Colour(float r, float g, float b, float a) {
      setColour(r, g, b, a);
    }
    
    public Vector4f getColour() {
      return _c;
    }
    
    public void setColour(Vector4f c) {
      _c = c;
    }
    
    public void setColour(float r, float g, float b, float a) {
      _c.x = r;
      _c.y = g;
      _c.z = b;
      _c.w = a;
    }
    
    @Override public void render(FontRenderState state) {
      state.recolour.set(_c);
    }
  }
  
  @Override public Iterator<TextStreamable> iterator() {
    return _stream.iterator();
  }
}