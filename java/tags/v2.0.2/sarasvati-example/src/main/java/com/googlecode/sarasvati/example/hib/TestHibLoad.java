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

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.hibernate.Session;

import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.example.ApprovalNode;
import com.googlecode.sarasvati.example.ApprovalSetupNode;
import com.googlecode.sarasvati.example.CustomTestNode;
import com.googlecode.sarasvati.example.MessageNode;
import com.googlecode.sarasvati.example.mem.MemExampleConsole;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.hib.HibGraph;
import com.googlecode.sarasvati.hib.HibNode;
import com.googlecode.sarasvati.impl.NestedProcessNode;
import com.googlecode.sarasvati.impl.ScriptNode;
import com.googlecode.sarasvati.impl.WaitNode;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.LoadResult;

public class TestHibLoad
{
  public static void main (final String[] args) throws Exception
  {
    HibTestSetup.init( false );

    Session sess = HibTestSetup.openSession();
    sess.beginTransaction();

    HibEngine engine = new HibEngine( sess );

    engine.addNodeType( "node", HibNode.class);
    engine.addNodeType( "task", HibExampleTaskNode.class );
    engine.addNodeType( "init", InitNode.class );
    engine.addNodeType( "dump", DumpNode.class );
    engine.addNodeType( "async", AsyncNode.class );
    engine.addNodeType( "custom", CustomNode.class );
    engine.addNodeType( "script", ScriptNode.class );
    engine.addNodeType( "nested", NestedProcessNode.class );
    engine.addNodeType( "wait", WaitNode.class );
    engine.addNodeType( "dumpTypeDupe", DumpNode.class );
    engine.addNodeType( "customTest", CustomTestNode.class );
    engine.addNodeType( "approval", ApprovalNode.class );
    engine.addNodeType( "approvalSetup", ApprovalSetupNode.class );
    engine.addNodeType( "message", MessageNode.class );

    GraphLoader<HibGraph> wfLoader = engine.getLoader();

    final File baseDir = new File(MemExampleConsole.class.getClassLoader().getResource("custom-node.wf.xml").toURI()).getParentFile();

    assert baseDir.exists() : "Workflow process def dir not found.";

    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept( final File dir, final String name )
      {
        return name.endsWith( ".wf.xml" ) && !name.equals( "demo-example.wf.xml" );
      }
    };

    List<LoadResult> results = wfLoader.loadNewAndChanged( baseDir, filter );

    for ( LoadResult result : results )
    {
      System.out.println( result );
    }

    sess.getTransaction().commit();
  }
}