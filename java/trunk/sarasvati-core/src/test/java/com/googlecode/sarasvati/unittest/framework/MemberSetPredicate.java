package com.googlecode.sarasvati.unittest.framework;

import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.TokenSetMember;

public class MemberSetPredicate extends TokenOnNodePredicate
{
  private final String tokenSetName;
  private final int tokenSetIdx;

  public MemberSetPredicate (final String nodeName,
                             final String tokenSetName,
                             final int tokenSetIdx)
  {
    super(nodeName);
    this.tokenSetName = tokenSetName;
    this.tokenSetIdx = tokenSetIdx;
  }

  /**
   * @see com.googlecode.sarasvati.unittest.framework.TokenOnNodePredicate#matches(com.googlecode.sarasvati.NodeToken)
   */
  @Override
  public boolean matches(final NodeToken token)
  {
    if (super.matches(token))
    {
      TokenSetMember tsMember = token.getTokenSetMember( tokenSetName );
      return tsMember != null && tsMember.getMemberIndex() == tokenSetIdx;
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "Token on node name " + getNodeName() + " at index " + tokenSetIdx + " in token set " + tokenSetName;
  }
}