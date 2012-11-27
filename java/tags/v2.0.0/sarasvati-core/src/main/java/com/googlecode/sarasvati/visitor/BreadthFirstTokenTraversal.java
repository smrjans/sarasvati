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

    Copyright 2008-2009 Paul Lorenz
*/

package com.googlecode.sarasvati.visitor;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;


public class BreadthFirstTokenTraversal implements TokenTraversal
{
  protected final LinkedList<NodeToken> nodeTokenQueue = new LinkedList<NodeToken>();
  protected final LinkedList<ArcToken> arcTokenQueue = new LinkedList<ArcToken>();

  protected final Set<Long> nodeTokens = new HashSet<Long>();
  protected final boolean forward;

  public BreadthFirstTokenTraversal ()
  {
    this.forward = true;
  }

  public BreadthFirstTokenTraversal (final boolean forward)
  {
    this.forward = forward;
  }

  @Override
  public void traverse (final NodeToken token, final TokenVisitor visitor)
  {
    enqueueNodeToken( token );
    traverse( visitor );
  }

  @Override
  public void traverse (final ArcToken token, final TokenVisitor visitor)
  {
    arcTokenQueue.add( token );
    traverse( visitor );
  }

  protected void traverse (final TokenVisitor visitor)
  {
    while ( !nodeTokenQueue.isEmpty() || !arcTokenQueue.isEmpty() )
    {
      while ( !nodeTokenQueue.isEmpty() )
      {
        NodeToken token = nodeTokenQueue.removeFirst();
        token.accept( visitor );
        arcTokenQueue.addAll( forward ? token.getChildTokens() : token.getParentTokens() );
      }

      while ( !arcTokenQueue.isEmpty() )
      {
        ArcToken token = arcTokenQueue.removeFirst();

        if ( visitor.follow( token ) )
        {
          token.accept( visitor );
          enqueueNodeToken( forward ? token.getChildToken() : token.getParentToken() );
        }
      }
    }
  }

  protected void enqueueNodeToken (final NodeToken token)
  {
    if ( token != null && !nodeTokens.contains( token.getId() ) )
    {
      nodeTokenQueue.add( token );
      nodeTokens.add( token.getId() );
    }
  }
}