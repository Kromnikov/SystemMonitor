--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.5
-- Dumped by pg_dump version 9.4.5
-- Started on 2016-04-17 23:27:14

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 189 (class 1259 OID 57355)
-- Name: FAVORITES; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "FAVORITES" (
    id integer NOT NULL,
    host_id integer,
    inst_metric_id integer,
    user_name text
);


ALTER TABLE "FAVORITES" OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 57353)
-- Name: FAVORITES_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "FAVORITES_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "FAVORITES_id_seq" OWNER TO postgres;

--
-- TOC entry 2085 (class 0 OID 0)
-- Dependencies: 188
-- Name: FAVORITES_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "FAVORITES_id_seq" OWNED BY "FAVORITES".id;


--
-- TOC entry 179 (class 1259 OID 24710)
-- Name: HOST_METRIC; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "HOST_METRIC" (
    id integer NOT NULL,
    host_id integer,
    metric_id integer
);


ALTER TABLE "HOST_METRIC" OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16395)
-- Name: VALUE_METRIC; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "VALUE_METRIC" (
    id integer NOT NULL,
    host integer,
    metric integer,
    value double precision,
    date_time timestamp without time zone
);


ALTER TABLE "VALUE_METRIC" OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 16393)
-- Name: HOST_METRIC_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "HOST_METRIC_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "HOST_METRIC_id_seq" OWNER TO postgres;

--
-- TOC entry 2086 (class 0 OID 0)
-- Dependencies: 172
-- Name: HOST_METRIC_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "HOST_METRIC_id_seq" OWNED BY "VALUE_METRIC".id;


--
-- TOC entry 178 (class 1259 OID 24708)
-- Name: HOST_METRIC_id_seq1; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "HOST_METRIC_id_seq1"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "HOST_METRIC_id_seq1" OWNER TO postgres;

--
-- TOC entry 2087 (class 0 OID 0)
-- Dependencies: 178
-- Name: HOST_METRIC_id_seq1; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "HOST_METRIC_id_seq1" OWNED BY "HOST_METRIC".id;


--
-- TOC entry 183 (class 1259 OID 32790)
-- Name: HOST_STATE; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "HOST_STATE" (
    id integer NOT NULL,
    host integer,
    resolved boolean,
    start_datetime timestamp without time zone,
    end_datetime timestamp without time zone,
    host_name text
);


ALTER TABLE "HOST_STATE" OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 32788)
-- Name: HOST_STATE_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "HOST_STATE_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "HOST_STATE_id_seq" OWNER TO postgres;

--
-- TOC entry 2088 (class 0 OID 0)
-- Dependencies: 182
-- Name: HOST_STATE_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "HOST_STATE_id_seq" OWNED BY "HOST_STATE".id;


--
-- TOC entry 181 (class 1259 OID 32779)
-- Name: INSTANCE_METRIC; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "INSTANCE_METRIC" (
    id integer NOT NULL,
    templ_metric integer,
    host integer,
    title text,
    query text,
    min_value double precision,
    max_value double precision
);


ALTER TABLE "INSTANCE_METRIC" OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 32777)
-- Name: INSTANCE_METRIC_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "INSTANCE_METRIC_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "INSTANCE_METRIC_id_seq" OWNER TO postgres;

--
-- TOC entry 2089 (class 0 OID 0)
-- Dependencies: 180
-- Name: INSTANCE_METRIC_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "INSTANCE_METRIC_id_seq" OWNED BY "INSTANCE_METRIC".id;


--
-- TOC entry 175 (class 1259 OID 16403)
-- Name: TEMPLATE_METRICS; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "TEMPLATE_METRICS" (
    id integer NOT NULL,
    title text,
    query text
);


ALTER TABLE "TEMPLATE_METRICS" OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 16401)
-- Name: METRICS_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "METRICS_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "METRICS_id_seq" OWNER TO postgres;

--
-- TOC entry 2090 (class 0 OID 0)
-- Dependencies: 174
-- Name: METRICS_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "METRICS_id_seq" OWNED BY "TEMPLATE_METRICS".id;


--
-- TOC entry 185 (class 1259 OID 32798)
-- Name: METRIC_STATE; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "METRIC_STATE" (
    id integer NOT NULL,
    state text,
    start_datetime timestamp without time zone,
    end_datetime timestamp without time zone,
    inst_metric integer,
    resolved boolean,
    host_id integer
);


ALTER TABLE "METRIC_STATE" OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 32796)
-- Name: METRIC_STATE_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "METRIC_STATE_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "METRIC_STATE_id_seq" OWNER TO postgres;

--
-- TOC entry 2091 (class 0 OID 0)
-- Dependencies: 184
-- Name: METRIC_STATE_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "METRIC_STATE_id_seq" OWNED BY "METRIC_STATE".id;


