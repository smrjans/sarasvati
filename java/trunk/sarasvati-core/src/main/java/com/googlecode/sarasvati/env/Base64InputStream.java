package com.googlecode.sarasvati.env;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class Base64InputStream extends InputStream
{
  private final Reader reader;
  
  int current = 0;
  int index = 0;
  int available = 0;
  
  public Base64InputStream(final Reader reader)
  {
    this.reader = reader;
  }
  
  @Override
  public int read() throws IOException
  {
    if (available == 0)
    {
      index = 0;
      current = reader.read();
      if (current == -1)
      {
        return -1;
      }
      
      current = Base64.CHAR_TO_BYTE[current];
      
      int next = reader.read();
      if (next == -1)
      {
        throw new IOException("Unexpected end of stream");
      }
      current = current << 6 | Base64.CHAR_TO_BYTE[next];
      
      available = 1;

      next = reader.read();
      if (next != -1)
      {
        current = current << 6 | Base64.CHAR_TO_BYTE[next];
        available = 2;
        
        next = reader.read();
        if (next != -1)
        {
          current = current << 6 | Base64.CHAR_TO_BYTE[next];
          available = 3;
        }
        else
        {
          current = current << 6;
        }
      }
      else
      {
        current = current << 12;
      }
    }
    
    available--;
    index++;
    
    if (index == 1)
    {
      return (current >> 16) & 255;
    }
    
    if (index == 2)
    {
      return (current >> 8) & 255;
    }
    
    return current & 255;
  }
}
