#helm install keycloak bitnami/keycloak -n keycloak -f keycloak-values.yml \
#--set global.security.allowInsecureImages=true

#helm install keycloak bitnami/keycloak -n keycloak -f keycloak-values.yml\
#  --set image.repository=quay.io/keycloak/keycloak \
#  --set image.tag=latest \
#  --set global.security.allowInsecureImages=true \
#  --set auth.adminUser=admin \
#  --set auth.adminPassword=admin \
#  --set postgresql.enabled=true \
#  --set service.type=NodePort

helm install keycloak bitnami/keycloak -n keycloak -f keycloak-values.yml \
  --set auth.adminUser=admin \
  --set auth.adminPassword=admin \
  --set postgresql.enabled=true
