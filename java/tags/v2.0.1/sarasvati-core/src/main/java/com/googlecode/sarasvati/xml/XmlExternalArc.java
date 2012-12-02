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

package com.googlecode.sarasvati.xml;

import java.security.MessageDigest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import com.googlecode.sarasvati.load.definition.ExternalArcDefinition;
import com.googlecode.sarasvati.util.SvUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlExternalArc implements ExternalArcDefinition, Comparable<XmlExternalArc>
{
  @XmlAttribute(name = "from", required = true)
  protected String from;

  @XmlAttribute(name = "external", required = false)
  protected String external;

  @XmlAttribute(name = "to", required = true)
  protected String to;

  @XmlAttribute(name = "name", required = false)
  protected String name;

  @Override
  public String getFrom()
  {
    return from;
  }

  public void setFrom( final String from )
  {
    this.from = from;
  }

  @Override
  public String getExternal()
  {
    return external;
  }

  public void setExternal( final String external )
  {
    this.external = external;
  }

  @Override
  public String getTo()
  {
    return to;
  }

  public void setTo( final String to )
  {
    this.to = to;
  }

  @Override
  public String getName()
  {
    return name;
  }

  public void setName( final String name )
  {
    this.name = name;
  }

  @Override
  public boolean isToExternal ()
  {
    return !SvUtil.isBlankOrNull( external );
  }

  public void addToDigest (final MessageDigest digest)
  {
    if ( !SvUtil.isBlankOrNull( name ) )
    {
      digest.update( name.getBytes() );
    }

    if ( !SvUtil.isBlankOrNull( to ) )
    {
      digest.update( to.getBytes() );
    }

    if ( !SvUtil.isBlankOrNull( from ) )
    {
      digest.update( from.getBytes() );
    }

    if ( !SvUtil.isBlankOrNull( external ) )
    {
      digest.update( external.getBytes() );
    }
  }

  @Override
  public int compareTo (final XmlExternalArc o)
  {
    if ( o == null )
    {
      return 1;
    }

    int c = SvUtil.compare( name, o.getName() );

    if ( c != 0 )
    {
      return c;
    }

    c = SvUtil.compare( to, o.getTo() );

    if ( c != 0 )
    {
      return c;
    }

    c = SvUtil.compare( from, o.getFrom() );

    return c != 0 ? c : SvUtil.compare( external, o.getExternal() );
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( external == null ) ? 0 : external.hashCode() );
    result = prime * result + ( ( from == null ) ? 0 : from.hashCode() );
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
    result = prime * result + ( ( to == null ) ? 0 : to.hashCode() );
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof XmlExternalArc ) ) return false;
    XmlExternalArc other = (XmlExternalArc)obj;
    if ( external == null )
    {
      if ( other.external != null ) return false;
    }
    else if ( !external.equals( other.external ) ) return false;
    if ( from == null )
    {
      if ( other.from != null ) return false;
    }
    else if ( !from.equals( other.from ) ) return false;
    if ( name == null )
    {
      if ( other.name != null ) return false;
    }
    else if ( !name.equals( other.name ) ) return false;
    if ( to == null )
    {
      if ( other.to != null ) return false;
    }
    else if ( !to.equals( other.to ) ) return false;
    return true;
  }

  @Override
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append( "<arc from=\"" );
    buf.append( from );
    buf.append( "\"" );

    if ( external != null )
    {
      buf.append( "external=\"" );
      buf.append( external );
      buf.append( "\"" );
    }
    buf.append( " to=\"" );
    buf.append( to );

    if (name != null)
    {
      buf.append( "\" name=\"" );
      buf.append( name );
      buf.append( "\"" );
    }

    buf.append( "/>" );
    return buf.toString();
  }
}
