const API_BASE = "http://127.0.0.1:8000";
const DEV_MODE = true;
const PUBLIC_PAGES = ["/index.html", "/register.html", "/frontend/index.html", "/frontend/register.html"];

function getToken() {
  return localStorage.getItem("alpinebuddy_token");
}

function setToken(token) {
  localStorage.setItem("alpinebuddy_token", token);
}

function clearToken() {
  localStorage.removeItem("alpinebuddy_token");
}

async function apiFetch(path, options = {}) {
  const headers = options.headers || {};
  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }
  if (options.json) {
    headers["Content-Type"] = "application/json";
  }
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers,
    body: options.json ? JSON.stringify(options.json) : options.body,
  });
  if (res.status === 401) {
    throw new Error("unauthorized");
  }
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "request_failed");
  }
  return res.json();
}

function requireAuth() {
  if (DEV_MODE) return;
  const path = window.location.pathname.replace(/\\/g, "/");
  const isPublic = PUBLIC_PAGES.some((p) => path.endsWith(p));
  if (!getToken() && !isPublic) {
    window.location.href = "index.html";
  }
}

function bindLogout() {
  const btn = document.querySelector("[data-logout]");
  if (!btn) return;
  btn.addEventListener("click", () => {
    clearToken();
    window.location.href = "index.html";
  });
}

function qs(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}
