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

import com.googlecode.sarasvati.adapter.Function;
import com.googlecode.sarasvati.visual.ProcessToImageMap;
import com.googlecode.sarasvati.visual.process.VisualProcessArc;
import com.googlecode.sarasvati.visual.process.VisualProcessNode;

public class ProcessHrefFunctionAdapter implements Function<String,Widget>
{
  protected ProcessToImageMap imageMapHelper;

  public ProcessHrefFunctionAdapter (final ProcessToImageMap imageMapHelper)
  {
    this.imageMapHelper = imageMapHelper;
  }

  @Override
  public String apply (final Widget widget)
  {
    Object o = ((ObjectScene)widget.getScene()).findObject( widget );

    if ( o instanceof VisualProcessNode )
    {
      return imageMapHelper.hrefForNode( (VisualProcessNode)o );
    }
    else if ( o instanceof VisualProcessArc )
    {
      return imageMapHelper.hrefForArc( (VisualProcessArc)o );
    }

    return null;
  }
}
