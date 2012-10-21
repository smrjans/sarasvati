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

import java.sql.ResultSet;
import java.sql.SQLException;

import com.googlecode.sarasvati.jdbc.JdbcGraph;

public abstract class AbstractGraphLoadAction extends AbstractLoadAction<JdbcGraph>
{
  public AbstractGraphLoadAction (String sql)
  {
    super( sql, true );
  }

  @Override
  protected JdbcGraph loadObject (ResultSet row) throws SQLException
  {
    long graphId = row.getLong( 1 );
    String graphName = row.getString( 2 );
    int version = row.getInt( 3 );

    return new JdbcGraph( graphId, graphName, version, null );
  }
}
