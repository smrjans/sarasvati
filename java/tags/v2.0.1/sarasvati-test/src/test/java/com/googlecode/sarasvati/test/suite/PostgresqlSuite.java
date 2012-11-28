package com.googlecode.sarasvati.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses ({
  HibOneSessionPostgresqlSuite.class,
  HibAsyncPostgresqlSuite.class,
  HibEachNewSessionPostgresqlSuite.class
})
@RunWith(Suite.class)
public class PostgresqlSuite
{
  // nothing by default
}
