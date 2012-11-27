package com.googlecode.sarasvati.join;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.TokenSetMember;
import com.googlecode.sarasvati.util.SvUtil;

public class TokenSetGroupFilter implements ArcTokenFilter
{
  private final TokenSet tokenSet;
  private final int memberIndex;

  public TokenSetGroupFilter (final TokenSet tokenSet, final int memberIndex)
  {
    this.tokenSet = tokenSet;
    this.memberIndex = memberIndex;
  }

  @Override
  public boolean isValidForJoin(final ArcToken token)
  {
    final TokenSetMember tsm = SvUtil.getTokenSetMember(token, tokenSet);
    return tsm != null && tsm.getMemberIndex() == memberIndex;
  }
}
