#!/bin/bash

SERVICES=("user-service" "appointment-service" "medical-records-service" "prescription-service" "insurance-service" "billing-service" "notification-service" "laboratory-service" "pharmacy-service")

for service in "${SERVICES[@]}"
do
    POM_FILE="$service/pom.xml"
    
    # Check if Eureka dependency already exists
    if grep -q "spring-cloud-starter-netflix-eureka-client" "$POM_FILE"; then
        echo "Eureka dependency already exists in $service"
    else
        echo "Adding Eureka dependency to $service"
        
        # Add dependency before </dependencies>
        sed -i '' '/<\/dependencies>/i\
        <dependency>\
            <groupId>org.springframework.cloud</groupId>\
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>\
        </dependency>
' "$POM_FILE"
        
        # Add Spring Cloud dependency management if not exists
        if ! grep -q "spring-cloud-dependencies" "$POM_FILE"; then
            sed -i '' '/<\/dependencies>/a\
\
    <dependencyManagement>\
        <dependencies>\
            <dependency>\
                <groupId>org.springframework.cloud</groupId>\
                <artifactId>spring-cloud-dependencies</artifactId>\
                <version>2023.0.0</version>\
                <type>pom</type>\
                <scope>import</scope>\
            </dependency>\
        </dependencies>\
    </dependencyManagement>
' "$POM_FILE"
        fi
    fi
done

echo "✅ Eureka dependencies added to all services!"
