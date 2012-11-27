#!/bin/sh
find doc -name "*.html" -exec svn propset "svn:mime-type" "text/html" {} \; -ls
find doc -name "*.html" -exec svn propset "svn:eol-style" "native" {} \; -ls
find doc -name "*.css" -exec svn propset "svn:mime-type" "text/css" {} \; -ls
find doc -name "*.css" -exec svn propset "svn:eol-style" "native" {} \; -ls
find doc -name "*.js" -exec svn propset "svn:mime-type" "text/javascript" {} \; -ls
find doc -name "*.js" -exec svn propset "svn:eol-style" "native" {} \; -ls
find doc -name "*.gif" -exec svn propset "svn:mime-type" "image/gif" {} \; -ls
