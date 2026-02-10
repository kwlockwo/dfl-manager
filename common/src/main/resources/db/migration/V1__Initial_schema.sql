-- Initial schema migration for DFL Manager
-- Exported from production database on main branch
-- Date: 2026-02-10
--
-- This establishes the baseline schema for the monorepo migration.
-- All tables from the existing production database are included.

--
-- PostgreSQL database dump
--

-- Dumped from database version 15.14
-- Dumped by pg_dump version 15.12 (Homebrew)

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
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

-- *not* creating schema, since initdb creates it


--
-- Name: pg_stat_statements; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_stat_statements WITH SCHEMA public;


SET default_table_access_method = heap;

--
-- Name: afl_fixture; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.afl_fixture (
    round integer NOT NULL,
    game integer NOT NULL,
    home_team character varying(4),
    away_team character varying(4),
    ground character varying(5),
    start_time timestamp with time zone,
    timezone character varying(50),
    end_time timestamp with time zone,
    stats_downloaded boolean
);


--
-- Name: afl_player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.afl_player (
    player_id character varying(7) NOT NULL,
    jumper_no integer,
    name character varying(50),
    first_name character varying(25),
    second_name character varying(25),
    team_id character varying(5),
    height integer,
    weight integer,
    dob date,
    dfl_player_id integer
);


--
-- Name: afl_team; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.afl_team (
    team_id character varying(5) NOT NULL,
    name character varying(30),
    nickname character varying(20),
    website character varying(50),
    senior_uri character varying(50),
    rookie_uri character varying(50),
    official_website character varying(50),
    official_senior_uri character varying(50),
    official_rookie_uri character varying(50)
);


--
-- Name: dfl_best_22_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dfl_best_22_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dfl_best_22; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_best_22 (
    id integer DEFAULT nextval('public.dfl_best_22_seq'::regclass) NOT NULL,
    round integer DEFAULT 0,
    player_id integer DEFAULT 0,
    score integer DEFAULT 0,
    bench boolean
);


--
-- Name: dfl_early_ins_and_outs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_early_ins_and_outs (
    id integer NOT NULL,
    team_code character varying(5) NOT NULL,
    round integer NOT NULL,
    team_player_id integer NOT NULL,
    in_or_out character varying(4)
);


--
-- Name: dfl_early_ins_and_outs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dfl_early_ins_and_outs_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dfl_early_ins_and_outs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.dfl_early_ins_and_outs_id_seq OWNED BY public.dfl_early_ins_and_outs.id;


--
-- Name: dfl_early_ins_and_outs_old; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_early_ins_and_outs_old (
    team_code character varying(5) NOT NULL,
    round integer NOT NULL,
    team_player_id integer NOT NULL,
    in_or_out character varying(4) DEFAULT NULL::character varying
);


--
-- Name: dfl_fixture; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_fixture (
    round integer NOT NULL,
    game integer NOT NULL,
    home_team character varying(5) DEFAULT NULL::character varying,
    away_team character varying(5) DEFAULT NULL::character varying
);


--
-- Name: dfl_ladder; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_ladder (
    round integer NOT NULL,
    team_code character varying(5) NOT NULL,
    wins integer,
    losses integer,
    draws integer,
    points_for integer,
    points_against integer,
    average_for double precision,
    average_against double precision,
    pts integer,
    percentage double precision,
    live boolean
);


--
-- Name: dfl_matthew_allen_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dfl_matthew_allen_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dfl_matthew_allen; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_matthew_allen (
    id integer DEFAULT nextval('public.dfl_matthew_allen_seq'::regclass) NOT NULL,
    round integer DEFAULT 0,
    game integer DEFAULT 0,
    player_id integer DEFAULT 0,
    votes integer DEFAULT 0,
    total integer DEFAULT 0,
    score integer DEFAULT 0
);


--
-- Name: dfl_player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_player (
    player_id integer NOT NULL,
    first_name character varying(25) DEFAULT NULL::character varying,
    last_name character varying(25) DEFAULT NULL::character varying,
    initial character varying(1) DEFAULT NULL::character varying,
    status character varying(6) DEFAULT NULL::character varying,
    afl_club character varying(5) DEFAULT NULL::character varying,
    "position" character varying(3) DEFAULT NULL::character varying,
    afl_player_id character varying(7) DEFAULT NULL::character varying,
    is_first_year boolean
);


