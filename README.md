# Spring-Boot

Simple application with Spring Boot.

Application has **Swagger** integration (available at `/swagger-ui/index.html`).

Application has 3 profiles:
- default (no action needed): it aims to the postgresql database;
- local (to activate use `spring.profiles.active=local`): it aims to the in memmory database;
- dump (to activate use `spring.profiles.active=dump`, could be used in pair with `default` or `local` profiles): it adds database prepopulation feature.

Application has 2 ways how to authorize user:
- Use `api/v1/login` with username and password;
- Use GitHub login;

Application has 4 endpoints:
- `api/v1/login` - gives access JWT token to the user based on the credentials;
- `api/v1/logged` - returns token that was passed to the query argument `token` (it's used for internal purposes, application redirects user after he was logged in using social applications, such as GitHub);
- `api/v1/user/me` - returns currently logged user json payload (**secured**);
- `api/v1/admin/test` - returns string that user has an admin authority, if user doesn't have admin authority - `403` will be returned (**secured**); 
