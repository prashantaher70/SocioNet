cd C:\Users\Warrior\Desktop\SocioNet-Server\server-src
javac -d "../Obj" NanoHTTPD.java
javac -d "../Obj" ServerRunner.java
javac -d "../Obj" SimpleWebServer.java

cd ..
cd ui-src

javac -d "../Obj" GraphPanel.java
javac -d "../Obj" ServerOperationsPanel.java ServerStatisticsPanel.java
javac -d "../Obj" SocioNet.java

pause