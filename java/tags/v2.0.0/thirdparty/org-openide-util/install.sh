version=7.3
mvn install:install-file -Dfile=${version}/org-openide-util-${version}.pom -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util -Dversion=${version} -Dpackaging=pom
mvn install:install-file -Dfile=${version}/org-openide-util-${version}.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util -Dversion=${version} -Dpackaging=jar
mvn install:install-file -Dfile=${version}/org-openide-util-${version}-javadoc.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util -Dversion=${version} -Dpackaging=jar -Dclassifier=javadoc
mvn install:install-file -Dfile=${version}/org-openide-util-${version}-sources.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util -Dversion=${version} -Dpackaging=jar -Dclassifier=sources
