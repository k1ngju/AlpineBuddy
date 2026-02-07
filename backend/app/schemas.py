from __future__ import annotations
from datetime import date
from typing import Optional, List
from pydantic import BaseModel, EmailStr


# ---------- GOROVJE ----------
class GorovjeBase(BaseModel):
    naziv: Optional[str] = None
    opis: Optional[str] = None


class GorovjeRead(GorovjeBase):
    gorovje_id: int

    class Config:
        from_attributes = True


# ---------- GORA ----------
class GoraBase(BaseModel):
    ime: Optional[str] = None
    gorovje_id: Optional[int] = None
    gps_sirina: Optional[float] = None
    gps_dolzina: Optional[float] = None


class GoraCreate(GoraBase):
    ime: str  # required
    gorovje_id: int


class GoraRead(GoraBase):
    gora_id: int

    class Config:
        from_attributes = True


# ---------- TEZAVNOST ----------
class TezavnostBase(BaseModel):
    oznaka: Optional[str] = None
    sistem: Optional[str] = None
    opis: Optional[str] = None


class TezavnostCreate(TezavnostBase):
    oznaka: str


class TezavnostRead(TezavnostBase):
    tezavnost_id: int

    class Config:
        from_attributes = True


# ---------- STIL SMERI ----------
class StilSmeriBase(BaseModel):
    naziv: Optional[str] = None
    opis: Optional[str] = None


class StilSmeriRead(StilSmeriBase):
    stil_id: int

    class Config:
        from_attributes = True


# ---------- SMER ----------
class SmerBase(BaseModel):
    gora_id: Optional[int] = None
    tezavnost_id: Optional[int] = None
    stil_id: Optional[int] = None
    ime: Optional[str] = None
    dolzina_m: Optional[int] = None
    topo_url: Optional[str] = None
    opis: Optional[str] = None


class SmerRead(SmerBase):
    smer_id: int

    class Config:
        from_attributes = True


# ---------- UPORABNIK ----------
class UporabnikBase(BaseModel):
    ime: Optional[str] = None
    email: Optional[EmailStr] = None


class UporabnikCreate(UporabnikBase):
    ime: str
    email: EmailStr
    geslo: str  # plain for now; later we hash


class UporabnikRead(UporabnikBase):
    uporabnik_id: int

    class Config:
        from_attributes = True


# ---------- VZPON ----------
class VzponBase(BaseModel):
    uporabnik_id: Optional[int] = None
    smer_id: Optional[int] = None
    datum: Optional[date] = None
    slog: Optional[str] = None
    razmere: Optional[str] = None
    partnerji: Optional[str] = None
    opombe: Optional[str] = None
    uspesen: Optional[bool] = None
    cas_trajanja: Optional[int] = None


class VzponCreate(VzponBase):
    uporabnik_id: int
    smer_id: int


class VzponRead(VzponBase):
    vzpon_id: int

    class Config:
        from_attributes = True
