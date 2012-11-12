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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.googlecode.sarasvati.ProcessState;
import com.googlecode.sarasvati.jdbc.JdbcEngine;
import com.googlecode.sarasvati.jdbc.JdbcGraph;
import com.googlecode.sarasvati.jdbc.JdbcGraphProcess;

/**
 * Assumes database is in the form
 * graph_id, state, parent_token_id, create_date, version
 *
 * @author Paul Lorenz
 *
 */
public class ProcessLoadAction extends AbstractLoadAction<JdbcGraphProcess>
{
  protected long processId;
  protected JdbcEngine engine;

  public ProcessLoadAction (final String sql,
                            final long processId,
                            final JdbcEngine engine)
  {
    super( sql, true );
    this.processId = processId;
    this.engine = engine;
  }

  @Override
  protected JdbcGraphProcess loadObject (final ResultSet row) throws SQLException
  {
    JdbcGraph graph    = engine.getRepository().getGraph( row.getLong( 1 ) );

    ProcessState state = ProcessState.values()[ row.getInt( 2 ) ];
    /* long parentTokenId = */ row.getLong( 3 );
    Date createDate    = row.getTimestamp( 4 );
    int version        = row.getInt( 5 );

    return new JdbcGraphProcess( processId, graph, state, null, createDate, version );
  }

  @Override
  protected void setParameters (final PreparedStatement stmt) throws SQLException
  {
    stmt.setLong( 1, processId );
  }
}
