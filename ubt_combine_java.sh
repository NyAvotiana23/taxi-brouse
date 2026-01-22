#!/bin/bash

# Define the base directory (adjust if needed for Linux mount point)
BASE_DIR="/home/itu/Documents/GitHub/taxi-brouse"  # Example if mounted under /mnt/d; change as per your setup

# Combine entity files
cat "$BASE_DIR/entity/"*.java > "$BASE_DIR/entity/entities.txt"

# Combine service files
cat "$BASE_DIR/service/"*.java > "$BASE_DIR/service/services.txt"

# Combine dto files
cat "$BASE_DIR/dto/"*.java > "$BASE_DIR/dto/dtos.txt"

# Combine view files
cat "$BASE_DIR/view/"*.java > "$BASE_DIR/view/views.txt"

echo "All files combined successfully."