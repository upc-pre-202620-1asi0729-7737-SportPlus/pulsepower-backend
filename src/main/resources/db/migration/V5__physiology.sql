-- physiology context: wearable connection, simulated readings and guidance alerts

CREATE TABLE wearable_connections (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id         BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    provider        VARCHAR(50) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    last_synced_at  TIMESTAMP WITH TIME ZONE
);

CREATE TABLE physiological_records (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    metric      VARCHAR(20) NOT NULL,
    value       DOUBLE PRECISION NOT NULL,
    unit        VARCHAR(10) NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_physiological_records_user_id ON physiological_records (user_id);

CREATE TABLE guidance_alerts (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title       VARCHAR(150) NOT NULL,
    message     VARCHAR(1000) NOT NULL,
    issued_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    reviewed_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_guidance_alerts_user_id ON guidance_alerts (user_id);
