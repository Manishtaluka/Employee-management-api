# Employee Management REST API

Spring Boot 3 · Java 17 · Spring Data JPA · MySQL

## Run

1. Start MySQL (the database `employee_db` is created automatically).
2. Set credentials in `src/main/resources/application.properties`, or via env vars:
   `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (defaults: `root` / `root` on `localhost:3306`).
3. `mvn spring-boot:run` → API at `http://localhost:8080` 

4. `mvn spring-boot:run "-Dspring-boot.run.profiles=local"`
  Spring loads application.properties first and then overrides the password from the local file. The tests are unaffected, because they use H2.



## Endpoints

| Method | URL | Description | Success |
|--------|-----|-------------|---------|
| POST   | `/api/employees` | Create employee | 201 + `Location` header |
| GET    | `/api/employees` | List all employees | 200 |
| GET    | `/api/employees?department=Engineering` | Search by department (case-insensitive) | 200 |
| GET    | `/api/employees/{id}` | Get by ID | 200 |
| PUT    | `/api/employees/{id}` | Update employee | 200 |
| DELETE | `/api/employees/{id}` | Delete employee | 204 |

## Employee JSON

```json
{
  "name": "Asha Rao",
  "email": "asha@corp.com",
  "department": "Engineering",
  "salary": 75000.50,
  "designation": "Software Engineer"
}
```

Validation: all fields required; `name` 2–100 chars; `email` valid format; `salary` > 0 (max 2 decimals).

## Error responses

| Status | When |
|--------|------|
| 400 | Validation failure, malformed JSON, or bad parameter type (e.g. `/api/employees/abc`) |
| 404 | Employee ID does not exist |
| 409 | Email already used by another employee (case-insensitive) |
| 500 | Unexpected error (details are logged, not leaked) |

Example (400):

```json
{
  "timestamp": "2026-11-21T21:58:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/employees",
  "fieldErrors": { "email": "Email must be a valid email address", "salary": "Salary must be greater than zero" }
}
```

## Try it

```bash
curl -i -X POST localhost:8080/api/employees -H 'Content-Type: application/json' \
  -d '{"name":"Asha Rao","email":"asha@corp.com","department":"Engineering","salary":75000.50,"designation":"Software Engineer"}'

curl 'localhost:8080/api/employees?department=engineering'
```

## Design notes

- Emails are trimmed and lower-cased; uniqueness is enforced by a service-level check **and** a DB unique constraint (a race-condition conflict is also mapped to 409).
- Request/response DTOs keep the entity (and the client-controlled `id`) out of the API surface.
- `salary` uses `BigDecimal` / `DECIMAL(12,2)` to avoid floating-point errors.
