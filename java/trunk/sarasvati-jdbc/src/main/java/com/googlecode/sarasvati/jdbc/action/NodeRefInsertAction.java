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
package com.googlecode.sarasvati.jdbc.action;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.googlecode.sarasvati.jdbc.JdbcNodeRef;

public class NodeRefInsertAction extends AbstractInsertAction<JdbcNodeRef>
{
  public NodeRefInsertAction (String sql, JdbcNodeRef nodeRef)
  {
    super( sql, nodeRef );
  }

  @Override
  protected void setParameters (PreparedStatement stmt) throws SQLException
  {
    stmt.setLong( 1, value.getGraph().getId() );
    stmt.setLong( 2, value.getNode().getId() );

    if ( value.getOriginatingExternalNode() == null )
    {
      stmt.setNull( 3, Types.INTEGER );
    }
    else
    {
      stmt.setLong( 3, value.getOriginatingExternalNode().getId() );
    }

    if ( value.getExternal() == null )
    {
      stmt.setNull( 4, Types.INTEGER );
    }
    else
    {
      stmt.setLong( 4, value.getExternal().getId() );
    }
  }
}