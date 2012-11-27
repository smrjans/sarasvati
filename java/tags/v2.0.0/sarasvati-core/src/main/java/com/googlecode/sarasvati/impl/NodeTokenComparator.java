/**
 * Created on Nov 20, 2009
 */
package com.googlecode.sarasvati.impl;

import java.util.Comparator;

import com.googlecode.sarasvati.NodeToken;

public class NodeTokenComparator implements Comparator<NodeToken>
{
  public static Comparator<NodeToken> INSTANCE = new NodeTokenComparator();

  @Override
  public int compare (final NodeToken o1, final NodeToken o2)
  {
    return o1.getCreateDate().compareTo( o2.getCreateDate() );
  }
}
