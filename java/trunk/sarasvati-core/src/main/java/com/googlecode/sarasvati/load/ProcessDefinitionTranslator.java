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

    Copyright 2009 Vincent Kirsch
 */
 package com.googlecode.sarasvati.load;

import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.load.definition.ProcessDefinition;

/**
 * A translator will take a process definition created by an external actor and in any format
 * (e.g. an hand-written XML file, any class that's the output of a graphical editor, etc.)
 * and translate (or convert or adapt) it into a {@link ProcessDefinition} that the {@link GraphLoader}
 * will understand and use in order to build a certain flavour of {@link Graph}.
 *
 * <br/>
 *
 * It only has one {@code translate} method that takes an instance of the external representation
 * (or a way to find or build it) and returns a {@link ProcessDefinition} built from the input.
 * @see GraphLoader
 * @author vkirsch
 *
 * @param T The class representing the external representation (or a way to find or build it, like a file name)
 */
public interface ProcessDefinitionTranslator<T>
{
  ProcessDefinition translate (T source) throws SarasvatiLoadException;
}
