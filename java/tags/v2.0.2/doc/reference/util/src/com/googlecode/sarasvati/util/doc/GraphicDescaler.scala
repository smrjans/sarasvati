package com.googlecode.sarasvati.util.doc

import scala.io._;
import java.io._;

object GraphicDescaler
{
  def main(args:Array[String])
  {
    val Graphic = """(.*<graphic.*?)(scalefit=".*" width=".*")(.*\n)""".r

    for ( arg <- args )
    {
      var file = new File( arg )
      val newFile = new File( file.getParent, file.getName.substring( 0, file.getName.length - 4 ) + "-html.xml" )

      val writer = new FileWriter( newFile );

      for ( line <- Source.fromFile( arg ).getLines )
      {
        line match {
          case Graphic(start, middle, end) =>
            writer.write( start + end )
          case _ =>
            writer.write( line )
        }
      }

      writer.close;
    }
  }
}
