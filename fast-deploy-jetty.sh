#!/bin/bash

mvn -Pfast && cd vhdllab-server && mvn jetty:run-exploded -Pdev
