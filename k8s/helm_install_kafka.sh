helm upgrade --install my-kafka oci://registry-1.docker.io/bitnamicharts/kafka \
 -n kafka \
 -f kafka-values.yml \
 --set global.storageClass=hostpath \
 --set image.registry=docker.io \
 --set controller.replicaCount=1 \
 --set image.repository=bitnamilegacy/kafka \
 --set image.tag=4.0.0-debian-12-r10
