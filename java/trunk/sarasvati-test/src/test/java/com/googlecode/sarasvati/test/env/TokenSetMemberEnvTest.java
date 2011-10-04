package com.googlecode.sarasvati.test.env;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.env.TokenSetMemberEnv;
import com.googlecode.sarasvati.hib.HibTokenSet;
import com.googlecode.sarasvati.hib.HibTokenSetMemberEnv;
import com.googlecode.sarasvati.mem.MemTokenSet;
import com.googlecode.sarasvati.mem.MemTokenSetMemberEnv;

public class TokenSetMemberEnvTest
{
  @Test
  public void testHibTokenSetMemberEnv()
  {
    final HibTokenSet tokenSet = new HibTokenSet(null, "testTokenSet", 3);
    final HibTokenSetMemberEnv env = new HibTokenSetMemberEnv(tokenSet);
    testEnv(env);
  }

  @Test
  public void testMemTokenSetMemberEnv()
  {
    final MemTokenSet tokenSet = new MemTokenSet(null, "testTokenSet", 3);
    final MemTokenSetMemberEnv env = new MemTokenSetMemberEnv(tokenSet);
    testEnv(env);
  }

  protected void testEnv(final TokenSetMemberEnv env)
  {
    // Execute twice, so we make sure it all still works after doing removeAttribute
    for ( int j = 0; j < 2; j++ )
    {
      for ( int i = 0; i < 3; i++ )
      {
        Assert.assertFalse( env.hasAttribute(i, "foo") );
        Assert.assertFalse( env.hasAttribute(i, "bar") );
        Assert.assertFalse( env.hasAttribute(i, "baz") );

        Assert.assertNull( env.getAttribute(i, "foo") );
        Assert.assertNull( env.getAttribute(i, "bar") );
        Assert.assertNull( env.getAttribute(i, "baz") );

        env.setAttribute(i, "foo", "fooValue" + i);
        env.setAttribute(i, "bar", "barValue" + i);
        env.setAttribute(i, "baz", "bazValue" + i);

        Assert.assertTrue( env.hasAttribute(i, "foo") );
        Assert.assertTrue( env.hasAttribute(i, "bar") );
        Assert.assertTrue( env.hasAttribute(i, "baz") );

        Assert.assertEquals( "fooValue" + i, env.getAttribute(i, "foo") );
        Assert.assertEquals( "barValue" + i, env.getAttribute(i, "bar") );
        Assert.assertEquals( "bazValue" + i, env.getAttribute(i, "baz") );

        env.setAttribute(i, "foo", "fooValue2" + i);
        env.setAttribute(i, "bar", "barValue2" + i);
        env.setAttribute(i, "baz", "bazValue2" + i);

        Assert.assertEquals( "fooValue2" + i, env.getAttribute( i, "foo" ) );
        Assert.assertEquals( "barValue2" + i, env.getAttribute( i, "bar" ) );
        Assert.assertEquals( "bazValue2" + i, env.getAttribute( i, "baz" ) );

        Assert.assertTrue( env.hasAttribute( i, "foo" ) );
        Assert.assertTrue( env.hasAttribute( i, "bar" ) );
        Assert.assertTrue( env.hasAttribute( i, "baz" ) );

        env.removeAttribute( i, "foo" );
        env.removeAttribute( i, "bar" );
        env.removeAttribute( i, "baz" );

        Assert.assertNull( env.getAttribute(i, "foo" ) );
        Assert.assertNull( env.getAttribute(i, "bar" ) );
        Assert.assertNull( env.getAttribute(i, "baz" ) );

        Assert.assertFalse( env.hasAttribute(i, "foo" ) );
        Assert.assertFalse( env.hasAttribute(i, "bar" ) );
        Assert.assertFalse( env.hasAttribute(i, "baz" ) );
      }

      env.setAttribute( "foo", Arrays.asList( new String[] { "foo0", "foo1", "foo2" } ) );
      env.setAttribute( "bar", Arrays.asList( new String[] { "bar0", "bar1", "bar2" } ) );
      env.setAttribute( "baz", Arrays.asList( new String[] { "baz0", "baz1", "baz2" } ) );

      for ( int i = 0; i < 3; i++ )
      {
        Assert.assertEquals( "foo" + i, env.getAttribute(i, "foo" ) );
        Assert.assertEquals( "bar" + i, env.getAttribute(i, "bar" ) );
        Assert.assertEquals( "baz" + i, env.getAttribute(i, "baz" ) );

        Assert.assertTrue( env.hasAttribute(i, "foo") );
        Assert.assertTrue( env.hasAttribute(i, "bar") );
        Assert.assertTrue( env.hasAttribute(i, "baz") );
      }

      env.setAttribute( "foo", Arrays.asList( new String[] { "2foo0", "2foo1", "2foo2" } ) );
      env.setAttribute( "bar", Arrays.asList( new String[] { "2bar0", "2bar1", "2bar2" } ) );
      env.setAttribute( "baz", Arrays.asList( new String[] { "2baz0", "2baz1", "2baz2" } ) );

      for ( int i = 0; i < 3; i++ )
      {
        Assert.assertEquals( "2foo" + i, env.getAttribute(i, "foo") );
        Assert.assertEquals( "2bar" + i, env.getAttribute(i, "bar") );
        Assert.assertEquals( "2baz" + i, env.getAttribute(i, "baz") );

        Assert.assertTrue( env.hasAttribute(i, "foo") );
        Assert.assertTrue( env.hasAttribute(i, "bar") );
        Assert.assertTrue( env.hasAttribute(i, "baz") );

        env.removeAttribute(i, "foo");
        env.removeAttribute(i, "bar");
        env.removeAttribute(i, "baz");

        Assert.assertNull( env.getAttribute(i, "foo") );
        Assert.assertNull( env.getAttribute(i, "bar") );
        Assert.assertNull( env.getAttribute(i, "baz") );

        Assert.assertFalse( env.hasAttribute(i, "foo") );
        Assert.assertFalse( env.hasAttribute(i, "bar") );
        Assert.assertFalse( env.hasAttribute(i, "baz") );
      }
    }
  }
}