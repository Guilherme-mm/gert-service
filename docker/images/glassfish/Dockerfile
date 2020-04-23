FROM centos:7

RUN yum install -y java-1.8.0-openjdk-devel &&\
    yum install -y wget &&\
    yum install -y unzip

RUN mkdir /srv/applications
    
COPY ./lib/glassfish-5.0.1.zip /tmp
COPY ./deploy.sh /tmp

RUN chmod +x /tmp/deploy.sh

RUN unzip -d /opt /tmp/glassfish-5.0.1.zip

ENTRYPOINT ["/tmp/deploy.sh"]