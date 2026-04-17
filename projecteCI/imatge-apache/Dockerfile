FROM httpd:2.4

RUN rm -rf /usr/local/apache2/htdocs/*
# Copiar configuració personalitzada
COPY balancer.conf /usr/local/apache2/conf/extra/balancer.conf
# Activem la configuració del balancer dins del httpd.conf original
RUN echo "Include conf/extra/balancer.conf" >> /usr/local/apache2/conf/httpd.conf

# Exposar port 80
EXPOSE 80

# IMPORTANT: Apache en foreground
CMD ["httpd", "-DFOREGROUND"]