--
-- Name: dfl_player_predicted_scores; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_player_predicted_scores (
    player_id integer NOT NULL,
    round integer NOT NULL,
    afl_player_id character varying(7) DEFAULT NULL::character varying,
    team_code character varying(5) DEFAULT NULL::character varying,
    team_player_id integer,
    predicted_score integer
);


--
-- Name: dfl_player_scores; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_player_scores (
    player_id integer NOT NULL,
    round integer NOT NULL,
    afl_player_id character varying(7) DEFAULT NULL::character varying,
    team_code character varying(5) DEFAULT NULL::character varying,
    team_player_id integer,
    score integer
);


--
-- Name: dfl_preseason_scores; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_preseason_scores (
    player_id integer NOT NULL,
    round integer NOT NULL,
    score integer NOT NULL
);


--
-- Name: dfl_round_early_game_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dfl_round_early_game_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dfl_round_early_games; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_round_early_games (
    id integer DEFAULT nextval('public.dfl_round_early_game_seq'::regclass) NOT NULL,
    round integer,
    afl_round integer,
    afl_game integer,
    start_time timestamp with time zone
);


--
-- Name: dfl_round_info; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_round_info (
    round integer NOT NULL,
    hard_lockout timestamp with time zone,
    split_round character varying(1) DEFAULT NULL::character varying
);


--
-- Name: dfl_round_mapping_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dfl_round_mapping_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dfl_round_mapping; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_round_mapping (
    id integer DEFAULT nextval('public.dfl_round_mapping_seq'::regclass) NOT NULL,
    round integer,
    afl_round integer,
    afl_game integer,
    afl_team character varying(4) DEFAULT NULL::character varying
);


--
-- Name: dfl_selected_player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_selected_player (
    round integer NOT NULL,
    player_id integer NOT NULL,
    team_player_id integer,
    team_code character varying(5) DEFAULT NULL::character varying,
    is_emergency integer,
    is_dnp boolean,
    score_used boolean,
    has_played boolean,
    replacement_ind character varying(2)
);


--
-- Name: dfl_selection_ids; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_selection_ids (
    id integer NOT NULL,
    round integer NOT NULL,
    team_code character varying(5) NOT NULL,
    selection_id character varying(25)
);


--
-- Name: dfl_selection_ids_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.dfl_selection_ids_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: dfl_selection_ids_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.dfl_selection_ids_id_seq OWNED BY public.dfl_selection_ids.id;


--
-- Name: dfl_team; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_team (
    team_code character varying(5) NOT NULL,
    name character varying(50) DEFAULT NULL::character varying,
    short_name character varying(10) DEFAULT NULL::character varying,
    coach_name character varying(50) DEFAULT NULL::character varying,
    home_ground character varying(50) DEFAULT NULL::character varying,
    colours character varying(50) DEFAULT NULL::character varying,
    coach_email character varying(50) DEFAULT NULL::character varying
);


--
-- Name: dfl_team_player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_team_player (
    player_id integer NOT NULL,
    team_code character varying(5) DEFAULT NULL::character varying,
    team_player_id integer
);


--
-- Name: dfl_team_predicted_scores; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_team_predicted_scores (
    team_code character varying(5) NOT NULL,
    round integer NOT NULL,
    predicted_score integer
);


--
-- Name: dfl_team_scores; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_team_scores (
    team_code character varying(5) NOT NULL,
    round integer NOT NULL,
    score integer
);


--
-- Name: dfl_unmatched_player; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dfl_unmatched_player (
    player_id integer NOT NULL,
    first_name character varying(25) DEFAULT NULL::character varying,
    last_name character varying(25) DEFAULT NULL::character varying,
    initial character varying(1) DEFAULT NULL::character varying,
    status character varying(6) DEFAULT NULL::character varying,
    afl_club character varying(5) DEFAULT NULL::character varying,
    "position" character varying(3) DEFAULT NULL::character varying,
    afl_player_id character varying(7) DEFAULT NULL::character varying,
    is_first_year boolean
);


