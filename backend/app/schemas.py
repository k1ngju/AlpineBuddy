from __future__ import annotations
from datetime import date
from typing import Optional, List
from pydantic import BaseModel, EmailStr, Field


# ---------- AUTH ----------
class Token(BaseModel):
    access_token: str
    token_type: str


# ---------- GOROVJE ----------
class GorovjeBase(BaseModel):
    naziv: Optional[str] = Field(default=None, min_length=1, max_length=200)
    opis: Optional[str] = Field(default=None, max_length=2000)


class GorovjeRead(GorovjeBase):
    gorovje_id: int

    class Config:
        from_attributes = True


# ---------- GORA ----------
class GoraBase(BaseModel):
    ime: Optional[str] = Field(default=None, min_length=1, max_length=200)
    gorovje_id: Optional[int] = Field(default=None, gt=0)
    slika_url: Optional[str] = Field(default=None, max_length=500)
    gps_sirina: Optional[float] = Field(default=None, ge=-90, le=90)
    gps_dolzina: Optional[float] = Field(default=None, ge=-180, le=180)


class GoraCreate(GoraBase):
    ime: str = Field(min_length=1, max_length=200)
    gorovje_id: int = Field(gt=0)


class GoraRead(GoraBase):
    gora_id: int

    class Config:
        from_attributes = True


# ---------- TEZAVNOST ----------
class TezavnostBase(BaseModel):
    oznaka: Optional[str] = Field(default=None, min_length=1, max_length=50)
    sistem: Optional[str] = Field(default=None, max_length=50)
    opis: Optional[str] = Field(default=None, max_length=2000)


class TezavnostCreate(TezavnostBase):
    oznaka: str = Field(min_length=1, max_length=50)


class TezavnostRead(TezavnostBase):
    tezavnost_id: int

    class Config:
        from_attributes = True


# ---------- STIL SMERI ----------
class StilSmeriBase(BaseModel):
    naziv: Optional[str] = Field(default=None, min_length=1, max_length=100)
    opis: Optional[str] = Field(default=None, max_length=2000)


class StilSmeriRead(StilSmeriBase):
    stil_id: int

    class Config:
        from_attributes = True


# ---------- SMER ----------
class SmerBase(BaseModel):
    gora_id: Optional[int] = Field(default=None, gt=0)
    tezavnost_id: Optional[int] = Field(default=None, gt=0)
    stil_id: Optional[int] = Field(default=None, gt=0)
    ime: Optional[str] = Field(default=None, min_length=1, max_length=200)
    dolzina_m: Optional[int] = Field(default=None, gt=0)
    opis: Optional[str] = Field(default=None, max_length=2000)
    skica_url_1: Optional[str] = Field(default=None, max_length=500)
    skica_url_2: Optional[str] = Field(default=None, max_length=500)


class SmerRead(SmerBase):
    smer_id: int

    class Config:
        from_attributes = True


# ---------- UPORABNIK ----------
class UporabnikBase(BaseModel):
    ime: Optional[str] = Field(default=None, min_length=1, max_length=100)
    email: Optional[EmailStr] = None


class UporabnikCreate(UporabnikBase):
    ime: str = Field(min_length=1, max_length=100)
    email: EmailStr
    geslo: str = Field(min_length=8, max_length=128)


class UporabnikRead(UporabnikBase):
    uporabnik_id: int
    email: EmailStr

    class Config:
        from_attributes = True


# ---------- VZPON ----------
class VzponBase(BaseModel):
    smer_id: Optional[int] = Field(default=None, gt=0)
    datum: Optional[date] = None
    slog: Optional[str] = Field(default=None, max_length=50)
    razmere: Optional[str] = Field(default=None, max_length=200)
    partnerji: Optional[str] = Field(default=None, max_length=200)
    opombe: Optional[str] = Field(default=None, max_length=2000)
    uspesen: Optional[bool] = None
    cas_trajanja: Optional[int] = Field(default=None, ge=0)


class VzponCreate(VzponBase):
    smer_id: int = Field(gt=0)
    datum: date


class VzponUpdate(VzponBase):
    pass


class VzponRead(VzponBase):
    vzpon_id: int
    uporabnik_id: int

    class Config:
        from_attributes = True
