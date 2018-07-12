@echo off


set workspace=C:\Users\Sacha\eclipse-workspace\blockchain-rmi\Blockchain\bin
echo %workspace%
cd %workspace%
start rmiregistry & java -Djava.security.policy=policy SlaveServer

