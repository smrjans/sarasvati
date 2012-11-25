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

    Copyright 2012 Paul Lorenz
*/
package com.googlecode.sarasvati;


public interface EngineFactory
{
  /**
   * Returns an Engine which can be used for running processes.
   *
   * @return an Engine which can be used for running processes.
   */
  Engine getEngine();

  /**
   * Handles cleaning up after an engine has been used. The success
   * flags allows the EngineFactory to potentially commit or rollback
   * if a database or other persistence mechanism is being used.
   *
   * @param engine The engine to be disposed of
   * @param success Indicator of whether the operation was a success
   */
  void dispose(Engine engine, boolean success);
}
