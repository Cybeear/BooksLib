--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Book" (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    author character varying(20) NOT NULL
);


ALTER TABLE public."Book" OWNER TO postgres;

--
-- Name: Book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Book_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Book_id_seq" OWNER TO postgres;

--
-- Name: Book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Book_id_seq" OWNED BY public."Book".id;


--
-- Name: Borrow; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Borrow" (
    id integer NOT NULL,
    reader_id integer NOT NULL,
    book_id integer NOT NULL
);


ALTER TABLE public."Borrow" OWNER TO postgres;

--
-- Name: Borrow_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Borrow_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Borrow_id_seq" OWNER TO postgres;

--
-- Name: Borrow_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Borrow_id_seq" OWNED BY public."Borrow".id;


--
-- Name: Reader; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."Reader" (
    id integer NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE public."Reader" OWNER TO postgres;

--
-- Name: Reader_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Reader_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."Reader_id_seq" OWNER TO postgres;

--
-- Name: Reader_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Reader_id_seq" OWNED BY public."Reader".id;


--
-- Name: Book id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Book" ALTER COLUMN id SET DEFAULT nextval('public."Book_id_seq"'::regclass);


--
-- Name: Borrow id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Borrow" ALTER COLUMN id SET DEFAULT nextval('public."Borrow_id_seq"'::regclass);


--
-- Name: Reader id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Reader" ALTER COLUMN id SET DEFAULT nextval('public."Reader_id_seq"'::regclass);


--
-- Data for Name: Book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Book" (id, name, author) FROM stdin;
1	name	author
3	qwe	qwe
4	test	test
5	retey	eter
6	efsd	sdfsvd
7	wqefd	sda
8	asd	saf
9	vzx	asf
10	test1	test1
11	test555	test555
12	qweasd	saasddsad/sda
\.


--
-- Data for Name: Borrow; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Borrow" (id, reader_id, book_id) FROM stdin;
13	1	1
15	1	4
16	2	3
17	2	5
18	3	10
19	3	11
20	3	9
\.


--
-- Data for Name: Reader; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."Reader" (id, name) FROM stdin;
1	test
2	qwer
3	test1
4	test555
5	349arq
\.


--
-- Name: Book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Book_id_seq"', 12, true);


--
-- Name: Borrow_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Borrow_id_seq"', 20, true);


--
-- Name: Reader_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Reader_id_seq"', 5, true);


--
-- Name: Book Book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Book"
    ADD CONSTRAINT "Book_pkey" PRIMARY KEY (id);


--
-- Name: Borrow Borrow_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Borrow"
    ADD CONSTRAINT "Borrow_pkey" PRIMARY KEY (id);


--
-- Name: Borrow Ids; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Borrow"
    ADD CONSTRAINT "Ids" UNIQUE (reader_id, book_id);


--
-- Name: Reader Reader_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Reader"
    ADD CONSTRAINT "Reader_pkey" PRIMARY KEY (id);


--
-- Name: Borrow book_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Borrow"
    ADD CONSTRAINT book_id FOREIGN KEY (book_id) REFERENCES public."Book"(id);


--
-- Name: Borrow reader_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."Borrow"
    ADD CONSTRAINT reader_id FOREIGN KEY (reader_id) REFERENCES public."Reader"(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

