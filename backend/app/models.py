from __future__ import annotations

from typing import Optional
from sqlalchemy import Boolean, Date, ForeignKey, Integer, Text, REAL
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.database import Base




class Gorovje(Base):
    __tablename__ = 'Gorovje'

    gorovje_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    naziv: Mapped[str] = mapped_column(Text, nullable=False, unique=True, index=True)
    opis: Mapped[Optional[str]] = mapped_column(Text)

    gore: Mapped[list['Gora']] = relationship('Gora', back_populates='gorovje')


class Gora(Base):
    __tablename__ = 'Gora'

    gora_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    gorovje_id: Mapped[int] = mapped_column(ForeignKey('Gorovje.gorovje_id'), nullable=False)
    ime: Mapped[str] = mapped_column(Text, nullable=False, index=True)
    slika_url: Mapped[Optional[str]] = mapped_column(Text)
    gps_sirina: Mapped[Optional[float]] = mapped_column(REAL)
    gps_dolzina: Mapped[Optional[float]] = mapped_column(REAL)

    gorovje: Mapped[Optional['Gorovje']] = relationship('Gorovje', back_populates='gore')
    smeri: Mapped[list['Smer']] = relationship('Smer', back_populates='gora')


class Tezavnost(Base):
    __tablename__ = 'Tezavnost'

    tezavnost_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    oznaka: Mapped[str] = mapped_column(Text, nullable=False)
    sistem: Mapped[Optional[str]] = mapped_column(Text)
    opis: Mapped[Optional[str]] = mapped_column(Text)

    smeri: Mapped[list['Smer']] = relationship('Smer', back_populates='tezavnost')


class StilSmeri(Base):
    __tablename__ = 'StilSmeri'

    stil_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    naziv: Mapped[str] = mapped_column(Text, nullable=False, unique=True, index=True)
    opis: Mapped[Optional[str]] = mapped_column(Text)

    smeri: Mapped[list['Smer']] = relationship('Smer', back_populates='stil')


class Uporabnik(Base):
    __tablename__ = 'Uporabnik'

    uporabnik_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    ime: Mapped[str] = mapped_column(Text, nullable=False)
    geslo: Mapped[str] = mapped_column(Text, nullable=False)
    email: Mapped[str] = mapped_column(Text, nullable=False, unique=True, index=True)

    vzponi: Mapped[list['Vzpon']] = relationship('Vzpon', back_populates='uporabnik')


class Smer(Base):
    __tablename__ = 'Smer'

    smer_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    gora_id: Mapped[int] = mapped_column(ForeignKey('Gora.gora_id'), nullable=False)
    tezavnost_id: Mapped[int] = mapped_column(ForeignKey('Tezavnost.tezavnost_id'), nullable=False)
    stil_id: Mapped[int] = mapped_column(ForeignKey('StilSmeri.stil_id'), nullable=False)
    ime: Mapped[str] = mapped_column(Text, nullable=False)
    dolzina_m: Mapped[Optional[int]] = mapped_column(Integer)
    opis: Mapped[Optional[str]] = mapped_column(Text)
    skica_url_1: Mapped[Optional[str]] = mapped_column(Text)
    skica_url_2: Mapped[Optional[str]] = mapped_column(Text)

    gora: Mapped[Optional['Gora']] = relationship('Gora', back_populates='smeri')
    tezavnost: Mapped[Optional['Tezavnost']] = relationship('Tezavnost', back_populates='smeri')
    stil: Mapped[Optional['StilSmeri']] = relationship('StilSmeri', back_populates='smeri')
    vzponi: Mapped[list['Vzpon']] = relationship('Vzpon', back_populates='smer')


class Vzpon(Base):
    __tablename__ = 'Vzpon'

    vzpon_id: Mapped[int] = mapped_column(Integer, primary_key=True)
    uporabnik_id: Mapped[int] = mapped_column(ForeignKey('Uporabnik.uporabnik_id'), nullable=False)
    smer_id: Mapped[int] = mapped_column(ForeignKey('Smer.smer_id'), nullable=False)
    datum: Mapped[Date] = mapped_column(Date, nullable=False)
    slog: Mapped[Optional[str]] = mapped_column(Text)
    razmere: Mapped[Optional[str]] = mapped_column(Text)
    partnerji: Mapped[Optional[str]] = mapped_column(Text)
    opombe: Mapped[Optional[str]] = mapped_column(Text)
    uspesen: Mapped[Optional[bool]] = mapped_column(Boolean)
    cas_trajanja: Mapped[Optional[int]] = mapped_column(Integer)

    smer: Mapped[Optional['Smer']] = relationship('Smer', back_populates='vzponi')
    uporabnik: Mapped[Optional['Uporabnik']] = relationship('Uporabnik', back_populates='vzponi')
