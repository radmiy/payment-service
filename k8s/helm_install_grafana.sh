helm upgrade --install grafana grafana/grafana \
  -n observability \
  --set adminPassword=admin \
  --set service.type=ClusterIP