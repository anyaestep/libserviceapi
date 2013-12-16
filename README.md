## Scala/Play/Akka Example - Library API
Library API provides a single endpoint
/api/v1/library/:bookId?userId=:userId

## Run sample
1. start postgresql
2. libdb/libdb, give postgres user access to all
3. Modify scripts/libdb.sql book insert to point to an existing file
3. create schema objects scripts/libdb.sql
4. ./activator run
5. Go to URL http://localhost:9000/api/v1/library/1?userId=1

6. run tests ./activator "test-only test.controllers.LibrarySpec"
