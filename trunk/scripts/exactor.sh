#!/bin/bash
#
# author: Patsy Phelan 
#


if [ $# -lt 1 ]
then
	echo 
	echo "This script needs at least one command line parameter."
	echo 
	echo "Usage: `basename $0` [Directory of Tests]"
	echo
	echo "This script will then excute all tests in the specified directory"
	echo "using the Exactor module."
	exit 0
fi

if [ -z $EXACTOR_HOME ]
then
	echo "EXACTOR_HOME must be set. It must point to the home directory"
	echo "of the EXACTOR module. See example below :"
	echo "    export EXACTOR_HOME=/home/developer/exactor-1.1.4"
	echo
	exit 0
fi

if [ -z $JAVA_HOME ]
then
	echo "JAVA_HOME must be set. It must point to the home directory"
	echo "of the installed Java Intrepretor.  See example below :"
	echo "   export JAVA_HOME=/home/developer/java1.5.2"
	echo
	echo "This is then expanded to $JAVA_HOME/bin/java in order to"
	echo "execute the tests."
	exit 0
fi

EXACTOR_CLASSPATH=
for jarFile in $EXACTOR_HOME/lib/*.jar
do
 EXACTOR_CLASSPATH=$jarFile:$EXACTOR_CLASSPATH 
done

if [ -d $1/resources ]
then
	EXACTOR_CLASSPATH=$1/resources:$EXACTOR_CLASSPATH
fi


$JAVA_HOME/bin/java -cp $EXACTOR_CLASSPATH com.exoftware.exactor.Runner $1
