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
package com.googlecode.sarasvati.jdbc;

import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.event.PersistedExecutionListener;

public class JdbcProcessListener implements PersistedExecutionListener
{
  protected Long id;

  protected String type;

  protected int eventTypeMask;

  protected GraphProcess process;

  public JdbcProcessListener (final String type, final int eventTypeMask, final GraphProcess process)
  {
    this.eventTypeMask = eventTypeMask;
    this.type = type;
    this.process = process;
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
  public String getType ()
  {
    return type;
  }

  public void setType (final String type)
  {
    this.type = type;
  }

  @Override
  public int getEventTypeMask ()
  {
    return eventTypeMask;
  }

  public void setEventTypeMask (final int eventTypeMask)
  {
    this.eventTypeMask = eventTypeMask;
  }

  public GraphProcess getProcess ()
  {
    return process;
  }

  public void setProcess (final GraphProcess process)
  {
    this.process = process;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals (final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof JdbcProcessListener))
      return false;
    JdbcProcessListener other = (JdbcProcessListener) obj;
    if (id == null)
    {
      if (other.getId() != null)
        return false;
    } else if (!id.equals(other.getId()))
      return false;
    return true;
  }
}
