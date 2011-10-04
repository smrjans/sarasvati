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
package com.googlecode.sarasvati.example.mem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.example.ApprovalNode;
import com.googlecode.sarasvati.example.ApprovalSetupNode;
import com.googlecode.sarasvati.example.CustomTestNode;
import com.googlecode.sarasvati.example.DumpNode;
import com.googlecode.sarasvati.example.InitNode;
import com.googlecode.sarasvati.example.LoggingExecutionListener;
import com.googlecode.sarasvati.example.MessageNode;
import com.googlecode.sarasvati.example.TaskState;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.ProcessDefinitionResolver;
import com.googlecode.sarasvati.mem.MemEngine;
import com.googlecode.sarasvati.mem.MemGraph;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;
import com.googlecode.sarasvati.xml.DefaultFileXmlProcessDefinitionResolver;
import com.googlecode.sarasvati.xml.XmlLoader;

public class MemExampleConsole
{
  public static boolean log = false;

  public static void main (final String[] args) throws Throwable
  {
    loadWorkflows();

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
        System.out.println( "iter: " + token.getEnv().getAttribute( "iter", Long.class ) );
        return token.getEnv().getAttribute( "iter", Long.class ) == 1000;
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

    while ( true )
    {
      MemEngine engine = new MemEngine();
      GraphProcess process = engine.startProcess( getGraph() );
      try
      {
        runWorkflow( process );
      }
      catch (Throwable t)
      {
        Throwable current = t;
        while ( current != null )
        {
          current.printStackTrace();
          if ( current.getCause() != null && current != current.getCause() )
          {
            current = current.getCause();
          }
          else
          {
            current = null;
          }
        }
        throw t;
      }
    }
  }

  public static void runWorkflow (final GraphProcess process)
  {
    while (true)
    {
      MemEngine engine = new MemEngine();
      if ( process.isComplete() )
      {
        System.out.println( "Workflow complete" );
        return;
      }

      List<MemExampleTask> tasks = MemExampleTaskList.getTasks();

      MemExampleTask t = null;

      while ( t == null )
      {
        int count = 0;
        for (MemExampleTask task : tasks )
        {
          System.out.println( (++count) + ": " + task.getName() + " - " + task.getState() +
                              (task.getNodeToken().getExecutionType().isBacktracked() ? " (backtracked)" : "" ));
        }

        System.out.print( "> " );
        String input = readLine();

        try
        {
          if ( "p".equalsIgnoreCase( input ) )
          {
            System.out.println( "Processing asynchronous tokens" );
            engine.executeQueuedArcTokens( process );
            break;
          }

          int line = Integer.parseInt( input );
          if ( line > 0 && line <= tasks.size() )
          {
            t = tasks.get( line - 1);
            processTask( t, engine );
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
    }
  }

  public static void processTask (final MemExampleTask t, final MemEngine engine)
  {
    System.out.println( "Task " );
    System.out.println( "\tName        : "  + t.getName() );
    System.out.println( "\tDescription : "  + t.getDescription() );
    System.out.println( "\tState       : "  + t.getState() );

    boolean backtrackable = t.getNodeToken().isComplete() && !t.getNodeToken().getExecutionType().isBacktracked();

    if ( t.getState() != TaskState.Open && !backtrackable )
    {
      return;
    }

    if ( backtrackable )
    {
      System.out.println( "1. Backtrack" );
    }
    else
    {
      System.out.println( "1. Complete" );

      if ( t.isRejectable() )
      {
        System.out.println( "2. Reject" );
      }
    }

    System.out.println( "Anything else to cancel" );

    System.out.print( "> " );
    String input = readLine();

    try
    {
      int line = Integer.parseInt( input );
      if ( line == 1 )
      {
        if ( backtrackable )
        {
          System.out.println( "Backtracking to task" );
          engine.backtrack( t.getNodeToken() );
        }
        else
        {
          System.out.println( "Completing task" );
          t.setState( TaskState.Completed );
          engine.complete( t.getNodeToken(), Arc.DEFAULT_ARC );
        }
      }
      else if ( line == 2 && t.isRejectable() )
      {
        System.out.println( "Rejecting task" );
        t.setState( TaskState.Rejected );
        engine.complete( t.getNodeToken(), "reject" );
      }
      else
      {
        System.out.println( "Ok. Doing nothing" );
      }
    }
    catch( NumberFormatException nfe )
    {
      System.out.println( "Ok. Doing nothing" );
    }
  }

  public static MemGraph getGraph ()
  {
    MemEngine engine = new MemEngine();

    MemGraph graph = null;

    while ( graph == null )
    {
      List<MemGraph> graphs = engine.getRepository().getGraphs();

      int count = 0;
      for ( MemGraph g : graphs )
      {
        System.out.println( (++count) + ": " + g.getName() + ": version " + g.getVersion() );
      }

      System.out.print( "> " );
      String input = readLine();

      if ( "log".equals( input ) )
      {
        if ( log )
        {
          engine.removeExecutionListener( LoggingExecutionListener.class );
        }
        else
        {
          engine.addExecutionListener( LoggingExecutionListener.class, ExecutionEventType.values() );
        }

        log = !log;
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

  public static void loadWorkflows () throws Exception
  {
    XmlLoader xmlLoader = new XmlLoader();
    MemEngine engine = new MemEngine();

    engine.addNodeType( "task", MemExampleTaskNode.class );
    engine.addNodeType( "init", InitNode.class );
    engine.addNodeType( "dump", DumpNode.class );
    engine.addNodeType( "customTest", CustomTestNode.class );
    engine.addNodeType( "approval", ApprovalNode.class );
    engine.addNodeType( "approvalSetup", ApprovalSetupNode.class );
    engine.addNodeType( "message", MessageNode.class );

    GraphLoader<MemGraph> wfLoader = engine.getLoader();

    File basePath = new File( "common/test-wf/" );
    ProcessDefinitionResolver resolver = new DefaultFileXmlProcessDefinitionResolver( xmlLoader, basePath );

    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept (final File dir, final String name)
      {
        return name.endsWith( ".wf.xml" ) && !name.equals( "demo-example.wf.xml" );
      }
    };

    for ( File file : basePath.listFiles( filter ) )
    {
      String name = file.getName();
      name = name.substring( 0, name.length() - ".wf.xml".length() );

      if ( engine.getRepository().getLatestGraph( name ) == null )
      {
        try
        {
          System.out.println( wfLoader.loadWithDependencies( name, resolver ) );
        }
        catch( Exception e )
        {
          System.out.println( "Failed to load: " + name + "   Message: " + e.getMessage() );
          e.printStackTrace();
        }
      }
    }
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