CREATE TABLE event(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    title character varying(255) NOT NULL,
    date date NOT NULL,
    "time" time without time zone NOT NULL,
    ticket_price real NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS event OWNER to spring_data_access;

CREATE TABLE users(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS users OWNER to spring_data_access;

CREATE TABLE ticket(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    user_id integer NOT NULL,
    event_id integer NOT NULL,
    category character varying(50) NOT NULL,
    place integer NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ticket_user_foreign_key FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
        CONSTRAINT ticket_event_foreign_key FOREIGN KEY (event_id)
        REFERENCES public.event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS ticket OWNER to spring_data_access;

CREATE TABLE user_account(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    user_id integer NOT NULL,
    amount real NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT user_account_user_foreign_key FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.user_account OWNER to spring_data_access;