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

import com.googlecode.sarasvati.jdbc.JdbcNode;


public class NodeInsertAction extends AbstractInsertAction<JdbcNode>
{
  public NodeInsertAction (final String sql, final JdbcNode node )
  {
    super( sql, node );
  }

  @Override
  protected void setParameters (PreparedStatement stmt) throws SQLException
  {
    stmt.setLong( 1, value.getGraph().getId() );
    stmt.setString( 2, value.getName() );
    stmt.setString( 3, value.getType() );
    stmt.setString( 4, value.getGuard() );
    stmt.setString( 5, value.isStart() ? "Y" : "N" );
    stmt.setInt( 6, value.getJoinType().ordinal() );
  }
}