--
-- Name: globals; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.globals (
    code character varying(50) NOT NULL,
    group_code character varying(50) NOT NULL,
    params character varying(100),
    value character varying(100)
);


--
-- Name: ins_and_outs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ins_and_outs (
    id integer NOT NULL,
    team_code character varying(5) NOT NULL,
    round integer NOT NULL,
    team_player_id integer NOT NULL,
    in_or_out character varying(4)
);


--
-- Name: ins_and_outs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.ins_and_outs_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: ins_and_outs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.ins_and_outs_id_seq OWNED BY public.ins_and_outs.id;


--
-- Name: process; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.process (
    process_id character varying(40) NOT NULL,
    start_time timestamp with time zone NOT NULL,
    end_time timestamp with time zone,
    params character varying(500) DEFAULT NULL::character varying,
    status character varying(16) DEFAULT NULL::character varying
);


--
-- Name: raw_player_stats; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.raw_player_stats (
    round integer NOT NULL,
    name character varying(50) NOT NULL,
    team character varying(4) NOT NULL,
    jumper_no integer NOT NULL,
    kicks integer,
    handballs integer,
    disposals integer,
    marks integer,
    hitouts integer,
    frees_for integer,
    frees_against integer,
    tackles integer,
    goals integer,
    behinds integer,
    scraping_status character varying(10)
);


--
-- Name: scheduler_blob_triggers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);


--
-- Name: scheduler_calendars; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);


--
-- Name: scheduler_cron_triggers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);


--
-- Name: scheduler_fired_triggers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_fired_triggers (
    sched_name character varying(120) NOT NULL,
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    instance_name character varying(200) NOT NULL,
    fired_time bigint NOT NULL,
    sched_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(200),
    job_group character varying(200),
    is_nonconcurrent boolean,
    requests_recovery boolean
);


--
-- Name: scheduler_job_details; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_job_details (
    sched_name character varying(120) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    job_class_name character varying(250) NOT NULL,
    is_durable boolean NOT NULL,
    is_nonconcurrent boolean NOT NULL,
    is_update_data boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);


--
-- Name: scheduler_locks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);


--
-- Name: scheduler_paused_trigger_grps; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);


--
-- Name: scheduler_scheduler_state; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);


--
-- Name: scheduler_simple_triggers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);


--
-- Name: scheduler_simprop_triggers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);


--
-- Name: scheduler_triggers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduler_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(200),
    misfire_instr smallint,
    job_data bytea
);


--
-- Name: stats_round_player_stats; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stats_round_player_stats (
    round integer NOT NULL,
    name character varying(50) NOT NULL,
    team character varying(4) NOT NULL,
    jumper_no integer NOT NULL,
    kicks integer,
    handballs integer,
    disposals integer,
    marks integer,
    hitouts integer,
    frees_for integer,
    frees_against integer,
    tackles integer,
    goals integer,
    behinds integer,
    scraping_status character varying(10)
);


--
-- Name: dfl_early_ins_and_outs id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_early_ins_and_outs ALTER COLUMN id SET DEFAULT nextval('public.dfl_early_ins_and_outs_id_seq'::regclass);


--
-- Name: dfl_selection_ids id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_selection_ids ALTER COLUMN id SET DEFAULT nextval('public.dfl_selection_ids_id_seq'::regclass);


--
-- Name: ins_and_outs id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ins_and_outs ALTER COLUMN id SET DEFAULT nextval('public.ins_and_outs_id_seq'::regclass);


--
-- Name: afl_fixture afl_fixture_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afl_fixture
    ADD CONSTRAINT afl_fixture_pk PRIMARY KEY (round, game);


--
-- Name: afl_player afl_player_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afl_player
    ADD CONSTRAINT afl_player_pk PRIMARY KEY (player_id);


--
-- Name: afl_team afl_team_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.afl_team
    ADD CONSTRAINT afl_team_pkey PRIMARY KEY (team_id);


--
-- Name: dfl_best_22 dfl_best_22_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_best_22
    ADD CONSTRAINT dfl_best_22_pkey PRIMARY KEY (id);


--
-- Name: dfl_early_ins_and_outs_old dfl_early_ins_and_outs_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_early_ins_and_outs_old
    ADD CONSTRAINT dfl_early_ins_and_outs_pkey PRIMARY KEY (team_code, round, team_player_id);


