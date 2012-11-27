package com.googlecode.sarasvati.test.suite;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.googlecode.sarasvati.test.TestEnv;
import com.googlecode.sarasvati.test.TestEnv.ExecutionMode;

@SuiteClasses ({
  IntegTestSuite.class
})
@RunWith(Suite.class)
public class MemAsyncSuite extends IntegTestSuite
{
  @BeforeClass
  public static void setup()
  {
    TestEnv.init(ExecutionMode.Async, TestEnv.ENGINE_MEMORY, null);
  }
}
