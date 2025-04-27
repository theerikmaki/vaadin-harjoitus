#!/bin/bash
set -e

host="$1"
shift
cmd="$@"

echo "Waiting for database at $host..."

until mysqladmin ping -h "$host" --silent; do
  >&2 echo "Database is unavailable - sleeping"
  sleep 2
done

>&2 echo "Database is up - executing command"
exec $cmd
