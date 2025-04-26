#!/usr/bin/env bash
set -e

host="$1"
shift
port="$1"
shift

until nc -z "$host" "$port"; do
  echo "Waiting for $host:$port..."
  sleep 2
done

echo "$host:$port is available. Starting application."
exec "$@"
