package com.googlecode.sarasvati.test;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.mem.MemEngine;

public class MemTestEnvProvider implements TestEnvProvider
{
  private MemEngine engine = new MemEngine();

  @Override
  public Engine getEngine()
  {
    return engine;
  }

  /**
   * @see com.googlecode.sarasvati.test.TestEnvProvider#commit()
   */
  @Override
  public void commit()
  {
    // does nothing by default
  }

  /**
   * @see com.googlecode.sarasvati.test.TestEnvProvider#refreshProcess(com.googlecode.sarasvati.GraphProcess)
   */
  @Override
  public GraphProcess refreshProcess(final GraphProcess process)
  {
    return process;
  }

  /**
   * @see com.googlecode.sarasvati.test.TestEnvProvider#refreshToken(com.googlecode.sarasvati.NodeToken)
   */
  @Override
  public NodeToken refreshToken(final NodeToken token)
  {
    return token;
  }

  /* (non-Javadoc)
   * @see com.googlecode.sarasvati.test.TestEnvProvider#dispose()
   */
  @Override
  public void dispose()
  {
    // Does nothing
  }
}