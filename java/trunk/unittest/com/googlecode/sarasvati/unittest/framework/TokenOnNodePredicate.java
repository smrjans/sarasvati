package com.googlecode.sarasvati.unittest.framework;

import com.googlecode.sarasvati.NodeToken;

public class TokenOnNodePredicate implements TestPredicate<NodeToken>
{
  private final String nodeName;
  public TokenOnNodePredicate(final String nodeName)
  {
    this.nodeName = nodeName;
  }

  protected String getNodeName()
  {
    return nodeName;
  }

  /**
   * @see com.googlecode.sarasvati.unittest.framework.TestPredicate#matches(java.lang.Object)
   */
  @Override
  public boolean matches(final NodeToken token)
  {
    return nodeName.equals(token.getNode().getName());
  }

  @Override
  public String toString()
  {
    return "Token on node " + nodeName;
  }
}
