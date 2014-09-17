package malachite.gfx.fonts;

import malachite.gfx.Matrix;
import malachite.gfx.shaders.Uniform;

class FontRenderState {
  Font.Face face;
  int x, y, w, h;
  int mask;
  Uniform recolour;
  
  Matrix matrix;
  
  FontRenderState(Font.Face face, int x, int y, int w, int h, int mask, Matrix matrix, Uniform recolour) {
    this.face = face;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.mask = mask;
    this.matrix = matrix;
    this.recolour = recolour;
  }
  
  void newLine() {
    matrix.pop();
    matrix.translate(0, face.getH());
    matrix.push();
    x = 0;
  }
}