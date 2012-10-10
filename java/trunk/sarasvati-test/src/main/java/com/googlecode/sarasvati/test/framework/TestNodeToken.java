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

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSetMember;

public class TestNodeToken extends TestToken<NodeToken>
{
  private final List<TestArcToken> parents  = new LinkedList<TestArcToken>();
  private final List<TestArcToken> children = new LinkedList<TestArcToken>();

  private final String             id;
  private final Node               node;
  private final String             tokenSetName;
  private final int                tokenSetIndex;

  public TestNodeToken (final int lineNumber,
                        final String id,
                        final Node node,
                        final boolean complete,
                        final ExecutionType executionType,
                        final String tokenSetName,
                        final int tokenSetIndex)
  {
    super( lineNumber, complete, executionType );
    this.id = id;
    this.node = node;
    this.tokenSetName = tokenSetName;
    this.tokenSetIndex = tokenSetIndex;
  }

  public String getId ()
  {
    return id;
  }

  public Node getNode ()
  {
    return node;
  }

  public String getTokenSetName ()
  {
    return tokenSetName;
  }

  public int getTokenSetIndex ()
  {
    return tokenSetIndex;
  }

  public List<TestArcToken> getParents ()
  {
    return parents;
  }

  public List<TestArcToken> getChildren ()
  {
    return children;
  }

  public void addChild (final TestArcToken childToken)
  {
    children.add( childToken );
  }

  public void addParent (final TestArcToken parentToken)
  {
    parents.add( parentToken );
  }

  public boolean matchesToken (final NodeToken token)
  {
    if ( !node.equals( token.getNode() ) )
    {
      return false;
    }

    if ( tokenSetName == null )
    {
      return true;
    }

    TokenSetMember member = token.getTokenSetMember( tokenSetName );
    return member != null && member.getMemberIndex() == tokenSetIndex;
  }

  @Override
  public void validate ()
  {
    Assert.assertEquals( "Node does not match on " + toString(), node, getToken().getNode() );
    super.validate();
  }

  @Override
  public String toString ()
  {
    return "[TestNodeToken id=" + id + " line=" + getLineNumber() + "]";
  }
}