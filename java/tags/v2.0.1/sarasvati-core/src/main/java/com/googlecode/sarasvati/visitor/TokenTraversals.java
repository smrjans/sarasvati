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

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;

public class TokenTraversals
{
  //==========================================================================================
  //               Breadth First Traversals
  //==========================================================================================

  public static void traverseParentsBreadthFirst (final NodeToken token, final TokenVisitor visitor)
  {
    new BreadthFirstTokenTraversal( false ).traverse( token, visitor );
  }

  public static void traverseChildrenBreadthFirst (final NodeToken token, final TokenVisitor visitor)
  {
    new BreadthFirstTokenTraversal( true ).traverse( token, visitor );
  }

  public static void traverseParentsBreadthFirst (final ArcToken token, final TokenVisitor visitor)
  {
    new BreadthFirstTokenTraversal( false ).traverse( token, visitor );
  }

  public static void traverseChildrenBreadthFirst (final ArcToken token, final TokenVisitor visitor)
  {
    new BreadthFirstTokenTraversal( true ).traverse( token, visitor );
  }

  //==========================================================================================
  //               Depth First Traversals
  //==========================================================================================

  public static void traverseParentsDepthFirst (final NodeToken token, final TokenVisitor visitor)
  {
    new DepthFirstTokenTraversal( false ).traverse( token, visitor );
  }

  public static void traverseChildrenDepthFirst (final NodeToken token, final TokenVisitor visitor)
  {
    new DepthFirstTokenTraversal( true ).traverse( token, visitor );
  }

  public static void traverseParentsDepthFirstTraversal (final ArcToken token, final TokenVisitor visitor)
  {
    new DepthFirstTokenTraversal( false ).traverse( token, visitor );
  }

  public static void traverseChildrenDepthFirstTraversal (final ArcToken token, final TokenVisitor visitor)
  {
    new DepthFirstTokenTraversal( true ).traverse( token, visitor );
  }

  //==========================================================================================
  //               Create Order Traversals
  //==========================================================================================

  public static void traverseChildrenInCreateOrder (final NodeToken token, final TokenVisitor visitor)
  {
    new CreateOrderTokenTraversal( true ).traverse( token, visitor );
  }

  public static void traverseChildrenInCreateOrder (final ArcToken token, final TokenVisitor visitor)
  {
    new CreateOrderTokenTraversal( true ).traverse( token, visitor );
  }
}