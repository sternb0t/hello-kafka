# hello kafka

This is a simple console app to show producing and consuming a kafka broker. Below are the steps I took to get it running on my machine, a MacBook Pro running macOS sierra 10.12.6.

## Verify Java

On macOS Sierra, the location of `JAVA_HOME` can be determined by running a program that comes with the OS:

```
$ /usr/libexec/java_home -v 1.8
/Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home
``` 

This means you can set your `JAVA_HOME` env variable like this:

```
$ export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"
```

I added that line to my `~/.bash_profile` file so that it's always set when I launch a Terminal shell.

## Install Zookeeper

Download Zookeeper 3.4.10 here: https://zookeeper.apache.org/releases.html

This is the "current stable release" version of Zookeeper at the time of writing this.

Here are the commands to install and start Zookeeper:

```
$ tar -xvf zookeeper-3.4.10.tar.gz
$ sudo mv zookeeper-3.4.10 /usr/local/zookeeper
$ mkdir -p /var/lib/zookeeper
$ cat > /usr/local/zookeeper/conf/zoo.cfg << EOF
> tickTime=2000
> dataDir=/var/lib/zookeeper
> clientPort=2181
> EOF
$ sudo /usr/local/zookeeper/bin/zkServer.sh start
JMX enabled by default
Using config: /usr/local/zookeeper/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```

## Install Kafka

Download Kafka 0.11.0 for Scala 2.11 here: http://kafka.apache.org/downloads

This is the "current stable release" version of Kafka at the time of writing this.

Here are the commands to install and start Kafka:

```
$ tar -xvf kafka_2.11-0.11.0.0.tgz
$ sudo mv kafka_2.11-0.11.0.0 /usr/local/kafka
$ mkdir /tmp/kafka-logs
$ sudo /usr/local/kafka/bin/kafka-server-start.sh -daemon /usr/local/kafka/config/server.properties
```

## Create Maven Project

In IntelliJ, choose File > New > Project... 

Select Maven and the "org.apache.maven.archetypes:maven-archetype-quickstart" archetype.

## Add Kafka Dependencies

Edit the pom.xml file and add this element under `<dependencies>`:

```
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-clients</artifactId>
  <version>0.11.0.0</version>
</dependency>
```

## Build

In IntelliJ, just Build > Build Project

In Terminal, `mvn compile`

## App.java

The Maven archetype creates simple public class `App` with a public `main()` method. So I just used that.

I also created two simple private methods:

* `produce()`
* `consume()`

As you can probably tell, `produce` creates a Kafka Producer object and uses it to send simple messages to the Kafka broker.

And, `consume` creates a Kafka Consumer object and uses it to read those simple messages from the broker.

 