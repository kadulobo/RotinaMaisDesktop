// ===== Codex Auto PR – content.js =====
console.log('[Codex Auto PR] content carregado');

let enabled = true;      // se você estiver usando popup, isso vem do storage
let running = false;

// (se estiver usando popup, pode manter esses listeners)
try {
  chrome.storage?.sync?.get({ autoPREnabled: true }, ({ autoPREnabled }) => enabled = !!autoPREnabled);
  chrome.runtime?.onMessage?.addListener((m)=>{ if(m.type==='runAutoPR') safeRun(); if(m.type==='autoPRStatusChanged') enabled = !!m.enabled; });
} catch {}

function sleep(ms){ return new Promise(r=>setTimeout(r,ms)); }
function visible(el){ if(!el) return false; const s=getComputedStyle(el), r=el.getBoundingClientRect(); return s.display!=='none'&&s.visibility!=='hidden'&&r.width>0&&r.height>0; }
function hi(el){ if(!el) return; const o=el.style.outline; el.style.outline='2px solid #22c55e'; setTimeout(()=>el.style.outline=o,1500); }
function qx(xpath, ctx=document){ return document.evaluate(xpath, ctx, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue; }

async function waitXPath(xps, {timeout=15000, interval=250}={}) {
  const end=Date.now()+timeout;
  while(Date.now()<end){
    for(const xp of (Array.isArray(xps)?xps:[xps])){
      const el = qx(xp);
      if(visible(el)) return el;
    }
    await sleep(interval);
  }
  throw new Error('Elemento (XPath) não encontrado: ' + JSON.stringify(xps));
}

async function waitBtnByText(texts,{timeout=15000,interval=250}={}) {
  const end=Date.now()+timeout;
  const lower = texts.map(t=>t.toLowerCase());
  while(Date.now()<end){
    const btns=[...document.querySelectorAll('button,[role="button"]')];
    const match=btns.find(b=>lower.some(t=>(b.innerText||'').trim().toLowerCase().includes(t)));
    if(visible(match)) return match;
    await sleep(interval);
  }
  throw new Error('Botão não encontrado por texto: '+texts.join(', '));
}

async function clickCodigo() {
  // 1) por texto direto
  const tries = [
    "//button[normalize-space()='Código']",
    "//button[.//text()[contains(.,'Código')]]",
    "//button[@aria-label='Código' or contains(@aria-label,'Código')]",
    // 2) irmão do “Perguntar”
    "//*[self::button][normalize-space()='Perguntar' or normalize-space()='Ask']/following-sibling::button[1]"
  ];
  let btn=null;
  for(const xp of tries){
    btn = qx(xp);
    if(visible(btn)) break;
  }
  // 3) fallback: vizinho do “Perguntar” no mesmo container
  if(!visible(btn)){
    const ask = qx("//button[normalize-space()='Perguntar' or normalize-space()='Ask']");
    if(ask){
      let p=ask.parentElement;
      for(let i=0;i<5 && p && !p.querySelector('button');i++) p=p.parentElement;
      if(p){
        const candidates=[...p.querySelectorAll('button')];
        btn = candidates.find(b=>(b.innerText||'').includes('Código')) || candidates[candidates.length-1];
      }
    }
  }
  if(!visible(btn)) throw new Error('Botão "Código" não encontrado.');
  hi(btn); btn.click();
}

function firstTaskCandidate() {
  // tenta achar a seção "Tarefas"
  const heading = [...document.querySelectorAll('h1,h2,h3,section,header')]
    .find(h=>(h.innerText||'').toLowerCase().includes('tarefas'));
  const scope = heading?.closest('section,main,div') || document;
  // candidatos clicáveis próximos ao topo da lista
  const cand = [...scope.querySelectorAll('a,button,[role="button"],div,li')]
    .filter(el=>{
      const txt=(el.innerText||'').trim();
      const cls=(el.className||'').toString().toLowerCase();
      const clickable = el.tagName==='A'||el.tagName==='BUTTON'||el.getAttribute('role')==='button'||el.onclick;
      const rowish = /task|tarefa|item|row|list|entry|card/.test(cls);
      return visible(el) && txt.length>0 && (clickable || rowish);
    })
    .sort((a,b)=>a.getBoundingClientRect().top - b.getBoundingClientRect().top);
  return cand[0] || null;
}

async function clickFirstTask() {
  const end = Date.now()+45000;
  while(Date.now()<end){
    const el = firstTaskCandidate();
    if(el){ hi(el); el.click(); return; }
    await sleep(250);
  }
  throw new Error('Primeiro item de tarefas não encontrado.');
}

async function runSequence() {
  await clickCodigo();                 // 1) Código
  await sleep(600);
  await clickFirstTask();              // 2) 1ª tarefa
  await sleep(600);
  const prBtn = await waitBtnByText(   // 3) esperar “Criar PR”
    ['Criar PR','Create PR','Create pull request'], { timeout: 120000 }
  );
  hi(prBtn); prBtn.click();            // 4) clicar
}

async function safeRun(){
  if(running) return;
  running = true;
  try{
    if(!enabled){ console.log('[Codex Auto PR] desligado.'); return; }
    console.log('[Codex Auto PR] iniciando…');
    await runSequence();
    console.log('[Codex Auto PR] concluído.');
  } catch(e){ console.warn('[Codex Auto PR] erro:', e); }
  finally { running=false; }
}

// Rode automaticamente 1x ao carregar (se quiser)
setTimeout(()=>safeRun(), 1500);

// Exponha para testes no Console
window.codexAutoPR = { run: safeRun, runSequence };
