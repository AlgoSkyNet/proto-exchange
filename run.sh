#!/bin/bash
# run it.
# see https://github.com/zeromq/jeromq/wiki for jvm opts.
JVM_OPTS="-server -XX:+UseConcMarkSweepGC -XX:+TieredCompilation -XX:+AggressiveOpts -XX:+UseCompressedOops -XX:+UseBiasedLocking -XX:+UseNUMA"
JAR="target/pan-exchange-jar-with-dependencies.jar"
if [ ! -e $JAR ] ; then
  echo "need to *make* f00."
  exit 0
fi
java $JAVA_OPTS -jar target/pan-exchange-jar-with-dependencies.jar $@
