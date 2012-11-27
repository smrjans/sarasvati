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

    Copyright 2008-2009 Paul Lorenz
*/
package com.googlecode.sarasvati.load.properties;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.sarasvati.load.SarasvatiLoadException;
import com.googlecode.sarasvati.load.definition.CustomDefinition;
import com.googlecode.sarasvati.util.SvUtil;

public class DOMToObjectLoadHelper
{
  public static void loadCustomIntoMap (final CustomDefinition customDefinition, final Map<String, String> map)
    throws SarasvatiLoadException
  {
    if ( customDefinition != null && customDefinition.getCustom() != null )
    {
      for ( Object custom : customDefinition.getCustom() )
      {
        if ( custom instanceof Element )
        {
          DOMToObjectLoadHelper.setBeanValues( EditorLoadPropertyMutator.INSTANCE, (Element)custom, null, map );
        }
      }
    }
  }

  public static Map<String, String> setBeanValues (final Object obj, final Node node) throws SarasvatiLoadException
  {
    Map<String, String> beanProperties = new HashMap<String, String>();
    setBeanValues( obj, node, null, beanProperties );
    return beanProperties;
  }

  public static void setBeanValues (final Object obj, final Node node, final String name, final Map<String, String> beanProperties) throws SarasvatiLoadException
  {
    PropertyMutator editor = null;
    Object currentValue = null;

    NodeList list = node.getChildNodes();

    boolean hasElementChildren = false;

    for ( int i = 0; i < list.getLength(); i++ )
    {
      Object child = list.item( i );
      if ( child instanceof Element )
      {
        if ( editor == null )
        {
          editor = getMutatorForProperty( obj, node.getLocalName() );
          currentValue = editor.getCurrentValue();
        }
        Element elemChild = (Element)child;
        String childName = getChildName( name, node.getLocalName() );
        setBeanValues( currentValue, elemChild, childName, beanProperties );
        hasElementChildren = true;
      }
    }

    if ( !hasElementChildren )
    {
      String value = node.getTextContent();

      if ( !SvUtil.isBlankOrNull( value ) )
      {
        if ( editor == null )
        {
          editor = getMutatorForProperty( obj, node.getLocalName() );
          currentValue = editor.getCurrentValue();
        }

        editor.setFromText( value.trim() );
        beanProperties.put( getChildName( name, node.getLocalName() ), value );
      }
    }

    NamedNodeMap attrs = node.getAttributes();

    for ( int i = 0; attrs != null && i < attrs.getLength(); i++ )
    {
      Attr attribute = (Attr)attrs.item( i );
      String attrName = attribute.getLocalName();

      if ( attrName.equals( "xmlns" ) || attribute.getName().startsWith( "xmlns:" ) )
      {
        continue;
      }

      if ( hasElementChildren )
      {
        if ( editor == null )
        {
          editor = getMutatorForProperty( obj, node.getLocalName() );
          currentValue = editor.getCurrentValue();
        }

        String childName = getChildName( name, attrName );
        setBeanValues( currentValue, attribute, childName, beanProperties );
      }
      else
      {
        // If we have something of the form
        // <foo bar="test">
        //   some stuff
        // </foo>
        // we want to set the 'foo' property to 'some stuff' and
        // set the fooBar property to 'test'
        String propertyName = node.getLocalName() + Character.toUpperCase( attrName.charAt( 0 ) ) + attrName.substring( 1 );
        String value = attribute.getNodeValue();
        getMutatorForProperty( obj, propertyName ).setFromText( value );
        beanProperties.put( getChildName( name, propertyName ), value );
      }
    }
  }

  private static String getChildName (final String prefix, final String name)
  {
    return prefix == null ? name : prefix + "." + name;
  }

  public static PropertyMutator getMutatorForProperty (final Object obj, final String name) throws SarasvatiLoadException
  {
    if ( obj == EditorLoadPropertyMutator.INSTANCE )
    {
      return EditorLoadPropertyMutator.INSTANCE;
    }

    BeanInfo beanInfo = null;

    try
    {
      beanInfo = Introspector.getBeanInfo( obj.getClass() );
    }
    catch( IntrospectionException ie )
    {
      throw new SarasvatiLoadException( "Could not introspect obj " + obj, ie );
    }

    PropertyDescriptor attr = null;

    for ( PropertyDescriptor pd : beanInfo.getPropertyDescriptors() )
    {
      if ( pd.getName().equals( name ) )
      {
        attr = pd;
        break;
      }
    }

    if ( attr == null )
    {
      throw new SarasvatiLoadException( obj.getClass().getName() + " has no attribute named " + name );
    }

    return PropertyMutatorRegistry.getMutator( attr, obj, new BasePropertyMutator() );
  }

  public static void setValues (final Object obj, final Map<String, String> values) throws SarasvatiLoadException
  {
    for (Entry<String, String> entry : values.entrySet() )
    {
      PropertyMutator mutator = null;
      Object target = obj;
      for ( String prop : entry.getKey().split( "\\." ) )
      {
        mutator = getMutatorForProperty( target, prop );
        target = mutator.getCurrentValue();
      }
      if (mutator != null)
      {
        mutator.setFromText( entry.getValue() );
      }
    }
  }

  public static List<Object> mapToDOM (final Map<String, String> properties,
                                       final Set<String> cdataTypes)
    throws IOException
  {
    Document doc = null;

    try
    {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware( false );
      doc = builderFactory.newDocumentBuilder().newDocument();
      doc.setDocumentURI( null );
    }
    catch ( ParserConfigurationException pce )
    {
      throw new IOException( "XML configuration error", pce );
    }

    if ( properties == null || properties.isEmpty() )
    {
      return Collections.emptyList();
    }

    List<Object> elements = new ArrayList<Object>( properties.size() );

    Map<String, Element> elemMap = new HashMap<String,Element>();

    for ( final Map.Entry<String, String> entry : properties.entrySet() )
    {
      Element parentElement = null;
      String key = entry.getKey();
      int index = key.lastIndexOf( '.' );
      String name = null;
      if ( index < 1)
      {
        name = key;
      }
      else
      {
        name = key.substring( index + 1 );
        String prefix = key.substring( 0, index );
        parentElement = elemMap.get( prefix );
        if ( parentElement == null )
        {
          StringBuilder fullName = new StringBuilder();
          String[] path = prefix.split( "\\." );
          for ( String pathElement : path )
          {
            if ( fullName.length() > 0 )
            {
              fullName.append( "." );
            }
            fullName.append( pathElement );

            Element element = elemMap.get( fullName.toString() );
            if ( element == null )
            {
              element = doc.createElementNS( "http://sarasvati.googlecode.com/ProcessDefinition", pathElement );
              if ( parentElement != null )
              {
                parentElement.appendChild( element );
              }
              else
              {
                elements.add( element );
              }
              elemMap.put( fullName.toString(), element );
            }
            parentElement = element;
          }
        }
      }

      Element element = doc.createElementNS( "http://sarasvati.googlecode.com/ProcessDefinition", name );

      if ( cdataTypes.contains( entry.getKey() ) )
      {
        element.appendChild( doc.createCDATASection( entry.getValue() ) );
      }
      else
      {
        element.appendChild( doc.createTextNode( entry.getValue() ) );
      }

      if ( parentElement != null )
      {
        parentElement.appendChild( element );
      }
      else
      {
        elements.add( element );
      }
      elemMap.put( key, element );
    }

    return elements;
  }
}