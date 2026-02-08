from __future__ import annotations

from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
if str(ROOT) not in sys.path:
    sys.path.append(str(ROOT))

from app.database import Base, SessionLocal, engine
from app import models


def get_or_create(session, model, defaults=None, **filters):
    existing = session.query(model).filter_by(**filters).first()
    if existing:
        return existing
    params = {**filters, **(defaults or {})}
    obj = model(**params)
    session.add(obj)
    return obj


def main() -> None:
    Base.metadata.create_all(bind=engine)
    db = SessionLocal()
    try:
        # Gorovja
        julijske = get_or_create(db, models.Gorovje, naziv="Julijske Alpe")
        kamniske = get_or_create(db, models.Gorovje, naziv="Kamnisko-Savinjske Alpe")
        karavanke = get_or_create(db, models.Gorovje, naziv="Karavanke")
        db.flush()

        # Tezavnosti (example)
        t_i = get_or_create(db, models.Tezavnost, oznaka="I")
        t_ii = get_or_create(db, models.Tezavnost, oznaka="II")
        t_iii = get_or_create(db, models.Tezavnost, oznaka="III")
        t_iv = get_or_create(db, models.Tezavnost, oznaka="IV")
        db.flush()

        # Stili smeri (example)
        stil_skalna = get_or_create(db, models.StilSmeri, naziv="Skalna")
        stil_ledna = get_or_create(db, models.StilSmeri, naziv="Ledna")
        stil_zimska = get_or_create(db, models.StilSmeri, naziv="Zimska")
        db.flush()

        # Gore (with local image URLs)
        triglav = get_or_create(
            db,
            models.Gora,
            ime="Triglav",
            defaults={"gorovje_id": julijske.gorovje_id, "slika_url": "/static/gore/triglav.jpg"},
        )
        grintovec = get_or_create(
            db,
            models.Gora,
            ime="Grintovec",
            defaults={"gorovje_id": kamniske.gorovje_id, "slika_url": "/static/gore/grintovec.jpg"},
        )
        bela_pec = get_or_create(
            db,
            models.Gora,
            ime="Bela Pec",
            defaults={"gorovje_id": karavanke.gorovje_id, "slika_url": "/static/gore/bela-pec.jpg"},
        )
        db.flush()

        # Smeri
        slovenska = get_or_create(
            db,
            models.Smer,
            ime="Slovenska smer",
            defaults={
                "gora_id": triglav.gora_id,
                "tezavnost_id": t_iv.tezavnost_id,
                "stil_id": stil_skalna.stil_id,
                "skica_url_1": "/static/smeri/slovenska-1.jpg",
                "skica_url_2": "/static/smeri/slovenska-2.jpg",
            },
        )
        grintovcev = get_or_create(
            db,
            models.Smer,
            ime="Grintovcev steber",
            defaults={
                "gora_id": grintovec.gora_id,
                "tezavnost_id": t_iii.tezavnost_id,
                "stil_id": stil_skalna.stil_id,
                "skica_url_1": "/static/smeri/grintovcev-1.jpg",
            },
        )
        avrikljev = get_or_create(
            db,
            models.Smer,
            ime="Avrikljev steber",
            defaults={
                "gora_id": bela_pec.gora_id,
                "tezavnost_id": t_ii.tezavnost_id,
                "stil_id": stil_zimska.stil_id,
                "skica_url_1": "/static/smeri/avrikljev-1.jpg",
            },
        )
        db.flush()

        db.commit()
    finally:
        db.close()


if __name__ == "__main__":
    main()
