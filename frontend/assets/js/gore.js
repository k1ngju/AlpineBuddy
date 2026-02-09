let gorovjaCache = [];
let goreCache = [];

function renderGore(gorovjeId) {
  const list = document.getElementById("goreList");

  const filtered = goreCache.filter((g) => g.gorovje_id === Number(gorovjeId));
  if (!filtered.length) {
    list.innerHTML = `<div class="alert alert-info">Ni najdenih gora za izbrano gorovje.</div>`;
    return;
  }

  list.innerHTML = filtered.map((g) => {
    const img = g.slika_url ? `<img class="img-frame" src="${API_BASE}${g.slika_url}" alt="${g.ime}">` : "";
    return `
      <div class="col-md-6 col-lg-4">
        <div class="card h-100 p-3 mountain-card">
          ${img}
          <h5 class="mt-3 text-center">${g.ime}</h5>
          <p class="small-muted text-center">${g.gps_sirina ?? ""} ${g.gps_dolzina ?? ""}</p>
          <a class="btn btn-outline-dark mt-auto" href="gora.html?id=${g.gora_id}">Odpri</a>
        </div>
      </div>
    `;
  }).join("");
}

async function loadGore() {
  const select = document.getElementById("gorovjeSelect");
  const list = document.getElementById("goreList");
  try {
    gorovjaCache = await apiFetch("/gorovja");
    goreCache = await apiFetch("/gore");

    if (!gorovjaCache.length) {
      list.innerHTML = `<div class="alert alert-info">Ni vnesenih gorovij.</div>`;
      return;
    }

    select.innerHTML = gorovjaCache.map((g) => `
      <option value="${g.gorovje_id}">${g.naziv}</option>
    `).join("");

    select.addEventListener("change", (e) => renderGore(e.target.value));
    renderGore(gorovjaCache[0].gorovje_id);
  } catch (err) {
    list.innerHTML = `<div class="alert alert-warning">Napaka pri nalaganju gora.</div>`;
  }
}

function initGore() {
  requireAuth();
  bindLogout();
  loadGore();
}

document.addEventListener("DOMContentLoaded", initGore);
