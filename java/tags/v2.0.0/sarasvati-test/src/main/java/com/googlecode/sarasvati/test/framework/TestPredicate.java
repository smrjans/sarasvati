package com.googlecode.sarasvati.test.framework;

public interface TestPredicate<T>
{
  boolean matches(final T token);
}
