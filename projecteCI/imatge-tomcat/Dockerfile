# Imatge base oficial de Tomcat
FROM tomcat:10.1-jdk17

# Instal·lar curl per al healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN rm -rf /usr/local/tomcat/webapps/*

# Configuració Tomcat
COPY config/* /usr/local/tomcat/conf/
COPY lib/* /usr/local/tomcat/lib/

# Aplicació
COPY webapps/*.war /usr/local/tomcat/webapps/ROOT.war
