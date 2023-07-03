# syntax=docker/dockerfile:1
FROM mongo:7.0.0-rc5

ENV MONGODB_INITDB_DATABASE MediscreenDatabase

ADD init.js /docker-entrypoint-initdb.d/
