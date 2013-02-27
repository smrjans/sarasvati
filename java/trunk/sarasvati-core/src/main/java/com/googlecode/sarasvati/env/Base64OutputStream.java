package com.googlecode.sarasvati.env;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class Base64OutputStream extends OutputStream
{
  private static final int MASK_6_BITS = 63;
  private final Writer writer;
  
  int current = 0;
  int index = 0;
  
  public Base64OutputStream(final Writer writer)
  {
    this.writer = writer;
  }
  
  @Override
  public void write(final int b) throws IOException
  {
    current |= b & 0xFF;
    index++;
    if (index == 3)
    {
      int charIdx = (current >> 18) & MASK_6_BITS;
      writer.write(Base64.BYTE_TO_CHAR[charIdx]);
      
      charIdx = (current >> 12) & MASK_6_BITS;
      writer.write(Base64.BYTE_TO_CHAR[charIdx]);
      
      charIdx = (current >> 6) & MASK_6_BITS;
      writer.write(Base64.BYTE_TO_CHAR[charIdx]);
      
      charIdx = current & MASK_6_BITS;
      writer.write(Base64.BYTE_TO_CHAR[charIdx]);
      
      current = 0;
      index = 0;
    }
    else
    {
      current = current << 8;
    }
  }
  
  @Override
  public void close() throws IOException
  {   
    if (index == 0)
    {
      return;
    }
    
    if (index == 1)
    {
      current = current << 8;
    }
    
    int charIdx = (current >> 18) & MASK_6_BITS;
    writer.write(Base64.BYTE_TO_CHAR[charIdx]);
    
    charIdx = (current >> 12) & MASK_6_BITS;
    writer.write(Base64.BYTE_TO_CHAR[charIdx]);      

    if (index == 2)
    {
      charIdx = (current >> 6) & MASK_6_BITS;
      writer.write(Base64.BYTE_TO_CHAR[charIdx]);      
    }
  }
}
