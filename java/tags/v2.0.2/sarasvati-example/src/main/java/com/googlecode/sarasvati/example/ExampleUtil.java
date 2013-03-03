package com.googlecode.sarasvati.example;

public class ExampleUtil
{
  public static void waitFor (final long millis)
  {
    Object o = new Object();
    synchronized ( o )
    {
      try
      {
        o.wait( millis );
      }
      catch (InterruptedException ie )
      {
        // ignore;
      }
    }
  }

}
