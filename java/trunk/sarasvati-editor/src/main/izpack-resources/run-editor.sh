#! /bin/sh

# Adapted from the run scripts in the Squirrel-SQL project

[ ${JAVA_HOME} ] && JAVA=${JAVA_HOME}/bin/java || [ %JAVA_HOME ]  && JAVA=%JAVA_HOME/bin/java  || JAVA=java

# Are we running within Cygwin on some version of Windows?
cygwin=false;
macosx=false;

case "`uname -s`" in
  CYGWIN*) cygwin=true ;;
esac
case "`uname -s`" in
        Darwin) macosx=true;;
esac

# Editor home.
if $macosx ; then
        EDITOR_HOME='%INSTALL_PATH/Contents/Resources/Java'
else
        EDITOR_HOME='%INSTALL_PATH'
fi

# Editor home in Unix format.
if $cygwin ; then
  UNIX_STYLE_HOME=`cygpath "$EDITOR_HOME"`
else
  UNIX_STYLE_HOME="$EDITOR_HOME"
fi

cd "$UNIX_STYLE_HOME"

# Check to see if the JVM meets the minimum required to run SQuirreL and inform the user if not and skip
# launch.  versioncheck.jar is a special jar file which has been compiled with javac version 1.2.2, which
# should be able to be run by that version of higher. The arguments to JavaVersionChecker below specify the
# minimum acceptable version (first arg) and any other acceptable subsequent versions.  <MAJOR>.<MINOR> should
# be all that is necessary for the version form.
#$JAVA -cp "$UNIX_STYLE_HOME/lib/versioncheck.jar" JavaVersionChecker 1.6 1.7
#if [ "$?" = "1" ]; then
#  exit
#fi

# First entry in classpath is the Squirrel application.
TMP_CP=""

# Then add all library jars to the classpath.
for a in "$UNIX_STYLE_HOME"/*.jar; do
  TMP_CP="$TMP_CP":"$a";
done

# Now add the system classpath to the classpath. If running
# Cygwin we also need to change the classpath to Windows format.
if $cygwin ; then
  TMP_CP=`cygpath -w -p $TMP_CP`
  TMP_CP=$TMP_CP';'$CLASSPATH
else
  TMP_CP=$TMP_CP:$CLASSPATH
fi

if $macosx ; then
        # Define mac-specific system properties if running on Mac OS X
        MACOSX_PROPS="-Dapple.laf.useScreenMenuBar=true -Dcom.apple.mrj.application.apple.menu.about.name=SQuirreLSQL"
        NATIVE_LAF_PROP="--native-laf"
fi

if $macosx ; then
        # macosx provides unknown args to the script, causing SQuirreL to bail..
        SCRIPT_ARGS=""
else
        SCRIPT_ARGS="$1 $2 $3 $4 $5 $6 $7 $8 $9"
fi

# Launch the Sarasvati Editor
$JAVA -Xmx256m -cp "$TMP_CP" $MACOSX_PROPS com.googlecode.sarasvati.editor.GraphEditor
