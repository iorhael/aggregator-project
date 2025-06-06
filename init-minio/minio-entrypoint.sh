#!/bin/sh

set -eu

echo "Configuring local alias..."
mc alias set local http://"${MINIO_HOST}":9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD"

echo "Importing bucket metadata..."
mc admin cluster bucket import local /init-minio/local-bucket-metadata.zip
