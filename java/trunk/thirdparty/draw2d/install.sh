version=3.8.1
mvn install:install-file -Dfile=${version}/draw2d-${version}.pom -DgroupId=com.googlecode.sarasvati.thirdparty.eclipse -DartifactId=draw2d -Dversion=${version} -Dpackaging=pom
mvn install:install-file -Dfile=${version}/draw2d-${version}.jar -DgroupId=com.googlecode.sarasvati.thirdparty.eclipse -DartifactId=draw2d -Dversion=${version} -Dpackaging=jar
mvn install:install-file -Dfile=${version}/draw2d-${version}-javadoc.jar -DgroupId=com.googlecode.sarasvati.thirdparty.eclipse -DartifactId=draw2d -Dversion=${version} -Dpackaging=jar -Dclassifier=javadoc
mvn install:install-file -Dfile=${version}/draw2d-${version}-sources.jar -DgroupId=com.googlecode.sarasvati.thirdparty.eclipse -DartifactId=draw2d -Dversion=${version} -Dpackaging=jar -Dclassifier=sources
