from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session

from app.database import engine, Base, get_db
from app import models
from app import crud
from app import schemas
from app import auth

app = FastAPI(title="AlpineBuddy API")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)
app.mount("/static", StaticFiles(directory="static"), name="static")

@app.on_event("startup")
def on_startup():
    Base.metadata.create_all(bind=engine)

@app.get("/")
def root():
    return {"status": "backend alive"}


@app.post("/auth/register", response_model=schemas.UporabnikRead, status_code=status.HTTP_201_CREATED)
def register(user: schemas.UporabnikCreate, db: Session = Depends(get_db)):
    existing = crud.get_uporabnik_by_email(db, user.email)
    if existing:
        raise HTTPException(status_code=400, detail="Email already registered")
    hashed = auth.get_password_hash(user.geslo)
    return crud.create_uporabnik(db, ime=user.ime, email=user.email, geslo_hash=hashed)


@app.post("/auth/token", response_model=schemas.Token)
def login_for_access_token(
    form_data: OAuth2PasswordRequestForm = Depends(),
    db: Session = Depends(get_db),
):
    user = auth.authenticate_user(db, form_data.username, form_data.password)
    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid credentials")

    access_token = auth.create_access_token(data={"sub": user.email})
    return {"access_token": access_token, "token_type": "bearer"}


@app.get("/auth/me", response_model=schemas.UporabnikRead)
def read_current_user(current_user: models.Uporabnik = Depends(auth.get_current_user)):
    return current_user


@app.get("/gorovja", response_model=list[schemas.GorovjeRead])
def list_gorovja(db: Session = Depends(get_db)):
    return crud.get_gorovja(db)


@app.get("/gorovja/{gorovje_id}", response_model=schemas.GorovjeRead)
def read_gorovje(gorovje_id: int, db: Session = Depends(get_db)):
    gorovje = crud.get_gorovje(db, gorovje_id=gorovje_id)
    if gorovje is None:
        raise HTTPException(status_code=404, detail="Gorovje not found")
    return gorovje



@app.get("/gore", response_model=list[schemas.GoraRead])
def list_gore(db: Session = Depends(get_db)):
    return crud.get_gore(db)

@app.get("/gore/{gora_id}", response_model=schemas.GoraRead)
def read_gora(gora_id: int, db: Session = Depends(get_db)):
    gora = crud.get_gora(db, gora_id=gora_id)
    if gora is None:
        raise HTTPException(status_code=404, detail="Gora not found")
    return gora


# ---------- SMERI ----------
@app.get("/smeri", response_model=list[schemas.SmerRead])
def list_smeri(db: Session = Depends(get_db)):
    return crud.get_smeri(db)


@app.get("/smeri/{smer_id}", response_model=schemas.SmerRead)
def get_smer(smer_id: int, db: Session = Depends(get_db)):
    smer = crud.get_smer(db, smer_id)
    if not smer:
        raise HTTPException(status_code=404, detail="Smer not found")
    return smer


@app.get("/stili-smeri", response_model=list[schemas.StilSmeriRead])
def list_stili_smeri(db: Session = Depends(get_db)):
    return crud.get_stili_smeri(db)


@app.get("/stili-smeri/{stil_id}", response_model=schemas.StilSmeriRead)
def get_stil_smeri(stil_id: int, db: Session = Depends(get_db)):
    stil = crud.get_stil_smeri(db, stil_id)
    if not stil:
        raise HTTPException(status_code=404, detail="Stil smeri not found")
    return stil


@app.get("/tezavnosti", response_model=list[schemas.TezavnostRead])
def list_tezavnosti(db: Session = Depends(get_db)):
    return crud.get_tezavnosti(db)


@app.get("/tezavnosti/{tezavnost_id}", response_model=schemas.TezavnostRead)
def get_tezavnost(tezavnost_id: int, db: Session = Depends(get_db)):
    tezavnost = crud.get_tezavnost(db, tezavnost_id)
    if not tezavnost:
        raise HTTPException(status_code=404, detail="Tezavnost not found")
    return tezavnost



# ---------- VZPONI ----------


@app.get("/vzponi", response_model=list[schemas.VzponRead])
def list_vzponi(
    db: Session = Depends(get_db),
    current_user: models.Uporabnik = Depends(auth.get_current_user),
):
    return crud.get_vzponi(db, current_user.uporabnik_id)



@app.post("/vzponi", response_model=schemas.VzponRead)
def create_vzpon(
    vzpon: schemas.VzponCreate,
    db: Session = Depends(get_db),
    current_user: models.Uporabnik = Depends(auth.get_current_user),
):
    return crud.create_vzpon(db, vzpon, current_user.uporabnik_id)


@app.put("/vzponi/{vzpon_id}", response_model=schemas.VzponRead)
def update_vzpon(
    vzpon_id: int,
    vzpon: schemas.VzponUpdate,
    db: Session = Depends(get_db),
    current_user: models.Uporabnik = Depends(auth.get_current_user),
):
    updated = crud.update_vzpon(db, vzpon_id, current_user.uporabnik_id, vzpon)
    if not updated:
        raise HTTPException(status_code=404, detail="Vzpon not found")
    return updated


@app.delete("/vzponi/{vzpon_id}")
def delete_vzpon(
    vzpon_id: int,
    db: Session = Depends(get_db),
    current_user: models.Uporabnik = Depends(auth.get_current_user),
):
    deleted = crud.delete_vzpon(db, vzpon_id, current_user.uporabnik_id)
    if not deleted:
        raise HTTPException(status_code=404, detail="Vzpon not found")
    return {"detail": "Vzpon deleted"}


