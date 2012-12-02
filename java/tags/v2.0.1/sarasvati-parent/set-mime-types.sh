find . -name "*.html" -exec svn propset svn:mime-type "text/html" {} \;
find . -name "*.css" -exec svn propset svn:mime-type "text/css" {} \;
find . -name "*.gif" -exec svn propset svn:mime-type "image/gif" {} \;
