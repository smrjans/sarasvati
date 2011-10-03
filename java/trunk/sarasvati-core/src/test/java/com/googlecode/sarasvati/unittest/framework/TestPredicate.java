package com.googlecode.sarasvati.unittest.framework;

public interface TestPredicate<T>
{
  boolean matches(final T token);
}
