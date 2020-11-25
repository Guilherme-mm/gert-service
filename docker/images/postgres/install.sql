--
-- Schemas Creation
--
CREATE SCHEMA authentication;
ALTER SCHEMA authentication OWNER TO postgres;

--
-- Extensions
--
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';

--
-- Custom types creation
--
CREATE TYPE auth_service AS ENUM('Google', 'Facebook');

--
-- Authentication schema tables
--
-- Access token table creation
CREATE TABLE authentication.access_token_tb (
    id uuid DEFAULT public.uuid_generate_v4(),
    created_at timestamp with time zone DEFAULT now(),
    expires_at timestamp with time zone DEFAULT (now() + '06:00:00'::interval),
    client uuid NOT NULL
);
ALTER TABLE authentication.access_token_tb OWNER TO postgres;
COMMENT ON COLUMN authentication.access_token_tb.client IS 'Indicates to which client this token belongs to';

-- Application scopes table creation
CREATE TABLE authentication.application_scope_tb (
    id character varying(255) NOT NULL,
    name character varying(255),
    description character varying(255)
);
ALTER TABLE authentication.application_scope_tb OWNER TO postgres;

-- Clients table creation
CREATE TABLE authentication.client_tb (
    id uuid DEFAULT public.uuid_generate_v4(),
    secret character varying(35) NOT NULL DEFAULT md5(random()::text),
    name character varying(200),
    resource_owner integer NOT NULL
);
ALTER TABLE authentication.client_tb OWNER TO postgres;

-- Resource owner table creation
CREATE TABLE authentication.resource_owner_tb (
    id integer NOT NULL,
    name character varying(200),
    username character varying (150) NOT NULL,
    password character varying(255)
);
ALTER TABLE authentication.resource_owner_tb OWNER TO postgres;

-- Resource owner sequencial id assist table creation
CREATE SEQUENCE authentication.resource_owner_tb_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE authentication.resource_owner_tb_id_seq OWNER TO postgres;
ALTER SEQUENCE authentication.resource_owner_tb_id_seq OWNED BY authentication.resource_owner_tb.id;

-- Setting the resource owner table id column value to be filled according to the sequencial assist table
ALTER TABLE ONLY authentication.resource_owner_tb ALTER COLUMN id SET DEFAULT nextval('authentication.resource_owner_tb_id_seq'::regclass);

-- Init the resource owner sequence for the id column on the value 1 
SELECT pg_catalog.setval('authentication.resource_owner_tb_id_seq', 1, false);

