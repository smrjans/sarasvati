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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.load.GraphRepository;

public class HibGraphRepository implements GraphRepository<HibGraph>
{
  protected Session session;

  HibGraphRepository (final Session session)
  {
    this.session = session;
  }

  @Override
  public void addGraph (final HibGraph graph)
  {
    // does nothing, as graph is added to database when it is created
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<HibGraph> getGraphs (final String name)
  {
    Query query = session.createQuery( "from HibGraph where name = :name " );
    query.setString(  "name", name );
    return query.list();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<HibGraph> getGraphs ()
  {
    return session.createQuery( "from HibGraph" ).list();
  }

  @Override
  public HibGraph getLatestGraph (final String name)
  {
    String query = "from HibGraph " +
                   " where name = :name " +
                   "   and version in (select max(version) from HibGraph where name = :name)";

    return
       (HibGraph)session.createQuery( query )
       .setString(  "name", name )
       .uniqueResult();
  }

  /**
   * @see com.googlecode.sarasvati.load.GraphRepository#getActiveNestedProcesses(com.googlecode.sarasvati.GraphProcess)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<GraphProcess> getActiveNestedProcesses(final GraphProcess process)
  {
    String query = "from HibGraphProcess where parentToken.process.id = :processId and state in (0, 1, 2, 4)";

    return session.createQuery( query )
                  .setLong("processId", ((HibGraphProcess)process).getId())
                  .list();
  }

  public HibGraph loadGraph (final long graphId)
  {
    return (HibGraph)session.load( HibGraph.class, graphId );
  }

  public HibGraph findGraph (final long graphId)
  {
    return (HibGraph)session.get( HibGraph.class, graphId );
  }

  public HibGraphProcess loadProcess (final long processId)
  {
    return (HibGraphProcess)session.load( HibGraphProcess.class, processId );
  }

  public HibGraphProcess findProcess (final long processId)
  {
    return (HibGraphProcess)session.get( HibGraphProcess.class, processId );
  }

  public HibNodeToken loadNodeToken (final long tokenId)
  {
    return (HibNodeToken)session.load( HibNodeToken.class, tokenId );
  }

  public HibArcToken loadArcToken (final long tokenId)
  {
    return (HibArcToken)session.load( HibArcToken.class, tokenId );
  }

  public HibNodeToken findNodeToken (final long tokenId)
  {
    return (HibNodeToken)session.get( HibNodeToken.class, tokenId );
  }
}