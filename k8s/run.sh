kubectl apply -f kafka.yml
kubectl apply -f postgres.yml
kubectl apply -f rabbit.yml
kubectl apply -f payment-service-app.yml
kubectl apply -f x-payment-adapter-service.yml

helm upgrade --install zipkin zipkin/zipkin -n observability
helm upgrade --install loki grafana/loki \
 -n observability --create-namespace -f loki-values.yml
helm upgrade --install promtail grafana/promtail \
 -n observability -f promtail-values.yaml
helm upgrade --install grafana grafana/grafana \
   -n observability \
   --set adminPassword=admin \
   --set service.type=ClusterIP