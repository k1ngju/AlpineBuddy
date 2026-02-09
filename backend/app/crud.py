from sqlalchemy.orm import Session
from app import models

# ----- GOROVJA ------
def get_gorovja(db: Session) -> list[models.Gorovje]:
    return db.query(models.Gorovje).order_by(models.Gorovje.naziv).all()


def get_gorovje(db: Session, gorovje_id: int) -> models.Gorovje | None:
    return db.query(models.Gorovje).filter(models.Gorovje.gorovje_id == gorovje_id).first()

# ----- GORE ------
def get_gore(db: Session) -> list[models.Gora]:
    return db.query(models.Gora).order_by(models.Gora.ime).all()


def get_gora(db: Session, gora_id: int) -> models.Gora | None:
    return db.query(models.Gora).filter(models.Gora.gora_id == gora_id).first()


# ---------- SMER ----------
def get_smeri(db):
    return db.query(models.Smer).all()


def get_smer(db, smer_id: int):
    return db.query(models.Smer).filter(models.Smer.smer_id == smer_id).first()


# ---------- STIL SMERI ----------
def get_stili_smeri(db: Session) -> list[models.StilSmeri]:
    return db.query(models.StilSmeri).order_by(models.StilSmeri.naziv).all()


def get_stil_smeri(db: Session, stil_id: int) -> models.StilSmeri | None:
    return db.query(models.StilSmeri).filter(models.StilSmeri.stil_id == stil_id).first()


# ---------- TEZAVNOST ----------
def get_tezavnosti(db: Session) -> list[models.Tezavnost]:
    return db.query(models.Tezavnost).order_by(models.Tezavnost.oznaka).all()


def get_tezavnost(db: Session, tezavnost_id: int) -> models.Tezavnost | None:
    return db.query(models.Tezavnost).filter(models.Tezavnost.tezavnost_id == tezavnost_id).first()


# ---------- VZPON ----------
def get_vzpon(db, vzpon_id: int):
    return db.query(models.Vzpon).filter(models.Vzpon.vzpon_id == vzpon_id).first()

def get_vzponi(db, uporabnik_id: int):
    return db.query(models.Vzpon).filter(models.Vzpon.uporabnik_id == uporabnik_id).all()


def get_vzpon_for_user(db, vzpon_id: int, uporabnik_id: int):
    return (
        db.query(models.Vzpon)
        .filter(
            models.Vzpon.vzpon_id == vzpon_id,
            models.Vzpon.uporabnik_id == uporabnik_id,
        )
        .first()
    )


def create_vzpon(db, vzpon, uporabnik_id: int):
    db_vzpon = models.Vzpon(**vzpon.dict(), uporabnik_id=uporabnik_id)
    db.add(db_vzpon)
    db.commit()
    db.refresh(db_vzpon)
    return db_vzpon


def update_vzpon(db, vzpon_id: int, uporabnik_id: int, vzpon):
    db_vzpon = get_vzpon_for_user(db, vzpon_id, uporabnik_id)
    if not db_vzpon:
        return None

    for key, value in vzpon.dict(exclude_unset=True).items():
        setattr(db_vzpon, key, value)

    db.commit()
    db.refresh(db_vzpon)
    return db_vzpon


def delete_vzpon(db, vzpon_id: int, uporabnik_id: int):
    db_vzpon = get_vzpon_for_user(db, vzpon_id, uporabnik_id)
    if not db_vzpon:
        return None

    db.delete(db_vzpon)
    db.commit()
    return db_vzpon


# ---------- UPORABNIK ----------
def get_uporabniki(db):
    return db.query(models.Uporabnik).all()


def get_uporabnik(db, uporabnik_id: int):
    return db.query(models.Uporabnik).filter(
        models.Uporabnik.uporabnik_id == uporabnik_id
    ).first()


def get_uporabnik_by_email(db, email: str):
    return db.query(models.Uporabnik).filter(models.Uporabnik.email == email).first()


def create_uporabnik(db, ime: str, email: str, geslo_hash: str):
    db_uporabnik = models.Uporabnik(ime=ime, email=email, geslo=geslo_hash)
    db.add(db_uporabnik)
    db.commit()
    db.refresh(db_uporabnik)
    return db_uporabnik
