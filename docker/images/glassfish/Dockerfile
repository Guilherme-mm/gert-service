FROM centos:7

## Installing dependencies
RUN yum install -y java-1.8.0-openjdk-devel &&\
    yum install -y unzip wget

# ==== BEGIN SECTION: optional manual jdk installation ====
# RUN wget -P /opt https://mirrors.huaweicloud.com/java/jdk/8u151-b12/jdk-8u151-linux-x64.tar.gz && \
#     tar vxzf /opt/jdk-8u151-linux-x64.tar.gz --directory /opt

## Changing the jdk folder permissions
# RUN chown -R root: /opt/jdk1.8.0_*

## Installing the jdk using alternatives
# RUN alternatives --install /usr/bin/java java /opt/jdk1.8.0_151/bin/java 1 && \
#     alternatives --auto java && \
#     alternatives --install /usr/bin/jar jar /opt/jdk1.8.0_151/bin/jar 1 && \
#     alternatives --set jar /opt/jdk1.8.0_151/bin/jar && \
#     alternatives --install /usr/bin/javac javac /opt/jdk1.8.0_151/bin/javac 2 && \
#     alternatives --set javac /opt/jdk1.8.0_151/bin/javac

## Cleaning
# RUN rm -f /opt/jdk-8u151*.tar.gz

## Defining environment variables
# RUN export JAVA_HOME=/opt/jdk1.8.0_151 && \
#     export JRE_HOME=/opt/jdk1.8.0_151/jre && \
#     export PATH=$PATH:/opt/jdk1.8.0_151/bin:/opt/jdk1.8.0_151/jre/bin
# ==== END SECTION ====

## Creating the applications folder
RUN mkdir /srv/applications

## Donwload the glassfish 5.1.0 zip
RUN wget -P /tmp http://eclipse.c3sl.ufpr.br/glassfish/glassfish-5.1.0.zip

## Copying the deploy sh
COPY ./deploy.sh /tmp

## Grantint run permission to the deploy script
RUN chmod +x /tmp/deploy.sh

## Expanding the glassfish zip
RUN unzip -d /opt /tmp/glassfish-5.1.0.zip

## Defining some environment variables
ENV GLASSFISH_PATH /opt/glassfish5/glassfish
ENV ADMIN_USER admin
ENV ADMIN_PASSWORD admin

## Creating utilitary files to alter glassfish admin password
RUN printf 'AS_ADMIN_PASSWORD=\nAS_ADMIN_NEWPASSWORD='${ADMIN_PASSWORD} >> /opt/tmpfile
RUN printf 'AS_ADMIN_PASSWORD='${ADMIN_PASSWORD} >> /opt/pwdfile

## Changing the glassfish admin password
RUN $GLASSFISH_PATH/bin/asadmin --user $ADMIN_USER --passwordfile /opt/tmpfile change-admin-password

# RUN \
#     $GLASSFISH_PATH/bin/asadmin start-domain && \
#     $GLASSFISH_PATH/bin/asadmin --user $ADMIN_USER --passwordfile /opt/tmpfile change-admin-password && \
#     $GLASSFISH_PATH/bin/asadmin stop-domain


# RUN \
#     $GLASSFISH_PATH/bin/asadmin start-domain && \
#     $GLASSFISH_PATH/bin/asadmin --user $ADMIN_USER --passwordfile=/opt/tmpfile change-admin-password && \
#     $GLASSFISH_PATH/bin/asadmin --user $ADMIN_USER --passwordfile=/opt/pwdfile enable-secure-admin && \
#     $GLASSFISH_PATH/bin/asadmin restart-domain

## Cleaning utilitary file
#RUN rm /opt/tmpfile

## Deploy scritp execution
ENTRYPOINT ["/tmp/deploy.sh"]