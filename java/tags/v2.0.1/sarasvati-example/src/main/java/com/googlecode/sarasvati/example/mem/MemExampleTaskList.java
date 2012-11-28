/**
 * Created on Jun 12, 2008
 */
package com.googlecode.sarasvati.example.mem;

import java.util.LinkedList;
import java.util.List;

public class MemExampleTaskList
{
  protected static List<MemExampleTask> tasks = new LinkedList<MemExampleTask>();

  public static List<MemExampleTask> getTasks ()
  {
    return tasks;
  }
}
