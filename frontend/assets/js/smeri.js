async function loadGora() {
  const id = qs("id");
  const header = document.getElementById("goraHeader");
  const smeriWrap = document.getElementById("smeriList");

  try {
    const gora = await apiFetch(`/gore/${id}`);
    const [smeri, tezavnosti, stili] = await Promise.all([
      apiFetch("/smeri"),
      apiFetch("/tezavnosti"),
      apiFetch("/stili-smeri"),
    ]);
    const tezMap = new Map(tezavnosti.map((t) => [t.tezavnost_id, t.oznaka]));
    const stilMap = new Map(stili.map((s) => [s.stil_id, s.naziv]));

    const goreImg = gora.slika_url ? `<img class="img-frame" src="${API_BASE}${gora.slika_url}" alt="${gora.ime}">` : "";
    header.innerHTML = `
      <div class="col-lg-5">
        ${goreImg}
      </div>
      <div class="col-lg-7">
        <h2 class="section-title">${gora.ime}</h2>
        <p class="small-muted">${gora.gps_sirina ?? ""} ${gora.gps_dolzina ?? ""}</p>
      </div>
    `;

    const filtered = smeri.filter((s) => s.gora_id === Number(id));

    smeriWrap.innerHTML = filtered.map((s) => `
      <div class="col-md-6">
        <div class="card p-3 h-100">
          <h5>${s.ime}</h5>
          <p class="small-muted">Dolzina: ${s.dolzina_m ?? ""} m</p>
          <p class="small-muted">Tezavnost: ${tezMap.get(s.tezavnost_id) ?? ""}</p>
          <p class="small-muted">Stil: ${stilMap.get(s.stil_id) ?? ""}</p>
          <a class="btn btn-accent" href="smer.html?id=${s.smer_id}">Odpri</a>
        </div>
      </div>
    `).join("");
  } catch (err) {
    header.innerHTML = `<div class="alert alert-warning">Napaka pri nalaganju gore.</div>`;
  }
}

function initGora() {
  requireAuth();
  bindLogout();
  loadGora();
}

document.addEventListener("DOMContentLoaded", initGora);