-- Creating Users table
CREATE TABLE authentication.user_tb (
    id integer NOT NULL,
    name character varying(200),
    email character varying(255),
    password character varying(255),
    created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE authentication.user_tb OWNER TO postgres;

-- Creating the sequence used to fill default incremental values of the user id column
CREATE SEQUENCE authentication.user_tb_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE authentication.user_tb_id_seq OWNER TO postgres;
ALTER SEQUENCE authentication.user_tb_id_seq OWNED BY authentication.user_tb.id;

-- Defining the sequence as the default value of the id column of users 
ALTER TABLE ONLY authentication.user_tb ALTER COLUMN id SET DEFAULT nextval('authentication.user_tb_id_seq'::regclass);

-- Setting the sequence to start at the value 1
SELECT pg_catalog.setval('authentication.user_tb_id_seq', 1, false);

-- Creating Auth Services Data table
CREATE TABLE authentication.auth_services_data_tb (
    id integer NOT NULL,
    user_id integer not null,
    name auth_service NOT NULL,
    unique_identifier_attribute character varying(100) not null,
    unique_identifier_value character varying(255) not null,
    metadata json,
    created_at timestamptz NOT NULL DEFAULT now()
);
ALTER TABLE authentication.auth_services_data_tb OWNER TO postgres;

-- Creating sequence for auth services data table id column
CREATE SEQUENCE authentication.auth_services_data_tb_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE authentication.auth_services_data_tb_id_seq OWNER TO postgres;
ALTER SEQUENCE authentication.auth_services_data_tb_id_seq OWNED BY authentication.auth_services_data_tb.id;

-- Defining the sequence as the default value of the id column of auth services data 
ALTER TABLE ONLY authentication.auth_services_data_tb ALTER COLUMN id SET DEFAULT nextval('authentication.auth_services_data_tb_id_seq'::regclass);

-- Setting the sequence to start at the value 1
SELECT pg_catalog.setval('authentication.auth_services_data_tb_id_seq', 1, false);

--
-- Creating the Access Token x Scope associative table
--
CREATE TABLE authentication.access_token_scope_as (
	access_token uuid NOT NULL,
	"scope" varchar(255) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT now()
);

--
-- Creating the Client x Scope associative table
--
CREATE TABLE authentication.client_scope_as (
	client uuid NOT NULL,
	"scope" varchar(255) NOT NULL,
	created_at timestamptz NOT NULL DEFAULT now()
);

-- Defining constraints
ALTER TABLE ONLY authentication.access_token_tb
    ADD CONSTRAINT access_token_tb_pk PRIMARY KEY (id);
ALTER TABLE ONLY authentication.application_scope_tb
    ADD CONSTRAINT application_scope_tb_pk PRIMARY KEY (id);
ALTER TABLE ONLY authentication.resource_owner_tb
    ADD CONSTRAINT resource_owner_tb_pkey PRIMARY KEY (id);
ALTER TABLE ONLY authentication.user_tb
    ADD CONSTRAINT user_tb_pkey PRIMARY KEY (id);
ALTER TABLE ONLY authentication.client_tb
    ADD CONSTRAINT client_tb_pk PRIMARY KEY (id);
ALTER TABLE ONLY authentication.auth_services_data_tb
    ADD CONSTRAINT auth_services_data_tb_pkey PRIMARY KEY (id);
ALTER TABLE ONLY authentication.access_token_scope_as
    ADD CONSTRAINT access_token_scope_as_pk PRIMARY KEY (access_token,"scope");
ALTER TABLE ONLY authentication.client_scope_as
    ADD CONSTRAINT client_scope_as_pk PRIMARY KEY (client,"scope");
ALTER TABLE authentication.user_tb ADD CONSTRAINT user_tb_un UNIQUE (email);
ALTER TABLE authentication.auth_services_data_tb ADD CONSTRAINT auth_services_data_tb_un UNIQUE (user_id,"name");


-- Data filling
INSERT INTO authentication.resource_owner_tb (name, username, password) VALUES ('GERT Admin', 'gert-admin', 'admin');
INSERT INTO authentication.client_tb (id, secret, name, resource_owner) VALUES ('e2cdbfb0-01a3-459f-82a7-32dbcf7d6798', '1b5fcd6d7b3a1b34838d333a0b404041', 'Rybius Web Client', 1);
INSERT INTO authentication.client_tb (id, secret, name, resource_owner) VALUES ('c080c584-c82c-4ede-b13b-16dd644e9315', '3ba25538894baf64f1237a0f33743606', 'Rybius Dev Client', 1);
INSERT INTO authentication.application_scope_tb (id, "name", description) VALUES('auth.client.accessToken.create', 'Create Access Token', 'Allows a client to create an access Token');
INSERT INTO authentication.application_scope_tb (id, "name", description) VALUES('auth.user.authServiceLogin', 'Login Via Auth Service', 'Allows a client to perform the login login of an user with an authentication service');
INSERT INTO authentication.client_scope_as (client, "scope") VALUES('e2cdbfb0-01a3-459f-82a7-32dbcf7d6798', 'auth.client.accessToken.create');
INSERT INTO authentication.client_scope_as (client, "scope") VALUES('e2cdbfb0-01a3-459f-82a7-32dbcf7d6798', 'auth.user.authServiceLogin');
INSERT INTO authentication.client_scope_as (client, "scope") VALUES('c080c584-c82c-4ede-b13b-16dd644e9315', 'auth.client.accessToken.create');
INSERT INTO authentication.client_scope_as (client, "scope") VALUES('c080c584-c82c-4ede-b13b-16dd644e9315', 'auth.user.authServiceLogin');

-- Defining foreign keys
ALTER TABLE ONLY authentication.access_token_tb
    ADD CONSTRAINT access_token_tb_fk FOREIGN KEY (client) REFERENCES authentication.client_tb(id) ON UPDATE CASCADE;
ALTER TABLE ONLY authentication.client_tb
    ADD CONSTRAINT client_tb_resource_owner_fkey FOREIGN KEY (resource_owner) REFERENCES authentication.resource_owner_tb(id);
ALTER TABLE ONLY authentication.auth_services_data_tb
    ADD CONSTRAINT auth_services_data_tb_resource_owner_fkey FOREIGN KEY (user_id) REFERENCES authentication.user_tb(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE authentication.access_token_scope_as
    ADD CONSTRAINT access_token_scope_as_fk_1 FOREIGN KEY ("scope") REFERENCES authentication.application_scope_tb(id) ON UPDATE CASCADE;
ALTER TABLE authentication.access_token_scope_as
    ADD CONSTRAINT access_token_scope_as_fk FOREIGN KEY (access_token) REFERENCES authentication.access_token_tb(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE authentication.client_scope_as 
    ADD CONSTRAINT client_scope_as_fk FOREIGN KEY (client) REFERENCES authentication.client_tb(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE authentication.client_scope_as
    ADD CONSTRAINT client_scope_as_fk_1 FOREIGN KEY ("scope") REFERENCES authentication.application_scope_tb(id) ON DELETE CASCADE ON UPDATE CASCADE;


--
-- END Authentication schema tables
--