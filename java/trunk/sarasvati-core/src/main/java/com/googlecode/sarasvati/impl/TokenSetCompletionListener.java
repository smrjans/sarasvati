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
package com.googlecode.sarasvati.impl;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;

public class TokenSetCompletionListener implements ExecutionListener
{
  @Override
  public EventActions notify (final ExecutionEvent event)
  {
    if ( event.getEventType() == ExecutionEventType.ARC_TOKEN_COMPLETED )
    {
      ArcToken token = event.getArcToken();
      for ( ArcTokenSetMember setMember : token.getTokenSetMemberships() )
      {
        setMember.getTokenSet().getActiveArcTokens( event.getEngine() ).remove( token );
      }
    }
    else if ( event.getEventType() == ExecutionEventType.NODE_TOKEN_COMPLETED )
    {
      NodeToken token = event.getNodeToken();
      for ( NodeTokenSetMember setMember : token.getTokenSetMemberships() )
      {
        setMember.getTokenSet().getActiveNodeTokens( event.getEngine() ).remove( token );
      }
    }

    return null;
  }
}
