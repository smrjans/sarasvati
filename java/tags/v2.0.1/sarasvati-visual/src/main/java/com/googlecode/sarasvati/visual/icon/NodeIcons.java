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

    Copyright 2009 Paul Lorenz
 */
package com.googlecode.sarasvati.visual.icon;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

public class NodeIcons
{
  private static final Map<NodeIconType, IconFactory> iconFactories = new HashMap<NodeIconType, IconFactory>();

  static
  {
    iconFactories.put( NodeIconType.Oval, new IconFactory()
    {
      @Override
      public Icon newIcon (final String label,
                           final Color backgroundColor,
                           final boolean isJoin,
                           final boolean isSelected)
      {
        return new OvalNodeIcon( label, backgroundColor, isJoin, isSelected );
      }
    });

    iconFactories.put( NodeIconType.Rectangular, new IconFactory()
    {
      @Override
      public Icon newIcon (final String label,
                           final Color backgroundColor,
                           final boolean isJoin,
                           final boolean isSelected)
      {
        return new RectangularNodeIcon( label, backgroundColor, isJoin, isSelected );
      }
    });

    iconFactories.put( NodeIconType.SmallCircle, new IconFactory()
    {
      @Override
      public Icon newIcon (final String label,
                           final Color backgroundColor,
                           final boolean isJoin,
                           final boolean isSelected)
      {
        return new SmallCircleNodeIcon( backgroundColor, isJoin, isSelected );
      }
    });
  }

  public static Icon newInstance (final NodeIconType nodeIconType,
                                  final String label,
                                  final Color color,
                                  final boolean isJoin,
                                  final boolean isSelected)
  {
    return iconFactories.get( nodeIconType ).newIcon( label, color, isJoin, isSelected );
  }
}