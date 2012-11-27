#! /bin/sh

# Build PDF
#xsltproc --output pdf/sarasvati-manual.fo --xinclude /usr/share/xml/docbook/stylesheet/nwalsh/fo/docbook.xsl src/sarasvati-manual.xml
#fop -fo pdf/sarasvati-manual.fo -pdf pdf/sarasvati-manual.pdf

mkdir -p classes

scalac -d classes -sourcepath util/src `find util -name "*.scala"`

scala -cp classes com.googlecode.sarasvati.util.doc.GraphicDescaler src/*.xml > src/sarasvati-manual-html.xml
scala -cp classes com.googlecode.sarasvati.util.doc.ReferenceFixer src/sarasvati-manual.xml > src/sarasvati-manual-html.xml

# Build paginated HTML
xsltproc --stringparam html.stylesheet ../docbook.css --output html/ --xinclude /usr/share/xml/docbook/stylesheet/nwalsh/html/chunk.xsl src/sarasvati-manual-html.xml

# Build single page HTML
xsltproc --stringparam html.stylesheet ../docbook.css --output html_single/ --xinclude /usr/share/xml/docbook/stylesheet/nwalsh/html/onechunk.xsl src/sarasvati-manual-html.xml

rm src/*-html.xml
rm -rf classes