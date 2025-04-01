DROP TABLE IF EXISTS public.messages;
DROP TABLE IF EXISTS public.users;

CREATE TABLE public.users (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	"name" varchar NOT NULL,
	"password" varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (name)
);

CREATE TABLE public.messages (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	"time" timestamp NOT NULL,
	to_user int4 NOT NULL,
	from_user int4 NOT NULL,
	message varchar NOT NULL,
	CONSTRAINT messages_pk PRIMARY KEY (id),
	CONSTRAINT messages_users_fk FOREIGN KEY (to_user) REFERENCES public.users(id),
	CONSTRAINT messages_users_fk_1 FOREIGN KEY (from_user) REFERENCES public.users(id)
);