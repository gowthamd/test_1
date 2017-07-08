<h3>Prerequisites</h3>
1. <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_balnk">Java 8</a> installed locally.<br><br>
2. <a href="http://maven.apache.org/download.html" target="_blank">Maven 3</a> installed locally.
<h3>Compile Project</h3>
<p>Go to services folder and do mvn package</p>
<h3>Run Server</h3>
<p>From services folder run java -jar target/dependency/jetty-runner.jar target/*.war</p>
<h3>To debug the services</h3>
    1. start the jetty server with the command below <br>
       java -Xdebug -agentlib:jdwp=transport=dt_socket,address=9999,server=y,suspend=n -jar target/dependency/jetty-runner.jar target/*.war
       <br>
    2. debug the services project in eclipse with remote debuggin and set the port as 9999.

