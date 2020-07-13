--
-- PostgreSQL database dump
--

-- Dumped from database version 12.3
-- Dumped by pg_dump version 12.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: authentication; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA authentication;


ALTER SCHEMA authentication OWNER TO postgres;

--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: access_token_tb; Type: TABLE; Schema: authentication; Owner: postgres
--

CREATE TABLE authentication.access_token_tb (
    id uuid,
    created_at timestamp with time zone DEFAULT now(),
    expires_at timestamp with time zone DEFAULT (now() + '06:00:00'::interval)
);


ALTER TABLE authentication.access_token_tb OWNER TO postgres;

--
-- Name: application_scope_tb; Type: TABLE; Schema: authentication; Owner: postgres
--

CREATE TABLE authentication.application_scope_tb (
    id character varying(255) NOT NULL,
    name character varying(255),
    description character varying(255)
);


ALTER TABLE authentication.application_scope_tb OWNER TO postgres;

--
-- Name: client_tb; Type: TABLE; Schema: authentication; Owner: postgres
--

CREATE TABLE authentication.client_tb (
    id uuid DEFAULT public.uuid_generate_v4(),
    name character varying(200),
    resource_owner integer
);


ALTER TABLE authentication.client_tb OWNER TO postgres;

--
-- Name: resource_owner_tb; Type: TABLE; Schema: authentication; Owner: postgres
--

CREATE TABLE authentication.resource_owner_tb (
    id integer NOT NULL,
    name character varying(200),
    password character varying(255)
);


ALTER TABLE authentication.resource_owner_tb OWNER TO postgres;

--
-- Name: resource_owner_tb_id_seq; Type: SEQUENCE; Schema: authentication; Owner: postgres
--

CREATE SEQUENCE authentication.resource_owner_tb_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE authentication.resource_owner_tb_id_seq OWNER TO postgres;

--
-- Name: resource_owner_tb_id_seq; Type: SEQUENCE OWNED BY; Schema: authentication; Owner: postgres
--

ALTER SEQUENCE authentication.resource_owner_tb_id_seq OWNED BY authentication.resource_owner_tb.id;


--
-- Name: resource_owner_tb id; Type: DEFAULT; Schema: authentication; Owner: postgres
--

ALTER TABLE ONLY authentication.resource_owner_tb ALTER COLUMN id SET DEFAULT nextval('authentication.resource_owner_tb_id_seq'::regclass);


--
-- Data for Name: access_token_tb; Type: TABLE DATA; Schema: authentication; Owner: postgres
--

COPY authentication.access_token_tb (id, created_at, expires_at) FROM stdin;
\.


--
-- Data for Name: application_scope_tb; Type: TABLE DATA; Schema: authentication; Owner: postgres
--

COPY authentication.application_scope_tb (id, name, description) FROM stdin;
\.


--
-- Data for Name: client_tb; Type: TABLE DATA; Schema: authentication; Owner: postgres
--

COPY authentication.client_tb (id, name, resource_owner) FROM stdin;
\.


--
-- Data for Name: resource_owner_tb; Type: TABLE DATA; Schema: authentication; Owner: postgres
--

COPY authentication.resource_owner_tb (id, name, password) FROM stdin;
\.


--
-- Name: resource_owner_tb_id_seq; Type: SEQUENCE SET; Schema: authentication; Owner: postgres
--

SELECT pg_catalog.setval('authentication.resource_owner_tb_id_seq', 1, false);


--
-- Name: application_scope_tb application_scope_tb_id_key; Type: CONSTRAINT; Schema: authentication; Owner: postgres
--

ALTER TABLE ONLY authentication.application_scope_tb
    ADD CONSTRAINT application_scope_tb_id_key UNIQUE (id);


--
-- Name: resource_owner_tb resource_owner_tb_pkey; Type: CONSTRAINT; Schema: authentication; Owner: postgres
--

ALTER TABLE ONLY authentication.resource_owner_tb
    ADD CONSTRAINT resource_owner_tb_pkey PRIMARY KEY (id);


--
-- Name: client_tb client_tb_resource_owner_fkey; Type: FK CONSTRAINT; Schema: authentication; Owner: postgres
--

ALTER TABLE ONLY authentication.client_tb
    ADD CONSTRAINT client_tb_resource_owner_fkey FOREIGN KEY (resource_owner) REFERENCES authentication.resource_owner_tb(id);


--
-- PostgreSQL database dump complete
--

