#!/bin/bash

SERVICES=("user-service" "appointment-service" "medical-records-service" "prescription-service" "insurance-service" "billing-service" "laboratory-service" "pharmacy-service")

for service in "${SERVICES[@]}"
do
    POM_FILE="$service/pom.xml"
    
    if grep -q "spring-boot-starter-data-redis" "$POM_FILE"; then
        echo "Redis already exists in $service"
    else
        echo "Adding Redis to $service"
        sed -i '' '/<\/dependencies>/i\
        <dependency>\
            <groupId>org.springframework.boot</groupId>\
            <artifactId>spring-boot-starter-data-redis</artifactId>\
        </dependency>\
        <dependency>\
            <groupId>org.springframework.boot</groupId>\
            <artifactId>spring-boot-starter-cache</artifactId>\
        </dependency>
' "$POM_FILE"
    fi
done

echo "✅ Redis dependencies added!"
