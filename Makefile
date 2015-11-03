all: .PHONY

.PHONY:
	mvn clean ; mvn package

clean:
	mvn clean