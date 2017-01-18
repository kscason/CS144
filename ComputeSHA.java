// compute sha-1 hash from file
// kaitlyn cason : 204411394
// alexander waz : 504480512

import java.security.*;
import java.io.*;

public class ComputeSHA {
  
  public static void main(String args[]) {

    // instantiate sha-1 digest obj
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      System.out.println("no such algo");
      return;
    }

    // read local file and contain in byte array
    File f = new File(args[0]);
    byte[] b = new byte[(int) f.length()];
    FileInputStream fis;
    try {
      fis = new FileInputStream(f);
      fis.read(b);
      fis.close();
    } catch (IOException e) {
      System.out.println("file error");
    }
    
    // process file through digest
    md.update(b);
    byte[] d = md.digest();

    // returns hash from byte array
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < d.length; i++) {
      sb.append(String.format("%02x", (0xFF & d[i])));
    }

    System.out.println(sb.toString());
  }
}
