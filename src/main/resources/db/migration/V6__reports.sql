-- reports context: generated progress reports, built client-side as a PDF and
-- stored here as metadata plus the document itself

CREATE TABLE progress_reports (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id          BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    report_from      DATE NOT NULL,
    report_to        DATE NOT NULL,
    requested_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    file_name        VARCHAR(150) NOT NULL,
    document_base64  TEXT NOT NULL
);

CREATE INDEX idx_progress_reports_user_id ON progress_reports (user_id);

CREATE TABLE progress_report_types (
    progress_report_id  BIGINT NOT NULL REFERENCES progress_reports (id) ON DELETE CASCADE,
    position             INTEGER NOT NULL,
    type                 VARCHAR(20) NOT NULL,
    PRIMARY KEY (progress_report_id, position)
);
