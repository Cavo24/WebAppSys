// modules.js
document.addEventListener('DOMContentLoaded', () => {
    fetch('/rest/modules')
        .then(response => {
            if (!response.ok) throw new Error('Netzwerkantwort war nicht ok: ' + response.status);
            return response.json();
        })
        .then(data => {
            // Unterstütze HAL (_embedded) oder plain JSON-Array
            let moduleList = [];
            if (data && data._embedded) {
                // Suche nach dem ersten eingebetteten Array (konservativ)
                const embedded = data._embedded;
                const keys = Object.keys(embedded);
                if (keys.length > 0 && Array.isArray(embedded[keys[0]])) {
                    moduleList = embedded[keys[0]];
                }
            } else if (Array.isArray(data)) {
                moduleList = data;
            } else {
                console.warn('Unbekanntes Format der Modulantwort', data);
            }

            const tableBody = document.querySelector('#modulesTable tbody');
            tableBody.innerHTML = ''; // vorher leeren

            moduleList.forEach(module => {
                const row = tableBody.insertRow();
                row.insertCell().textContent = module.id ?? '';
                row.insertCell().textContent = module.name ?? '';
                row.insertCell().textContent = module.grade ?? '';
                row.insertCell().textContent = module.creditPoints ?? '';

                const linkCell = row.insertCell();
                const selfLink = module._links && module._links.self && module._links.self.href ? module._links.self.href : null;
                if (selfLink) {
                    const a = document.createElement('a');
                    a.href = selfLink;
                    a.textContent = 'Details';
                    a.target = '_blank';
                    linkCell.appendChild(a);
                } else {
                    linkCell.textContent = '-';
                }
            });
        })
        .catch(error => {
            console.error('Fehler beim Laden der Module:', error);
            const tableBody = document.querySelector('#modulesTable tbody');
            tableBody.innerHTML = '<tr><td colspan="5">Fehler beim Laden der Module. Siehe Konsole.</td></tr>';
        });
});