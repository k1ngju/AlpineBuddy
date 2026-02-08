# AlpineBuddy


Pregledni katalog alpinističnih smeri in dnevnik alpističnih vzponov. Projekt uporablja REST API z dvema odjemalcema: Spletna aplikacija
in mobilna Android aplikacija

## Architecture
- Clients: web app and Android app.
- Backend: FastAPI + SQLAlchemy.
- Database: SQLite (local file created on startup).
- Communication: REST API over HTTP.

```
Web / Android
		|
		v
FastAPI (REST)
		|
		v
SQLite
```

## Data Model
Reference data is read-only through the API. Users can only create and manage ascents.

Entities and relations:
- Gorovje (mountain range)
	- gorovje_id, naziv, opis
	- 1 -> N to Gora
- Gora (mountain)
	- gora_id, gorovje_id, ime, slika_url, gps_sirina, gps_dolzina
	- N -> 1 to Gorovje
	- 1 -> N to Smer
- Tezavnost (grade)
	- tezavnost_id, oznaka, sistem, opis
	- 1 -> N to Smer
- StilSmeri (route style)
	- stil_id, naziv, opis
	- 1 -> N to Smer
- Smer (route)
	- smer_id, gora_id, tezavnost_id, stil_id, ime, dolzina_m, opis, skica_url_1, skica_url_2
	- N -> 1 to Gora, Tezavnost, StilSmeri
	- 1 -> N to Vzpon
- Uporabnik (user)
	- uporabnik_id, ime, email, geslo
	- 1 -> N to Vzpon
- Vzpon (ascent)
	- vzpon_id, uporabnik_id, smer_id, datum, slog, razmere, partnerji, opombe, uspesen, cas_trajanja
	- N -> 1 to Uporabnik, Smer
	- datum is a date; uspesen is boolean; cas_trajanja is in hours
- geslo is stored as a hash

## API Documentation
Base URL: `http://127.0.0.1:8000`

### Health
- GET `/` -> status check

### Gorovja (read-only)
- GET `/gorovja`
- GET `/gorovja/{gorovje_id}`

### Gore (read-only)
- GET `/gore`
- GET `/gore/{gora_id}`

### Smeri (read-only)
- GET `/smeri`
- GET `/smeri/{smer_id}`

### Stili smeri (read-only)
- GET `/stili-smeri`
- GET `/stili-smeri/{stil_id}`

### Auth
- POST `/auth/register`
- POST `/auth/token`
- GET `/auth/me`

### Vzponi (user data)
- GET `/vzponi`
- POST `/vzponi`
- PUT `/vzponi/{vzpon_id}`
- DELETE `/vzponi/{vzpon_id}`

## Running the Backend
1) Create and activate a virtual environment
2) Install dependencies
```
python -m pip install -r requirements.txt
```

3) Start the API
```
cd backend
python -m uvicorn app.main:app --reload
```

The SQLite database file `alpinebuddy.db` is created on startup in the `backend` folder.

### Local images (static)
For a proof-of-concept, store images locally and expose them via `/static`.

- Put mountain images in `backend/static/gore/`
- Put route sketches in `backend/static/smeri/`
- Store URLs in the DB, for example:
	- `/static/gore/triglav.jpg`
	- `/static/smeri/slovenska-1.jpg`
	- `/static/smeri/slovenska-2.jpg`

### Seed reference data
Example seed script (gorovja, tezavnosti, stili):
```
python backend/scripts/seed.py
```

### Secret key config
Set the JWT secret via environment variable before starting the API:

PowerShell:
```
$env:ALPINEBUDDY_SECRET_KEY = "change-this-in-dev"
```

### Auth flow check (example)
1) Register
```
curl -X POST http://127.0.0.1:8000/auth/register -H "Content-Type: application/json" -d '{"ime":"Test User","email":"test@example.com","geslo":"strongpass123"}'
```

2) Login (token)
```
curl -X POST http://127.0.0.1:8000/auth/token -H "Content-Type: application/x-www-form-urlencoded" -d "username=test@example.com&password=strongpass123"
```

3) Access protected endpoint
```
curl -H "Authorization: Bearer <TOKEN>" http://127.0.0.1:8000/vzponi
```

## Notes
- Reference data (gorovja, gore, smeri, tezavnosti, stili) is managed outside the public API.
- JWT authentication is required for all Vzpon endpoints.
