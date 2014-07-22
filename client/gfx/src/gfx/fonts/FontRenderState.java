package gfx.fonts;

import gfx.Matrix;

class FontRenderState {
  Font.Face face;
  int x, y, w, h;
  int mask;
  float[] c;
  
  Matrix matrix;

  FontRenderState(Font.Face face, int x, int y, int w, int h, int mask, Matrix matrix) {
    this.face = face;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.mask = mask;
    this.matrix = matrix;
  }
  
  void newLine() {
    matrix.pop();
    matrix.translate(0, face.getH());
    matrix.push();
    x = 0;
  }
}