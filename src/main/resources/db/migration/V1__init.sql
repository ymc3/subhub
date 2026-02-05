-- SubHub schema (v1)

CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  email TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT users_email_uk UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS subscriptions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  plan TEXT NOT NULL,
  status TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT subscriptions_user_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS subscriptions_user_id_idx ON subscriptions(user_id);
