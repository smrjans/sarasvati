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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.googlecode.sarasvati.load.definition.ExternalArcDefinition;
import com.googlecode.sarasvati.load.definition.ExternalDefinition;
import com.googlecode.sarasvati.load.properties.DOMToObjectLoadHelper;
import com.googlecode.sarasvati.util.SvUtil;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlExternal implements ExternalDefinition, Comparable<XmlExternal>
{
  @XmlAttribute(name = "processDefinition", required = true)
  protected String  processDefinition;

  @XmlAttribute(name = "name", required = true)
  protected String  name;

  @XmlAttribute(name = "x", required = false)
  public Integer x;

  @XmlAttribute(name = "y", required = false)
  public Integer y;

  @XmlElement(name = "arc", required = false)
  protected List<XmlExternalArc> externalArcs = new ArrayList<XmlExternalArc>();

  @XmlElement(name = "custom")
  protected XmlCustom            custom;

  @Override
  public String getProcessDefinition ()
  {
    return processDefinition;
  }

  public void setProcessDefinition (final String processDefinition)
  {
    this.processDefinition = processDefinition;
  }

  @Override
  public String getName ()
  {
    return name;
  }

  public void setName (final String name)
  {
    this.name = name;
  }

  @Override
  public Integer getX ()
  {
    return x;
  }

  public void setX (final Integer x)
  {
    this.x = x;
  }

  @Override
  public Integer getY ()
  {
    return y;
  }

  public void setY (final Integer y)
  {
    this.y = y;
  }

  @Override
  public List<XmlExternalArc> getExternalArcs()
  {
    return externalArcs;
  }

  public void setExternalArcs( final List<XmlExternalArc> externalArcs )
  {
    this.externalArcs = externalArcs;
  }

  @Override
  public XmlCustom getCustom ()
  {
    return custom;
  }

  public void setCustom (final XmlCustom custom)
  {
    this.custom = custom;
  }

  public void addToDigest (final MessageDigest digest)
  {
    if ( !SvUtil.isBlankOrNull( name ) )
    {
      digest.update( name.getBytes() );
    }

    if ( !SvUtil.isBlankOrNull( processDefinition ) )
    {
      digest.update( processDefinition.getBytes() );
    }

    Map<String, String> customProps = new TreeMap<String, String>();
    DOMToObjectLoadHelper.loadCustomIntoMap( custom, customProps );

    for ( Entry<String, String> entry : customProps.entrySet() )
    {
      digest.update( entry.getKey().getBytes() );
      if ( !SvUtil.isBlankOrNull( entry.getValue() ) )
      {
        digest.update( entry.getValue().getBytes() );
      }
    }

    Collections.sort( externalArcs );

    if ( externalArcs != null )
    {
      for ( XmlExternalArc arc : externalArcs )
      {
        arc.addToDigest( digest );
      }
    }
  }

  @Override
  public int compareTo (final XmlExternal o)
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
    if ( !( obj instanceof XmlExternal ) ) return false;
    XmlExternal other = (XmlExternal)obj;
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
    buf.append( "<external name=\"" );
    buf.append( name );
    buf.append( "\" processDefinition=\"" );
    buf.append( processDefinition );

    if (x != null)
    {
      buf.append( "\" x=\"" );
      buf.append( x );
    }

    if (y != null)
    {
      buf.append( "\" y=\"" );
      buf.append( y );
    }

    buf.append( ">\n" );

    for (ExternalArcDefinition arc : externalArcs)
    {
      buf.append( arc );
      buf.append( "\n" );
    }

    if (custom != null)
    {
      buf.append( custom );
      buf.append( "\n" );
    }

    buf.append( "</external>\n" );
    return buf.toString();
  }
}