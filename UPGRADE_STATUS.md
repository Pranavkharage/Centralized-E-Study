# Upgrade status — Phase 3

## Included (on top of Phase 2)
- AI quiz per study topic: the backend sends the topic's own theory/example/code to the Gemini REST API and stores 10 generated questions per topic (`quiz_banks`). Later quizzes on that topic reuse them (saves free-tier quota).
- Each quiz picks 5 random questions. Correct answers stay on the server; grading happens on `POST /api/quiz/submit`.
- Study progress: "Mark as studied" per topic (`study_progress`).
- Quiz history per student (`quiz_attempts`).
- React: progress bars on subject cards, ticks on studied topics, stats strip on the dashboard, quiz popup with review + explanations, and a Profile & progress page (stats, per-subject progress, recent results).
- First admin account is created at startup from `ADMIN_USERNAME` / `ADMIN_PASSWORD` (skipped if the user exists).
- `JWT_SECRET` is now a normal string of 32+ characters (Base64 no longer required).
- Errors from the new endpoints use the same `{success, message}` shape.

## Not included yet
- Admin screen to add/edit subjects and topics (still `POST /api/subjects/add` with an ADMIN token).
- Extra profile fields (name, email, branch, year), token refresh/revocation, automated tests, CI.

## Important
- Not compiled with Maven here (Maven Central is unreachable from the build sandbox). The React app was built successfully with `npm run build`. Run `mvn clean package` locally first.
- Set `GEMINI_API_KEY` on Render. Without it the app works normally and the quiz shows a friendly "not configured" message.
- Gemini model names change; if quizzes start failing with "AI service rejected the request", set `GEMINI_MODEL` to a current model from https://ai.google.dev/gemini-api/docs/models.
