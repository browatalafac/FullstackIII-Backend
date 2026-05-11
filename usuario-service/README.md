# Microservicio: Usuario Service

Este microservicio es responsable de gestionar las identidades, credenciales y roles del sistema (Brigadistas, Funcionarios, etc.).

## Características Técnicas
- **Arquitectura en Capas:** separación entre Controllers, Services y Repositories.
- **Patrón DTO:** expone solo la información necesaria (`UsuarioResponseDTO`), asegurando que datos sensibles como contraseñas encriptadas nunca viajen en las respuestas HTTP.

## Instalación y Ejecución
1. Configura los parámetros de la base de datos en el archivo `application.properties`.
2. Compila el proyecto:
   ```bash
   mvn clean install
3. Levantar el servidor
    ```bash
    mvn spring-boot:run 

El servicio se ejecutará por defecto en el puerto **8080**.