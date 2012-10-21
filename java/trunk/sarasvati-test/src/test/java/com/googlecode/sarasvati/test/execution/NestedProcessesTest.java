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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class NestedProcessesTest extends ExecutionTest
{
  protected static AtomicInteger processCount = new AtomicInteger();

  public static class NestedProcessListener implements ExecutionListener
  {
    protected static Map<NodeToken, GraphProcess> nestedProcesses = new ConcurrentHashMap<NodeToken, GraphProcess>();

    /**
     * @see com.googlecode.sarasvati.event.ExecutionListener#notify(com.googlecode.sarasvati.event.ExecutionEvent)
     */
    @Override
    public EventActions notify(final ExecutionEvent event)
    {
      if (event.getEventType() == ExecutionEventType.PROCESS_CREATED)
      {
        if (event.getProcess().getParentToken() != null)
        {
          nestedProcesses.put(event.getProcess().getParentToken(), event.getProcess());
        }
      }
      return null;
    }

    protected static GraphProcess getNestedProcess(final NodeToken token)
    {
      return nestedProcesses.get(token);
    }

    public static void reset()
    {
      nestedProcesses.clear();
    }
  }

  public static class NestedTestNode extends CustomNode
  {
    private void testProcessEnv(final Env env)
    {
      int count = env.getAttribute("count", Integer.class);
      env.setAttribute("count", ++count);
      processCount.incrementAndGet();
    }

    private void testTokenEnv(final NodeToken token)
    {
      if (token.getNode().getGraph().getName().equals("nested-user"))
      {
        token.getEnv().setAttribute(token.getNode().getName(), token.getNode().getName() + "Foo");
      }
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

  @Test
  public void testNestedProcesses() throws Exception
  {
    NestedProcessListener.reset();
    processCount.set(0);
    TestEnv.getEngine().addGlobalCustomNodeType("nestedTest", NestedTestNode.class);
    TestEnv.getEngine().addExecutionListener(NestedProcessListener.class);
    ensureLoaded( "nested" );

    MapEnv initialEnv = new MapEnv();
    initialEnv.setAttribute("count", processCount.get());

    GraphProcess p = startProcess("nested-user", initialEnv);

    verifyExecuting(p);

    NodeToken token = getActiveToken( p, "N1" );

    String state = "[1 N1 I F]";
    TestProcess.validate( p, state );

    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 N2 I F]" +
      "[3 N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N2");
    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 N2 C F]" +
      "  (C F 4)" +
      "[3 N3 I F]" +
      "[4 N4 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N3");
    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 N2 C F]" +
      "  (C F 4)" +
      "[3 N3 C F]" +
      "  (C F 5)" +
      "[4 N4 I F]" +
      "[5 N5 I F]"
      ;
    TestProcess.validate( p, state );

    NodeToken n4Token = getActiveToken(p, "N4");
    GraphProcess p2 = NestedProcessListener.getNestedProcess(n4Token);
    Assert.assertNotNull("Nested process not found", p2);

    state =
        "[1 N1 I F]"
        ;
    TestProcess.validate( p2, state );

    Assert.assertEquals("N1Foo", p2.getEnv().getAttribute("N1"));
    Assert.assertEquals("N2Foo", p2.getEnv().getAttribute("N2"));
    Assert.assertNull(p2.getEnv().getAttribute("N3"));

    NodeToken n5Token = getActiveToken(p, "N5");
    GraphProcess p3 = NestedProcessListener.getNestedProcess(n5Token);
    Assert.assertNotNull("Nested process not found", p3);

    state =
        "[1 N1 I F]"
        ;
    TestProcess.validate( p3, state );

    Assert.assertEquals("N1Foo", p3.getEnv().getAttribute("N1"));
    Assert.assertEquals("N3Foo", p3.getEnv().getAttribute("N3"));
    Assert.assertNull(p3.getEnv().getAttribute("N2"));

    NodeToken p2token = getActiveToken( p2, "N1" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 I F]" +
        "[3 N3 I F]"
        ;
    TestProcess.validate( p2, state );

    NodeToken p3token = getActiveToken( p3, "N1" );
    completeToken( p3token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 I F]" +
        "[3 N3 I F]"
        ;
    TestProcess.validate( p3, state );


    p2token = getActiveToken( p2, "N2" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 I F]"
        ;
    TestProcess.validate( p2, state );

    p2token = getActiveToken( p2, "N4" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p2, state );

    p2token = getActiveToken( p2, "N5" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 C F]"
        ;
    TestProcess.validate( p2, state );

    verifyExecuting(p2);

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 I F]" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p, state );

    p2token = getActiveToken( p2, "N3" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 C F]"
        ;
    TestProcess.validate( p2, state );

    verifyComplete(p2);
    Assert.assertEquals(8, TestEnv.refreshProcess(p2).getEnv().getAttribute("count", Integer.class).intValue());

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 C F]" +
        "  (I F N6)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p, state );

    p3token = getActiveToken( p3, "N3" );
    completeToken( p3token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 I F]" +
        "[3 N3 C F]"
        ;
    TestProcess.validate( p3, state );

    p3token = getActiveToken( p3, "N2" );
    completeToken( p3token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "[4 N4 I F]"
        ;
    TestProcess.validate( p3, state );

    p3token = getActiveToken( p3, "N4" );
    completeToken( p3token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p3, state );

    verifyExecuting(p3);

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 C F]" +
        "  (I F N6)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p, state );

    p3token = getActiveToken( p3, "N5" );
    completeToken( p3token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 C F]"
        ;
    TestProcess.validate( p3, state );
    verifyComplete(p3);
    Assert.assertEquals(8, TestEnv.refreshProcess(p3).getEnv().getAttribute("count", Integer.class).intValue());

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 C F]" +
        "  (C F 6)" +
        "[5 N5 C F]" +
        "  (C F 6)" +
        "[6 N6 I F]"
        ;
    TestProcess.validate( p, state );

    token = getActiveToken( p, "N6" );
    completeToken( token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 C F]" +
        "  (C F 5)" +
        "[4 N4 C F]" +
        "  (C F 6)" +
        "[5 N5 C F]" +
        "  (C F 6)" +
        "[6 N6 C F]"
        ;
    TestProcess.validate( p, state );
    verifyComplete(p);

    Assert.assertEquals(4, TestEnv.refreshProcess(p).getEnv().getAttribute("count", Integer.class).intValue());
  }

  @Test
  public void testNestedProcessesCancel() throws Exception
  {
    processCount.set(0);
    TestEnv.getEngine().addGlobalCustomNodeType("nestedTest", NestedTestNode.class);
    TestEnv.getEngine().addExecutionListener(NestedProcessListener.class);
    ensureLoaded( "nested" );

    MapEnv initialEnv = new MapEnv();
    initialEnv.setAttribute("count", processCount.get());

    GraphProcess p = startProcess("nested-user", initialEnv);

    verifyExecuting(p);

    NodeToken token = getActiveToken( p, "N1" );

    String state = "[1 N1 I F]";
    TestProcess.validate( p, state );

    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 N2 I F]" +
      "[3 N3 I F]"
      ;
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N2");
    completeToken( token, Arc.DEFAULT_ARC );

    state =
      "[1 N1 C F]" +
      "  (C F 2)" +
      "  (C F 3)" +
      "[2 N2 C F]" +
      "  (C F 4)" +
      "[3 N3 I F]" +
      "[4 N4 I F]"
      ;
    TestProcess.validate( p, state );

    NodeToken n4Token = getActiveToken(p, "N4");
    GraphProcess p2 = NestedProcessListener.getNestedProcess(n4Token);
    Assert.assertNotNull("Nested process not found", p2);

    state =
        "[1 N1 I F]"
        ;
    TestProcess.validate( p2, state );

    Assert.assertEquals("N1Foo", p2.getEnv().getAttribute("N1"));
    Assert.assertEquals("N2Foo", p2.getEnv().getAttribute("N2"));
    Assert.assertNull(p2.getEnv().getAttribute("N3"));

    NodeToken p2token = getActiveToken( p2, "N1" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 I F]" +
        "[3 N3 I F]"
        ;
    TestProcess.validate( p2, state );

    p2token = getActiveToken( p2, "N2" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 I F]"
        ;
    TestProcess.validate( p2, state );

    p2token = getActiveToken( p2, "N4" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p2, state );

    TestEnv.getEngine().cancelProcess(TestEnv.refreshProcess(p));
    verifyCancelled(p);

    token = getActiveToken(p, "N3");

    completeToken(token, Arc.DEFAULT_ARC);

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 I F]"
        ;

    verifyCancelled(p);
    TestProcess.validate( p, state );

    verifyCancelled(p2);

    p2token = getActiveToken( p2, "N5" );
    completeToken( p2token, Arc.DEFAULT_ARC );

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "[2 N2 C F]" +
        "  (C F 4)" +
        "[3 N3 I F]" +
        "[4 N4 C F]" +
        "  (C F 5)" +
        "[5 N5 I F]"
        ;
    TestProcess.validate( p2, state );
  }
}