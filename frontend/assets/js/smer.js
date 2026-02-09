async function loadSmer() {
  const id = qs("id");
  const header = document.getElementById("smerHeader");
  const gallery = document.getElementById("skiceGallery");

  try {
    const [smer, tezavnosti, stili] = await Promise.all([
      apiFetch(`/smeri/${id}`),
      apiFetch("/tezavnosti"),
      apiFetch("/stili-smeri"),
    ]);
    const tezMap = new Map(tezavnosti.map((t) => [t.tezavnost_id, t.oznaka]));
    const stilMap = new Map(stili.map((s) => [s.stil_id, s.naziv]));
    header.innerHTML = `
      <h2 class="section-title">${smer.ime}</h2>
      <p class="small-muted">Dolzina: ${smer.dolzina_m ?? ""} m</p>
      <p class="small-muted">Tezavnost: ${tezMap.get(smer.tezavnost_id) ?? ""}</p>
      <p class="small-muted">Stil: ${stilMap.get(smer.stil_id) ?? ""}</p>
      <p>${smer.opis ?? ""}</p>
    `;

    const imgs = [smer.skica_url_1, smer.skica_url_2].filter(Boolean);
    gallery.innerHTML = imgs.map((url) => `
      <div class="col-md-6">
        <img class="img-frame" src="${API_BASE}${url}" alt="Skica">
      </div>
    `).join("") || `<div class="alert alert-info">Skic se ni.</div>`;
  } catch (err) {
    header.innerHTML = `<div class="alert alert-warning">Napaka pri nalaganju smeri.</div>`;
  }
}

function initSmer() {
  requireAuth();
  bindLogout();
  loadSmer();
}

document.addEventListener("DOMContentLoaded", initSmer);
