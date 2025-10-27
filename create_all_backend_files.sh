#!/bin/bash

echo "🚀 Creating ALL Backend Files..."
echo "================================"

BASE="/Users/ngoubimaximilliandiamgha/Desktop/healthfinance-plus"

# Count files created
COUNT=0

# Helper function to create file
create_file() {
    local filepath="$1"
    local content="$2"
    
    if [ ! -f "$filepath" ]; then
        echo "$content" > "$filepath"
        echo "✅ Created: $filepath"
        COUNT=$((COUNT + 1))
    else
        echo "⏭️  Exists: $filepath"
    fi
}

echo ""
echo "📦 USER SERVICE FILES"
echo "====================="

# I'll create the actual content in the next message
# This is just the framework

echo ""
echo "================================"
echo "✅ Total files created: $COUNT"
