# Microservicio: BFF Emergencia (Orquestador)

El Backend for Frontend (BFF) actúa como el **API Gateway** y único punto de entrada para la aplicación React. Su responsabilidad es orquestar las llamadas a los microservicios internos y centralizar la seguridad del sistema.

## Patrones y Arquitectura
- **API Gateway / Orquestador:** Intercepta y enruta las peticiones usando clientes Feign.
- **Filtro JWT:** valida las firmas de los tokens y los roles de usuario antes de permitir el paso a los servicios internos, protegiendo los endpoints.
- **Circuit Breaker (Resilience4j):** implementa tolerancia a fallos. Si un microservicio interno se cae, el BFF intercepta el error y devuelve un *fallback* para evitar que el frontend colapse.

## Instalación y Ejecución
1. Configura los parámetros de la base de datos en el archivo `application.properties`.
2. Compila el proyecto:
   ```bash
   mvn clean install
3. Levantar el servidor
    ```bash
    mvn spring-boot:run 

El servicio se ejecutará por defecto en el puerto **8082**.