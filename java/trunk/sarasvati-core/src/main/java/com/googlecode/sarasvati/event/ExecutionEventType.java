/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.event;

public enum ExecutionEventType
{
  /**
   * NOTE: The eventType ids below are used in constructing bitmasks. Since
   *       the bitmasks are stored in the database, these ids must be
   *       stable. If adding a new event type, it must be assigned the next
   *       highest power of 2.
   */
  PROCESS_CREATED           ( 1 << 0 ),
  PROCESS_STARTED           ( 1 << 1 ),
  PROCESS_PENDING_COMPLETE  ( 1 << 2 ),
  PROCESS_COMPLETED         ( 1 << 3 ),
  PROCESS_PENDING_CANCEL    ( 1 << 4 ),
  PROCESS_CANCELED          ( 1 << 5 ),

  NODE_TOKEN_CREATED        ( 1 << 6 ),
  NODE_TOKEN_ACCEPTED       ( 1 << 7 ),
  NODE_TOKEN_EXECUTED       ( 1 << 8 ),
  NODE_TOKEN_DISCARDED      ( 1 << 9 ),
  NODE_TOKEN_SKIPPED        ( 1 << 10 ),
  NODE_TOKEN_COMPLETED      ( 1 << 11 ),
  NODE_TOKEN_BACKTRACKED    ( 1 << 12 ),
  NODE_TOKEN_DELAYED        ( 1 << 18 ),

  ARC_TOKEN_CREATED         ( 1 << 13 ),
  ARC_TOKEN_PROCESSED       ( 1 << 14 ),
  ARC_TOKEN_COMPLETED       ( 1 << 15 ),
  ARC_TOKEN_BACKTRACKED     ( 1 << 16 ),
  ARC_TOKEN_MERGED          ( 1 << 17 ),
  ARC_TOKEN_INCOMPLETE_JOIN ( 1 << 19 );

  private static final int INVERSE_MASK = 0xFFFFFFFF;

  private int eventType;

  private ExecutionEventType (final int eventType)
  {
    this.eventType = eventType;
  }

  public int getEventType ()
  {
    return eventType;
  }

  public boolean isProcessEvent ()
  {
    return
         this == PROCESS_CREATED ||
           this == PROCESS_STARTED ||
           this == PROCESS_PENDING_COMPLETE ||
           this == PROCESS_COMPLETED ||
           this == PROCESS_PENDING_CANCEL ||
           this == PROCESS_CANCELED;
  }

  public boolean isNodeTokenEvent ()
  {
    return this == NODE_TOKEN_CREATED     ||
           this == NODE_TOKEN_ACCEPTED    ||
           this == NODE_TOKEN_EXECUTED    ||
           this == NODE_TOKEN_COMPLETED   ||
           this == NODE_TOKEN_DISCARDED   ||
           this == NODE_TOKEN_SKIPPED     ||
           this == NODE_TOKEN_BACKTRACKED ||
           this == NODE_TOKEN_DELAYED
           ;
  }

  public boolean isArcTokenEvent ()
  {
    return this == ARC_TOKEN_CREATED     ||
           this == ARC_TOKEN_PROCESSED   ||
           this == ARC_TOKEN_COMPLETED   ||
           this == ARC_TOKEN_MERGED      ||
           this == ARC_TOKEN_BACKTRACKED ||
           this == ARC_TOKEN_INCOMPLETE_JOIN;
  }

  public static int toMask (final ExecutionEventType...eventTypes)
  {
    if ( eventTypes == null || eventTypes.length == 0 )
    {
      return INVERSE_MASK;
    }

    int eventTypeMask = 0;

    for ( ExecutionEventType eventType : eventTypes )
    {
      if ( eventType != null )
      {
        eventTypeMask |= eventType.getEventType();
      }
    }

    return eventTypeMask;
  }

  public static int invertMask (final int mask)
  {
    return mask ^ INVERSE_MASK;
  }

  public static void main (final String[] args)
  {
    for ( ExecutionEventType type : values() )
    {
      System.out.println( type.eventType );
    }
  }
}