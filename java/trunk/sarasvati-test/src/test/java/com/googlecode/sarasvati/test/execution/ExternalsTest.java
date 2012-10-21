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

    Copyright 2012 Paul Lorenz
*/
package com.googlecode.sarasvati.test.execution;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class ExternalsTest extends ExecutionTest
{
  public static final AtomicInteger processCount = new AtomicInteger(0);

  public static class ExternalTestNode extends CustomNode
  {
    private void testProcessEnv(final Env env)
    {
      int count = env.getAttribute("count", Integer.class);
      env.setAttribute("count", ++count);
      processCount.incrementAndGet();
    }

    private void testTokenEnv(final NodeToken token)
    {
      String path = token.getEnv().getAttribute("path");
      if (path == null)
      {
        Assert.assertTrue(token.getParentTokens().isEmpty());
        path = token.getNode().getName();
      }
      else
      {
        String[] pathNodes = path.split(",");
        NodeToken current = token;

        for (int i = pathNodes.length - 1; i >= 0; i--)
        {
          String nodeName = pathNodes[i];
          NodeToken parent = null;
          for (final ArcToken t : current.getParentTokens())
          {
            if (nodeName.equals(t.getParentToken().getNode().getName()))
            {
              parent = t.getParentToken();
            }
          }
          if (parent == null)
          {
            throw new AssertionError("Parent not found. Path: " + path);
          }
          current = parent;
        }
        path = path + "," + token.getNode().getName();
      }

      token.getEnv().setAttribute("path", path);
    }

    /**
     * @see com.googlecode.sarasvati.Node#execute(com.googlecode.sarasvati.Engine, com.googlecode.sarasvati.NodeToken)
     */
    @Override
    public void execute(final Engine engine, final NodeToken token)
    {
      testProcessEnv(token.getProcess().getEnv());
      testTokenEnv(token);
    }
  }

  private void validateEnv(final NodeToken token, final boolean external)
  {
    if (external)
    {
      Assert.assertNotNull("External must not be null", token.getNode().getExternal());

      if ("E1".equals(token.getNode().getExternal().getName()))
      {
        Assert.assertEquals("A", token.getNode().getExternalEnv().getAttribute("name"));
        Assert.assertNull(token.getNode().getExternalEnv().getAttribute("extra"));
      }
      else if ("E2".equals(token.getNode().getExternal().getName()))
      {
        Assert.assertEquals("B", token.getNode().getExternalEnv().getAttribute("name"));
        Assert.assertEquals("ThisIsExtra", token.getNode().getExternalEnv().getAttribute("extra"));
      }
      else
      {
        throw new RuntimeException("Unknow external name encountered: " + token.getNode().getExternal().getName());
      }
    }
    else
    {
      Assert.assertNull("External must be null", token.getNode().getExternal());
    }

    int count = token.getProcess().getEnv().getAttribute("count", Integer.class);
    Assert.assertEquals("ProcessLevel count doesn't match: ", processCount.get(), count);
  }

  @Test
  public void testExternals() throws Exception
  {
    processCount.set(0);
    TestEnv.getEngine().addGlobalCustomNodeType("externalTest", ExternalTestNode.class);

    ensureLoaded( "external" );
    Graph g = ensureLoaded( "external-user" );

    MapEnv initialEnv = new MapEnv();
    initialEnv.setAttribute("count", processCount.get());
    GraphProcess p = startProcess(g, initialEnv);

    verifyExecuting(p);

    NodeToken token = getActiveToken( p, ":N1" );
    validateEnv(token, false);

    String state = "[1 :N1 I F]";
    TestProcess.validate( p, state );

    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 I F]" +
      "[3 E2:N1 I F]"
      ;
    TestProcess.validate( p, state );

    // Verify that completing an already completed token has no effect
    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 I F]" +
      "[3 E2:N1 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N1" );
    validateEnv(token, true);

    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 I F]" +
      "[4 E1:N2 I F]" +
      "[5 E1:N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N3" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 I F]" +
      "[4 E1:N2 I F]" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N1" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 I F]" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 I F]" +
      "[7 E2:N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N2" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 I F]" +
      "[7 E2:N3 I F]" +
      "[8 E1:N4 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N2" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 I F]" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N4" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 I F]" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 C F]" +
      "  (I F E2:N5)"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N3" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E2:N5" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (I F E1:N5)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 I F]" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (I F :N2)"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N4" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (C F 11)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 C F]" +
      "  (C F 11)" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (I F :N2)" +
      "[11 E1:N5 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "E1:N5" );
    validateEnv(token, true);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (C F 11)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 C F]" +
      "  (C F 11)" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (C F 12)" +
      "[11 E1:N5 C F]" +
      "  (C F 12)" +
      "[12 :N2 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, ":N2" );
    validateEnv(token, false);
    completeToken( token, Arc.DEFAULT_ARC );
    state =
      "[1 :N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 E1:N1 C F]" +
      "  (C F 4)" +
      "  (C F 5)" +
      "[3 E2:N1 C F]" +
      "  (C F 6)" +
      "  (C F 7)" +
      "[4 E1:N2 C F]" +
      "  (C F 8)" +
      "[5 E1:N3 C F]" +
      "  (C F 11)" +
      "[6 E2:N2 C F]" +
      "  (C F 9)" +
      "[7 E2:N3 C F]" +
      "  (C F 10)" +
      "[8 E1:N4 C F]" +
      "  (C F 11)" +
      "[9 E2:N4 C F]" +
      "  (C F 10)" +
      "[10 E2:N5 C F]" +
      "  (C F 12)" +
      "[11 E1:N5 C F]" +
      "  (C F 12)" +
      "[12 :N2 C F]"
      ;
    TestProcess.validate( p, state );

    verifyComplete(p);

    Assert.assertEquals("total count should match", processCount.get(), 12);
    Assert.assertEquals("total count should match", TestEnv.refreshedProcess(p).getEnv().getAttribute("count", Integer.class).intValue(), 12);
  }
}