package malachite.engine.physics;

import malachite.engine.util.Point;

public abstract class Movable {
  public final Point loc = new Point();
  protected float _bear;
  protected float _acc, _dec;
  protected float _vel;
  protected float _velTerm;
  protected float _velTarget;
  
  public float getX() { return loc.getX(); }
  public float getY() { return loc.getY(); }
  
  public void setX(float x) { loc.setX(x); }
  public void setY(float y) { loc.setY(y); }
  
  public final float getBear     () { return _bear; }
  public final float getAcc      () { return _acc; }
  public final float getDec      () { return _dec; }
  public final float getVel      () { return _vel; }
  public final float getVelTerm  () { return _velTerm; }
  public final float getVelTarget() { return _velTarget; }
  
  public final void setBear     (float bear)      { _bear      = bear; }
  public final void setAcc      (float acc)       { _acc       = acc; }
  public final void setDec      (float dec)       { _dec       = dec; }
  public final void setVel      (float vel)       { _vel       = vel; }
  public final void setVelTerm  (float velTerm)   { _velTerm   = velTerm; }
  public final void setVelTarget(float velTarget) { _velTarget = velTarget; }
  
  public void startMoving() {
    _velTarget = _velTerm;
  }
  
  public void stopMoving() {
    _velTarget = 0;
  }
}