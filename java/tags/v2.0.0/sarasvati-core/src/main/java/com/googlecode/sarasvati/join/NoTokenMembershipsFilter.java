package com.googlecode.sarasvati.join;

import com.googlecode.sarasvati.ArcToken;

public enum NoTokenMembershipsFilter implements ArcTokenFilter
{
  INSTANCE;

  @Override
  public boolean isValidForJoin(final ArcToken token)
  {
    return !token.isTokenSetMember();
  }
}
