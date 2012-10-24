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

package com.googlecode.sarasvati.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.Token;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.TokenSetMember;
import com.googlecode.sarasvati.load.SarasvatiLoadException;
import com.googlecode.sarasvati.load.definition.ExternalDefinition;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;

public class SvUtil
{
  public static boolean equals (final Object o1, final Object o2)
  {
    if ( o1 == null )
    {
      return o2 == null;
    }
    return o1.equals( o2 );
  }

  public static boolean isBlankOrNull (final String str)
  {
    return str == null || str.trim().equals( "" );
  }

  public static String nullIfBlank (final String str)
  {
    return isBlankOrNull( str ) ? null : str;
  }

  public static String blankIfNull (final String str)
  {
    return isBlankOrNull( str ) ? "" : str;
  }

  public static boolean falseIfNull (final Boolean value)
  {
    return value != null && value;
  }

  public static int parseInt (final String value)
  {
    try
    {
      return Integer.parseInt( value );
    }
    catch (NumberFormatException nfe )
    {
      // Ignore exception. ANLTR will be throwing an alternate exception
    }

    return 0;
  }

  public static String normalizeQuotedString (final String string)
  {
    if ( string == null )
    {
      return null;
    }

    StringBuilder buf = new StringBuilder( string.substring( 1, string.length() - 1 ) );

    for ( int i = 0; i < buf.length(); i++ )
    {
      if ( i != buf.length() - 1 &&
          buf.charAt( i ) == '\\' &&
          buf.charAt( i + 1 ) == '"' )
      {
        buf.deleteCharAt( i );
        i--;
      }
    }

    return buf.toString();
  }

  public static TokenSet getTokenSet (final ArcToken token)
  {
    String tokenSetName = token.getArc().getEndNode().getJoinParam();

    // If a token set name is specified, wait for that token set
    if ( !SvUtil.isBlankOrNull( tokenSetName ) )
    {
      return SvUtil.getTokenSet( token, tokenSetName );
    }

    // Otherwise, wait on the first incomplete token set
    for ( ArcTokenSetMember setMember : token.getTokenSetMemberships() )
    {
      TokenSet tokenSet = setMember.getTokenSet();
      if ( !tokenSet.isComplete() )
      {
        return tokenSet;
      }
    }

    return null;
  }

  public static TokenSet getTokenSet (final Token token, final String name)
  {
    for ( TokenSetMember setMember : token.getTokenSetMemberships() )
    {
      if ( SvUtil.equals( name, setMember.getTokenSet().getName() ) )
      {
        return setMember.getTokenSet();
      }
    }
    return null;
  }

  public static TokenSetMember getTokenSetMember (final Token token, final String name)
  {
    for ( TokenSetMember setMember : token.getTokenSetMemberships() )
    {
      if ( SvUtil.equals( name, setMember.getTokenSet().getName() ) )
      {
        return setMember;
      }
    }
    return null;
  }

  public static int compare (final String s1, final String s2)
  {
    if ( s1 == null )
    {
      return s2 == null ? 0 : -1;
    }
    return s2 == null ? 1 : s1.compareTo( s2 );
  }

  public static void visitRecursive (final File basePath,
                                     final FileVisitor visitor,
                                     final boolean recurse)
  {
    Queue<File> dirs = new LinkedList<File>();

    if ( basePath.isDirectory() )
    {
      dirs.add( basePath );
    }

    while ( !dirs.isEmpty() )
    {
      File dir = dirs.remove();

      for ( File file : dir.listFiles() )
      {
        if ( recurse && file.isDirectory() )
        {
          dirs.add( file );
        }
        else if ( visitor.accept( dir, file.getName() ) )
        {
          visitor.accept( file );
        }
      }
    }
  }

  public static Collection<ProcessDefinition> getSorted (final Map<String, ProcessDefinition> processDefsByName)
  {
    Set<ProcessDefinition> processed = new LinkedHashSet<ProcessDefinition>( processDefsByName.size() );

    for ( ProcessDefinition def : processDefsByName.values() )
    {
      if ( !processed.contains( def ) )
      {
        addWithPrerequisites( def, processed, processDefsByName );
      }
    }

    return processed;
  }

  public static Object newInstanceOf(final String className, final String baseType)
  {
    Class<?> clazz = null;
    try
    {
      clazz = Class.forName(className);
    }
    catch ( final Exception e )
    {
      // If this fails, try using the thread's class loader
    }

    if ( clazz == null )
    {
      try
      {
        clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
      }
      catch( final Exception e )
      {
        // If this fails, try using the classloader for SvUtil
      }
    }

    if ( clazz == null )
    {
      try
      {
        clazz = SvUtil.class.getClassLoader().loadClass(className);
      }
      catch( final Exception e )
      {
        throw new SarasvatiException( "Failed to load " + baseType + " class: " + className, e );
      }
    }

    return newInstanceOf( clazz, baseType );
  }

  public static <T> T newInstanceOf(final Class<T> clazz, final String baseType)
  {
    try
    {
      return clazz.newInstance();
    }
    catch ( final InstantiationException e )
    {
      throw new SarasvatiException( baseType + "s must have a default public constructor. " +
                                    "They may not be non-static inner classes. " +
                                    "In other words, you must be able create new ones using " +
                                    clazz.getName() + ".class.newInstance()", e );
    }
    catch ( final IllegalAccessException e )
    {
      throw new SarasvatiException( baseType + "s must have a default public constructor. " +
                                    "They may not be non-static inner classes. " +
                                    "In other words, you must be able create new ones using " +
                                    clazz.getName() + ".class.newInstance()", e );
    }
  }

  private static void addWithPrerequisites (final ProcessDefinition def,
                                            final Set<ProcessDefinition> processed,
                                            final Map<String, ProcessDefinition> processDefsByName)
  {
    for ( ExternalDefinition external : def.getExternals() )
    {
      ProcessDefinition externalPD = processDefsByName.get( external.getProcessDefinition() );

      if ( externalPD == null )
      {
        throw new SarasvatiLoadException( "While loading process definition \"" + def.getName() +
                                          "\", could not find referenced external with name=\"" + external.getName() +
                                          "\" and process-definition=\"" + external.getProcessDefinition() + "\". " );
      }

      if ( !processed.contains( externalPD ) )
      {
        addWithPrerequisites( externalPD, processed, processDefsByName );
      }
    }

    processed.add( def );
  }

  /**
   * From http://www.rgagnon.com/javadetails/java-0596.html
   */
  private static final byte[] HEX_CHAR_TABLE = {
    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
  };

  /**
   * From http://www.rgagnon.com/javadetails/java-0596.html
   */
  public static String getHexString (final byte[] raw)
  {
    byte[] hex = new byte[2 * raw.length];
    int index = 0;

    for (byte b : raw)
    {
      int v = b & 0xFF;
      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
    }

    try
    {
      return new String(hex, "ASCII");
    }
    catch ( UnsupportedEncodingException uee )
    {
      throw new RuntimeException( uee );
    }
  }

  public static String getShortClassName(final Object o)
  {
    final String className = o.getClass().getName();
    final int lastDot = className.lastIndexOf('.');
    return lastDot >= 0 ? className.substring(lastDot + 1) : className;
  }
}