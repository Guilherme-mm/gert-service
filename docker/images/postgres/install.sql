CREATE DATABASE gert;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE resource_owner_tb (id SERIAL PRIMARY KEY, name VARCHAR(200), password VARCHAR(255));

CREATE TABLE client_tb (id UUID DEFAULT uuid_generate_v4(), name VARCHAR(200), resource_owner INT REFERENCES resource_owner_tb(id) );

CREATE TABLE access_token_tb(id UUID, created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(), expires_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() + INTERVAL '6 hours');

CREATE TABLE application_scope_tb(id VARCHAR(255) UNIQUE NOT NULL, name VARCHAR(255), description VARCHAR(255));