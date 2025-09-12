const toggle = document.getElementById('toggleAuto');
const runNow = document.getElementById('runNow');

// carrega estado
chrome.storage.sync.get({ autoPREnabled: false }, ({ autoPREnabled }) => {
  toggle.checked = autoPREnabled;
});

// salva estado
toggle.addEventListener('change', async () => {
  const enabled = toggle.checked;
  await chrome.storage.sync.set({ autoPREnabled: enabled });
  // avisa a aba ativa
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  if (tab?.id) {
    chrome.tabs.sendMessage(tab.id, { type: 'autoPRStatusChanged', enabled });
  }
});

// rodar sob demanda
runNow.addEventListener('click', async () => {
  const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
  if (tab?.id) {
    chrome.tabs.sendMessage(tab.id, { type: 'runAutoPR' });
  }
});
