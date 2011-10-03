package com.googlecode.sarasvati.unittest.editor.xml;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.editor.xml.EditorXmlLoader;
import com.googlecode.sarasvati.editor.xml.XmlEditorExternal;
import com.googlecode.sarasvati.editor.xml.XmlEditorNode;
import com.googlecode.sarasvati.editor.xml.XmlEditorProperties;

public class TestEditorXml
{
  @Test
  public void testXml () throws Exception
  {
    XmlEditorProperties props = new XmlEditorProperties();

    XmlEditorNode node = new XmlEditorNode();
    node.setName( "foo" );
    node.setX( 1 );
    node.setY( 2 );
    props.getNodes().add( node );

    node = new XmlEditorNode();
    node.setName( "bar" );
    node.setX( 3 );
    node.setY( 4 );
    props.getNodes().add( node );


    XmlEditorExternal external = new XmlEditorExternal();
    external.setName( "baz" );
    external.setX( 5 );
    external.setY( 6 );
    props.getExternals().add( external );

    external = new XmlEditorExternal();
    external.setName( "zeb" );
    external.setX( 7 );
    external.setY( 8 );
    props.getExternals().add( external );

    EditorXmlLoader loader = new EditorXmlLoader();

    File tmpFile = File.createTempFile( "unitTest", "" );
    tmpFile.deleteOnExit();

    loader.saveEditorProperties( props, tmpFile );

    XmlEditorProperties props2  = loader.loadEditorProperties( tmpFile );

    tmpFile.delete();

    Assert.assertEquals( "Loader editor properties should have two nodes", 2, props2.getNodes().size() );
    Assert.assertEquals( "Loader editor properties should have two externals", 2, props2.getExternals().size() );

    Assert.assertEquals( "First node should have name 'foo'", "foo", props2.getNodes().get( 0 ).getName() );
    Assert.assertEquals( "First node should have name x=1", 1, props2.getNodes().get( 0 ).getX() );
    Assert.assertEquals( "First node should have name y=2", 2, props2.getNodes().get( 0 ).getY() );

    Assert.assertEquals( "Second node should have name 'bar'", "bar", props2.getNodes().get( 1 ).getName() );
    Assert.assertEquals( "Second node should have name x=3", 3, props2.getNodes().get( 1 ).getX() );
    Assert.assertEquals( "Second node should have name y=4", 4, props2.getNodes().get( 1 ).getY() );

    Assert.assertEquals( "First external should have name 'baz'", "baz", props2.getExternals().get( 0 ).getName() );
    Assert.assertEquals( "First external should have name x=5", 5, props2.getExternals().get( 0 ).getX() );
    Assert.assertEquals( "First external should have name y=6", 6, props2.getExternals().get( 0 ).getY() );

    Assert.assertEquals( "Second node should have name 'zeb'", "zeb", props2.getExternals().get( 1 ).getName() );
    Assert.assertEquals( "Second node should have name x=7", 7, props2.getExternals().get( 1 ).getX() );
    Assert.assertEquals( "Second node should have name y=8", 8, props2.getExternals().get( 1 ).getY() );
  }
}
