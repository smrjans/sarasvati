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

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.Token;

public class CreateOrderTokenTraversal implements TokenTraversal
{
  protected static final Comparator<Token> tokenSorter = new Comparator<Token>()
  {
    @Override
    public int compare (final Token o1, final Token o2)
    {
      if ( o1 instanceof NodeToken )
      {
        NodeToken t1 = (NodeToken)o1;
        if ( o2 instanceof NodeToken )
        {
          NodeToken t2 = (NodeToken)o2;
          return t1.getCreateDate().compareTo( t2.getCreateDate() );
        }

        ArcToken t2 = (ArcToken)o2;
        return t1.getCreateDate().compareTo( t2.getParentToken().getCompleteDate() );
      }

      ArcToken t1 = (ArcToken)o1;
      if ( o2 instanceof NodeToken )
      {
        NodeToken t2 = (NodeToken)o2;
        return t1.getParentToken().getCompleteDate().compareTo( t2.getCreateDate() );
      }

      ArcToken t2 = (ArcToken)o2;
      return t1.getParentToken().getCompleteDate().compareTo( t2.getParentToken().getCompleteDate() );
    }
  };

  private final Queue<Token> queue = new PriorityQueue<Token>( 10, tokenSorter );
  private final Set<Long> nodeTokens = new HashSet<Long>();

  private final boolean forward;

  public CreateOrderTokenTraversal ()
  {
    this.forward = true;
  }

  public CreateOrderTokenTraversal (final boolean forward)
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
    queue.add( token );
    traverse( visitor );
  }

  protected void traverse (final TokenVisitor visitor)
  {
    while ( !queue.isEmpty() )
    {
      Token next = queue.remove();

      if ( next instanceof NodeToken )
      {
        NodeToken token = (NodeToken)next;
        token.accept( visitor );
        queue.addAll( forward ? token.getChildTokens() : token.getParentTokens() );
      }
      else
      {
        ArcToken token = (ArcToken)next;

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
      queue.add( token );
      nodeTokens.add( token.getId() );
    }
  }
}