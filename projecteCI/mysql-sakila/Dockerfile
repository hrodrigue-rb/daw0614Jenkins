FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=1234
ENV MYSQL_DATABASE=sakila
ENV MYSQL_USER=userSakila
ENV MYSQL_PASSWORD=User1234

COPY initdb/InitSakila.sql /docker-entrypoint-initdb.d/
