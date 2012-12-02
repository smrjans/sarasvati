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
package com.googlecode.sarasvati.hib;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;

import org.hibernate.annotations.ForeignKey;

@Entity
public class HibPropertyNode extends HibNode
{
  @ForeignKey(name="FK_node_attr")
  @ElementCollection(targetClass=String.class)
  @JoinTable( name="wf_node_attr", joinColumns={@JoinColumn( name="node_id")})
  @MapKeyColumn(name="name")
  @Column( name="value")
  protected Map<String, String> attrMap;

  public String getProperty (final String key)
  {
    return attrMap == null ? null : attrMap.get( key );
  }

  public void setProperty (final String key, final String value)
  {
    if ( attrMap == null )
    {
      attrMap = new HashMap<String,String>();
    }
    attrMap.put( key, value );
  }

  public void importProperties (final Map<String, String> properties)
  {
    if ( properties != null )
    {
      if ( attrMap == null )
      {
        attrMap = new HashMap<String,String>();
      }
      attrMap.putAll( properties );
    }
  }
}