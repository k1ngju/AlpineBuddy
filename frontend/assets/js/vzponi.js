let activeVzponId = null;
let gorovjaCache = [];
let goreCache = [];
let smeriCache = [];

function findSmerName(id) {
  const smer = smeriCache.find((s) => s.smer_id === Number(id));
  return smer ? smer.ime : id;
}

function findGoraNameBySmer(id) {
  const smer = smeriCache.find((s) => s.smer_id === Number(id));
  if (!smer) return "";
  const gora = goreCache.find((g) => g.gora_id === smer.gora_id);
  return gora ? gora.ime : "";
}

function setOptions(select, items, valueKey, labelKey, placeholder) {
  if (!select) return;
  const options = items.map((item) => `
    <option value="${item[valueKey]}">${item[labelKey]}</option>
  `);
  select.innerHTML = placeholder ? `<option value="">${placeholder}</option>` + options.join("") : options.join("");
}

function renderGore(gorovjeId) {
  const goraSelect = document.getElementById("goraSelect");
  const smerSelect = document.getElementById("smerSelect");
  const filtered = goreCache.filter((g) => g.gorovje_id === Number(gorovjeId));
  setOptions(goraSelect, filtered, "gora_id", "ime", "Izberi goro");
  if (filtered.length) {
    goraSelect.value = filtered[0].gora_id;
    renderSmeri(filtered[0].gora_id);
  } else {
    setOptions(smerSelect, [], "smer_id", "ime", "Ni smeri");
  }
}

function renderSmeri(goraId) {
  const smerSelect = document.getElementById("smerSelect");
  const filtered = smeriCache.filter((s) => s.gora_id === Number(goraId));
  setOptions(smerSelect, filtered, "smer_id", "ime", "Izberi smer");
  if (filtered.length) {
    smerSelect.value = filtered[0].smer_id;
  }
}

async function loadReferenceData() {
  gorovjaCache = await apiFetch("/gorovja");
  goreCache = await apiFetch("/gore");
  smeriCache = await apiFetch("/smeri");

  const gorovjeSelect = document.getElementById("gorovjeSelect");
  setOptions(gorovjeSelect, gorovjaCache, "gorovje_id", "naziv", "Izberi gorovje");

  gorovjeSelect.addEventListener("change", (e) => renderGore(e.target.value));
  document.getElementById("goraSelect").addEventListener("change", (e) => renderSmeri(e.target.value));

  if (gorovjaCache.length) {
    gorovjeSelect.value = gorovjaCache[0].gorovje_id;
    renderGore(gorovjaCache[0].gorovje_id);
  }
}

async function loadVzponi() {
  const table = document.getElementById("vzponiTable");
  try {
    const data = await apiFetch("/vzponi");
    table.innerHTML = data.map((v) => `
      <tr>
        <td>${v.vzpon_id}</td>
        <td>${findSmerName(v.smer_id)}</td>
        <td>${findGoraNameBySmer(v.smer_id)}</td>
        <td>${v.datum}</td>
        <td>${v.uspesen ? "Da" : "Ne"}</td>
        <td>${v.cas_trajanja ?? ""}</td>
        <td>
          <button class="btn btn-sm btn-outline-dark" data-edit="${v.vzpon_id}">Uredi</button>
          <button class="btn btn-sm btn-outline-danger" data-del="${v.vzpon_id}">Izbrisi</button>
        </td>
      </tr>
    `).join("");

    table.querySelectorAll("[data-edit]").forEach((btn) => {
      btn.addEventListener("click", () => loadIntoForm(btn.dataset.edit));
    });
    table.querySelectorAll("[data-del]").forEach((btn) => {
      btn.addEventListener("click", () => deleteVzpon(btn.dataset.del));
    });
  } catch (err) {
    table.innerHTML = `<tr><td colspan="6">Napaka pri nalaganju vzponov.</td></tr>`;
  }
}

async function loadIntoForm(id) {
  const data = await apiFetch("/vzponi");
  const vzpon = data.find((v) => v.vzpon_id === Number(id));
  if (!vzpon) return;

  activeVzponId = vzpon.vzpon_id;
  const smer = smeriCache.find((s) => s.smer_id === vzpon.smer_id);
  if (smer) {
    const gora = goreCache.find((g) => g.gora_id === smer.gora_id);
    if (gora) {
      const gorovjeSelect = document.getElementById("gorovjeSelect");
      gorovjeSelect.value = gora.gorovje_id;
      renderGore(gora.gorovje_id);
      const goraSelect = document.getElementById("goraSelect");
      goraSelect.value = gora.gora_id;
      renderSmeri(gora.gora_id);
      const smerSelect = document.getElementById("smerSelect");
      smerSelect.value = smer.smer_id;
    }
  }
  document.getElementById("datum").value = vzpon.datum;
  document.getElementById("uspesen").checked = !!vzpon.uspesen;
  document.getElementById("cas").value = vzpon.cas_trajanja ?? "";
  document.getElementById("opombe").value = vzpon.opombe ?? "";
  document.getElementById("formTitle").textContent = "Uredi vzpon";
}

async function saveVzpon(event) {
  event.preventDefault();
  const smerId = Number(document.getElementById("smerSelect").value);
  if (!smerId) {
    alert("Izberi smer.");
    return;
  }
  const payload = {
    smer_id: smerId,
    datum: document.getElementById("datum").value,
    uspesen: document.getElementById("uspesen").checked,
    cas_trajanja: document.getElementById("cas").value ? Number(document.getElementById("cas").value) : null,
    opombe: document.getElementById("opombe").value || null,
  };

  try {
    if (activeVzponId) {
      await apiFetch(`/vzponi/${activeVzponId}`, { method: "PUT", json: payload });
    } else {
      await apiFetch("/vzponi", { method: "POST", json: payload });
    }
    activeVzponId = null;
    document.getElementById("vzponForm").reset();
    document.getElementById("formTitle").textContent = "Nov vzpon";
    loadVzponi();
  } catch (err) {
    alert("Napaka pri shranjevanju vzpona.");
  }
}

async function deleteVzpon(id) {
  if (!confirm("Zelis izbrisati ta vzpon?")) return;
  try {
    await apiFetch(`/vzponi/${id}`, { method: "DELETE" });
    loadVzponi();
  } catch (err) {
    alert("Napaka pri brisanju vzpona.");
  }
}

function initVzponi() {
  requireAuth();
  bindLogout();
  document.getElementById("vzponForm").addEventListener("submit", saveVzpon);
  loadReferenceData().then(loadVzponi);
}

document.addEventListener("DOMContentLoaded", initVzponi);
