#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

./mvnw clean install
rm -fR .db
mkdir .db
java -jar target/movie-library-0.0.1-SNAPSHOT.jar server config.yml
