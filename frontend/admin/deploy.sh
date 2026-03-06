#!/bin/bash
set -e

echo "Building..."
quasar build

echo "Deploying..."
sudo rsync -a --delete dist/spa/ /var/www/crm.arenco/

sudo nginx -t && sudo systemctl reload nginx

echo "Deploy concluído!"