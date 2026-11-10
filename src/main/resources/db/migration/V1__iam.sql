-- iam context: users, their personal goals, and subscriptions

CREATE TABLE users (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL,
    email             VARCHAR(150) NOT NULL UNIQUE,
    password_hash     VARCHAR(100) NOT NULL,
    display_name      VARCHAR(100) NOT NULL,
    focus             VARCHAR(20) NOT NULL,
    age               INTEGER,
    weight_kg         DOUBLE PRECISION,
    height_cm         DOUBLE PRECISION,
    main_sport        VARCHAR(80) NOT NULL,
    training_routine  VARCHAR(2000) NOT NULL,
    physical_history  VARCHAR(2000) NOT NULL,
    status            VARCHAR(20) NOT NULL
);

CREATE TABLE personal_goals (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    description VARCHAR(200) NOT NULL,
    completed   BOOLEAN NOT NULL,
    user_id     BIGINT REFERENCES users (id) ON DELETE CASCADE,
    position    INTEGER
);

CREATE INDEX idx_personal_goals_user_id ON personal_goals (user_id);

CREATE TABLE subscription_plans (
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    name VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE subscription_plan_features (
    plan_id  BIGINT NOT NULL REFERENCES subscription_plans (id) ON DELETE CASCADE,
    position INTEGER NOT NULL,
    feature  VARCHAR(200),
    PRIMARY KEY (plan_id, position)
);

CREATE TABLE subscriptions (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at           TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id              BIGINT NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    plan_id              BIGINT NOT NULL REFERENCES subscription_plans (id),
    status               VARCHAR(20) NOT NULL,
    cancel_at_period_end BOOLEAN NOT NULL,
    ends_at              TIMESTAMP WITH TIME ZONE
);
