cat >/import.cql <<EOF
DROP keyspace test;
CREATE keyspace test with replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
CREATE TABLE test.message (uuid text, email text, title text, content text, magicNumber text, PRIMARY KEY((email, magicNumber), uuid));
CREATE INDEX ON test.message (email);
CREATE INDEX ON test.message (magicNumber);
EOF

until cqlsh -f /import.cql;
do
  echo "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 2
done &

exec /docker-entrypoint.sh "$@"