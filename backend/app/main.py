from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import Optional


from app.database import engine, Base, get_db
from app import models
from app import crud
from app import schemas

app = FastAPI(title="AlpineBuddy API")

@app.on_event("startup")
def on_startup():
    Base.metadata.create_all(bind=engine)

@app.get("/")
def root():
    return {"status": "backend alive"}


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



# ---------- VZPONI ----------


@app.get("/vzponi", response_model=list[schemas.VzponRead])
def list_vzponi(
    uporabnik_id: Optional[int] = None,
    db: Session = Depends(get_db),
):
    return crud.get_vzponi(db, uporabnik_id)



@app.post("/vzponi", response_model=schemas.VzponRead)
def create_vzpon(vzpon: schemas.VzponCreate, db: Session = Depends(get_db)):
    return crud.create_vzpon(db, vzpon)


@app.put("/vzponi/{vzpon_id}", response_model=schemas.VzponRead)
def update_vzpon(
    vzpon_id: int,
    vzpon: schemas.VzponBase,
    db: Session = Depends(get_db),
):
    updated = crud.update_vzpon(db, vzpon_id, vzpon)
    if not updated:
        raise HTTPException(status_code=404, detail="Vzpon not found")
    return updated


@app.delete("/vzponi/{vzpon_id}")
def delete_vzpon(vzpon_id: int, db: Session = Depends(get_db)):
    deleted = crud.delete_vzpon(db, vzpon_id)
    if not deleted:
        raise HTTPException(status_code=404, detail="Vzpon not found")
    return {"detail": "Vzpon deleted"}


# ---------- UPORABNIKI ----------
@app.get("/uporabniki", response_model=list[schemas.UporabnikRead])
def list_uporabniki(db: Session = Depends(get_db)):
    return crud.get_uporabniki(db)


@app.get("/uporabniki/{uporabnik_id}", response_model=schemas.UporabnikRead)
def get_uporabnik(uporabnik_id: int, db: Session = Depends(get_db)):
    uporabnik = crud.get_uporabnik(db, uporabnik_id)
    if not uporabnik:
        raise HTTPException(status_code=404, detail="Uporabnik not found")
    return uporabnik


