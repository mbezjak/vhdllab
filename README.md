# VHDLLab
Educational software for modeling and simulation of digital circuits.

[![Build Status](https://secure.travis-ci.org/mbezjak/vhdllab.png?branch=master)](http://travis-ci.org/mbezjak/vhdllab)

## Summary
VHDLLab is a web based IDE for [VHDL](http://en.wikipedia.org/wiki/Vhdl). It's
designed for educational purpose as an alternative to commercial products that
provide solution for VHDL.

## Screenshots
For the impatient: http://imgur.com/a/CS6zn

## Rationale
The need for VHDLLab appeared at Faculty of Electrical Engineering and
Computing, University of Zagreb, Croatia where VHDL was taught in *Digital
Electronics* course. At the time (2006), [Xilinx WebISE](http://www.xilinx.com)
was used by students for VHDL programming. However, WebISE had a couple of
problems when used for educational purposes. One of the big ones was licensing
that forbade universities from distributing WebISE to students for work at home.
Other problems included: inability to describe circuits using automatons, lots
of unnecessary features that distracts students, lots of bugs, huge footprint,
etc. VHDLLab was designed as a lightweight alternative to fix those problems as
well as add a few other niceties.

## Tried & Tested
VHDLLab is being used at Faculty of Electrical Engineering and Computing,
University of Zagreb, Croatia in *Digital Electronics* course since 2007. Every
year approximately 700 students use it to complete programming assignments.

## Award
A paper describing VHDLLab, authored by Miro Bezjak, Davor Delač and Aleksandar
Prokopec under supervision of mr.sc. Marko Čupić (now dr.sc.), was among the
[winners] [ra-award] of University of Zagreb's Rector's Award in academic year
2007/2008. Paper is [available] [ra-paper] online as a PDF in Croatian. Paper
summary is available both in [Croatian] [ra-sum-hr] and in [English]
[ra-sum-en]. As a backup those files are available in `award` directory. Source
files can be obtained by `git checkout v1` then navigating to `rector` directory
or via [github] [ra-source].

[ra-award]:  http://www.unizg.hr/fileadmin/rektorat/dokumenti/rektnagrade/Rektorova_nagrada_2007-2008.pdf
[ra-paper]:  http://java.zemris.fer.hr/rektor/vhdllab/vhdllab-rad.pdf
[ra-sum-hr]: http://java.zemris.fer.hr/rektor/vhdllab/sazetak.html
[ra-sum-en]: http://java.zemris.fer.hr/rektor/vhdllab/summary.html
[ra-source]: https://github.com/mbezjak/vhdllab/tree/v1/rector

## Features

 * Low requirement for end users: only JRE 6+
 * Create digital circuits by:
    * writing VHDL source code
    * drawing schemes
    * drawing automatons
 * Test circuits using testbench editor
 * Compile, simulate and view results
 * Projects, files and user preferences are stored on server (no manual
   synchronization required when end user wants to work at different locations)
 * Dependency resolution for files
 * VHDL source code generation from schematic, automaton or testbench files
 * Security (HTTP, mandatory login, roles)
 * Auditing:
    * via project and file snapshots on every change
    * client log output is stored on server

Some features are visible in
[screenshots](http://imgur.com/a/CS6zn).

## Limitations
VHDLLab is in no way a complete replacement for WebISE. Many WebISE's features
are missing. It has enough features to be useful in educational setting.

One major limitation is case sensitivity. VHDL is case insensitive, but VHDLLab
might not be. That is, majority of code in VHDLLab assumes case insensitivity,
but there are no guaranties. Major uncertainty lies with database management
system. For that reason, it's advisable to use the same entity/component names
throughout VHDLLab editors.

## Install
Unfortunately, current state of build system and lack of server side code
doesn't allow for pre-built WAR application. That might change sometime in the
future. Until then, the only way to acquire WAR file is to build it from source.

### Build from Source Code
Building requires JDK 6+ and Maven.

1. Download and install [GHDL](http://ghdl.free.fr).

2. Acquire VHDLLab source code by either of these methods:

 * `git clone https://github.com/mbezjak/vhdllab.git`
 * download tagged version https://github.com/mbezjak/vhdllab/tags
 * download latest version https://github.com/mbezjak/vhdllab/zipball/master

3. Copy `configuration.properties-sample` to `configuration.properties` and edit
it.

4. Create MySQL database named `vhdllab` with all privileges. Appropriate
database structure is created automatically by Hibernate the first time that
server application is started. Note that database name can be changed in
`configuration.properties`.

5. Start build in *production* profile by executing these commands in a command
line. This can take a while, because maven will try to download the Internet. :)

        $ mvn clean install
        $ mvn clean
        $ mvn -Pprod

6. Root VHDLLab directory should now contain `vhdllab.war` file. Deploy it to
Tomcat container.

7. End user installation is simple. Only requirement is JRE 6+. Assuming Tomcat
is started on `localhost` at port `8080`, client application can be started by
navigation to https://localhost:8080/vhdllab/launch.jnlp.

Note that while developing VHDLLab, there is no need to do steps 5. and 6. They
can take a long time. Instead `fast-deploy-jetty.sh` (or `.bat` in Microsoft
Windows) can be used. The script will deploy server applcation in development
mode much faster. Client application can be run or debugged directly from
Eclipse by executing
`vhdllab-client/src/main/java/hr/fer/zemris/vhdllab/platform/Main.java`.

## Technical Description
VHDLLab consists of server and client application communicating by Spring HTTP
Invoker over HTTPS.

Client application is distributed by JNLP. It uses
[Spring Rich Client](spring-rich-c.sourceforge.net) and Swing. The use of
VHDLLab server is transparent to the end user.

Server application is architected as a service for client application. It has no
presentation layer, only service and DAO. It provides a way to: create, update,
retrieve projects, files and preferences; compile and simulate VHDL; extract
metadata, resolve file dependencies and generate VHDL code. Server application
uses [Springframework](http://www.springframework.org) (DI, ORM, AOP, etc.),
Spring Security and [Hibernate](http://www.hibernate.org). Recommended
[DBMS](http://en.wikipedia.org/wiki/Dbms) is MySQL.

Server application can be viewed as an example of
[SAAS](http://en.wikipedia.org/wiki/Software_as_a_Service).

### Statistics
[cloc](http://cloc.sourceforge.net) reports roughly 600 Java files, 45,000 lines
of code + 20,000 lines of comments + 13,000 blank lines.

### Requirements
Client side is expected to have [JRE](http://java.com) 6+ and connectivity to
VHDLLab server. That is about it.

Server side requirements are as follows:

 * JRE 6+
 * [Apache Tomcat](http://tomcat.apache.org) 6+
 * [MySQL](http://www.mysql.com) 5+
 * [GHDL](http://ghdl.free.fr) 0.29+ for VHDL compilation and simulation

Building VHDLLab requires JDK 6+ and [Maven](http://maven.apache.org) 2.2.1.

### Natural Language
A portion of codebase is in Croatian but most is in English. On the other hand,
end user interacts with client application exclusively in English. I18N files
exist but don't cover 100% of use cases.

### Codebase Quality
It varies. VHDLLab was written by students for students. Many were still
learning Java and OOP at the time. That being said, a large portion of VHDLLab
was written, then rewritten, and rewritten again. To the point where most of
VHDLLab (editor components being a large exception) is in excellent condition.

## Further Resources

 * Homepage:       https://github.com/mbezjak/vhdllab
 * Issue tracker:  https://github.com/mbezjak/vhdllab/issues
 * Wiki:           https://github.com/mbezjak/vhdllab/wiki
 * Mail list:      http://groups.google.com/group/vhdllab
 * CI server:      http://travis-ci.org/mbezjak/vhdllab
 * License:        Apache 2.0 (see LICENSE and NOTICE files)

No longer in use, but it could still be useful, at least for historical or
sentimental reasons:

 * original SVN repository; it's read and write protected:
   http://morgoth.zemris.fer.hr/svn/vhdllab
 * trac project; information present there is mostly in Croatian but it has read
   access to original SVN repository: http://morgoth.zemris.fer.hr/trac/vhdllab
