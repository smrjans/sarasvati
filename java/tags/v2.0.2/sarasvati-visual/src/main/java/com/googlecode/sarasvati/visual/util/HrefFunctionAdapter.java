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
package com.googlecode.sarasvati.visual.util;

import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.adapter.Function;
import com.googlecode.sarasvati.visual.GraphToImageMap;

public class HrefFunctionAdapter implements Function<String,Widget>
{
  protected GraphToImageMap imageMapHelper;

  public HrefFunctionAdapter (final GraphToImageMap imageMapHelper)
  {
    this.imageMapHelper = imageMapHelper;
  }

  @Override
  public String apply (final Widget widget)
  {
    Object o = ((ObjectScene)widget.getScene()).findObject( widget );

    if ( o instanceof Node )
    {
      return imageMapHelper.hrefForNode( (Node)o );
    }
    else if ( o instanceof Arc )
    {
      return imageMapHelper.hrefForArc( (Arc)o );
    }

    return null;
  }
}
