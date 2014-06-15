package malachite.engine.world;

import malachite.api.Settings;
import malachite.engine.physics.Movable;

public class Entity extends Movable {
  @Override public String toString() {
    return "Entity '" + _name + "' on " + world + " at (" + _x + ", " + _y + ", " + _z + ')'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
  }
  
  public final int   id;
  public final World world;
  
  private Region _region;
  
  private String _name;
  
  private double _rx, _ry;
  private int    _mx, _my;
  private int    _z;
  
  public Entity(int id, World world) {
    this.id    = id;
    this.world = world;
    
    setAcc(0.148f);
    setDec(0.361f);
    setVelTerm(1.75f);
    
    world.add(this);
    setRegion(world.getRegion(0, 0));
  }
  
  public void setXYZ(float x, float y, int z) {
    setXY(x, y);
    setZ(z);
  }
  
  public void setXY(float x, float y) {
    _x = x;
    _y = y;
    _rx = (_x % Settings.Map.Size);
    _ry = (_y % Settings.Map.Size);
    if(_rx < 0) _rx += Settings.Map.Size;
    if(_ry < 0) _ry += Settings.Map.Size;
    
    int mx = (int)_x / Settings.Map.Size;
    int my = (int)_y / Settings.Map.Size;
    if(_x < 0) mx -= 1;
    if(_y < 0) my -= 1;
    
    if(_mx != mx || _my != my) {
      _mx = mx;
      _my = my;
      
      setRegion(world.getRegion(_mx, _my));
    }
  }
  
  public void setX(float x) {
    _x = x;
    _rx = (_x % Settings.Map.Size);
    if(_rx < 0) _rx += Settings.Map.Size;
    
    int mx = (int)_x / Settings.Map.Size;
    if(_x < 0) mx -= 1;
    if(mx != _mx) {
      _mx = mx;
      setRegion(world.getRegion(_mx, _my));
    }
  }
  
  public void setY(float y) {
    _y = y;
    _ry = (_y % Settings.Map.Size);
    if(_ry < 0) _ry += Settings.Map.Size;
    
    int my = (int)_y / Settings.Map.Size;
    if(_y < 0) my -= 1;
    if(my != _my) {
      _my = my;
      setRegion(world.getRegion(_mx, _my));
    }
  }
  
  public int getZ() {
    return _z;
  }
  
  public void setZ(int z) {
    _z = z;
  }
  
  public double getRX() {
    return _rx;
  }
  
  public double getRY() {
    return _ry;
  }
  
  public int getMX() {
    return _mx;
  }
  
  public int getMY() {
    return _my;
  }
  
  public Region getRegion() {
    return _region;
  }
  
  public void setRegion(Region r) {
    _region = r;
  }
}