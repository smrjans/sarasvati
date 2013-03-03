package com.googlecode.sarasvati.example;

import org.hibernate.Session;

import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.example.hib.Task;
import com.googlecode.sarasvati.example.hib.TaskDAO;
import com.googlecode.sarasvati.example.mem.MemExampleTask;
import com.googlecode.sarasvati.example.mem.MemExampleTaskList;
import com.googlecode.sarasvati.hib.HibEngine;
import com.googlecode.sarasvati.mem.MemEngine;

public class TaskUtil
{
  public static void createMemTask (NodeToken token, String name, String description)
  {
    MemExampleTask newTask = new MemExampleTask( token, name, description, TaskState.Open );
    MemExampleTaskList.getTasks().add( newTask );

    Env env = token.getEnv();
    env.setAttribute( newTask.getName(), env.getAttribute( newTask.getName(), Long.class, 0L ) + 1 );

    env = token.getProcess().getEnv();
    env.setAttribute( newTask.getName(), env.getAttribute( newTask.getName(), Long.class, 0L ) + 1 );
  }

  public static void createHibTask (HibEngine engine, NodeToken token, String name, String description)
  {
    Session session = engine.getSession();

    Task newTask = new Task( token, name, description, TaskState.Open );
    session.save( newTask );

    Env env = token.getEnv();
    env.setAttribute( newTask.getName(), env.getAttribute( newTask.getName(), Integer.class, 0 ) + 1 );

    env = token.getProcess().getEnv();
    env.setAttribute( newTask.getName(), env.getAttribute( newTask.getName(), Integer.class, 0 ) + 1 );
  }

  public static void createTask (Engine engine, NodeToken token, String name, String description)
  {
    if ( engine instanceof MemEngine )
    {
      createMemTask( token, name, description );
    }
    else if ( engine instanceof HibEngine )
    {
      createHibTask( (HibEngine)engine, token, name, description );
    }
  }

  public static void backtrackTask (Engine engine, NodeToken token)
  {
    if (engine instanceof MemEngine )
    {
      for (MemExampleTask t : MemExampleTaskList.getTasks() )
      {
        if ( t.getNodeToken().equals( token ) )
        {
          t.setState( TaskState.Cancelled );
          return;
        }
      }
    }
    else if ( engine instanceof HibEngine )
    {
      Task task = TaskDAO.getTaskForToken( ((HibEngine)engine).getSession(), token );
      task.setState( TaskState.Cancelled );
    }
  }
}
