package com.googlecode.sarasvati.test.framework;

import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;

public class TokenOnNodePredicate implements TestPredicate<NodeToken>
{
  private final boolean checkExternal;
  private final String nodeName;
  private final String externalName;

  public TokenOnNodePredicate(final String nodeName)
  {
    if (nodeName.startsWith(":"))
    {
      this.checkExternal = true;
      this.externalName = null;
      this.nodeName = nodeName.substring(1);
    }
    else if (nodeName.contains(":"))
    {
      this.checkExternal = true;
      String[] parts = nodeName.split(":");
      this.externalName = parts[0];
      this.nodeName = parts[1];
    }
    else
    {
      this.checkExternal = true;
      this.externalName = null;
      this.nodeName = nodeName;
    }
  }

  protected String getNodeName()
  {
    return nodeName;
  }

  /**
   * @see com.googlecode.sarasvati.test.framework.TestPredicate#matches(java.lang.Object)
   */
  @Override
  public boolean matches(final NodeToken token)
  {
    final Node node = token.getNode();
    return nodeName.equals(node.getName()) &&
           (!checkExternal ||
            (node.getExternal() != null && node.getExternal().getName().equals(externalName)) ||
            (node.getExternal() == null && externalName == null));
  }

  @Override
  public String toString()
  {
    return "Token on node " + nodeName;
  }
}
