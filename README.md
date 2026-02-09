# AlpineBuddy

Pregledni katalog alpinisticnih smeri in dnevnik alpinsticnih vzponov. Projekt uporablja REST API z dvema odjemalcema: spletna aplikacija in mobilna Android aplikacija.

## Arhitektura
- Odjemalci: spletna aplikacija in Android aplikacija.
- Zaledje: FastAPI + SQLAlchemy.
- Podatkovna baza: SQLite (lokalna datoteka, ustvari se ob zagonu).
- Komunikacija: REST API prek HTTP.

```
Web / Android
		|
		v
FastAPI (REST)
		|
		v
SQLite
```

## Shema podatkov

<img width="796" height="419" alt="image" src="https://github.com/user-attachments/assets/49988276-5cd6-4858-a118-9d2c108c12a5" />


## API dokumentacija
Osnovni URL: `http://127.0.0.1:8000`

### Health
- GET `/` -> status check

### Gorovja
- GET `/gorovja`
- GET `/gorovja/{gorovje_id}`

### Gore 
- GET `/gore`
- GET `/gore/{gora_id}`

### Smeri 
- GET `/smeri`
- GET `/smeri/{smer_id}`

### Stili smeri 
- GET `/stili-smeri`
- GET `/stili-smeri/{stil_id}`

### Auth
- POST `/auth/register`
- POST `/auth/token`
- GET `/auth/me`

### Vzponi (uporabniski podatki)
- GET `/vzponi`
- POST `/vzponi`
- PUT `/vzponi/{vzpon_id}`
- DELETE `/vzponi/{vzpon_id}`

## Zagon zaledja
1) Ustvari in aktiviraj virtualno okolje
2) Namesti odvisnosti
```
python -m pip install -r requirements.txt
```

3) Zazeni API
```
cd backend
python -m uvicorn app.main:app --reload
```

SQLite datoteka `alpinebuddy.db` se ustvari ob zagonu v mapi `backend`.

### Lokalne slike (static)
Za proof-of-concept se slike shranijo lokalno in izpostavijo prek `/static`.

- Slike gora shrani v `backend/static/gore/`
- Skice smeri shrani v `backend/static/smeri/`
- URL-je shrani v bazo, na primer:
	- `/static/gore/triglav.jpg`
	- `/static/smeri/slovenska-1.jpg`
	- `/static/smeri/slovenska-2.jpg`

### Seed referencnih podatkov
Primer seed skripte (gorovja, tezavnosti, stili):
```
python backend/scripts/seed.py
```

### Konfiguracija skrivnega kljuca
JWT skrivni kljuc nastavi prek okoljske spremenljivke pred zagonom API:

PowerShell:
```
$env:ALPINEBUDDY_SECRET_KEY = "change-this-in-dev"
```

### Preverjanje auth toka s curl
1) Registracija
```
curl -X POST http://127.0.0.1:8000/auth/register -H "Content-Type: application/json" -d '{"ime":"Test User","email":"test@example.com","geslo":"strongpass123"}'
```

2) Prijava (token)
```
curl -X POST http://127.0.0.1:8000/auth/token -H "Content-Type: application/x-www-form-urlencoded" -d "username=test@example.com&password=strongpass123"
```

3) Dostop do zascitenega endpointa
```
curl -H "Authorization: Bearer <TOKEN>" http://127.0.0.1:8000/vzponi
```

