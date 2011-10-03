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
package com.googlecode.sarasvati.hib;

import java.util.Collections;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.ForeignKey;

import com.googlecode.sarasvati.External;
import com.googlecode.sarasvati.env.ReadEnv;
import com.googlecode.sarasvati.impl.MapEnv;

@Entity
@Table (name="wf_external")
public class HibExternal implements External
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long   id;

  protected String name;

  @ForeignKey(name="FK_ext_graph")
  @ManyToOne (fetch=FetchType.LAZY)
  @JoinColumn(name="graph_id", nullable=false)
  protected HibGraph graph;

  @ForeignKey(name="FK_ext_ext_graph")
  @ManyToOne (fetch=FetchType.LAZY)
  @JoinColumn(name="external_graph_id", nullable=false)
  protected HibGraph externalGraph;

  @ForeignKey(name="FK_external_attr")
  @CollectionOfElements
  @JoinTable( name="wf_external_attr", joinColumns={@JoinColumn( name="external_id")})
  @org.hibernate.annotations.MapKey( columns={@Column(name="name")})
  @Column( name="value")
  protected Map<String, String> attrMap;

  @Transient
  protected ReadEnv env;

  protected HibExternal ()
  {
    /* Default constructor for Hibernate */
  }

  public HibExternal (final String name, final HibGraph graph, final HibGraph externalGraph, final Map<String, String> attrMap)
  {
    this.name = name;
    this.graph = graph;
    this.externalGraph = externalGraph;
    this.attrMap = attrMap;
  }

  public Long getId ()
  {
    return id;
  }

  public void setId (final Long id)
  {
    this.id = id;
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
  public HibGraph getGraph ()
  {
    return graph;
  }

  public void setGraph (final HibGraph graph)
  {
    this.graph = graph;
  }

  @Override
  public HibGraph getExternalGraph ()
  {
    return externalGraph;
  }

  public void setExternalGraph (final HibGraph externalGraph)
  {
    this.externalGraph = externalGraph;
  }

  @Override
  public ReadEnv getEnv ()
  {
    if ( env == null )
    {
      env = new MapEnv( Collections.unmodifiableMap( attrMap ) );
    }
    return env;
  }
}
