#!/bin/bash

set -eu

function create_user_and_database() {
    local username=$1
    local password=$2
    echo "Creating user '$username' and database '$username' with password"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "postgres" <<-EOSQL
        CREATE USER "$username" WITH PASSWORD '$password';
        CREATE DATABASE "$username";
EOSQL
    echo "Granting schema privileges in database '$username'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$username" <<-EOSQL
        GRANT ALL ON SCHEMA public TO "$username";
EOSQL
}

if [ -n "${POSTGRES_MULTIPLE_DATABASES:-}" ]; then
    IFS=' ' read -ra user_password_pairs <<< "$POSTGRES_MULTIPLE_DATABASES"

    for pair in "${user_password_pairs[@]}"; do
        IFS=':' read -r username password <<< "$pair"
        if [ -z "$username" ] || [ -z "$password" ]; then
            echo "Error: Invalid format for user:password pair '$pair'. Both username and password are required."
            exit 1
        fi
        create_user_and_database "$username" "$password"
    done

    echo "Multiple databases created with users and passwords"
fi
