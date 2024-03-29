package malachite.gfx;

import malachite.gfx.fonts.FontBuilder;
import malachite.gfx.textures.Texture;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.Point;
import malachite.gfx.util.Time;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class Context {
  private static final Logger logger = LoggerFactory.getLogger(Context.class);
  
  public final VertexManager  vertices = new VertexManager();
  public final ContextEvents  events   = new ContextEvents();
  public final Camera         camera   = new Camera();
  public final Threads        threads  = new Threads();
  public final TextureBuilder textures;
  public final FontBuilder    fonts;
  public final Matrix         matrix;
  
  private final Deque<Runnable> _loaderCallbacks = new ConcurrentLinkedDeque<>();
  
  private final int[] _selectColour = {1, 0, 0, 255};
  
  private int _mouseX = 0;
  private int _mouseY = 0;
  
  private boolean _running;
  
  private int    _fpsLimit;
  private double _lastSPF;
  private double _spfAvg;
  private int    _spfs;
  
  private final double[] _spf = new double[10];
  
  protected Context() {
    textures = new TextureBuilder();
    fonts    = new FontBuilder(this);
    matrix   = Objects.requireNonNull(createMatrix());
  }
  
  public String  getTitle()     { return Display.getTitle(); }
  public boolean getResizable() { return Display.isResizable(); }
  public int     getW()         { return Display.getWidth(); }
  public int     getH()         { return Display.getHeight(); }
  public int     getFPSLimit()  { return _fpsLimit; }
  public double  getFPS()       { return 1000 / _spfAvg; }
  public double  getSPF()       { return _spfAvg; }
  
  public int getMouseX() { return Mouse.getX(); }
  public int getMouseY() { return getH() - Mouse.getY(); }
  
  public void resize(int w, int h) throws LWJGLException {
    Display.setDisplayMode(new DisplayMode(w, h));
    updateSize();
  }
  
  protected abstract void createDisplay() throws LWJGLException;
  protected abstract void updateSize();
  protected abstract void cleanup();
  
  protected abstract Matrix createMatrix();
  protected abstract Vertex newVertex();
  
  protected abstract Shader  newShader (String source, int type);
  protected abstract Program newProgram(Shader vsh, Shader fsh);
  
  protected abstract Drawable newDrawable(Texture texture, Program program, float[] loc, boolean visible);
  protected abstract Scalable newScalable(Texture texture, Program program, float[] loc, boolean visible);
  
  public DrawableBuilder drawable() { return new DrawableBuilder(this); }
  public ShaderBuilder   shader  () { return new ShaderBuilder  (this); }
  public CanvasBuilder   canvas  () { return new CanvasBuilder  (this); }
  
  final boolean create(String title, boolean resizable, float[] clearColour, int w, int h, int fps) {
    if(!Display.isCreated()) {
      try {
        Display.setTitle(title);
        Display.setResizable(resizable);
        Display.setInitialBackground(clearColour[0], clearColour[1], clearColour[2]);
        Display.setDisplayMode(new DisplayMode(w, h));
        createDisplay();
      } catch(LWJGLException e) {
        logger.error("Error creating context", e); //$NON-NLS-1$
        return false;
      }
    }
    
    _fpsLimit = fps;
    
    logger.info("Creating context {}", Display.getTitle()); //$NON-NLS-1$
    logger.info("Display adapter: {}", Display.getAdapter()); //$NON-NLS-1$
    logger.info("Driver version:  {}", Display.getVersion()); //$NON-NLS-1$
    logger.info("OpenGL version:  {}", GL11.glGetString(GL11.GL_VERSION)); //$NON-NLS-1$
    
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
    Keyboard.enableRepeatEvents(true);
    
    updateSize();
    
    return true;
  }
  
  public void destroy() {
    if(_running) {
      _running = false;
    } else {
      cleanup();
      
      Display.destroy();
    }
  }
  
  public void run() {
    events.raiseLoad();
    
    _running = true;
    
    _lastSPF = Time.get();
    
    while(_running) {
      checkContext();
      checkLoader();
      clearContext();
      drawScene();
      updateContext();
      updateFrameRate();
      mouse();
      syncContext();
    }
    
    destroy();
  }
  
  private void checkContext() {
    if(Display.isCloseRequested()) {
      destroy();
    }
    
    if(Display.wasResized()) {
      updateSize();
    }
  }
  
  private void checkLoader() {
    Runnable cb = _loaderCallbacks.poll();
    if(cb != null) { cb.run(); }
  }
  
  public void clearContext() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }
  
  private void updateContext() {
    Display.update();
  }
  
  private void syncContext() {
    Display.sync(_fpsLimit);
  }
  
  private void drawScene() {
    matrix.push(() -> {
      matrix.translate(camera);
      events.raiseDraw(matrix);
    });
  }
  
  private void updateFrameRate() {
    _spfAvg = 0;
    
    for(int i = _spfs - 1; i > 0; i--) {
      _spf[i] = _spf[i - 1];
      _spfAvg += _spf[i - i];
    }
    
    _spf[0] = Sys.getTime() - _lastSPF;
    
    _spfAvg += _spf[0];
    _spfAvg = _spfAvg / _spfs;
    
    if(_spfs < _spf.length) {
      _spfs++;
    }
    
    _lastSPF = Sys.getTime();
  }
  
  private void mouse() {
    if(_mouseX != getMouseX() || _mouseY != getMouseY()) {
      _mouseX = getMouseX();
      _mouseY = getMouseY();
      events.raiseMouseMove(_mouseX, _mouseY);
    }
    
    if(Mouse.next()) {
      if(Mouse.getEventButton() != -1) {
        if(Mouse.getEventButtonState()) {
          events.raiseMouseDown(_mouseX, _mouseY, Mouse.getEventButton());
        } else {
          events.raiseMouseUp(_mouseX, _mouseY, Mouse.getEventButton());
        }
      }
      
      if(Mouse.getEventDWheel() != 0) {
        events.raiseMouseWheel(Mouse.getEventDWheel());
      }
    }
  }
  
  public int[] getPixel(int x, int y) {
    ByteBuffer pixels = BufferUtils.createByteBuffer(3);
    GL11.glReadPixels(x, getH() - y, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixels);
    byte[] b = {pixels.get(0), pixels.get(1), pixels.get(2)};
    return new int[] {b[0] >= 0 ? b[0] : 256 + b[0], b[1] >= 0 ? b[1] : 256 + b[1], b[2] >= 0 ? b[2] : 256 + b[2]};
  }
  
  public int[] getNextSelectColour() {
    int[] colour = {_selectColour[0], _selectColour[1], _selectColour[2], _selectColour[3]};
    
    _selectColour[0]++;
    if(_selectColour[0] >= 255) {
      _selectColour[0] = 0;
      _selectColour[1]++;
      if(_selectColour[1] >= 255) {
        _selectColour[1] = 0;
        _selectColour[2]++;
      }
    }
    
    return colour;
  }
  
  public class Threads {
    public Threads gfx(Runnable callback) {
      _loaderCallbacks.push(callback);
      return this;
    }
  }
  
  public class Camera extends Point {
    @Override public float getX() {
      return _x + (_bindX != null ? -_bindX.getX() : 0) + getW() / 2;
    }
    
    @Override public float getY() {
      return _y + (_bindY != null ? -_bindY.getY() : 0) + getH() / 2;
    }
  }
  
  public final class VertexManager {
    private static final float TEXEL_OFFSET = 0.5f;
    
    private VertexManager() { }
    
    public Vertex[] newVertices(int count) {
      Vertex[] vertices = new Vertex[count];
      
      for(int i = 0; i < count; i++) {
        vertices[i] = newVertex();
      }
      
      return vertices;
    }
    
    public Vertex[] createQuad(float[] loc, float[] tex, float[] col) {
      Vertex[] v = newVertices(4);
      v[0].set(new float[] {loc[0]         , loc[1]         }, new float[] {tex[0]         , tex[1]         }, col);
      v[1].set(new float[] {loc[0]         , loc[1] + loc[3]}, new float[] {tex[0]         , tex[1] + tex[3]}, col);
      v[2].set(new float[] {loc[0] + loc[2], loc[1]         }, new float[] {tex[0] + tex[2], tex[1]         }, col);
      v[3].set(new float[] {loc[0] + loc[2], loc[1] + loc[3]}, new float[] {tex[0] + tex[2], tex[1] + tex[3]}, col);
      return v;
    }
    
    public Vertex[] createBorder(float[] loc, float[] col) {
      Vertex[] v = newVertices(5);
      v[0].set(new float[] {loc[0]          + TEXEL_OFFSET, loc[1]          + TEXEL_OFFSET}, new float[] {0, 0}, col);
      v[1].set(new float[] {loc[0] + loc[2] - TEXEL_OFFSET, loc[1]          + TEXEL_OFFSET}, new float[] {0, 0}, col);
      v[2].set(new float[] {loc[0] + loc[2] - TEXEL_OFFSET, loc[1] + loc[3] - TEXEL_OFFSET}, new float[] {0, 0}, col);
      v[3].set(new float[] {loc[0]          + TEXEL_OFFSET, loc[1] + loc[3] - TEXEL_OFFSET}, new float[] {0, 0}, col);
      v[4].set(new float[] {loc[0]          + TEXEL_OFFSET, loc[1]          + TEXEL_OFFSET}, new float[] {0, 0}, col);
      return v;
    }
  }
}