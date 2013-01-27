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
                        chung-onn

*/
package com.googlecode.sarasvati.hib;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author chungonn
 *
 */
@Entity
@Table(name = "wf_node_type")
public class HibNodeType
{
  @Id
  protected String    id;

  private String      description;

  @OneToOne
  @JoinColumn(name = "behaviour")
  private HibNodeType behaviour;

  protected HibNodeType ()
  {
    /* default constructor for hibernate */
  }

  public HibNodeType (final String id, final String description)
  {
    this.id = id;
    this.description = description;
    this.behaviour = this;
  }

  public HibNodeType (final String id, final String description, final HibNodeType type)
  {
    this.id = id;
    this.description = description;
    this.behaviour = type;
  }

  public String getId ()
  {
    return id;
  }

  public HibNodeType getBehaviour ()
  {
    return behaviour;
  }

  public String getDescription ()
  {
    return description;
  }
}