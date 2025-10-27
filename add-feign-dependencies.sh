#!/bin/bash

SERVICES=("appointment-service" "billing-service" "prescription-service")

for service in "${SERVICES[@]}"
do
    POM_FILE="$service/pom.xml"
    
    if grep -q "spring-cloud-starter-openfeign" "$POM_FILE"; then
        echo "OpenFeign already exists in $service"
    else
        echo "Adding OpenFeign to $service"
        sed -i '' '/<\/dependencies>/i\
        <dependency>\
            <groupId>org.springframework.cloud</groupId>\
            <artifactId>spring-cloud-starter-openfeign</artifactId>\
        </dependency>
' "$POM_FILE"
    fi
done

echo "✅ OpenFeign dependencies added!"