--
-- Name: dfl_early_ins_and_outs dfl_early_ins_and_outs_pkey1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_early_ins_and_outs
    ADD CONSTRAINT dfl_early_ins_and_outs_pkey1 PRIMARY KEY (id);


--
-- Name: dfl_fixture dfl_fixture_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_fixture
    ADD CONSTRAINT dfl_fixture_pkey PRIMARY KEY (round, game);


--
-- Name: dfl_ladder dfl_ladder_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_ladder
    ADD CONSTRAINT dfl_ladder_pkey PRIMARY KEY (round, team_code);


--
-- Name: dfl_matthew_allen dfl_matthew_allen_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_matthew_allen
    ADD CONSTRAINT dfl_matthew_allen_pkey PRIMARY KEY (id);


--
-- Name: dfl_player dfl_player_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_player
    ADD CONSTRAINT dfl_player_pkey PRIMARY KEY (player_id);


--
-- Name: dfl_player_predicted_scores dfl_player_predicted_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_player_predicted_scores
    ADD CONSTRAINT dfl_player_predicted_scores_pkey PRIMARY KEY (player_id, round);


--
-- Name: dfl_player_scores dfl_player_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_player_scores
    ADD CONSTRAINT dfl_player_scores_pkey PRIMARY KEY (player_id, round);


--
-- Name: dfl_preseason_scores dfl_preseason_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_preseason_scores
    ADD CONSTRAINT dfl_preseason_scores_pkey PRIMARY KEY (player_id, round);


--
-- Name: dfl_round_early_games dfl_round_early_games_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_round_early_games
    ADD CONSTRAINT dfl_round_early_games_pkey PRIMARY KEY (id);


--
-- Name: dfl_round_info dfl_round_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_round_info
    ADD CONSTRAINT dfl_round_info_pkey PRIMARY KEY (round);


--
-- Name: dfl_round_mapping dfl_round_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_round_mapping
    ADD CONSTRAINT dfl_round_mapping_pkey PRIMARY KEY (id);


--
-- Name: dfl_selected_player dfl_selected_player_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_selected_player
    ADD CONSTRAINT dfl_selected_player_pkey PRIMARY KEY (round, player_id);


--
-- Name: dfl_selection_ids dfl_selection_ids_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_selection_ids
    ADD CONSTRAINT dfl_selection_ids_pkey PRIMARY KEY (id);


--
-- Name: dfl_team dfl_team_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_team
    ADD CONSTRAINT dfl_team_pkey PRIMARY KEY (team_code);


--
-- Name: dfl_team_player dfl_team_player_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_team_player
    ADD CONSTRAINT dfl_team_player_pkey PRIMARY KEY (player_id);


--
-- Name: dfl_team_predicted_scores dfl_team_predicted_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_team_predicted_scores
    ADD CONSTRAINT dfl_team_predicted_scores_pkey PRIMARY KEY (team_code, round);


--
-- Name: dfl_team_scores dfl_team_scores_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_team_scores
    ADD CONSTRAINT dfl_team_scores_pkey PRIMARY KEY (team_code, round);


--
-- Name: dfl_unmatched_player dfl_unmatched_player_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dfl_unmatched_player
    ADD CONSTRAINT dfl_unmatched_player_pkey PRIMARY KEY (player_id);


--
-- Name: globals globals_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.globals
    ADD CONSTRAINT globals_pk PRIMARY KEY (code, group_code);


--
-- Name: ins_and_outs ins_and_outs_pkey1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ins_and_outs
    ADD CONSTRAINT ins_and_outs_pkey1 PRIMARY KEY (id);


--
-- Name: process process_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.process
    ADD CONSTRAINT process_pkey PRIMARY KEY (process_id, start_time);


--
-- Name: scheduler_blob_triggers qrtz_blob_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_calendars qrtz_calendars_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);


--
-- Name: scheduler_cron_triggers qrtz_cron_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_fired_triggers qrtz_fired_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);


--
-- Name: scheduler_job_details qrtz_job_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);


--
-- Name: scheduler_locks qrtz_locks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);


--
-- Name: scheduler_paused_trigger_grps qrtz_paused_trigger_grps_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);