--
-- TOC entry 195 (class 1259 OID 81931)
-- Name: Roles; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "Roles" (
    id integer NOT NULL,
    role text,
    username text
);


ALTER TABLE "Roles" OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 81929)
-- Name: Role1_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "Role1_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "Role1_id_seq" OWNER TO postgres;

--
-- TOC entry 2092 (class 0 OID 0)
-- Dependencies: 194
-- Name: Role1_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "Role1_id_seq" OWNED BY "Roles".id;


--
-- TOC entry 186 (class 1259 OID 40969)
-- Name: SERVER_STATE_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "SERVER_STATE_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "SERVER_STATE_id_seq" OWNER TO postgres;

--
-- TOC entry 2093 (class 0 OID 0)
-- Dependencies: 186
-- Name: SERVER_STATE_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "SERVER_STATE_id_seq" OWNED BY "HOST_STATE".id;


--
-- TOC entry 187 (class 1259 OID 49169)
-- Name: Users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE IF NOT EXISTS "Users" (
    username text NOT NULL,
    password text,
    enabled text
);


ALTER TABLE "Users" OWNER TO postgres;

--
-- TOC entry 1952 (class 2604 OID 57358)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "FAVORITES" ALTER COLUMN id SET DEFAULT nextval('"FAVORITES_id_seq"'::regclass);


--
-- TOC entry 1948 (class 2604 OID 49184)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "HOST_METRIC" ALTER COLUMN id SET DEFAULT nextval('"HOST_METRIC_id_seq1"'::regclass);


--
-- TOC entry 1950 (class 2604 OID 49185)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "HOST_STATE" ALTER COLUMN id SET DEFAULT nextval('"SERVER_STATE_id_seq"'::regclass);


--
-- TOC entry 1949 (class 2604 OID 49186)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "INSTANCE_METRIC" ALTER COLUMN id SET DEFAULT nextval('"INSTANCE_METRIC_id_seq"'::regclass);


--
-- TOC entry 1951 (class 2604 OID 49187)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "METRIC_STATE" ALTER COLUMN id SET DEFAULT nextval('"METRIC_STATE_id_seq"'::regclass);


--
-- TOC entry 1953 (class 2604 OID 81934)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "Roles" ALTER COLUMN id SET DEFAULT nextval('"Role1_id_seq"'::regclass);


--
-- TOC entry 1947 (class 2604 OID 49188)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "TEMPLATE_METRICS" ALTER COLUMN id SET DEFAULT nextval('"METRICS_id_seq"'::regclass);


--
-- TOC entry 1946 (class 2604 OID 49189)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "VALUE_METRIC" ALTER COLUMN id SET DEFAULT nextval('"HOST_METRIC_id_seq"'::regclass);


--
-- TOC entry 1955 (class 2606 OID 16400)
-- Name: HOST_METRIC_ID; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "VALUE_METRIC"
    ADD CONSTRAINT "HOST_METRIC_ID" PRIMARY KEY (id);


--
-- TOC entry 1957 (class 2606 OID 16411)
-- Name: METRICS_ID; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "TEMPLATE_METRICS"
    ADD CONSTRAINT "METRICS_ID" PRIMARY KEY (id);


--
-- TOC entry 1959 (class 2606 OID 24715)
-- Name: VALUE_METRIC_ID; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "HOST_METRIC"
    ADD CONSTRAINT "VALUE_METRIC_ID" PRIMARY KEY (id);


--
-- TOC entry 1971 (class 2606 OID 81939)
-- Name: idRole; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "Roles"
    ADD CONSTRAINT "idRole" PRIMARY KEY (id);


--
-- TOC entry 1969 (class 2606 OID 57360)
-- Name: id_Favorites; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "FAVORITES"
    ADD CONSTRAINT "id_Favorites" PRIMARY KEY (id);


--
-- TOC entry 1961 (class 2606 OID 32787)
-- Name: id_inst; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "INSTANCE_METRIC"
    ADD CONSTRAINT id_inst PRIMARY KEY (id);


--
-- TOC entry 1963 (class 2606 OID 32795)
-- Name: id_state_host; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "HOST_STATE"
    ADD CONSTRAINT id_state_host PRIMARY KEY (id);


--
-- TOC entry 1965 (class 2606 OID 32806)
-- Name: metric_state_id; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "METRIC_STATE"
    ADD CONSTRAINT metric_state_id PRIMARY KEY (id);


--
-- TOC entry 1967 (class 2606 OID 49176)
-- Name: usernameUsers; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "Users"
    ADD CONSTRAINT "usernameUsers" PRIMARY KEY (username);


-- Completed on 2016-04-17 23:27:14

--
-- PostgreSQL database dump complete
--

