#!/bin/sh

set -eu

/opt/keycloak/bin/kc.sh build --vault=keystore

/opt/keycloak/bin/kc.sh start \
    --proxy-headers xforwarded \
    --optimized \
    --vault-file=/opt/keycloak/keystore.p12 \
    --vault-pass="${KC_VAULT_PASS}" \
    --http-enabled true \
    --import-realm
