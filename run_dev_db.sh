#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

#java -cp target/movie-library-0.0.1-SNAPSHOT.jar org.h2.tools.Server -?

rm -fR .dev-db
mkdir .dev-db
java -cp target/movie-library-0.0.1-SNAPSHOT.jar org.h2.tools.Server -baseDir $DIR/.dev-db
