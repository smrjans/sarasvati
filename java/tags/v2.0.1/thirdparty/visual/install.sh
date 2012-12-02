version=7.3
mvn install:install-file -Dfile=${version}/visual-${version}.pom -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=visual -Dversion=${version} -Dpackaging=pom
mvn install:install-file -Dfile=${version}/visual-${version}.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=visual -Dversion=${version} -Dpackaging=jar
mvn install:install-file -Dfile=${version}/visual-${version}-javadoc.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=visual -Dversion=${version} -Dpackaging=jar -Dclassifier=javadoc
mvn install:install-file -Dfile=${version}/visual-${version}-sources.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=visual -Dversion=${version} -Dpackaging=jar -Dclassifier=sources
