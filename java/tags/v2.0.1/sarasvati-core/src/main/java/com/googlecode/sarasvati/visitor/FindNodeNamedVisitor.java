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

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.visitor;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;

/**
 * Token visitor which looks for the first non-backtracked node token
 * that is situated on a node with the given name.
 *
 * @author Paul Lorenz
 */
public class FindNodeNamedVisitor extends TokenVisitorAdaptor
{
  private NodeToken match = null;
  private final String nodeName;

  public FindNodeNamedVisitor (final String nodeName)
  {
    this.nodeName = nodeName;
  }

  @Override
  public boolean follow (final ArcToken child)
  {
    return match == null;
  }

  @Override
  public void visit (final NodeToken token)
  {
    if ( match == null &&
         !token.getExecutionType().isBacktracked() &&
         nodeName.equals( token.getNode().getName() ) )
    {
      match = token;
    }
  }

  public NodeToken getMatch ()
  {
    return match;
  }

  public static NodeToken findFirstNamedParent (final NodeToken token, final String nodeName)
  {
    final FindNodeNamedVisitor visitor = new FindNodeNamedVisitor( nodeName );
    TokenTraversals.traverseParentsBreadthFirst( token, visitor );
    return visitor.getMatch();
  }
}