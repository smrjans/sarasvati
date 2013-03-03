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

    Copyright 2009 Paul Lorenz/Vincent Kirsch
 */
package com.googlecode.sarasvati.load.definition;

import java.util.List;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.load.GraphFactory;
import com.googlecode.sarasvati.load.GraphLoader;
import com.googlecode.sarasvati.load.ProcessDefinitionTranslator;

/**
 * An ExternalDefinition is the result of the translation of a certain external source containing
 * the definition of a process. That external source may contain information indicating it will
 * point to another definition (external), and that information is translated into an ExternalDefinition
 * through a {@link ProcessDefinitionTranslator}. This ExternalDefinition will then in its turn be used
 * by the {@link GraphLoader} to create a {@link Graph} through the {@link GraphFactory}.
 * The default kind of external source is an XML file containing &lt;external&gt; tags.
 */
public interface ExternalDefinition
{
  public String getProcessDefinition ();

  public String getName ();

  public List<? extends ExternalArcDefinition> getExternalArcs ();

  public CustomDefinition getCustom ();

  public Integer getX ();

  public Integer getY ();
}