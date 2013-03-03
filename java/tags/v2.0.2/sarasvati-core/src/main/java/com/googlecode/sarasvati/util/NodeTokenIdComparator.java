/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008-2009 Paul Lorenz
*/
package com.googlecode.sarasvati.util;

import java.io.Serializable;
import java.util.Comparator;

import com.googlecode.sarasvati.NodeToken;

/**
 * Compares node tokens by age, using the assumption that node tokens with
 * lower ids were created earlier.
 *
 * @author Paul Lorenz
 */
public class NodeTokenIdComparator implements Comparator<NodeToken>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final boolean oldestFirst;

  public NodeTokenIdComparator (final boolean oldestFirst)
  {
    this.oldestFirst = oldestFirst;
  }

  @Override
  public int compare (final NodeToken o1, final NodeToken o2)
  {
    return oldestFirst ? o1.getId().compareTo( o2.getId() ) : o2.getId().compareTo( o1.getId() );
  }
}
