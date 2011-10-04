package com.googlecode.sarasvati.unittest.load;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.load.SarasvatiLoadException;
import com.googlecode.sarasvati.mem.MemEngine;

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

}

