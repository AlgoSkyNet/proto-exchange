#!/bin/bash
# run it.
# see https://github.com/zeromq/jeromq/wiki for jvm opts.
JAR="target/pan-oms-jar-with-dependencies.jar"
java -cp target/pan-oms-jar-with-dependencies.jar net.parasec.pan.oms.Client $@