--
-- Name: scheduler_scheduler_state qrtz_scheduler_state_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);


--
-- Name: scheduler_simple_triggers qrtz_simple_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_simprop_triggers qrtz_simprop_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_triggers qrtz_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);


--
-- Name: raw_player_stats raw_player_stats_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.raw_player_stats
    ADD CONSTRAINT raw_player_stats_pk PRIMARY KEY (round, name, team, jumper_no);


--
-- Name: stats_round_player_stats stats_round_player_stats_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stats_round_player_stats
    ADD CONSTRAINT stats_round_player_stats_pk PRIMARY KEY (round, name, team, jumper_no);


--
-- Name: idx_qrtz_ft_inst_job_req_rcvry; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON public.scheduler_fired_triggers USING btree (sched_name, instance_name, requests_recovery);


--
-- Name: idx_qrtz_ft_j_g; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_ft_j_g ON public.scheduler_fired_triggers USING btree (sched_name, job_name, job_group);


--
-- Name: idx_qrtz_ft_jg; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_ft_jg ON public.scheduler_fired_triggers USING btree (sched_name, job_group);


--
-- Name: idx_qrtz_ft_t_g; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_ft_t_g ON public.scheduler_fired_triggers USING btree (sched_name, trigger_name, trigger_group);


--
-- Name: idx_qrtz_ft_tg; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_ft_tg ON public.scheduler_fired_triggers USING btree (sched_name, trigger_group);


--
-- Name: idx_qrtz_ft_trig_inst_name; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_ft_trig_inst_name ON public.scheduler_fired_triggers USING btree (sched_name, instance_name);


--
-- Name: idx_qrtz_j_grp; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_j_grp ON public.scheduler_job_details USING btree (sched_name, job_group);


--
-- Name: idx_qrtz_j_req_recovery; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_j_req_recovery ON public.scheduler_job_details USING btree (sched_name, requests_recovery);


--
-- Name: idx_qrtz_t_c; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_c ON public.scheduler_triggers USING btree (sched_name, calendar_name);


--
-- Name: idx_qrtz_t_g; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_g ON public.scheduler_triggers USING btree (sched_name, trigger_group);


--
-- Name: idx_qrtz_t_j; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_j ON public.scheduler_triggers USING btree (sched_name, job_name, job_group);


--
-- Name: idx_qrtz_t_jg; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_jg ON public.scheduler_triggers USING btree (sched_name, job_group);


--
-- Name: idx_qrtz_t_n_g_state; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_n_g_state ON public.scheduler_triggers USING btree (sched_name, trigger_group, trigger_state);


--
-- Name: idx_qrtz_t_n_state; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_n_state ON public.scheduler_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);


--
-- Name: idx_qrtz_t_next_fire_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_next_fire_time ON public.scheduler_triggers USING btree (sched_name, next_fire_time);


--
-- Name: idx_qrtz_t_nft_misfire; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_nft_misfire ON public.scheduler_triggers USING btree (sched_name, misfire_instr, next_fire_time);


--
-- Name: idx_qrtz_t_nft_st; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_nft_st ON public.scheduler_triggers USING btree (sched_name, trigger_state, next_fire_time);


--
-- Name: idx_qrtz_t_nft_st_misfire; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_nft_st_misfire ON public.scheduler_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);


--
-- Name: idx_qrtz_t_nft_st_misfire_grp; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON public.scheduler_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);


--
-- Name: idx_qrtz_t_state; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_qrtz_t_state ON public.scheduler_triggers USING btree (sched_name, trigger_state);


--
-- Name: scheduler_blob_triggers qrtz_blob_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES public.scheduler_triggers(sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_cron_triggers qrtz_cron_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES public.scheduler_triggers(sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_simple_triggers qrtz_simple_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES public.scheduler_triggers(sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_simprop_triggers qrtz_simprop_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES public.scheduler_triggers(sched_name, trigger_name, trigger_group);


--
-- Name: scheduler_triggers qrtz_triggers_sched_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduler_triggers
    ADD CONSTRAINT qrtz_triggers_sched_name_fkey FOREIGN KEY (sched_name, job_name, job_group) REFERENCES public.scheduler_job_details(sched_name, job_name, job_group);


--
-- PostgreSQL database dump complete
--

