/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/
package com.googlecode.sarasvati.unittest.converters;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.sarasvati.impl.MapEnv;

public class AttributeConvertersTest
{
  @Test public void testByte ()
  {
    MapEnv env = new MapEnv ();
    Byte test = 1;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Byte.class ) );
  }

  @Test public void testBoolean ()
  {
    MapEnv env = new MapEnv ();
    Boolean test = Boolean.TRUE;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Boolean.class ) );
  }

  @Test public void testShort ()
  {
    MapEnv env = new MapEnv ();
    Short test = 1;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Short.class ) );
  }

  @Test public void testCharacter ()
  {
    MapEnv env = new MapEnv ();
    Character test = 1;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Character.class ) );
  }

  @Test public void testInteger ()
  {
    MapEnv env = new MapEnv ();
    Integer test = 1;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Integer.class ) );
  }

  @Test public void testFloat ()
  {
    MapEnv env = new MapEnv ();
    Float test = 1f;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Float.class ) );
  }

  @Test public void testLong ()
  {
    MapEnv env = new MapEnv ();
    Long test = 1l;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Long.class ) );
  }

  @Test public void testDouble ()
  {
    MapEnv env = new MapEnv ();
    Double test = 1d;
    env.setAttribute( "test", test );
    Assert.assertEquals( test, env.getAttribute( "test", Double.class ) );
  }
}