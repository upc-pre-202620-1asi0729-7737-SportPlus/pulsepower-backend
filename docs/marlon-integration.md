# Integracion de shared, physiology y reports

Rama de Marlon: feature/shared-physiology-reports, basada en develop (84809f9).
Los archivos de estos contextos proceden de PulsePower-con-pruebas.zip.
No se incluyen frontend ni los modulos asignados a otros integrantes.

## Validacion

- 29 pruebas unitarias de fisiologia y reportes aprobadas con Java 27.
- Comando reproducible, limitado al dominio de estos modulos:
  `./mvnw -f validation/marlon-domain/pom.xml test`
- La compilacion completa con `./mvnw test` falla actualmente porque develop
  todavia no contiene IAM y wellness. Las pruebas aisladas no sustituyen la
  prueba de integracion ni afirman que esta rama pueda arrancar por si sola.

## Dependencias pendientes antes de integrar y desplegar

- IAM: EmailAddress, UserRepository y la implementacion de CurrentUserProvider.
  La implementacion JWT del ZIP pertenece a IAM y corresponde a su entrega.
  Shared aporta el contrato de usuario autenticado y la documentacion Bearer,
  no una implementacion de autenticacion alternativa.
- Wellness: WellnessQueryService, GetCheckInsByUserIdQuery y WellnessContextFacade.
- Configuracion final de base de datos, esquema y seguridad del equipo.
  El proyecto inicial usa MySQL, Java 27 y Spring Boot 4.1.2; se conservan.
  El ZIP completo se habia probado con PostgreSQL: no se certifica aqui una
  migracion a MySQL ni el despliegue del proyecto inicial.

Cuando se integren las dependencias, ejecutar de nuevo toda la suite y las
pruebas de endpoints. No fusionar esta entrega como una aplicacion terminada.

## Diez commits

1. Shared: excepciones y contrato de usuario autenticado.
2. Shared: persistencia, CORS, errores REST, OpenAPI y validacion.
3. Fisiologia: dominio y calculador de recuperacion.
4. Fisiologia: servicios de aplicacion y carga de demostracion.
5. Fisiologia: persistencia y endpoints, incluido asistente restringido.
6. Fisiologia: pruebas unitarias.
7. Reportes: dominio y validacion de historial.
8. Reportes: servicios de aplicacion e integracion entre contextos.
9. Reportes: persistencia y endpoints REST.
10. Reportes: pruebas, verificacion aislada e instrucciones de integracion.
