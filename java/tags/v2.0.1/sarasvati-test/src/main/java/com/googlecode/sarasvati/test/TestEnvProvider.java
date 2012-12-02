package com.googlecode.sarasvati.test;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;

public interface TestEnvProvider
{
  Engine getEngine();
  void commit();
  GraphProcess refreshProcess(final GraphProcess process);
  NodeToken refreshToken(final NodeToken token);
  void dispose();
}
