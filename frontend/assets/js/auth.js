async function registerUser(event) {
  event.preventDefault();
  const ime = document.getElementById("regName").value.trim();
  const email = document.getElementById("regEmail").value.trim();
  const geslo = document.getElementById("regPass").value;

  const out = document.getElementById("authMsg");
  if (out) out.textContent = "";

  try {
    const res = await fetch(`${API_BASE}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ime, email, geslo }),
    });
    if (!res.ok) throw new Error("register_failed");
    await loginUser(email, geslo);
  } catch (err) {
    if (out) out.textContent = "Registracija ni uspela. Preveri podatke ali e-naslov.";
  }
}

async function loginUser(emailOverride, passOverride) {
  const email = emailOverride || document.getElementById("loginEmail").value.trim();
  const password = passOverride || document.getElementById("loginPass").value;

  const out = document.getElementById("authMsg");
  if (out) out.textContent = "";

  try {
    const body = `username=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
    const res = await fetch(`${API_BASE}/auth/token`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body,
    });
    if (!res.ok) throw new Error("login_failed");
    const data = await res.json();
    setToken(data.access_token);
    window.location.href = "dashboard.html";
  } catch (err) {
    if (out) out.textContent = "Prijava ni uspela. Preveri e-naslov in geslo.";
  }
}

function initAuthPage() {
  const regForm = document.getElementById("registerForm");
  const loginForm = document.getElementById("loginForm");
  if (regForm) regForm.addEventListener("submit", registerUser);
  if (loginForm) loginForm.addEventListener("submit", (e) => { e.preventDefault(); loginUser(); });
}

document.addEventListener("DOMContentLoaded", initAuthPage);
