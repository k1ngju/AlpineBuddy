from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

# SQLite DB file will be created in the backend/ folder (current working dir)
DATABASE_URL = "sqlite:///./alpinebuddy.db"

engine = create_engine(
    DATABASE_URL,
    connect_args={"check_same_thread": False},  # needed for SQLite + FastAPI
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()


# Dependency: each request gets its own DB session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


