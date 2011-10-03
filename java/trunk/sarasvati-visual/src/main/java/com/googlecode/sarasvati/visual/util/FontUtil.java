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

import java.awt.Font;
import java.awt.Graphics;

public class FontUtil
{
  public static void setSizedFont (final Graphics g,
                                   final String text,
                                   final float maxFontSize,
                                   final int maxWidth)
  {
    if ( g.getFont().getSize() != maxFontSize )
    {
      Font newFont = g.getFont().deriveFont( maxFontSize );
      g.setFont( newFont );
    }

    float currentSize = maxFontSize;

    while ( g.getFontMetrics().getStringBounds( text, g ).getWidth() > maxWidth )
    {
      g.setFont( g.getFont().deriveFont( --currentSize ) );
    }
  }

  public static String[] split (final String text)
  {
    String[] lines = text.split( " " );

    if ( lines.length < 3 )
    {
      return lines;
    }

    String fst = lines[0];
    String snd = lines[lines.length - 1];

    for ( int i = 1; i < lines.length - 1; i++ )
    {
      String cur = lines[i];
      String tmp1 = fst + " " + cur;
      String tmp2 = concat( lines, i, " " );

      if ( tmp1.length() < tmp2.length() )
      {
        fst = tmp1;
      }
      else
      {
        snd = tmp2;
        break;
      }
    }

    return new String[] { fst, snd };
  }

  public static String concat (final String[] str, final int start, final String middle)
  {
    StringBuilder buf = new StringBuilder();
    for ( int i = start; i < str.length; i++ )
    {
      buf.append( str[i] );
      if ( i != str.length - 1 )
      {
        buf.append( middle );
      }
    }
    return buf.toString();
  }
}
