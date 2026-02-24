helm upgrade --install loki grafana/loki \
 -n observability --create-namespace -f loki-values.yml
