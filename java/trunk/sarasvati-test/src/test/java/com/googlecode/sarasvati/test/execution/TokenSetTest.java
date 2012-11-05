package com.googlecode.sarasvati.test.execution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.impl.MapEnv;
import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.test.TestEnv.ExecutionMode;
import com.googlecode.sarasvati.test.framework.ExecutionTest;
import com.googlecode.sarasvati.test.framework.TestProcess;

public class TokenSetTest extends ExecutionTest
{
  public static class TokenSetTestSetupNode extends CustomNode
  {
    /**
     * @see com.googlecode.sarasvati.Node#execute(com.googlecode.sarasvati.Engine, com.googlecode.sarasvati.NodeToken)
     */
    @Override
    public void execute(final Engine engine, final NodeToken token)
    {
      final Env initialEnv = new MapEnv();
      initialEnv.setAttribute("testOne", "hello");
      initialEnv.setAttribute("testTwo", "world!");

      final Map<String, List<?>> memberEnv = new HashMap<String, List<?>>();

      memberEnv.put("doc", Arrays.asList( new String[] { "index.html", "page1.html" }));
      memberEnv.put("user", Arrays.asList( new String[] { "indexer", "system" }));

      engine.completeWithNewTokenSet(token,
                                     Arc.DEFAULT_ARC,
                                     "ts",
                                     2,
                                     TestEnv.getMode() == ExecutionMode.Async,
                                     initialEnv,
                                     memberEnv);
    }
  }

  public static class TokenSetTestNode extends CustomNode
  {
    /**
     * @see com.googlecode.sarasvati.Node#execute(com.googlecode.sarasvati.Engine, com.googlecode.sarasvati.NodeToken)
     */
    @Override
    public void execute(final Engine engine, final NodeToken token)
    {
      final TokenSet tokenSet = token.getTokenSet("ts");
      Assert.assertNotNull(tokenSet);
      Assert.assertNull(token.getTokenSet("foo"));

      Assert.assertEquals("hello", tokenSet.getEnv().getAttribute("testOne"));
      Assert.assertEquals("world!", tokenSet.getEnv().getAttribute("testTwo"));

      final NodeTokenSetMember membership = token.getTokenSetMember("ts");
      Assert.assertNotNull(membership);
      Assert.assertNull(token.getTokenSetMember("foo"));

      if (membership.getMemberIndex() == 0)
      {
        Assert.assertEquals("index.html", membership.getEnv().getAttribute("doc"));
        Assert.assertEquals("indexer", membership.getEnv().getAttribute("user"));
      }
      else
      {
        Assert.assertEquals("page1.html", membership.getEnv().getAttribute("doc"));
        Assert.assertEquals("system", membership.getEnv().getAttribute("user"));
      }

      System.out.println("Completed node: " + token.getNode().getName());
    }
  }

  @Test
  public void testTokenSets() throws Exception
  {
    TestEnv.getEngine().addGlobalCustomNodeType("tokenSetTestSetup", TokenSetTestSetupNode.class);
    TestEnv.getEngine().addGlobalCustomNodeType("tokenSetTest", TokenSetTestNode.class);

    final GraphProcess p = startProcess("token-set");

    completeQueuedArcTokens(p);

    String state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 I F ts 0]" +
        "[3 N3 I F ts 0]" +
        "[4 N2 I F ts 1]" +
        "[5 N3 I F ts 1]";
    TestProcess.validate( p, state );

