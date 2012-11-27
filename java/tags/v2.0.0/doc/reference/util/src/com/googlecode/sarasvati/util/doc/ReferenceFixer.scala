package com.googlecode.sarasvati.util.doc

import scala.io.Source;

object ReferenceFixer
{
  def main(args:Array[String])
  {
    // <xi:include href="what-is-workflow.xml" xmlns:xi="http://www.w3.org/2001/XInclude"/>
    val Include = """(.*href=")(.*)(\.xml".*\n)""".r;

    for ( arg <- args )
    {
      for ( line <- Source.fromFile( arg ).getLines )
      {
        line match {
          case Include(prefix, href, suffix) =>
            System.out.print( "%s%s-html%s".format( prefix, href, suffix ) )
          case _ => System.out.print( line );
        }
      }
    }
  }
}
