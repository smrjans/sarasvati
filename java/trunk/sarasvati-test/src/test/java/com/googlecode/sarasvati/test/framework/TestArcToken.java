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
package com.googlecode.sarasvati.test.framework;

import junit.framework.Assert;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.Node;

public class TestArcToken extends TestToken<ArcToken>
{
  private final TestNodeToken parent;
  private TestNodeToken childToken;
  protected Node          childNode;

  private final boolean       pending;

  public TestArcToken (final int lineNumber,
                       final TestNodeToken parent,
                       final boolean pending,
                       final boolean complete,
                       final ExecutionType executionType)
  {
    super( lineNumber, complete, executionType );
    this.parent = parent;
    this.pending = pending;
  }

  public TestNodeToken getParent ()
  {
    return parent;
  }

  public TestNodeToken getChildToken ()
  {
    return childToken;
  }

  public void setChildToken (final TestNodeToken childToken)
  {
    this.childToken = childToken;
  }

  public Node getChildNode ()
  {
    return childNode;
  }

  public void setChildNode (final Node childNode)
  {
    this.childNode = childNode;
  }

  public boolean isPending ()
  {
    return pending;
  }

  @Override
  public void validate ()
  {
    Assert.assertEquals( "Parent token does not match on " + toString(), parent.getToken(), getToken().getParentToken() );
    Assert.assertEquals( "IsPending? does not match on " + toString(), pending, getToken().isPending() );

    if ( isComplete() )
    {
      Assert.assertNotNull( "Completed test arc token should have child token: " + toString(), childToken );
    }
    else
    {
      Assert.assertNotNull( "Incomplete test arc token should have child node: " + toString(), childNode );
      Assert.assertEquals( "Child node does not match on " + toString(), childNode, getToken().getArc().getEndNode() );
    }

    super.validate();
  }

  @Override
  public String toString ()
  {
    return "[TestArcToken parentId=" + getParent().getId() + " line=" + getLineNumber() + "]";
  }
}