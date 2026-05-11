# Microservicio: Reporte Service
Este es el microservicio central del negocio. Gestiona toda la lógica relacionada con el ciclo de vida de una emergencia, desde su creación por un ciudadano hasta su actualización de estado por parte de un brigadista.

## Características Técnicas
- **Desacoplamiento:** totalmente independiente del servicio de usuarios, lo que permite escalar sus instancias en caso de un aumento masivo de reportes durante una emergencia forestal.
- **Protección de Datos:** implementa validaciones en la capa de servicio para evitar modificaciones no autorizadas mediante el uso estricto de DTOs (`ReporteUpdateDTO`).

## Instalación y Ejecución
1. Configura los parámetros de la base de datos en el archivo `application.properties`.
2. Compila el proyecto:
   ```bash
   mvn clean install
3. Levantar el servidor
    ```bash
    mvn spring-boot:run 

El servicio se ejecutará por defecto en el puerto **8081**.