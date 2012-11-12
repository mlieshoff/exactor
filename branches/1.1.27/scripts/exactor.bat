@echo off
if (%OS%) == (Windows_NT) setlocal
rem --------------------------------------------------------------------
rem Copyright (c) 2004, Exoftware Limited. All rights reserved.
rem --------------------------------------------------------------------
rem
rem NAME:		    exactor.bat
rem
rem DESCRIPTION:
rem
rem   This script is used to run EXACTOR against an acceptance test script
rem   or directory of scripts.
rem
rem USAGE:
rem
rem   exactor dirname|filename
rem ---------------------------------------------------------------------

rem check for java
set _JAVACMD=%JAVACMD%

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe

set EXACTOR_HOME=%~pd0..

rem
rem Preserve the environment CLASSPATH
rem
set _CP=%CLASSPATH%

if not "%1" == "-lib" goto nolib
shift
set CLASSPATH=%CLASSPATH%;%~1
shift

:nolib

set EXACTOR_ARGS=%1

if "%EXACTOR_ARGS%" == "" set EXACTOR_ARGS=%cd%

for %%i in (%EXACTOR_HOME%\lib\*.*) do call %EXACTOR_HOME%\bin\cpappend.bat %%i

set PATH=%PATH%;%EXACTOR_HOME%\lib\

%_JAVACMD% -cp %CLASSPATH% com.exoftware.exactor.Runner %EXACTOR_ARGS%

:cleanup
set CLASSPATH=%_CP%
set _CP=

if (%OS%) == (Windows_NT) endlocal
