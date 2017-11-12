all: clean build

clean:
	mvn clean

build:
	mvn compile assembly:single
