# Erişim ve Port Forward

## 1) Uygulamaya Port Forward ile erişim

```bash
kubectl get pods -n kafka-lab
kubectl port-forward svc/order 8090:8090 -n kafka-lab
kubectl port-forward svc/consumer 8095:8095 -n kafka-lab
kubectl port-forward svc/kafka-ui 8080:8080 -n kafka-lab
kubectl port-forward svc/prometheus 9090:9090 -n kafka-lab
kubectl port-forward svc/grafana 3000:3000 -n kafka-lab
kubectl port-forward svc/kafka 9092:9092 -n kafka-lab
```

- Order: http://localhost:8090/orders/add?orderId=ORDER-1001&status=CREATED
- Consumer: http://localhost:8095/actuator/health
- Kafka UI: http://localhost:8080
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin / admin)
- Kafka broker: localhost:9092 (for local admin tools)

## 2) Ingress (opsiyonel)

Rancher Desktop içinde Traefik varsa, aşağıdaki örnek ingress kullanılabilir:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: kafka-lab
  namespace: kafka-lab
spec:
  rules:
    - host: kafka.local
      http:
        paths:
          - path: /order
            pathType: Prefix
            backend:
              service:
                name: order
                port:
                  number: 8090
          - path: /kafka-ui
            pathType: Prefix
            backend:
              service:
                name: kafka-ui
                port:
                  number: 8080
          - path: /grafana
            pathType: Prefix
            backend:
              service:
                name: grafana
                port:
                  number: 3000
```

Doğrudan Ingress kullanmak yerine `port-forward` daha güvenli ve daha net bir eğitim akışı sağlar.
