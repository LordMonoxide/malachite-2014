package malachite.client.gui;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import malachite.api.Settings;
import malachite.client.game.Game;
import malachite.client.gfx.RegionRenderer;
import malachite.engine.world.Region;
import malachite.gfx.fonts.Font;
import malachite.gfx.fonts.FontBuilder;
import malachite.gfx.fonts.TextStream;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.control.Textbox;

public class GameGUI extends GUI {
  private static final Font _font = FontBuilder.getInstance().getDefault();
  
  public final Game.GameInterface gameInterface;
  
  private HashMap<String, RegionRenderer> _region = new HashMap<>();

  private Textbox _txtChat;
  
  private boolean[] _key = new boolean[4];
  private boolean   _showChat;
  
  public GameGUI(Game.GameInterface gi) {
    gameInterface = gi;
    ready();
  }
  
  @Override protected void load() {
    _txtChat = new Textbox();
    _txtChat.setX(4);
    _txtChat.setWH(200, 20);
    _txtChat.hide();
    _txtChat.events().addKeyHandler(new Textbox.Events.Key() {
      @Override public void up(int key) { }
      @Override public void text(char key) { }
      @Override public void down(int key, boolean repeat) {
        handleChatText(key, repeat);
      }
    });
    
    controls().add(_txtChat);
    
    _context.camera.bind(gameInterface.me().loc);
    
    resize();
  }
  
  @Override public void destroy() {
    
  }
  
  @Override protected void resize() {
    _txtChat.setY(_context.getH() - _txtChat.getH() - 4);
  }
  
  private RegionRenderer getRegion(Region r) {
    RegionRenderer rr = _region.get(r.name);
    
    if(rr == null) {
      rr = new RegionRenderer(r);
      _region.put(r.name, rr);
    }
    
    return rr;
  }
  
  @Override protected void draw() {
    for(int z = 0; z < Settings.Map.Depth; z++) {
      getRegion(gameInterface.me().getRegion()).draw(z);
    }
    
    _font.draw(4, 4, new TextStream(String.valueOf(Math.round(_context.getFPS())) + " FPS")); //$NON-NLS-1$
  }
  
  @Override protected boolean logic() {
    return false;
  }
  
  @Override protected boolean handleKeyDown(int key, boolean repeat) {
    if(_txtChat.isHidden()) {
      switch(key) {
        case Keyboard.KEY_W:
          _key[0] = true;
          checkMovement();
          return true;
          
        case Keyboard.KEY_A:
          _key[2] = true;
          checkMovement();
          return true;
          
        case Keyboard.KEY_S:
          _key[1] = true;
          checkMovement();
          return true;
        
        case Keyboard.KEY_D:
          _key[3] = true;
          checkMovement();
          return true;
          
        case Keyboard.KEY_T:
          _txtChat.show();
          _showChat = true;
          return true;
          
        case Keyboard.KEY_SLASH:
          _txtChat.show();
          _txtChat.setFocus(true);
          return true;
      }
    }
    
    return false;
  }
  
  @Override protected boolean handleKeyUp(int key) {
    if(_txtChat.isHidden()) {
      switch(key) {
        case Keyboard.KEY_W:
          _key[0] = false;
          checkMovement();
          return true;
          
        case Keyboard.KEY_A:
          _key[2] = false;
          checkMovement();
          return true;
          
        case Keyboard.KEY_S:
          _key[1] = false;
          checkMovement();
          return true;
          
        case Keyboard.KEY_D:
          _key[3] = false;
          checkMovement();
          return true;
      }
    } else {
      if(_showChat) {
        _txtChat.setFocus(true);
        _showChat = false;
      }
    }
    
    return false;
  }
  
  private void handleChatText(int key, boolean repeat) {
    if(!repeat) {
      if(key == Keyboard.KEY_RETURN) {
        _txtChat.hide();
        
        if(_txtChat.getText() == null || _txtChat.getText().length() == 0) {
          return;
        }
        
        String chat = _txtChat.getText();
        _txtChat.setText(null);
        
        System.out.println("Chat: " + chat);
        //_game.send(new Chat(chat));
      }
    }
  }
  
  private void checkMovement() {
    float a = -1;
    if(_key[0] && !_key[1]) {
      if(_key[2] && !_key[3]) {
        a = 225;
      } else if(_key[3] && !_key[2]) {
        a = 315;
      } else {
        a = 270;
      }
    } else if(_key[1] && !_key[0]) {
      if(_key[2] && !_key[3]) {
        a = 135;
      } else if(_key[3] && !_key[2]) {
        a = 45;
      } else {
        a = 90;
      }
    } else {
      if(_key[2] && !_key[3]) {
        a = 180;
      } else if(_key[3] && !_key[2]) {
        a = 0;
      }
    }
    
    if(a != -1) {
      if(gameInterface.me().getVelTarget() == 0) {
        gameInterface.startMoving(a);
      } else {
        if(gameInterface.me().getBear() != a) {
          gameInterface.startMoving(a);
        }
      }
    } else {
      if(gameInterface.me().getVelTarget() != 0) {
        gameInterface.stopMoving();
      }
    }
  }
}