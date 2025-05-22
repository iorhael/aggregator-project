--liquibase formatted sql

--changeset iorhael:1747149151452
CREATE TABLE retailer_job_executions
(
    id               UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    retailer_id      UUID REFERENCES retailers ON DELETE CASCADE,
    job_execution_id BIGINT,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now()
);

--rollback DROP TABLE user_job_executions
