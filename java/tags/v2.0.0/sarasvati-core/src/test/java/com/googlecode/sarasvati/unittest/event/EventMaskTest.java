/**
 * Created on Sep 3, 2009
 */
package com.googlecode.sarasvati.unittest.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.event.DefaultExecutionEventQueue;
import com.googlecode.sarasvati.event.ExecutionEventType;

public class EventMaskTest
{
  @Test
  public void testEventMask ()
  {
    DefaultExecutionEventQueue.RegisteredExecutionListener regListener = new DefaultExecutionEventQueue.RegisteredExecutionListener( null, 0 );

    for ( ExecutionEventType eventType : ExecutionEventType.values() )
    {
      Assert.assertFalse( regListener.isRegisteredForEventType( eventType ) );
      regListener.addEventTypesMask( ExecutionEventType.toMask( eventType ) );
      Assert.assertTrue( regListener.isRegisteredForEventType( eventType ) );
    }

    for ( ExecutionEventType eventType : ExecutionEventType.values() )
    {
      Assert.assertTrue( regListener.isRegisteredForEventType( eventType ) );
      regListener.removeEventType( eventType );
      Assert.assertFalse( regListener.isRegisteredForEventType( eventType ) );
    }

    for ( ExecutionEventType eventType : ExecutionEventType.values() )
    {
      Assert.assertFalse( regListener.isRegisteredForEventType( eventType ) );
      regListener.addEventTypesMask( ExecutionEventType.toMask( eventType ) );
      Assert.assertTrue( regListener.isRegisteredForEventType( eventType ) );
    }

    List<ExecutionEventType> eventTypes = Arrays.asList( ExecutionEventType.values() );
    Collections.reverse( eventTypes );

    for ( ExecutionEventType eventType : eventTypes )
    {
      Assert.assertTrue( regListener.isRegisteredForEventType( eventType ) );
      regListener.removeEventType( eventType );
      Assert.assertFalse( regListener.isRegisteredForEventType( eventType ) );
    }
  }
}
