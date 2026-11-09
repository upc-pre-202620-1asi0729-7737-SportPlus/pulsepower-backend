-- community context: the achievement catalog, what each user has earned, and
-- private progress notes the user may share with a group or contact audience

CREATE TABLE achievements (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    code        VARCHAR(60) NOT NULL UNIQUE,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500) NOT NULL
);

CREATE TABLE user_achievements (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id        BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    achievement_id BIGINT NOT NULL REFERENCES achievements (id) ON DELETE CASCADE,
    earned_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX uq_user_achievements_user_achievement ON user_achievements (user_id, achievement_id);

CREATE TABLE shared_progresses (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title       VARCHAR(100) NOT NULL,
    content     VARCHAR(2000) NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_shared_progresses_user_id ON shared_progresses (user_id);

CREATE TABLE shared_progress_audiences (
    shared_progress_id  BIGINT NOT NULL REFERENCES shared_progresses (id) ON DELETE CASCADE,
    audience            VARCHAR(20) NOT NULL,
    PRIMARY KEY (shared_progress_id, audience)
);
