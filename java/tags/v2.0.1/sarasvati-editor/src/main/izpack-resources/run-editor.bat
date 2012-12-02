@echo off

@rem Adapted from the Squirrel SQL Project

IF "%JAVA_HOME%"=="" SET LOCAL_JAVA=java
IF NOT "%JAVA_HOME%"=="" SET LOCAL_JAVA=%JAVA_HOME%\bin\java

set basedir=%~f0
:strip
set removed=%basedir:~-1%
set basedir=%basedir:~0,-1%
if NOT "%removed%"=="\" goto strip
set EDITOR_HOME=%basedir%

@rem build the editors's classpath
set TMP_CP=""
dir /b "%EDITOR_HOME%\*.jar" > %TEMP%\editor-lib.tmp
FOR /F %%I IN (%TEMP%\editor-lib.tmp) DO CALL "%EDITOR_HOME%\addpath.bat" "%EDITOR_HOME%\%%I"
SET EDITOR_CP=%TMP_CP%;"%CLASSPATH%"
echo "EDITOR_CP=%EDITOR_CP%"

SET TMP_PARMS= %1 %2 %3 %4 %5 %6 %7 %8 %9

@rem Run with no command window. This may not work with older versions of Windows. Use the command above then.
start "Sarasvati Editor" /B "%LOCAL_JAVA%w" -Xmx256m -cp %EDITOR_CP% com.googlecode.sarasvati.editor.GraphEditor %TMP_PARMS%

:ExitForWrongJavaVersion
exit
