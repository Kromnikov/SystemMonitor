CREATE TABLE sshoncfigurationhibernate (
	sshconfigurationhibernate_id serial PRIMARY KEY,
	host integer,
	login text,
	password text,
	port integer,
	name text,
	location text
);