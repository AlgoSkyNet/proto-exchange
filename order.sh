#!/bin/bash
# run it.
# see https://github.com/zeromq/jeromq/wiki for jvm opts.
JAR="target/pan-exchange-jar-with-dependencies.jar"
java -cp target/pan-exchange-jar-with-dependencies.jar net.parasec.pan.exchange.ManualOrder $@
