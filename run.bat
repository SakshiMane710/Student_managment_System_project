@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-25"
.\apache-maven-3.9.6\bin\mvn.cmd exec:java -Dexec.mainClass="com.student.management.Main"
