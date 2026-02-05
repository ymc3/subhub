-- Enforce subscription plan values (v3)

-- Normalize existing data (best-effort)
UPDATE subscriptions
SET plan = upper(plan)
WHERE plan IS NOT NULL;

-- Add check constraint (idempotent)
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint
    WHERE conname = 'subscriptions_plan_ck'
  ) THEN
    ALTER TABLE subscriptions
      ADD CONSTRAINT subscriptions_plan_ck
      CHECK (plan IN ('BASIC', 'PLUS', 'PRO'));
  END IF;
END $$;
