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

package com.googlecode.sarasvati.editor.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.googlecode.sarasvati.util.SvUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEditorNode implements Comparable<XmlEditorNode>
{
  @XmlAttribute(name = "name", required = true)
  protected String               name;

  @XmlAttribute(name = "x", required = true)
  protected int                  x;

  @XmlAttribute(name = "y", required = true)
  protected int                  y;

  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  public int getX ()
  {
    return x;
  }

  public void setX (final int x)
  {
    this.x = x;
  }

  public int getY ()
  {
    return y;
  }

  public void setY (final int y)
  {
    this.y = y;
  }

  @Override
  public int compareTo (final XmlEditorNode o)
  {
    if ( o == null )
    {
      return 1;
    }
    return SvUtil.compare( name, o.getName() );
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof XmlEditorNode ) ) return false;
    XmlEditorNode other = (XmlEditorNode)obj;
    if ( name == null )
    {
      if ( other.name != null ) return false;
    }
    else if ( !name.equals( other.name ) ) return false;
    return true;
  }

  @Override
  public String toString ()
  {
    StringBuilder buf = new StringBuilder();
    buf.append( "<node name=\"" );
    buf.append( name );
    buf.append( "\" x=\"" );
    buf.append( getX() );
    buf.append( "\" y=\"" );
    buf.append( y );
    buf.append( "\"/>\n" );

    return buf.toString();
  }
}
