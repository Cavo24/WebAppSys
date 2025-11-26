// add_module.js (Wird benötigt, um den Formular-Submit zu steuern)

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('moduleForm');
    const API_URL = '/rest/modules';
    const LIST_URL = 'list.html';

    form.addEventListener('submit', async (event) => {
        // 1. Standard-Submit des Browsers verhindern (wichtig!)
        event.preventDefault(); 

        // 2. Daten aus den Formularfeldern sammeln
        const formData = new FormData(form);
        const moduleData = {
            name: formData.get('name'),
            grade: parseFloat(formData.get('grade')),
            creditPoints: parseFloat(formData.get('creditPoints'))
            // ID wird NICHT gesendet, da der Server sie erstellt
        };

        try {
            // --- 3. JSON-Payload senden (REST-Vertrag) ---
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    // 💡 LÖSUNG DES 415-FEHLERS: Wir sagen dem Server, dass der Body JSON ist
                    'Content-Type': 'application/json' 
                },
                // JSON.stringify wandelt das JavaScript-Objekt in einen JSON-String um
                body: JSON.stringify(moduleData) 
            });

            if (response.status === 201) {
                // Erfolg: 201 Created
                console.log('Modul erfolgreich erstellt!');
                // 4. Weiterleitung zur Liste (PRG-Muster)
                window.location.href = LIST_URL; 
            } else if (response.status === 422 || response.status === 400) {
                // Fehler: Ungültige Validierung (z.B. Note < 1.0)
                const errorBody = await response.json();
                alert('Validierungsfehler: ' + (errorBody.detail || errorBody.message));
            } else {
                 throw new Error(`Unbekannter Fehler: ${response.status}`);
            }

        } catch (error) {
            console.error('API-Fehler beim Hinzufügen:', error);
            alert('Fehler beim Senden der Daten.');
        }
    });
});