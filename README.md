# df-useragent

The User Agent Parser is an Actian DataFlow operator for detecting information about a network client based on a user agent string.  This implementation uses the [UADetector](http://uadetector.sourceforge.net/) parser.

## Configuration

Before building useragent you need to define the following environment variables to point to the local DataFlow update site [dataflow-p2-site](https://github.com/ActianCorp/dataflow-p2-site) root directory and the DataFlow version.

    export DATAFLOW_REPO_HOME=/Users/myuser/dataflow-p2-site
    export DATAFLOW_VER=6.5.2.112


## Building

The update site is built using [Apache Maven 3.0.5 or later](http://maven.apache.org/).

To build, run:

    mvn clean install
    
You can update the version number by running

    mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=version
    
where version is of the form x.y.z or x.y.z-SNAPSHOT.


## Using the User Agent Parser with the DataFlow Engine

The build generates a JAR file in the target directory under useragent-dataflow-extensions with a name similar to 

    useragent-dataflow-extensions-1.y.z.jar

which can be included on the classpath when using the DataFlow engine.


## Installing the User Agent Parser plug-in in KNIME

The build also produces a ZIP file which can be used as an archive file with the KNIME 'Help/Install New Software...' dialog.
The ZIP file can be found in the target directory under useragent-knime-extensions-update-site and with a name like 


    com.actian.services.knime.useragent.update-1.y.z.zip





