package com.googlecode.sarasvati.env;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

class Base64
{
  public static char[] BYTE_TO_CHAR = { 
      'A', 'B', 'C', 'D',
      'E', 'F', 'G', 'H',
      'I', 'J', 'K', 'L',
      'M', 'N', 'O', 'P',
      'Q', 'R', 'S', 'T',
      'U', 'V', 'W', 'X',
      'Y', 'Z', 'a', 'b', 
      'c', 'd', 'e', 'f', 
      'g', 'h', 'i', 'j', 
      'k', 'l', 'm', 'n', 
      'o', 'p', 'q', 'r', 
      's', 't', 'u', 'v', 
      'w', 'x', 'y', 'z', 
      '0', '1', '2', '3',
      '4', '5', '6', '7',
      '8', '9', '=', '/' };
  
  public static byte[] CHAR_TO_BYTE = new byte[128];
 
  static
  {
    for (int i = 0; i < BYTE_TO_CHAR.length; i++)
    {
      CHAR_TO_BYTE[BYTE_TO_CHAR[i]] = (byte)i;
    }
  }
  
  public static String encode(final byte[] bytes)
  {
    try
    {
      final StringWriter stringWriter = new StringWriter();
      final Base64OutputStream out = new Base64OutputStream(stringWriter);
      out.write(bytes);
      out.close();
      stringWriter.close();
      return stringWriter.toString();
    }
    catch(final IOException ioe)
    {
      throw new RuntimeException("Unexpected failure while encoding bytes to base64", ioe);
    }
  }
  
  public static byte[] decode(final String base64)
  {
    try
    {
      final StringReader stringReader = new StringReader(base64);
      final Base64InputStream in = new Base64InputStream(stringReader);
      final int length = ((base64.length() / 4) * 3) + Math.max(0, ((base64.length() % 4) - 1)); 
      byte[] bytes = new byte[length];
      in.read(bytes);
      in.close();
      stringReader.close();
      return bytes;
    }
    catch(final IOException ioe)
    {
      throw new RuntimeException("Unexpected failure while decoding bytes from base64", ioe);
    }
  }
  
  public static void main(String[] args)
  {
    for (int i = 0; i < BYTE_TO_CHAR.length; i++)
    {
      System.out.println(i + ". " + BYTE_TO_CHAR[i] + ": " + (int)BYTE_TO_CHAR[i]);
    }

    for (int i = 0; i < CHAR_TO_BYTE.length; i++)
    {
      if (CHAR_TO_BYTE[i] != 0)
      {
        System.out.println(i + ": " + CHAR_TO_BYTE[i] + " " + (i == BYTE_TO_CHAR[CHAR_TO_BYTE[i]]));
      }
    }
  }
}
