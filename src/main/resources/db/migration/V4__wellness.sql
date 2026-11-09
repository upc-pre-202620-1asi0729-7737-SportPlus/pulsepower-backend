-- wellness context: daily check-ins, habits and their logs

CREATE TABLE wellness_check_ins (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    date        DATE NOT NULL,
    mood        INTEGER NOT NULL,
    stress      INTEGER NOT NULL,
    discomfort  BOOLEAN NOT NULL,
    notes       VARCHAR(2000) NOT NULL
);

CREATE INDEX idx_wellness_check_ins_user_id ON wellness_check_ins (user_id);
CREATE UNIQUE INDEX uq_wellness_check_ins_user_date ON wellness_check_ins (user_id, date);

CREATE TABLE wellness_habits (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id        BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name           VARCHAR(80) NOT NULL,
    weekly_target  INTEGER NOT NULL
);

CREATE INDEX idx_wellness_habits_user_id ON wellness_habits (user_id);

CREATE TABLE habit_logs (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id    BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    habit_id   BIGINT NOT NULL REFERENCES wellness_habits (id) ON DELETE CASCADE,
    date       DATE NOT NULL
);

CREATE INDEX idx_habit_logs_habit_id ON habit_logs (habit_id);
CREATE UNIQUE INDEX uq_habit_logs_habit_date ON habit_logs (habit_id, date);
