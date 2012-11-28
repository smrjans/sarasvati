package com.googlecode.sarasvati.example.demo;

import java.io.File;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.mem.MemEngine;
import com.googlecode.sarasvati.xml.XmlLoader;

public class Demo
{
  @SuppressWarnings("unused")
  public static void main (final String[] args) throws Exception
  {
    MemEngine engine = new MemEngine();
    engine.addNodeType( "task", TaskNode.class );
    engine.getLoader().loadDefinition( new XmlLoader().translate( new File("/home/paul/workspace/wf-common/test-wf/hello-world.wf.xml") ) );
    Graph graph = engine.getRepository().getLatestGraph( "hello-world" );
    GraphProcess p = engine.startProcess( graph );
  }
}
