#!/bin/bash

LOG_DIR="/var/log/getMyParking"

java  -Xms512m -Xmx512m -Xloggc:$LOG_DIR/gc.out -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=8083 -Dcom.sun.management.jmxremote.authenticate=false -jar target/getMyParking-0.0.1.jar db migrate src/main/resources/config/aws/migration_config.yaml 2>&1

java  -Xms512m -Xmx512m -Xloggc:$LOG_DIR/gc.out -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=8083 -Dcom.sun.management.jmxremote.authenticate=false -jar target/getMyParking-0.0.1.jar server src/main/resources/config/aws/config.yaml 2>&1

