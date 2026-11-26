# Centralized E-Study Platform — Upgraded Version

Existing Spring Boot + MySQL + MongoDB application upgraded with a React (JavaScript) frontend, environment-based cloud configuration, and BCrypt password storage, JWT login tokens, and role-based API authorization.

## Stack
- Java 21, Spring Boot 3.2.0, Maven
- React + JavaScript + Vite + CSS
- MySQL (existing Aiven service) for student accounts
- MongoDB (existing Atlas cluster) for study subjects and topics
- Spring Security + JWT for stateless authentication and ADMIN/STUDENT roles
- Postman for manual API checks
- Docker multi-stage build for Render

## What is included in this phase
- React login/register screen and responsive study dashboard.
- Existing `/api/auth/register`, `/api/auth/login`, `/api/subjects/all`, and `/api/subjects/{subjectName}` API contracts retained.
- New registrations are stored using BCrypt hashes and receive the STUDENT role.
- Login returns a signed JWT access token and the user's role.
- Requests to protected APIs must include `Authorization: Bearer <token>`.
- `POST /api/subjects/add` requires ADMIN; public subject GET endpoints remain available.
- Existing plaintext-password accounts are upgraded to BCrypt automatically after their first successful login. This avoids forcing all existing users to reset passwords.
- Existing MongoDB subject model and `DataSeeder` content retained. Seeder inserts the five starter subjects only when the configured MongoDB `subjects` collection is empty.
- Database credentials are read from environment variables; no production secrets are included in this repository.
- Docker builds the React frontend and packages it with Spring Boot.

## Cloud deployment — existing Render service
The current deployment document says these environment variable names are already configured in Render. Keep their existing values:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATA_MONGODB_URI`

`application.properties` now reads those exact variable names. It does not contain your live passwords or Atlas URI. Do not paste credentials into source files or GitHub.

For Render, keep the existing Docker runtime and deploy the updated repository. Render should build using the root `Dockerfile`. The application listens on `${PORT:8080}`.

## Local development
1. Install JDK 21, Maven, Node.js 20+ and npm.
2. Create local MySQL database `estudy` and start local MongoDB, or provide your own local connection values.
3. Copy `.env.example` values into your terminal environment (do not commit a real `.env`).
4. Start backend: `mvn spring-boot:run` from the project root.
5. In another terminal: `cd frontend`, `npm install`, `npm run dev`.
6. Open the Vite URL shown in the terminal. Vite proxies `/api` requests to `http://localhost:8080`.

Alternatively, build the complete deployment image with `docker build -t centralized-estudy .` and run it with the required environment variables.

## Existing API endpoints (Postman)
| Method | URL | Body / purpose |
|---|---|---|
| POST | `/api/auth/register` | `{"username":"student1","password":"at-least-8-chars"}` |
| POST | `/api/auth/login` | `{"username":"student1","password":"your-password"}` |
| GET | `/api/subjects/all` | List subject names |
| GET | `/api/subjects/OOP` | Fetch complete subject content |
| POST | `/api/subjects/add` | Add subject JSON (existing endpoint) |

## Important migration and safety notes
- Back up Aiven MySQL and MongoDB Atlas before deploying.
- The `users.password` column was increased to 100 characters because BCrypt hashes are longer than the old plaintext values. Hibernate `ddl-auto=update` is retained for this incremental phase.
- When an existing user successfully logs in with their old password, the service replaces the plaintext value with a BCrypt hash. Failed logins do not change stored passwords.
- This phase does not yet implement profiles, learning progress, quizzes, AI content generation or an admin dashboard.
- The existing `/api/subjects/add` endpoint is not protected by admin authorization yet. Do not expose it as a public production admin feature until authorization is added.
- JWT is stored in sessionStorage by the React frontend and sent in the Authorization header. Logout clears the browser's token; it does not revoke a token already copied elsewhere.


## Phase 2 — JWT and roles

### Render environment variable
Add `JWT_SECRET` with a strong Base64-encoded secret (at least 32 decoded bytes). Keep the value private. Optional: `JWT_EXPIRATION_MS` (default 86400000 = 24 hours).

### Existing MySQL database migration
Before deploying, export a backup of Aiven MySQL, then run the SQL in `database/migrations/V2__add_user_roles.sql`. The migration assigns existing users the `STUDENT` role. Promote only your verified admin account using the commented UPDATE statement in that file.

### Postman checks
1. `POST /api/auth/register` with `{"username":"student1","password":"password123"}` — creates a STUDENT.
2. `POST /api/auth/login` with the same credentials — response `data.accessToken` contains the JWT and `data.role` is `STUDENT`.
3. `GET /api/subjects/all` — remains public for the current app.
4. `POST /api/subjects/add` without a token — expect HTTP 401.
5. `POST /api/subjects/add` with a STUDENT token — expect HTTP 403.
6. Promote a verified account to ADMIN in MySQL, log in again, then repeat the POST with its new token — authorized.

For protected requests, set Postman Authorization type to Bearer Token and paste `data.accessToken`.

Import `postman/E-Study-Phase2.postman_collection.json` into Postman. Run Register, then Login (the collection automatically saves the JWT), then test subject endpoints. The Add Subject request requires an ADMIN token.


## Phase 3 — AI quiz, progress and profile

| Method | URL | Purpose |
|---|---|---|
| POST | `/api/quiz/start` | `{subject, topicName, count}` -> questions (no answers) |
| POST | `/api/quiz/submit` | `{attemptId, answers:[0,2,1,...]}` -> score + review |
| POST | `/api/progress/topic` | `{subject, topicName}` -> mark topic studied |
| GET | `/api/progress/summary` | progress per subject + quiz stats + recent attempts |

All four need a Bearer token. New Render environment variables: `GEMINI_API_KEY` (required for quizzes), `GEMINI_MODEL` (optional), plus `JWT_SECRET`, `ADMIN_USERNAME`, `ADMIN_PASSWORD` if not already set.

New MongoDB collections are created automatically: `quiz_banks`, `quiz_attempts`, `study_progress`. MySQL needs no change beyond Phase 2.
