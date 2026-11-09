-- sleep context: recorded sleep periods and a per-user sleep routine

CREATE TABLE sleep_records (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    started_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    ended_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    source      VARCHAR(10) NOT NULL,
    sleep_score INTEGER,
    notes       VARCHAR(2000) NOT NULL
);

CREATE INDEX idx_sleep_records_user_id ON sleep_records (user_id);

CREATE TABLE sleep_routines (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id           BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    bedtime           TIME NOT NULL,
    wake_time         TIME NOT NULL,
    target_hours      DOUBLE PRECISION NOT NULL,
    reminder_enabled  BOOLEAN NOT NULL
);
