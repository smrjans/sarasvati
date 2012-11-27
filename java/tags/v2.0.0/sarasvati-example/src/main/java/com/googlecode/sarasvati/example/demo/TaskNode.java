package com.googlecode.sarasvati.example.demo;

import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.mem.MemNode;

public class TaskNode extends MemNode {
  protected String job, user;

  public String getTaskJob () { return job; }
  public void setTaskJob (final String job) { this.job = job; }
  public String getTaskUser () { return user; }
  public void setTaskUser (final String user) { this.user = user; }

  @Override
  public void execute (final Engine engine, final NodeToken token) {
    System.out.println( "Job: " + job + " User: " + user );
    engine.complete( token, Arc.DEFAULT_ARC );
  }
}
