-- Keycloak needs its own database; the application database is created
-- automatically by the JDBC URL (createDatabaseIfNotExist=true).
CREATE DATABASE IF NOT EXISTS keycloak CHARACTER SET utf8mb4;
