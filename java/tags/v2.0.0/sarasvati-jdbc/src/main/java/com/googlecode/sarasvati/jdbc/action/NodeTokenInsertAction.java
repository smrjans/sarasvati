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
import java.sql.Timestamp;
import java.sql.Types;

import com.googlecode.sarasvati.jdbc.JdbcNodeToken;

/**
 * Assumes insert is of the form:
 * <br>
 * (process_id, node_ref_id, attr_set_id, create_date, execution_type)
 *
 * @author Paul Lorenz
 *
 */
public class NodeTokenInsertAction extends AbstractInsertAction<JdbcNodeToken>
{
  public NodeTokenInsertAction (final String sql, JdbcNodeToken nodeToken)
  {
    super( sql, nodeToken );
  }

  @Override
  protected void setParameters (PreparedStatement stmt) throws SQLException
  {
    stmt.setLong( 1, value.getProcess().getId() );
    stmt.setLong( 2, value.getNode().getId() );

    if ( value.getAttrSetToken() == null )
    {
      stmt.setNull( 3, Types.BIGINT );
    }
    else
    {
      stmt.setLong( 3, value.getAttrSetToken().getId() );
    }

    stmt.setTimestamp( 4, new Timestamp( value.getCreateDate().getTime() ) );
    stmt.setInt( 5, value.getExecutionType().ordinal() );
  }
}