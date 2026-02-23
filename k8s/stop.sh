kubectl delete -f x-payment-adapter-service.yml
kubectl delete -f payment-service-app.yml
kubectl delete -f postgres.yml
kubectl delete -f rabbit.yml
kubectl delete -f kafka.yml

helm uninstall zipkin -n observability
helm uninstall loki -n observability
helm uninstall promtail -n observability
helm uninstall grafana -n observability