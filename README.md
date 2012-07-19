# VHDLLab
Educational software for modelling and simulation of digital circuits.

## Summary
VHDLLab is a web based IDE for [VHDL](http://en.wikipedia.org/wiki/Vhdl). It's
designed for educational purpose as an alternative to commercial products that
provide solution for VHDL.

## Screenshots
For the impatient: https://github.com/mbezjak/vhdllab/wiki/Screenshots

## Rationale
The need for VHDLLab appeared at Faculty of Electrical Engineering and
Computing, University of Zagreb, Croatia where VHDL was taught in *Digital
Electronics* course. At the time (2006), [Xilinx WebISE](http://www.xilinx.com)
was used by students for VHDL programming. However, WebISE had a couple of
problems when used for educational purposes. One of the big ones was licensing
that forbade universities from distributing WebISE to students for work at home.
Other problems included: inability to describe circuits as automatons, lots of
unnecessary features that distracts students, lots of bugs, huge footprint, etc.
VHDLLab was designed as a lightweight alternative to fix those problems as well
as add a few other niceties.

## Tried & Tested
VHDLLab is being used at Faculty of Electrical Engineering and Computing,
University of Zagreb, Croatia in *Digital Electronics* course since 2007. Every
year approximately 700 students use it to complete programming assignments.

## Award
A paper describing VHDLLab, authored by Miro Bezjak, Davor Delač and Aleksandar
Prokopec under supervision of Mr.sc. Marko Čupić (now Dr.sc.), was among the
[winners] [ra-source] of University of Zagreb's Rector's Award in academic year
2007/2008. Paper is [available] [ra-paper] online as a PDF in Croatian. Paper
summary is available both in [Croatian] [ra-sum-hr] and in
[English] [ra-sum-en].

[ra-source]: http://www.unizg.hr/fileadmin/rektorat/dokumenti/rektnagrade/Rektorova_nagrada_2007-2008.pdf
[ra-paper]:  http://java.zemris.fer.hr/rektor/vhdllab/vhdllab-rad.pdf
[ra-sum-hr]: http://java.zemris.fer.hr/rektor/vhdllab/sazetak.html
[ra-sum-en]: http://java.zemris.fer.hr/rektor/vhdllab/summary.html

## Features
TBW

## Limitations
VHDLLab is in no way a complete replacement for WebISE. Many WebISE's features
are missing. It has enough features to be useful in educational setting.

## Statistics
[cloc](http://cloc.sourceforge.net) reports roughly 600 Java files, 45,000
lines of code + 20,000 lines of comments + 13,000 blank lines.

## Requirements
Client side is expected to have [JRE](http://java.com) 6+ and connectivity to
VHDLLab server. That is about it.

Server side requirements are as follows:

 * JRE 6+
 * [Apache Tomcat](http://tomcat.apache.org) 6+
 * [MySQL](http://www.mysql.com) 5+
 * [GHDL](http://ghdl.free.fr) 0.29+ for VHDL compilation and simulation

Building VHDLLab requires JDK 6+ and [Maven](http://maven.apache.org) 2.1.0.

## Install
TBW

## Configuration
TBW

## Development documentation
TBW

## Source code
Source code is available at github: https://github.com/mbezjak/vhdllab

## Additional documentation
Additional documentation is available at: https://github.com/mbezjak/vhdllab/wiki

## License
Project uses Apache license version 2.0. Check LICENSE file for more info.
