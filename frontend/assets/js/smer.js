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
    const opisRaw = (smer.opis ?? "").trim();
    const opisParts = opisRaw
      ? opisRaw.split(/\n\s*\n/).map((part) => part.trim()).filter(Boolean)
      : [];
    const opisHtml = opisParts.length
      ? `
        <div class="col-lg-7">
          <div class="smer-opis">
            <h5 class="smer-opis-title">Opis</h5>
            ${opisParts
              .map((part) => `<p>${part.replace(/\n+/g, "<br>")}</p>`)
              .join("")}
          </div>
        </div>
      `
      : "";
    const metaColClass = opisHtml ? "col-lg-5 d-flex" : "col-12 d-flex";

    header.innerHTML = `
      <h2 class="section-title mb-3">${smer.ime}</h2>
      <div class="row g-4">
        <div class="${metaColClass}">
          <div class="smer-meta w-100">
            <p class="small-muted">Dolzina: ${smer.dolzina_m ?? ""} m</p>
            <p class="small-muted">Tezavnost: ${tezMap.get(smer.tezavnost_id) ?? ""}</p>
            <p class="small-muted">Stil: ${stilMap.get(smer.stil_id) ?? ""}</p>
          </div>
        </div>
        ${opisHtml}
      </div>
    `;

    const imgs = [smer.skica_url_1, smer.skica_url_2].filter(Boolean);
    gallery.innerHTML = imgs.map((url) => `
      <div class="col-md-6">
        <img class="img-frame sketch-img" src="${API_BASE}${url}" alt="Skica" data-lightbox>
      </div>
    `).join("") || `<div class="alert alert-info">Skic se ni.</div>`;

    bindLightbox();
  } catch (err) {
    header.innerHTML = `<div class="alert alert-warning">Napaka pri nalaganju smeri.</div>`;
  }
}

function bindLightbox() {
  const lightbox = document.getElementById("lightbox");
  const lightboxImage = document.getElementById("lightboxImage");
  const closeBtn = document.querySelector(".lightbox-close");
  if (!lightbox || !lightboxImage || !closeBtn) return;

  document.querySelectorAll("[data-lightbox]").forEach((img) => {
    img.addEventListener("click", () => {
      lightboxImage.src = img.src;
      lightbox.classList.add("is-open");
      lightbox.setAttribute("aria-hidden", "false");
    });
  });

  function closeLightbox() {
    lightbox.classList.remove("is-open");
    lightbox.setAttribute("aria-hidden", "true");
    lightboxImage.src = "";
  }

  closeBtn.addEventListener("click", closeLightbox);
  lightbox.addEventListener("click", (e) => {
    if (e.target === lightbox) closeLightbox();
  });
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && lightbox.classList.contains("is-open")) {
      closeLightbox();
    }
  });
}

function initSmer() {
  requireAuth();
  bindLogout();
  loadSmer();
}

document.addEventListener("DOMContentLoaded", initSmer);
