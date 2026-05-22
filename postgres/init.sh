#!/usr/bin/env bash
set -euo pipefail

psql -v ON_ERROR_STOP=1 --username postgres <<-SQL
    CREATE USER "${KC_DB_USER}" WITH PASSWORD '${KC_DB_PASSWORD}';
    CREATE DATABASE "${KC_DB_DATABASE}" OWNER "${KC_DB_USER}";
SQL