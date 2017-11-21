all: clean proto-build mvn-build

clean:
	mvn clean

proto-build:
	protoc --java_out src/main/java src/main/proto/exchange.proto

mvn-build:
	mvn compile assembly:single
