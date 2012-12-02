package com.googlecode.sarasvati.unittest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.googlecode.sarasvati.unittest.converters.AttributeConvertersTest;
import com.googlecode.sarasvati.unittest.event.EventMaskTest;
import com.googlecode.sarasvati.unittest.joinlang.JoinLangCompilerTest;
import com.googlecode.sarasvati.unittest.load.LoaderTest;
import com.googlecode.sarasvati.unittest.rubric.NotPushDownTest;
import com.googlecode.sarasvati.unittest.rubric.RubricCompilerTest;
import com.googlecode.sarasvati.unittest.rubric.RubricGuardResultTest;
import com.googlecode.sarasvati.unittest.rubric.RubricNumberTest;
import com.googlecode.sarasvati.unittest.rubric.RubricStringTest;
import com.googlecode.sarasvati.unittest.rubric.TypeValidatorTest;

@SuiteClasses ({
  AttributeConvertersTest.class,
  EventMaskTest.class,
  JoinLangCompilerTest.class,
  LoaderTest.class,
  NotPushDownTest.class,
  RubricCompilerTest.class,
  RubricGuardResultTest.class,
  RubricNumberTest.class,
  RubricStringTest.class,
  TypeValidatorTest.class
})
@RunWith(Suite.class)
public class UnitTestSuite
{
  // nothing by default
}
