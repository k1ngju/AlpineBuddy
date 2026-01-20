from fastapi import FastAPI
from app.database import engine, Base
from app import models  # IMPORTANT: registers models on Base.metadata

app = FastAPI(title="AlpineBuddy API")

# Optional: keep this ON for dev; it won't delete anything.
@app.on_event("startup")
def on_startup():
    Base.metadata.create_all(bind=engine)

@app.get("/")
def root():
    return {"status": "backend alive"}
