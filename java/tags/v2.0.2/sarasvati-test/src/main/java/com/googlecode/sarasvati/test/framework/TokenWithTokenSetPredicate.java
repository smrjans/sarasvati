package com.googlecode.sarasvati.test.framework;

import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSetMember;

public class TokenWithTokenSetPredicate extends TokenOnNodePredicate
{
  private final String tokenSetName;
  private final int    memberIndex;

  public TokenWithTokenSetPredicate (final String nodeName,
                                     final String tokenSetName,
                                     final int memberIndex)
  {
    super(nodeName);
    this.tokenSetName = tokenSetName;
    this.memberIndex = memberIndex;
  }

  /**
   * @see com.googlecode.sarasvati.test.framework.TokenOnNodePredicate#matches(com.googlecode.sarasvati.NodeToken)
   */
  @Override
  public boolean matches(final NodeToken token)
  {
    final TokenSetMember ts = token.getTokenSetMember(tokenSetName);
    return super.matches(token) && ts != null && ts.getMemberIndex() == memberIndex;
  }

  @Override
  public String toString()
  {
    return "Token on node name " + getNodeName() + " with token set parent " + tokenSetName;
  }
}
