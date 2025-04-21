#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE users_db;
    \c users_db
    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        phoneNumber VARCHAR(255) UNIQUE,
        login VARCHAR(255) UNIQUE,
        password VARCHAR(255),
        firstName VARCHAR(255),
        lastName VARCHAR(255),
        passportNumber VARCHAR(255) UNIQUE
    );
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE data_db;
    \c data_db
    CREATE TABLE user_card (
        card_id BIGSERIAL PRIMARY KEY,
        card_number VARCHAR(20) NOT NULL,
        expired_at DATE NOT NULL,
        cvv VARCHAR(3) NOT NULL,
        notify BOOLEAN NOT NULL,
        limitcash DOUBLE PRECISION,
        balance DOUBLE PRECISION NOT NULL,
        is_freeze BOOLEAN NOT NULL,
        is_blocked BOOLEAN NOT NULL,
        pin VARCHAR(4) NOT NULL,
        user_id BIGINT NOT NULL
    );

EOSQL