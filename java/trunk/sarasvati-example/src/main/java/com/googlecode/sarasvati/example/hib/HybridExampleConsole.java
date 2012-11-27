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
package com.googlecode.sarasvati.example.hib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StringType;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.example.ApprovalNode;
import com.googlecode.sarasvati.example.ApprovalSetupNode;
import com.googlecode.sarasvati.example.CustomTestNode;
import com.googlecode.sarasvati.example.LoggingExecutionListener;
import com.googlecode.sarasvati.example.MessageNode;
import com.googlecode.sarasvati.example.mem.MemExampleConsole;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraph;
import com.googlecode.sarasvati.load.DefaultNodeFactory;
import com.googlecode.sarasvati.mem.MemEngine;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricDateFunction;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;

public class HybridExampleConsole
{
  public static boolean log = false;

  public static void main (final String[] args) throws Exception
  {
    DefaultRubricFunctionRepository repository = DefaultRubricFunctionRepository.getGlobalInstance();

    repository.registerPredicate( "isRandOdd", new RubricPredicate()
    {
      @Override
      public boolean eval( final Engine engine, final NodeToken token )
      {
        return token.getEnv().getAttribute( "rand", Long.class ) % 2 == 1;
      }
    });

    repository.registerPredicate( "isRandEven", new RubricPredicate()
    {
      @Override
      public boolean eval( final Engine engine, final NodeToken token )
      {
        return token.getEnv().getAttribute( "rand", Long.class ) % 2 == 0;
      }
    });

    repository.registerPredicate( "isTenthIteration", new RubricPredicate()
    {
      @Override
      public boolean eval( final Engine engine, final NodeToken token )
      {
        return token.getEnv().getAttribute( "iter", Long.class ) == 10;
      }
    });

    repository.registerPredicate( "Approved", new RubricPredicate()
    {
      @Override
      public boolean eval( final Engine engine, final NodeToken token )
      {
        return true;
      }
    });

    repository.registerPredicate( "isFirstGuardEvaluation", new RubricPredicate()
    {
      @Override
      public boolean eval( final Engine engine, final NodeToken token )
      {
        return token.getDelayCount() == 0;
      }
    });

    repository.registerDateFunction("now", new RubricDateFunction()
    {
      @Override
      public Date eval(final Engine engine, final NodeToken token)
      {
        return new Date();
      }
    });

    HibTestSetup.init(false);

    DefaultNodeFactory.addGlobalCustomType( "customTest", CustomTestNode.class );
    DefaultNodeFactory.addGlobalCustomType( "approval", ApprovalNode.class );
    DefaultNodeFactory.addGlobalCustomType( "approvalSetup", ApprovalSetupNode.class );
    DefaultNodeFactory.addGlobalCustomType( "message", MessageNode.class );

    while ( true )
    {
      Session session = HibTestSetup.openSession();
      Transaction t = session.beginTransaction();
      HibEngine engine = new HibEngine( session );

      HibGraph graph = getGraph( engine );

      MemEngine memEngine = new MemEngine();
      GraphProcess process = memEngine.startProcess( graph );
      session.flush();
      t.commit();
      session.close();

      MemExampleConsole.runWorkflow( process );
    }
  }

  @SuppressWarnings("unchecked")
  public static HibGraph getGraph (final HibEngine engine)
  {
    HibGraph graph = null;

    while ( graph == null )
    {
      List<String> graphNames =
        engine.getSession()
              .createSQLQuery( "select distinct name from wf_graph order by name" )
              .addScalar( "name", StringType.INSTANCE )
              .list();

      List<HibGraph> graphs = new ArrayList<HibGraph>( graphNames.size() );

      for ( String graphName : graphNames )
      {
        graphs.add( engine.getRepository().getLatestGraph( graphName ) );
      }

      int count = 0;
      for ( HibGraph g : graphs )
      {
        System.out.println( (++count) + ": " + g.getName() + ": version " + g.getVersion() );
      }

      System.out.print( "> " );
      String input = readLine();

      if ( "log".equals( input ) )
      {
        log = !log;
        if ( log )
        {
          engine.addExecutionListener( LoggingExecutionListener.class, ExecutionEventType.values() );
        }
        else
        {
          engine.removeExecutionListener( LoggingExecutionListener.class );
        }
        System.out.println( "Logging set to: " + log );
        continue;
      }

      try
      {
        int line = Integer.parseInt( input );
        if ( line > 0 && line <= graphs.size() )
        {
          graph = graphs.get( line - 1);
        }
        else
        {
          System.out.println( "Please enter a valid number" );
        }
      }
      catch( NumberFormatException nfe )
      {
        System.out.println( "Please enter a valid number" );
      }
    }

    return graph;
  }

  public static String readLine ()
  {
    try
    {
      return new BufferedReader( new InputStreamReader( System.in ) ).readLine();
    }
    catch (IOException ioe )
    {
      throw new RuntimeException( ioe );
    }
  }
}