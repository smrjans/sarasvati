package com.googlecode.sarasvati.join;

import com.googlecode.sarasvati.ArcToken;

/**
 * Interface for filtering arc tokens to be used when considering joins.
 *
 * @author Paul Lorenz
 */
public interface ArcTokenFilter
{
  boolean isValidForJoin(ArcToken token);
}
