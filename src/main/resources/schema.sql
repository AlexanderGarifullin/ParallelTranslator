CREATE TABLE api_translation_requests_logs (
    id SERIAL PRIMARY KEY,
    client_ip VARCHAR(45) NOT NULL,
    text TEXT NOT NULL,
    translated_text TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
