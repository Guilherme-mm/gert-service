#!/bin/bash

/opt/glassfish5/glassfish/bin/asadmin start-domain
/opt/glassfish5/glassfish/bin/asadmin deploy /srv/applications/hello.war

tail -f /dev/null