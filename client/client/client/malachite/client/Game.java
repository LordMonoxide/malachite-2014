package malachite.client;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import net.Client;
import malachite.engine.GameInterface;

public class Game implements GameInterface {
  private Client _client;
  
  @Override public void start() {
    _client = new Client();
    _client.setTimeout(3000);
    
    try(Scanner in = new Scanner(System.in)) {
      for(;;) {
        System.out.print("IP? [127.0.0.1] "); //$NON-NLS-1$
        String input = in.nextLine().toLowerCase();
        if(input.isEmpty()) { input = "127.0.0.1"; }
        
        try {
          InetAddress addr = InetAddress.getByName(input);
          if(addr.getHostAddress().equals(input) && addr instanceof Inet4Address) {
            _client.setAddress(input, 4000);
            break;
          }
        } catch(UnknownHostException e) {
          e.printStackTrace();
        }
      }
    }

    _client.connect(success -> {
      if(success) {
        System.out.println("Connected");
      } else {
        System.out.println("Connection failed");
        _client.shutdown();
      }
    });
  }
}