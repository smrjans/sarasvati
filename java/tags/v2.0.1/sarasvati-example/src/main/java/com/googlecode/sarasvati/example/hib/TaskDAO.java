package com.googlecode.sarasvati.example.hib;

import org.hibernate.Session;

import com.googlecode.sarasvati.NodeToken;

public class TaskDAO
{
  public static Task getTaskForToken (final Session session, final NodeToken token)
  {
    String query = "from Task where nodeToken = :token";
    return (Task)session.createQuery( query ).setEntity( "token", token ).uniqueResult();
  }
}
