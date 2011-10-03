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

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.visitor;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.NodeToken;

/**
 * Class which provides default implementations in {@link TokenVisitor}.
 *
 * @author Paul Lorenz
 */
public class TokenVisitorAdaptor implements TokenVisitor
{
  @Override
  public boolean follow (final ArcToken child)
  {
    return true;
  }

  @Override
  public void visit( final NodeToken token )
  {
    // does nothing by default
  }

  @Override
  public void visit( final ArcToken token )
  {
    // does nothing by default
  }
}
