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
package com.googlecode.sarasvati.hib;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.JoinType;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSetMember;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;
import com.googlecode.sarasvati.event.NodeTokenEvent;

public class TokenSetDeadEndListener implements ExecutionListener
{
  private static final String KEY = "com.googlecode.sarasvati.tokenset.tokens_waiting_for_tokenset";

  @Override
  public EventActions notify (final ExecutionEvent event)
  {
    if ( event.getEventType() == ExecutionEventType.ARC_TOKEN_INCOMPLETE_JOIN &&
         event.getArcToken().getArc().getEndNode().getJoinType() == JoinType.TOKEN_SET )
    {
      for ( TokenSetMember setMember : event.getArcToken().getTokenSetMemberships() )
      {
        final Env tsEnv = setMember.getTokenSet().getEnv();
        long[] checkNodes = tsEnv.getAttribute(KEY, long[].class);
        if (checkNodes == null)
        {
          checkNodes = new long[] { event.getArcToken().getId() };
        }
        else
        {
          final long[] newCheckNodes = new long[checkNodes.length + 1];
          System.arraycopy(checkNodes, 0, newCheckNodes, 0, checkNodes.length);
          newCheckNodes[checkNodes.length] = event.getArcToken().getId();
        }
        tsEnv.setAttribute(KEY, checkNodes);
      }
    }
    else if ( event.getEventType() == ExecutionEventType.NODE_TOKEN_COMPLETED )
    {
      final NodeToken token = event.getNodeToken();
      final NodeTokenEvent nodeTokenEvent = (NodeTokenEvent) event;
      if (nodeTokenEvent.getExitArcs().isEmpty())
      {
        final HibEngine hibEngine = (HibEngine)event.getEngine();
        for ( NodeTokenSetMember setMember : token.getTokenSetMemberships() )
        {
          final Env tsEnv = setMember.getTokenSet().getEnv();
          long[] checkNodes = tsEnv.getAttribute(KEY, long[].class);
          if (checkNodes != null)
          {
            for (final long arcTokenId : checkNodes)
            {
              final ArcToken arcToken = hibEngine.getRepository().loadArcToken(arcTokenId);
              event.getEngine().retryIncompleteArcToken(arcToken);
            }
          }
        }
      }
    }

    return null;
  }
}