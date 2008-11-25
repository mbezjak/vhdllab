#!/bin/bash

mvn -Pfast && cd vhdllab-war && mvn tomcat:exploded
