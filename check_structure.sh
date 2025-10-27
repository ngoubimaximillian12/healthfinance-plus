#!/bin/bash

echo "🔍 Checking Complete Project Structure..."
echo "=========================================="
echo ""

MISSING=0

# Function to check if directory exists
check_dir() {
    if [ -d "$1" ]; then
        echo "✅ $1"
    else
        echo "❌ MISSING: $1"
        MISSING=$((MISSING + 1))
    fi
}

echo "USER SERVICE:"
check_dir "user-service/src/main/java/com/healthfinance/userservice/controller"
check_dir "user-service/src/main/java/com/healthfinance/userservice/service"
check_dir "user-service/src/main/java/com/healthfinance/userservice/repository"
check_dir "user-service/src/main/java/com/healthfinance/userservice/model"
check_dir "user-service/src/main/java/com/healthfinance/userservice/dto"
check_dir "user-service/src/main/java/com/healthfinance/userservice/config"
check_dir "user-service/src/main/java/com/healthfinance/userservice/security"
check_dir "user-service/src/main/java/com/healthfinance/userservice/exception"
check_dir "user-service/src/main/java/com/healthfinance/userservice/util"

echo ""
echo "MEDICAL RECORDS SERVICE:"
check_dir "medical-records-service/src/main/java/com/healthfinance/medical/controller"
check_dir "medical-records-service/src/main/java/com/healthfinance/medical/service"
check_dir "medical-records-service/src/main/java/com/healthfinance/medical/repository"
check_dir "medical-records-service/src/main/java/com/healthfinance/medical/model"

echo ""
echo "FINANCIAL SERVICE:"
check_dir "financial-service/src/main/java/com/healthfinance/financial/controller"
check_dir "financial-service/src/main/java/com/healthfinance/financial/service"
check_dir "financial-service/src/main/java/com/healthfinance/financial/repository"
check_dir "financial-service/src/main/java/com/healthfinance/financial/model"

echo ""
echo "=========================================="
if [ $MISSING -eq 0 ]; then
    echo "✅ ALL DIRECTORIES EXIST!"
else
    echo "❌ MISSING $MISSING DIRECTORIES"
    echo ""
    echo "Run: ./create_missing_directories.sh"
fi
