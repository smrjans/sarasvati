package com.googlecode.sarasvati.example;

import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionListener;

public class LoggingExecutionListener implements ExecutionListener
{
  @Override
  public EventActions notify (final ExecutionEvent event)
  {
    if ( event.isProcessEvent() )
    {
      System.out.println( event.getEventType() + ": " + event.getProcess() );
    }

    if ( event.isNodeTokenEvent() )
    {
      System.out.println( event.getEventType() + ": " + event.getNodeToken() );
    }

    if ( event.isArcTokenEvent() )
    {
      System.out.println( event.getEventType() + ": " + event.getArcToken() );
    }

    return null;
  }
}
