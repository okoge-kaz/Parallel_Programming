#####
## if you want to specify a directory java installed explicitly,
## set the java binary directory here
#####
JAVABIN=
#  JAVABIN=$(HOME)/parawork/jdk-17.0.3/bin/
#  JAVABIN=/usr/lib/jvm/default-java/bin

#####
## set javafx-sdk directory your installed 
#####
JAVAFXMODULE=../javafx-sdk-18.0.1/lib

#####
##  set proxy server address and port number, if your machine connects
##  to the internet via proxy
JAVADOCPROXY=
#  JAVADOCPROXY=-J-Dhttp.proxyHost=proxy.csc.titech.ac.jp -J-Dhttp.proxyPort=8080

#####
## csc room setting
#####
#JAVABIN=
#JAVAFXMODULE=/Library/Java/JavaFX/javafx-sdk-17.0.2/lib
##JAVADOCPROXY= -J-Dhttp.proxyHost=proxy.csc.titech.ac.jp -J-Dhttp.proxyPort=8080

# OS-dependent commands and separator
ifeq ($(OS),Windows_NT)
	SEP=;
	RM=rd /s /q
	FIND=echo
else
	SEP=:
	RM=rm -rf
	FIND=find
endif

JAVA=$(JAVABIN)java $(JAVAFLAGS)
JAVAC=$(JAVABIN)javac $(JAVACFLAGS)
JAVADOC0=$(JAVABIN)javadoc $(JAVADOCFLAGS)
MKDIR=mkdir
MAKE=make

MODULEPATH=$(JAVAFXMODULE)
CLASSPATH=lib/*$(SEP)
CLASSFLAGS=-classpath "bin$(SEP)$(CLASSPATH)resource"
JAVACCLASSFLAGS=-classpath "$(CLASSPATH)"
MODULEFLAGS=--module-path $(MODULEPATH) --add-modules javafx.controls,javafx.swing
JAVACFLAGS= -encoding utf8 -d bin -sourcepath src $(MODULEFLAGS) $(JAVACCLASSFLAGS) -Xlint:deprecation -Xdiags:verbose -Xlint:unchecked
JAVAFLAGS = $(MODULEFLAGS) $(CLASSFLAGS)
JAVADOCFLAGS= -html5 -encoding utf-8 -charset utf-8 -package -d javadoc -sourcepath src $(JAVADOCPROXY) -link https://docs.oracle.com/javase/jp/11/docs/api -link https://openjfx.io/javadoc/11 $(MODULEFLAGS) $(CLASSFLAGS)

SERVADDR=localhost
NUMBER=`echo \`whoami\`0 | md5 | sed -e "s/[^0-9]//g" |cut -c 1-6`

.PHONY: clean doc

.SUFFIXES: .class .java

.java.class:
	echo $@

.class:
	$(MAKE) bin
	$(eval CLS := $(subst /,.,$(@:src/%=%)))
	$(JAVAC) $@.java
	$(JAVA) $(CLS)  $(PARAMETER)

ALL::	Game01

Main04:
	$(MAKE) src/para/$@

Main05:
	$(MAKE) src/para/$@

Main06:
	$(MAKE) src/para/$@

Game01:
	$(MAKE) src/para/$@

bin:
	$(MKDIR) bin

javadoc:
	$(MKDIR) javadoc

clean:: bin javadoc
	$(RM) bin
	$(RM) javadoc

cleanall:: javadoc bin
	$(FIND) . -name "._*" -exec $(RM) {} \;
	$(RM) bin
	$(RM) javadoc

doc:: javadoc
	$(FIND) . -name "._*" -exec $(RM) {} \;
	$(JAVADOC0) para para.game para.graphic.shape para.graphic.target para.graphic.parser
