helm upgrade --install my-pg oci://registry-1.docker.io/bitnamicharts/postgresql \
 -n db \
 --set auth.postgresPassword=secret \
 --set primary.persistence.size=8Gi \
 --set image.registry=docker.io \
 --set image.repository=bitnamilegacy/postgresql \
 --set image.tag=17.6.0-debian-12-r4
