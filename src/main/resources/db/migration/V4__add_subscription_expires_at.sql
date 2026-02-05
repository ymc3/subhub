-- Add subscription expiration timestamp (v4)

ALTER TABLE subscriptions
  ADD COLUMN IF NOT EXISTS expires_at TIMESTAMPTZ NULL;
