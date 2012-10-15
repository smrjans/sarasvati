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

    Copyright 2010 Paul Lorenz
*/
package com.googlecode.sarasvati.test.env;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.test.TestEnv.ExecutionMode;
import com.googlecode.sarasvati.test.framework.ExecutionTest;

public class TestTransientsAttributes extends ExecutionTest
{
  @Test
  public void testSetGetTokenEnv () throws Exception
  {
    ensureLoaded( "two-node" );
    GraphProcess p = startProcess(  "two-node" );

    NodeToken t = getActiveToken( p, "nodeA");

    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertFalse( t.getFullEnv().hasTransientAttribute( "foo" ) );

    String expected = "bar";
    t.getEnv().setTransientAttribute( "foo", expected );
    String actual = (String)t.getEnv().getTransientAttribute( "foo" );
    Assert.assertEquals( expected, actual );

    Assert.assertTrue( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertTrue( t.getFullEnv().hasTransientAttribute( "foo" ) );

    t.getEnv().removeTransientAttribute( "foo" );
    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertFalse( t.getFullEnv().hasTransientAttribute( "foo" ) );
  }

  @Test
  public void testSetGetTokenFullEnv () throws Exception
  {
    Graph graph = ensureLoaded( "two-node" );
    GraphProcess p = startProcess( graph );

    NodeToken t = getActiveToken( p, "nodeA");

    String expected = "bar";
    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertFalse( t.getFullEnv().hasTransientAttribute( "foo" ) );
    t.getFullEnv().setTransientAttribute( "foo", expected );
    Assert.assertTrue( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertTrue( t.getFullEnv().hasTransientAttribute( "foo" ) );
    String actual = (String)t.getFullEnv().getTransientAttribute( "foo" );

    Assert.assertEquals( expected, actual );

    t.getEnv().removeTransientAttribute( "foo" );
    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertFalse( t.getFullEnv().hasTransientAttribute( "foo" ) );
  }

  @Test
  public void testSetGetProcessEnv () throws Exception
  {
    Graph graph = ensureLoaded( "two-node" );
    GraphProcess p = startProcess( graph );

    NodeToken t = getActiveToken( p, "nodeA");
    p = TestEnv.refreshedProcess(p);

    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertFalse( t.getFullEnv().hasTransientAttribute( "foo" ) );

    String expected = "bar";
    p.getEnv().setTransientAttribute( "foo", expected );
    String actual = (String)p.getEnv().getTransientAttribute( "foo" );
    Assert.assertEquals( expected, actual );

    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertTrue( t.getFullEnv().hasTransientAttribute( "foo" ) );

    p.getEnv().removeTransientAttribute( "foo" );

    Assert.assertFalse( t.getEnv().hasTransientAttribute( "foo" ) );
    Assert.assertFalse( t.getFullEnv().hasTransientAttribute( "foo" ) );
  }

  @Test
  public void testSetGetTokenEnvBetweenTokens () throws Exception
  {
    TestEnv.setExeuctionMode(ExecutionMode.OneSession);
    Graph graph = ensureLoaded( "two-node" );
    GraphProcess p = startProcess( graph );

    NodeToken t = getActiveToken( p, "nodeA");

    String expected = "bar";
    t.getEnv().setTransientAttribute( "foo", expected );

    completeToken( t, Arc.DEFAULT_ARC );

    t = getActiveToken( p, "nodeB");
    String actual = (String)t.getEnv().getTransientAttribute( "foo" );

    Assert.assertEquals( expected, actual );
  }

  @Test
  public void testSetGetTokenFullEnvBetweenTokens () throws Exception
  {
    TestEnv.setExeuctionMode(ExecutionMode.OneSession);

    Graph graph = ensureLoaded( "two-node" );
    GraphProcess p = startProcess( graph );

    NodeToken t = getActiveToken( p, "nodeA");

    String expected = "bar";
    t.getFullEnv().setTransientAttribute( "foo", expected );

    completeToken( t, Arc.DEFAULT_ARC );

    t = getActiveToken( p, "nodeB");
    String actual = (String)t.getFullEnv().getTransientAttribute( "foo" );

    Assert.assertEquals( expected, actual );
  }

  @Test
  public void testSetGetProcessEnvBetweenTokens () throws Exception
  {
    TestEnv.setExeuctionMode(ExecutionMode.OneSession);

    Graph graph = ensureLoaded( "two-node" );
    GraphProcess p = startProcess( graph );

    NodeToken t = getActiveToken( p, "nodeA");

    String expected = "bar";
    p.getEnv().setTransientAttribute( "foo", expected );

    completeToken( t, Arc.DEFAULT_ARC );

    t = getActiveToken( p, "nodeB");
    String actual = (String)p.getEnv().getTransientAttribute( "foo" );

    Assert.assertEquals( expected, actual );
  }
}
