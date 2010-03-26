Seam Drools 3.0.0 Beta 1
==========================

Seam Drools provides integration with the Drools 5 API.


Contents of distribution
========================

doc/

  API Docs and reference guide.
  
examples/

  Seam Drools Examples
  
lib/

  Seam Drools jar files
  
Licensing
=========

This distribution, as a whole, is licensed under the terms of the GNU Lesser General Public License
(LGPL) Version 2.1, the text of which is contained in the file lgpl.txt.

Seam Remoting URLs
==================

Seam Framework Home Page:      http://www.seamframework.org
Downloads:                     http://www.seamframework.org/Download/SeamDownloads
Forums:                        http://www.seamframework.org/Community/SeamUsers
Source Code:                   http://anonsvn.jboss.org/repos/seam/modules/drools/
Issue Tracking:                http://jira.jboss.org/jira/browse/SEAMDROOLS

Release Notes
=============

Version 3.0.0 Beta 1
--------------------
First beta release of Seam Drools 3.x, ported from Seam 2.x to CDI.


* If using Maven, some artifacts may only be available in the JBoss Repository. To allow Seam Remoting to correctly function, add the JBoss Repository to Maven. Edit your ~/.m2/settings.xml, and add the following entry:

      <profile>
         <id>jboss.repository</id>
         <activation>
            <activeByDefault>true</activeByDefault>
         </activation>
         <repositories>
            <repository>
               <id>repository.jboss.org</id>
               <url>http://repository.jboss.org/maven2</url>
               <releases>
                  <enabled>true</enabled>
               </releases>
               <snapshots>
                  <enabled>false</enabled>
               </snapshots>
            </repository>
         </repositories>
      </profile>
