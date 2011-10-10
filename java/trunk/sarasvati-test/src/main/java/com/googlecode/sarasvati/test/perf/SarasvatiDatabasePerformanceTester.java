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

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati.test.perf;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraphProcess;
import com.googlecode.sarasvati.hib.HibNode;
import com.googlecode.sarasvati.impl.NestedProcessNode;
import com.googlecode.sarasvati.impl.ScriptNode;
import com.googlecode.sarasvati.impl.WaitNode;
import com.googlecode.sarasvati.load.ProcessDefinitionResolver;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricPredicate;
import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.xml.DefaultFileXmlProcessDefinitionResolver;
import com.googlecode.sarasvati.xml.XmlLoader;

public class SarasvatiDatabasePerformanceTester
{
  protected static final Set<String> allowed = new HashSet<String>();

  static
  {
    allowed.add( "densest.wf.xml" );
    allowed.add( "embedded-task-rej.wf.xml" );
    allowed.add( "random-guard.wf.xml" );
    allowed.add( "three-deep.wf.xml" );
    allowed.add( "token-set-approvals.wf.xml" );
    allowed.add( "custom-node.wf.xml" );
  }

  private static final FilenameFilter filter = new FilenameFilter()
  {
    @Override
    public boolean accept( final File dir, final String name )
    {
      return allowed.contains( name );
    }
  };

  private final Map<String, TestPerfStats> graphs = new HashMap<String,TestPerfStats>();

  private HibEngine newEngine ()
  {
    HibEngine engine = (HibEngine)TestEnv.getEngine();

    engine.addNodeType( "node", HibNode.class);
    engine.addNodeType( "custom", CustomNode.class );
    engine.addNodeType( "script", ScriptNode.class );
    engine.addNodeType( "nested", NestedProcessNode.class );
    engine.addNodeType( "wait", WaitNode.class );

    return engine;
  }

  public long testLoad (final String name)
  {
    Engine engine = newEngine();
    long start = System.currentTimeMillis();
    Graph g = engine.getRepository().getLatestGraph( name );
    for ( Node node : g.getNodes() )
    {
      node.getName();
    }
    for ( Arc arc : g.getArcs() )
    {
      arc.getName();
    }
    TestEnv.commitSession();
    return System.currentTimeMillis() - start;
  }

  public void init () throws Exception
  {
    Engine engine = TestEnv.getEngine();
    XmlLoader xmlLoader = new XmlLoader();

    File baseDir = new File( "common/test-wf/" );
    assert baseDir.exists() : "Workflow process def dir not found.";

    ProcessDefinitionResolver resolver = new DefaultFileXmlProcessDefinitionResolver( xmlLoader, baseDir );

    for ( File file : baseDir.listFiles( filter ) )
    {
      ProcessDefinition xmlDef = xmlLoader.translate( file );
      engine.getLoader().loadWithDependencies( xmlDef.getName(), resolver );
      graphs.put( xmlDef.getName(), new TestPerfStats( xmlDef ) );
    }

    TestEnv.commitSession();
  }

  public void runGraphTest (final int iterations,
                            final int avgRollover)
    throws Exception
  {
    TestPerfStats.setRollover( avgRollover );

    for ( int i = 0; i < iterations; i++ )
    {
      Engine engine = TestEnv.getEngine();
      for ( TestPerfStats tg : graphs.values() )
      {
        long start = System.currentTimeMillis();
        engine.getLoader().loadDefinition( tg.getXmlProcDef() );
        tg.addInsertStat( System.currentTimeMillis() - start );
      }

      TestEnv.commitSession();

      for ( TestPerfStats tg : graphs.values() )
      {
        tg.addLoadStat( testLoad( tg.getName() ) );
      }

      System.out.println( "Completed iteration: " + i );
    }
  }

  public void dumpStats ()
  {
    for ( TestPerfStats tg : graphs.values() )
    {
      tg.dumpStats();
      System.out.println();
    }
  }

  public void testProcess (final String graphName,
                           final int iterations)
  {
    for ( int count = 0; count < iterations; count++ )
    {
      HibEngine engine = (HibEngine)TestEnv.getEngine();
      HibGraphProcess p = (HibGraphProcess) engine.startProcess( graphName );

      long processId = p.getId();

      TestEnv.commitSession();

      while ( !p.isComplete() )
      {
        int iter = 0;
        engine = newEngine();

        long start = System.currentTimeMillis();
        p = engine.getRepository().findProcess( processId );

        while ( !p.getExecutionQueue().isEmpty() && iter < 202)
        {
          engine.executeQueuedArcTokens( p );
          iter++;
        }

        TestEnv.commitSession();
        System.out.println( "Iteration " + count + ". Execute time: " + (System.currentTimeMillis() - start ) );
      }
    }
  }

  public static void main (final String[] args) throws Exception
  {
    System.getProperties().put(TestEnv.ENGINE_KEY, TestEnv.ENGINE_HIBERNATE);
    System.getProperties().put(TestEnv.DB_KEY, TestEnv.DATABASE_POSTGRESQL);

    SarasvatiDatabasePerformanceTester perfTester = new SarasvatiDatabasePerformanceTester();
    perfTester.init();

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
        return token.getEnv().getAttribute( "iter", Long.class ) == 100;
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


    System.out.println( "================================START========================================" );
    perfTester.testProcess( "random-guard", 1000 );
    //perfTester.runGraphTest( 100, 10 );
    //perfTester.dumpStats();
  }
}