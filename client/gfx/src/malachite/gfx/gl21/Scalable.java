package malachite.gfx.gl21;

import malachite.gfx.Program;
import malachite.gfx.Vertex;
import malachite.gfx.textures.Texture;

import org.lwjgl.opengl.GL11;

public class Scalable extends malachite.gfx.Scalable {
  private final Vertex[] _vertex = new Vertex[36];
  
  private float _borderL;
  private float _borderT;
  private float _borderR;
  private float _borderB;
  private float _borderL2;
  private float _borderT2;
  private float _borderR2;
  private float _borderB2;
  private float _borderH2;
  private float _borderV2;
  private float _tw, _th, _ts;

  public Scalable(Context ctx, Texture texture, Program program, float[] loc, boolean visible) {
    super(ctx, texture, program, loc, visible);
  }

  @Override public void setSize(float[] s1, float[] s2, float tw, float th, float ts) {
    _borderL = s1[0];
    _borderT = s1[1];
    _borderR = s1[2];
    _borderB = s1[3];
    _borderL2 = s2[0];
    _borderT2 = s2[1];
    _borderR2 = s2[2];
    _borderB2 = s2[3];
    _borderH2 = _borderL2 + _borderR2;
    _borderV2 = _borderT2 + _borderB2;
    _tw = tw;
    _th = th;
    _ts = ts;
  }

  @Override protected void createQuad(float[] size, float[] tex, float[] col) {
    if(_texture == null) { return; }

    float[][] border = {
      {0, 0, _borderL, _borderT},
      {_borderL2, 0, _loc[2] - _borderH2, _borderT},
      {_loc[2] - _borderR2, 0, _borderR, _borderT},
      {0, _borderT2, _borderL, _loc[3] - _borderV2},
      {_borderL2, _borderT2, _loc[2] - _borderH2, _loc[3] - _borderV2},
      {_loc[2] - _borderR2, _borderT2, _borderR, _loc[3] - _borderV2},
      {0, _loc[3] - _borderB2, _borderR, _borderB},
      {_borderL2, _loc[3] - _borderB2, _loc[2] - _borderH2, _borderB},
      {_loc[2] - _borderR2, _loc[3] - _borderB2, _borderR, _borderB}
    };

    float[][] borderS = {
      {0,  0, _borderL, _borderT}, {_borderL,  0, _ts, _borderT}, {_tw - _borderR,  0, _borderR, _borderT},
      {0, _borderT, _borderL, _ts}, {_borderL, _borderT, _ts, _ts}, {_tw - _borderR, _borderT, _borderR, _ts},
      {0, _th - _borderB, _borderL, _borderB}, {_borderL, _th - _borderB, _ts, _borderB}, {_tw - _borderR, _th - _borderB, _borderR, _borderB}
    };

    for(int i = 0; i < borderS.length; i++) {
      borderS[i][0] /= _texture.w;
      borderS[i][2] /= _texture.w;
      borderS[i][1] /= _texture.h;
      borderS[i][3] /= _texture.h;
    }

    int i = 0;
    for(int n = 0; n < border.length; n++) {
      Vertex[] v = _ctx.vertices.createQuad(border[n], borderS[n], col);
      _vertex[i++] = v[0];
      _vertex[i++] = v[2];
      _vertex[i++] = v[1];
      _vertex[i++] = v[3];
    }
  }
  
  @Override protected void createBorder(float[] size, float[] tex, float[] col) {
    
  }

  @Override public void draw() {
    if(_vertex == null || !_visible) { return; }

    _matrix.push();
    _matrix.translate(_loc[0], _loc[1]);

    if(_texture != null) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      _texture.use();
    } else {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

    for(Vertex vertex : _vertex) {
      if(vertex == null) { continue; }
      vertex.use();
    }

    GL11.glEnd();

    _matrix.pop();
  }
}