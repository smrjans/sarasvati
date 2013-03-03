version=7.3
mvn install:install-file -Dfile=${version}/org-openide-util-lookup-${version}.pom -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util-lookup -Dversion=${version} -Dpackaging=pom
mvn install:install-file -Dfile=${version}/org-openide-util-lookup-${version}.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util-lookup -Dversion=${version} -Dpackaging=jar
mvn install:install-file -Dfile=${version}/org-openide-util-lookup-${version}-javadoc.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util-lookup -Dversion=${version} -Dpackaging=jar -Dclassifier=javadoc
mvn install:install-file -Dfile=${version}/org-openide-util-lookup-${version}-sources.jar -DgroupId=com.googlecode.sarasvati.thirdparty.netbeans -DartifactId=org-openide-util-lookup -Dversion=${version} -Dpackaging=jar -Dclassifier=sources
