# Use mvn versions:set to update versions

version=`cd sarasvati-parent && mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | sed -n -e '/^\[.*\]/ !{ /^[0-9]/ { p; q } }'`
passphrase=$1

echo "Release version $version"

if [[ "$version" == *SNAPSHOT* ]]
then 
  echo "Should not release a SNAPSHOT version! Exiting."
  exit
fi

pushd sarasvati-parent 
mvn clean
mvn install 
mvn source:jar
mvn javadoc:jar
mvn javadoc:aggregate
popd

function deployFile {
  if [ -f $1/target/$2 ]  
  then
    echo "mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=$1/pom.xml -Dfile=$1/target/$2 -Dgpg.passphrase=$3 $4"
    mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=$1/pom.xml -Dfile=$1/target/$2 -Dgpg.passphrase=$3 $4
  fi
}

function deployModule {

  echo mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=$1/pom.xml -Dfile=$1/pom.xml -Dgpg.passphrase=$3 
  mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=$1/pom.xml -Dfile=$1/pom.xml -Dgpg.passphrase=$3 

  deployFile $1 $1-$2.jar $3
  deployFile $1 $1-$2-sources.jar $3 -Dclassifier=sources
  deployFile $1 $1-$2-javadoc.jar $3 -Dclassifier=javadoc
}

deployModule sarasvati-parent $version $passphrase
deployModule sarasvati-core $version $passphrase
deployModule sarasvati-hibernate $version $passphrase
deployModule sarasvati-visual $version $passphrase
