package com.googlecode.sarasvati.editor.model;

import java.util.LinkedList;
import java.util.List;

public class ValidationResults
{
  protected final List<String> errors = new LinkedList<String>();
  protected final List<String> warnings = new LinkedList<String>();
  protected final List<String> infos = new LinkedList<String>();

  public void error (final String msg)
  {
    errors.add( msg );
  }

  public void warning (final String msg)
  {
    warnings.add( msg );
  }

  public void info (final String msg)
  {
    infos.add( msg );
  }

  public boolean hasErrors ()
  {
    return !errors.isEmpty();
  }

  public boolean hasWarnings ()
  {
    return !warnings.isEmpty();
  }

  /**
   * @return the errors
   */
  public List<String> getErrors ()
  {
    return errors;
  }

  /**
   * @return the warnings
   */
  public List<String> getWarnings ()
  {
    return warnings;
  }

  /**
   * @return the infos
   */
  public List<String> getInfos ()
  {
    return infos;
  }
}
