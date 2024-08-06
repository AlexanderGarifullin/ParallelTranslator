CREATE TABLE IF NOT EXISTS api_translation_requests_logs (
    id SERIAL PRIMARY KEY,
    client_ip VARCHAR(45) NOT NULL,
    text TEXT NOT NULL,
    translated_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
