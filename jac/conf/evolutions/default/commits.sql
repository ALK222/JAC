-- Commits schema

-- !Ups
CREATE TABLE commits {
    id TEXT NOT NULL,
    message TEXT NOT NULL,
    description TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
    PRIMARY KEY (id)
};

-- !Downs
DROP TABLE commits