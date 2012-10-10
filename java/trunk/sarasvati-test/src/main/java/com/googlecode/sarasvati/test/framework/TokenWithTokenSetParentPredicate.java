package com.googlecode.sarasvati.test.framework;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;

public class TokenWithTokenSetParentPredicate extends TokenOnNodePredicate
{
  private final String tokenSetName;

  public TokenWithTokenSetParentPredicate (final String nodeName,
                                           final String tokenSetName)
  {
    super(nodeName);
    this.tokenSetName = tokenSetName;
  }

  /**
   * @see com.googlecode.sarasvati.test.framework.TokenOnNodePredicate#matches(com.googlecode.sarasvati.NodeToken)
   */
  @Override
  public boolean matches(final NodeToken token)
  {
    if (super.matches(token))
    {
      for (final ArcToken parent : token.getParentTokens())
      {
        if (parent.getParentToken().getTokenSetMember(tokenSetName) != null)
        {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "Token on node name " + getNodeName() + " with token set parent " + tokenSetName;
  }
}
