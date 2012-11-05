package com.googlecode.sarasvati.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.googlecode.sarasvati.test.backtracking.BacktrackCompletedTest;
import com.googlecode.sarasvati.test.backtracking.BacktrackJoinTest;
import com.googlecode.sarasvati.test.backtracking.BacktrackLinearTest;
import com.googlecode.sarasvati.test.backtracking.BacktrackSplitJoinTest;
import com.googlecode.sarasvati.test.backtracking.BacktrackUnevenTreesTest;
import com.googlecode.sarasvati.test.env.TestTransientsAttributes;
import com.googlecode.sarasvati.test.env.TokenSetMemberEnvTest;
import com.googlecode.sarasvati.test.execution.ExecutionErrorsTest;
import com.googlecode.sarasvati.test.execution.ExternalsTest;
import com.googlecode.sarasvati.test.execution.MultiLabelCompleteTest;
import com.googlecode.sarasvati.test.execution.MultiStartNodeTest;
import com.googlecode.sarasvati.test.execution.NestedProcessesTest;
import com.googlecode.sarasvati.test.execution.TokenSetTest;
import com.googlecode.sarasvati.test.joinlang.ArcsRequiredRequiredTest;
import com.googlecode.sarasvati.test.joinlang.AtLeastArcsRequiredRequiredTest;
import com.googlecode.sarasvati.test.joinlang.AtLeastLabelArcsRequiredRequiredTest;
import com.googlecode.sarasvati.test.joinlang.LabelArcsRequiredRequiredTest;
import com.googlecode.sarasvati.test.joinlang.NodeRequiredTest;
import com.googlecode.sarasvati.test.joinlang.TokenSetRequiredTest;
import com.googlecode.sarasvati.test.traversal.TraversalTest;

@SuiteClasses ({
  BacktrackCompletedTest.class,
  BacktrackJoinTest.class,
  BacktrackLinearTest.class,
  BacktrackSplitJoinTest.class,
  BacktrackUnevenTreesTest.class,

  TestTransientsAttributes.class,
  TokenSetMemberEnvTest.class,

  ExecutionErrorsTest.class,
  ExternalsTest.class,
  MultiLabelCompleteTest.class,
  MultiStartNodeTest.class,
  NestedProcessesTest.class,
  TokenSetTest.class,

  ArcsRequiredRequiredTest.class,
  AtLeastArcsRequiredRequiredTest.class,
  AtLeastLabelArcsRequiredRequiredTest.class,
  LabelArcsRequiredRequiredTest.class,
  NodeRequiredTest.class,
  TokenSetRequiredTest.class,

  TraversalTest.class
})
@RunWith(Suite.class)
public class IntegTestSuite
{
  // nothing by default
}
