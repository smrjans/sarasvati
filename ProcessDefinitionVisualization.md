# Introduction #

Sarasvati offers the ability to generate an HTML image map of a process definition.

# API #

The most convenient way to create process definition image maps is to use the `GraphImageMapCreator` class. This is used in conjunction with an instance of `GraphToImageMap`, which is used to help render the image and image map.

`GraphImageMapCreator` provides the following:
  * getMapContents() - The text which should go into a `map` tag
  * getImage () - The graph image
  * writeImageToFile() - Convenience method to write the graph image to a file

`GraphToImageMap` provides the following:
  * The `Icon` used to render each node
  * The link for each node and arc
  * The hover text for each node and arc
  * A preference whether or not arc labels are rendered

`GraphToImageMapAdapter` is a implementation of `GraphToImageMap` which provides default implementations of each of the methods.

# Example #

Its use is demonstrated here, as it could be used in a JSP file.

```
<%
  String basePath = config.getServletContext().getRealPath( "/" );

  HibEngine hibEngine = new HibEngine( hibSession );
  Graph graph = hibEngine.getRepository().getLatestGraph( "embedded-task-rej" );

  GraphToImageMapAdapter helper = new GraphToImageMapAdapter ()
  {
    public String hrefForNode (Node node)
    {
      return "javascript:alert( 'You have selected " + node.getName() + "' );";
    }

    public String hoverForNode (Node node)
    {
      return "Name: " + node.getName() + ",  Type: " + node.getType() +
             ",  Guard: " + node.getGuard() + ",  Is start: " + node.isStart() +
             ",  Is join: " + node.isJoin();
    }
  };

  GraphImageMapCreator imageMapCreator = new GraphImageMapCreator( graph, helper );
  imageMapCreator.writeImageToFile( "gif", basePath + "/test.gif" );
%>

  <map name="graphMap">
    <%=imageMapCreator.getMapContents()%>
  </map>

  <div style="margin-left:10px; padding-top:10px">
    <image style="border:2px black solid" 
           src="<%=request.getContextPath()%>/test.gif" 
           usemap="#graphMap"/>
  </div>
```

The resulting page would look something like:

![http://sarasvati.googlecode.com/svn/wiki/images/graph-visualization.jpg](http://sarasvati.googlecode.com/svn/wiki/images/graph-visualization.jpg)