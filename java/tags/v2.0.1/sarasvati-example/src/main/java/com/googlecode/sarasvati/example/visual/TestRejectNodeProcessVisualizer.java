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

    Copyright 2009 Cheong Chung Onn
                   Paul Lorenz
*/

package com.googlecode.sarasvati.example.visual;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;

import org.hibernate.Session;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.example.hib.HibTestSetup;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraph;
import com.googlecode.sarasvati.hib.HibNode;
import com.googlecode.sarasvati.impl.WaitNode;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.ProcessDefinitionResolver;
import com.googlecode.sarasvati.xml.DefaultFileXmlProcessDefinitionResolver;
import com.googlecode.sarasvati.xml.XmlLoader;

public class TestRejectNodeProcessVisualizer extends AbstractProcessVisualizer
{
  @Override
  public void init () throws Exception
  {
    HibTestSetup.init( false );
  }

  @Override
  public Session getSession ()
  {
    return HibTestSetup.openSession();
  }

  public static void main (final String[] args) throws Exception
  {
    load();
    completeProcess();
    new TestRejectNodeProcessVisualizer().run();
  }

  private static void completeProcess ()
  {
    Session session = HibTestSetup.openSession();
    session.beginTransaction();
    HibEngine engine = new HibEngine( session );

    /*
     * Select Exam Approval PD graph and start process
     */
    HibGraph graph = engine.getRepository().getLatestGraph( "reject-node" );
    GraphProcess process = engine.startProcess( graph );

    //Do a complete at the 3rd Node
    {
      Collection<NodeToken> activeNodeTokens = process.getActiveNodeTokens();
      NodeToken activeToken = activeNodeTokens.iterator().next();
      engine.complete( activeToken, Arc.DEFAULT_ARC );

    }

    //Do a reject at the 5th Node
    {
      Collection<NodeToken> activeNodeTokens = process.getActiveNodeTokens();
      NodeToken activeToken = activeNodeTokens.iterator().next();
      engine.complete( activeToken, "reject" );
    }

    session.getTransaction().commit();
  }

  private static void load () throws Exception
  {
    HibTestSetup.init( false );

    Session sess = HibTestSetup.openSession();
    sess.beginTransaction();

    HibEngine engine = new HibEngine( sess );
    XmlLoader xmlLoader = new XmlLoader();

    engine.addNodeType( "node", HibNode.class );
    engine.addNodeType( "custom", CustomNode.class );
    engine.addNodeType( "wait", WaitNode.class );
    engine.addNodeType( "end", HibNode.class);

    GraphLoader<HibGraph> wfLoader = engine.getLoader();

    File baseDir = new File( "test/com/googlecode/sarasvati/visual/test/" );
    assert baseDir.exists() : "Workflow process def dir not found.";

    ProcessDefinitionResolver resolver = new DefaultFileXmlProcessDefinitionResolver( xmlLoader, baseDir );

    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept (final File dir, final String name)
      {
        return name.endsWith( ".wf.xml" ) && name.equals( "reject-node.wf.xml" );
      }
    };

    for ( File file : baseDir.listFiles( filter ) )
    {
      String name = file.getName();
      name = name.substring( 0, name.length() - ".wf.xml".length() );

      try
      {
        wfLoader.loadWithDependencies( name, resolver );
        System.out.println( "Loaded " + name );
      }
      catch ( Exception t )
      {
        System.out.println( "Failed to load: " + name + "  because: " + t.getMessage() );
        t.printStackTrace();
        return;
      }
    }

    sess.getTransaction().commit();
  }
}