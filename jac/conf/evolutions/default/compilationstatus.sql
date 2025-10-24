-- CompilationStatus schema

-- !Ups
CREATE TABLE compilationstatus {
    id TEXT NOT NULL,
    target TEXT NOT NULL,
    status INTEGER,
    PRIMARY KEY (id, target)
    FOREIGN KEY (id) REFERENCES commits (id)
};

-- !Downs
DROP TABLE CompilationStatus