    NodeToken token = getActiveToken(p, "N2", "ts", 0);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (I F N5)" +
        "[3 N3 I F ts 0]" +
        "[4 N2 I F ts 1]" +
        "[5 N3 I F ts 1]" +
        "[6 N4 I F ts 0]";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N4", "ts", 0);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (I F N5)" +
        "[3 N3 I F ts 0]" +
        "[4 N2 I F ts 1]" +
        "[5 N3 I F ts 1]" +
        "[6 N4 C F ts 0]" +
        "  (I F N6)";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N3", "ts", 1);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (I F N5)" +
        "[3 N3 I F ts 0]" +
        "[4 N2 I F ts 1]" +
        "[5 N3 C F ts 1]" +
        "  (I F N5)" +
        "[6 N4 C F ts 0]" +
        "  (I F N6)";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N2", "ts", 1);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (I F N5)" +
        "[3 N3 I F ts 0]" +
        "[4 N2 C F ts 1]" +
        "  (C F 7)" +
        "  (C F 8)" +
        "[5 N3 C F ts 1]" +
        "  (C F 8)" +
        "[6 N4 C F ts 0]" +
        "  (I F N6)" +
        "[7 N4 I F ts 1]" +
        "[8 N5 I F ts 1]";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N5", "ts", 1);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (I F N5)" +
        "[3 N3 I F ts 0]" +
        "[4 N2 C F ts 1]" +
        "  (C F 7)" +
        "  (C F 8)" +
        "[5 N3 C F ts 1]" +
        "  (C F 8)" +
        "[6 N4 C F ts 0]" +
        "  (I F N6)" +
        "[7 N4 I F ts 1]" +
        "[8 N5 C F ts 1]";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N4", "ts", 1);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (I F N5)" +
        "[3 N3 I F ts 0]" +
        "[4 N2 C F ts 1]" +
        "  (C F 7)" +
        "  (C F 8)" +
        "[5 N3 C F ts 1]" +
        "  (C F 8)" +
        "[6 N4 C F ts 0]" +
        "  (C F 9)" +
        "[7 N4 C F ts 1]" +
        "  (C F 9)" +
        "[8 N5 C F ts 1]" +
        "[9 N6 I F]";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N3", "ts", 0);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (C F 10)" +
        "[3 N3 C F ts 0]" +
        "  (C F 10)" +
        "[4 N2 C F ts 1]" +
        "  (C F 7)" +
        "  (C F 8)" +
        "[5 N3 C F ts 1]" +
        "  (C F 8)" +
        "[6 N4 C F ts 0]" +
        "  (C F 9)" +
        "[7 N4 C F ts 1]" +
        "  (C F 9)" +
        "[8 N5 C F ts 1]" +
        "[9 N6 I F]" +
        "[10 N5 I F ts 0]";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N5", "ts", 0);
    completeToken(token);
    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (C F 10)" +
        "[3 N3 C F ts 0]" +
        "  (C F 10)" +
        "[4 N2 C F ts 1]" +
        "  (C F 7)" +
        "  (C F 8)" +
        "[5 N3 C F ts 1]" +
        "  (C F 8)" +
        "[6 N4 C F ts 0]" +
        "  (C F 9)" +
        "[7 N4 C F ts 1]" +
        "  (C F 9)" +
        "[8 N5 C F ts 1]" +
        "[9 N6 I F]" +
        "[10 N5 C F ts 0]";
    TestProcess.validate( p, state );

    token = getActiveToken(p, "N6");
    completeToken(token);

    state =
        "[1 N1 C F]" +
        "  (C F 2)" +
        "  (C F 3)" +
        "  (C F 4)" +
        "  (C F 5)" +
        "[2 N2 C F ts 0]" +
        "  (C F 6)" +
        "  (C F 10)" +
        "[3 N3 C F ts 0]" +
        "  (C F 10)" +
        "[4 N2 C F ts 1]" +
        "  (C F 7)" +
        "  (C F 8)" +
        "[5 N3 C F ts 1]" +
        "  (C F 8)" +
        "[6 N4 C F ts 0]" +
        "  (C F 9)" +
        "[7 N4 C F ts 1]" +
        "  (C F 9)" +
        "[8 N5 C F ts 1]" +
        "[9 N6 C F]" +
        "[10 N5 C F ts 0]";
    TestProcess.validate( p, state );

    verifyComplete(p);
  }
}