What is Jackrabbit Plugin ?
============

**Jackrabbit plugin** is a module that seamlessly integrates the [Apache Jackrabbit](http://jackrabbit.apache.org/) with  the [Play! framework](http://www.playframework.org/) 2. 

Requirements
=========

* Java 5 or 6, 7
* [Play 2.1.2](http://www.playframework.com/)

Dependencies
============

* [Apache Jackrabbit 2.7.0](http://jackrabbit.apache.org/)
* [Apache Jackrabbit OCM 2.0.0](http://jackrabbit.apache.org/object-content-mapping.html)
* [JCROM 2.1.0](https://code.google.com/p/jcrom/)

Install
====

  1)  Install Play framework 2.1.2
  
  2)  Executing the command for installing the Jackrabbit Plugin
               
         % git clone git@github.com:sgougi/play21-jackrabbit-plugin.git
         
  3)  Publishing the Jackrabbit Plugin to your local repository

         % cd play21-jackrabbit-plugin
         % play publish-local


Run sample application and Usage
=======================

At a command prompt, type the following commands:

         % cd play21-jackrabbit-plugin
         % cd samples
         % cd jackrabbit-simple-ocm-app
            ( or % cd jackrabbit-simple-jcrom-app ) 
         % play run

There are basic usage in the source code of a [sample application](samples). 

* [Annotated model classes](samples/jackrabbit-sample-ocm-app/app/models)
* [Application configuration: conf/application.conf](samples/jackrabbit-sample-ocm-app/conf/application.conf)
* [Dependency settings: project/Build.scala](samples/jackrabbit-sample-ocm-app/project/Build.scala)  
* [Controller with OCM](samples/jackrabbit-sample-ocm-app/app/controllers/Application.java)

## Facade Class for JCR

The com.wingnest.play2.jackrabbit.[Jcr](app/com/wingnest/play2/jackrabbit/Jcr.java) class is a Facade class for the JCR.

* Repository Jcr.getRepository()
* Session Jcr.login()
* Session Jcr.getCurrentSession
* void Jcr.setCurrentSession()

## Facade Class for [OCM](http://jackrabbit.apache.org/object-content-mapping.html)

The com.wingnest.play2.jackrabbit.[OCM](app/com/wingnest/play2/jackrabbit/OCM.java) class is a Facade class for the OCM.

* ObjectContentManager OCM.getOCM(...)

## Facade Class for [JCROM](https://code.google.com/p/jcrom/)

The com.wingnest.play2.jackrabbit.[JCROM](app/com/wingnest/play2/jackrabbit/JCROM.java) class is a Facade class for the JCROM.

* Jcrom JCROM.createJcrom()

Known Issues
=============
* Nothing

Licence
========
Jackrabbit Plugin is distributed under the [Apache 2 licence](http://www.apache.org/licenses/LICENSE-2.0.html).

