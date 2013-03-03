package com.googlecode.sarasvati.unittest.load;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Element;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.load.SarasvatiLoadException;
import com.googlecode.sarasvati.mem.MemEngine;
import com.googlecode.sarasvati.xml.XmlLoader;
import com.googlecode.sarasvati.xml.XmlProcessDefinition;

public class LoaderTest
{
  @Test(expected=SarasvatiLoadException.class)
  public void testMissingExternals()
  {
    Engine engine = new MemEngine();
    File basePath = new File( "src/test/process-definition/missing-external" );
    assert basePath.exists();
    engine.getLoader().loadNewAndChanged( basePath );
  }

  @Test
  public void testExternalPresent()
  {
    Engine engine = new MemEngine();
    File basePath = new File( "src/test/process-definition/external-present" );
    assert basePath.exists();
    engine.getLoader().loadNewAndChanged( basePath );

    Assert.assertNotNull( "Graph should be loaded", engine.getRepository().getLatestGraph( "external-present" ) );
    Assert.assertNotNull( "Graph should be loaded", engine.getRepository().getLatestGraph( "external" ) );
  }

  @Test
  public void testCustomProcessData() throws Exception
  {
    String wf = "<process-definition name='test' "
              + " xmlns='http://sarasvati.googlecode.com/ProcessDefinition'>"
              + "  <custom><testdata/></custom>"
              + "  <node type='wait' name='nodeA'/>"
              + "</process-definition>";

    StringLoader loader = new StringLoader();
    XmlProcessDefinition def = loader.load( wf );
    List<Object> customData = def.getCustomProcessData();
    Assert.assertNotNull( customData );
    Assert.assertEquals( 1, customData.size() );

    Element elem = (Element)customData.get(0);
    Assert.assertEquals( "testdata", elem.getLocalName() );
  }

  @Test
  public void testCustomProcessDataNeverNull() throws Exception
  {
    String wf = "<process-definition name='test' "
              + " xmlns='http://sarasvati.googlecode.com/ProcessDefinition'>"
              + "  <node type='wait' name='nodeA'/>"
              + "</process-definition>";

    StringLoader loader = new StringLoader();
    XmlProcessDefinition def = loader.load( wf );
    List<Object> customData = def.getCustomProcessData();
    Assert.assertNotNull( customData );
    Assert.assertEquals( 0, customData.size() );
  }
}


class StringLoader extends XmlLoader
{
  public XmlProcessDefinition load( String content ) throws JAXBException
  {
    Reader reader = new StringReader( content );
    return (XmlProcessDefinition)getUnmarshaller().unmarshal( reader );
  }
}

