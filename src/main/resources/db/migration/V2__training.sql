-- training context: a per-user training plan with planned activities, and recorded sessions

CREATE TABLE training_plans (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    name        VARCHAR(100) NOT NULL,
    period_from DATE,
    period_to   DATE
);

CREATE TABLE planned_activities (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    date              DATE NOT NULL,
    start_time        TIME,
    activity          VARCHAR(80) NOT NULL,
    duration_minutes  INTEGER NOT NULL,
    intensity         VARCHAR(10) NOT NULL,
    kind              VARCHAR(20) NOT NULL,
    status            VARCHAR(10) NOT NULL,
    training_plan_id  BIGINT REFERENCES training_plans (id) ON DELETE CASCADE,
    position          INTEGER
);

CREATE INDEX idx_planned_activities_training_plan_id ON planned_activities (training_plan_id);

CREATE TABLE training_sessions (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id              BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    date                 DATE NOT NULL,
    activity             VARCHAR(80) NOT NULL,
    duration_minutes     INTEGER NOT NULL,
    perceived_effort     INTEGER NOT NULL,
    notes                VARCHAR(2000) NOT NULL,
    planned_activity_id  BIGINT
);

CREATE INDEX idx_training_sessions_user_id ON training_sessions (user_id);
