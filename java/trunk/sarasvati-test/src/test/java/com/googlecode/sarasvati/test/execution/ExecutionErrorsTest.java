package com.googlecode.sarasvati.test.execution;

import org.junit.Test;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.test.framework.ExecutionTest;

public class ExecutionErrorsTest extends ExecutionTest
{
  @Test(expected=SarasvatiException.class)
  public void testStartNullGraph()
  {
    TestEnv.getEngine().startProcess((Graph)null);
  }

  @Test(expected=SarasvatiException.class)
  public void testStartNullString()
  {
    TestEnv.getEngine().startProcess((String)null);
  }

  @Test(expected=SarasvatiException.class)
  public void testStartWithNonExistantGraph()
  {
    TestEnv.getEngine().startProcess("this-graph-does-not-exist");
  }

  @Test(expected=SarasvatiException.class)
  public void testStartWithGraphNameWithNoStartNode()
  {
    TestEnv.getEngine().startProcess("external");
  }

  @Test(expected=SarasvatiException.class)
  public void testStartWithGraphWithNoStartNode() throws Exception
  {
    final Graph graph = reloadDefinition("no-start-nodes");
    TestEnv.getEngine().startProcess(graph);
  }
}
