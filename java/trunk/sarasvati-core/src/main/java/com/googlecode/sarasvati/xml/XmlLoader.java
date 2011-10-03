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
                        Vincent Kirsch
 */

package com.googlecode.sarasvati.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.googlecode.sarasvati.load.ProcessDefinitionTranslator;
import com.googlecode.sarasvati.load.SarasvatiLoadException;

public class XmlLoader implements ProcessDefinitionTranslator<File>
{
  protected JAXBContext context;
  protected Schema      schema;

  public XmlLoader (final JAXBContext context) throws SarasvatiLoadException
  {
    this.context = context;
    loadSchema();
  }

  public XmlLoader (final String... extraPackages) throws SarasvatiLoadException
  {
    StringBuilder packages = new StringBuilder();
    packages.append( "com.googlecode.sarasvati.xml" );

    if (extraPackages != null)
    {
      for (String p : extraPackages)
      {
        packages.append( ":" );
        packages.append( p );
      }
    }

    try
    {
      this.context = JAXBContext.newInstance( packages.toString() );
    }
    catch (JAXBException e)
    {
      throw new SarasvatiLoadException( "Error while creating JAXB context", e );
    }
    loadSchema();
  }

  private void loadSchema () throws SarasvatiLoadException
  {
    InputStream is = getClass().getClassLoader().getResourceAsStream(
        "com/googlecode/sarasvati/ProcessDefinition.xsd" );

    if (is == null)
    {
      is = getClass().getClassLoader().getResourceAsStream(
          "/com/googlecode/sarasvati/ProcessDefinition.xsd" );
    }

    if (is == null)
    {
      throw new SarasvatiLoadException( "ProcessDefinition.xsd not found in classpath" );
    }

    try
    {
      SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
      schema = factory.newSchema( new StreamSource( is ) );
    }
    catch (SAXException se)
    {
      throw new SarasvatiLoadException( "Failed to load schema", se );
    }
    finally
    {
      try
      {
        is.close();
      }
      catch (IOException ioe)
      {
        // ignore
      }
    }
  }

  protected Unmarshaller getUnmarshaller () throws JAXBException
  {
    Unmarshaller u = context.createUnmarshaller();
    u.setSchema( schema );
    u.setEventHandler( new javax.xml.bind.helpers.DefaultValidationEventHandler() );
    return u;
  }

  protected Marshaller getMarshaller () throws JAXBException
  {
    Marshaller m = context.createMarshaller();
    m.setSchema( schema );
    m.setEventHandler( new javax.xml.bind.helpers.DefaultValidationEventHandler() );
    m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
    return m;
  }

  private XmlProcessDefinition loadProcessDefinition (final File file)
    throws SarasvatiLoadException
  {
    XmlProcessDefinition def = null;
    try
    {
      def = (XmlProcessDefinition) getUnmarshaller().unmarshal( file );
    }
    catch(JAXBException e)
    {
      throw new SarasvatiLoadException("Error while unmarshmalling " + file, e);
    }

    return def;
  }

  public void saveProcessDefinition (final XmlProcessDefinition xmlProcDef, final File file)
      throws JAXBException, IOException
  {
    FileOutputStream fOut = new FileOutputStream( file );

    try
    {
      getMarshaller().marshal( xmlProcDef, fOut );
    }
    finally
    {
      fOut.close();
    }
  }

  @Override
  public XmlProcessDefinition translate (final File source) throws SarasvatiLoadException
  {
    return loadProcessDefinition( source );
  }
}