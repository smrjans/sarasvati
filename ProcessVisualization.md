# Introduction #

Sarasvati offers the ability to generate an HTML image map of a process.

# API #

The API for process visualizations is almost exactly the same as that for graph visualizations. The difference is that when visualizing we are dealing with instances of `Node` and `Arc`, whereas with processes, we have instances of `VisualProcessNode` and `VisualProcessArc`. A `VisualProcessNode` wraps a `Node` as well as a `NodeToken`, which may be null, since not every `Node` may have been executed.

The most convenient way to create process definition image maps is to use the `ProcessImageMapCreator` class. This is used in conjunction with an instance of `ProcessToImageMap`, which is used to help render the image and image map.

`ProcessImageMapCreator` provides the following:
  * getMapContents() - The text which should go into a `map` tag
  * getImage () - The process image
  * writeImageToFile() - Convenience method to write the process image to a file

`ProcessToImageMap` provides the following:
  * The `Icon` used to render each node
  * The link for each node and arc
  * The hover text for each node and arc
  * A preference whether or not arc labels are rendered

`ProcessToImageMapAdapter` is a implementation of `ProcessToImageMap` which provides default implementations of each of the methods.

# Example #

Its use is demonstrated here, as it could be used in a JSP file.

```
<%
  String basePath = config.getServletContext().getRealPath( "/" );

  HibEngine hibEngine = new HibEngine( hibSession );
  GraphProcess process = hibEngine.getRepository().findProcess( 1 );
  final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

  ProcessToImageMapAdapter helper = new ProcessToImageMapAdapter ()
  {
    public String hrefForNode (VisualProcessNode node)
    {
      return "javascript:alert( 'You have selected " + node.getNode().getName() + "' );";
    }

    public String hoverForNode (VisualProcessNode node)
    {
      NodeToken token = node.getToken();
      if ( token == null )
      {
        return null;
      }
      String start = sdf.format( token.getCreateDate() );
      String end = (token.getCompleteDate() == null ? 
                      "Not yet finished" : 
                      sdf.format( token.getCompleteDate() ) )
      return "Started: " + start +
             " Finished: " + end;
      }
    };

    ProcessImageMapCreator imageMapCreator = new ProcessImageMapCreator( process, helper );
    imageMapCreator.writeImageToFile( "gif", basePath + name );
%>

  <map name="processMap">
    <%=imageMapCreator.getMapContents()%>
  </map>

  <div style="margin-left:10px; padding-top:10px">
    <image style="border:2px black solid" 
           src="<%=request.getContextPath()%>/test.gif" 
           usemap="#processMap"/>
  </div>
```

The resulting page would look something like:

![http://sarasvati.googlecode.com/svn/wiki/images/process-visualization1.jpg](http://sarasvati.googlecode.com/svn/wiki/images/process-visualization1.jpg)