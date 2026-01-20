from __future__ import annotations

from typing import Optional
from sqlalchemy import ForeignKey, Integer, Text, REAL
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.database import Base




class Gora(Base):
    __tablename__ = 'Gora'

    gora_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    ime: Mapped[Optional[str]] = mapped_column(Text)
    gorovje: Mapped[Optional[str]] = mapped_column(Text)
    gps_sirina: Mapped[Optional[float]] = mapped_column(REAL)
    gps_dolzina: Mapped[Optional[float]] = mapped_column(REAL)

    smeri: Mapped[list['Smer']] = relationship('Smer', back_populates='gora')


class Tezavnost(Base):
    __tablename__ = 'Tezavnost'

    tezavnost_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    oznaka: Mapped[Optional[str]] = mapped_column(Text)
    sistem: Mapped[Optional[str]] = mapped_column(Text)
    opis: Mapped[Optional[str]] = mapped_column(Text)

    smeri: Mapped[list['Smer']] = relationship('Smer', back_populates='tezavnost')


class Uporabnik(Base):
    __tablename__ = 'Uporabnik'

    uporabnik_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    ime: Mapped[Optional[str]] = mapped_column(Text)
    geslo: Mapped[Optional[str]] = mapped_column(Text)
    email: Mapped[Optional[str]] = mapped_column(Text)

    vzponi: Mapped[list['Vzpon']] = relationship('Vzpon', back_populates='uporabnik')


class Smer(Base):
    __tablename__ = 'Smer'

    smer_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    gora_id: Mapped[Optional[int]] = mapped_column(ForeignKey('Gora.gora_id'))
    tezavnost_id: Mapped[Optional[int]] = mapped_column(ForeignKey('Tezavnost.tezavnost_id'))
    ime: Mapped[Optional[str]] = mapped_column(Text)
    dolzina_m: Mapped[Optional[int]] = mapped_column(Integer)
    topo_url: Mapped[Optional[str]] = mapped_column(Text)
    opis: Mapped[Optional[str]] = mapped_column(Text)

    gora: Mapped[Optional['Gora']] = relationship('Gora', back_populates='smeri')
    tezavnost: Mapped[Optional['Tezavnost']] = relationship('Tezavnost', back_populates='smeri')
    vzponi: Mapped[list['Vzpon']] = relationship('Vzpon', back_populates='smer')


class Vzpon(Base):
    __tablename__ = 'Vzpon'

    vzpon_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    uporabnik_id: Mapped[Optional[int]] = mapped_column(ForeignKey('Uporabnik.uporabnik_id'))
    smer_id: Mapped[Optional[int]] = mapped_column(ForeignKey('Smer.smer_id'))
    datum: Mapped[Optional[str]] = mapped_column(Text)
    slog: Mapped[Optional[str]] = mapped_column(Text)
    razmere: Mapped[Optional[str]] = mapped_column(Text)
    partnerji: Mapped[Optional[str]] = mapped_column(Text)
    opombe: Mapped[Optional[str]] = mapped_column(Text)
    uspesen: Mapped[Optional[int]] = mapped_column(Integer)
    cas_trajanja: Mapped[Optional[int]] = mapped_column(Integer)

    smer: Mapped[Optional['Smer']] = relationship('Smer', back_populates='vzponi')
    uporabnik: Mapped[Optional['Uporabnik']] = relationship('Uporabnik', back_populates='vzponi')
