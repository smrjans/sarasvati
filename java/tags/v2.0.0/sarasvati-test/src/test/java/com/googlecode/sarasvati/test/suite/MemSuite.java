package com.googlecode.sarasvati.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses ({
  MemOneSessionSuite.class,
  MemAsyncSuite.class
})
@RunWith(Suite.class)
public class MemSuite
{
  // nothing by default
}
