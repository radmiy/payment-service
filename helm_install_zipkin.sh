helm upgrade --install zipkin zipkin/zipkin -n observability

kubectl create namespace observability
kubectl -n observability port-forward svc/zipkin 9411:9411