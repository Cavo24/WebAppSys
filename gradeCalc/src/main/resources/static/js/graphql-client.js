const GRAPHQL_ENDPOINT = '/graphql';

async function graphqlQuery(query, variables = {}) {
    const response = await fetch(GRAPHQL_ENDPOINT, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query, variables })
    });
    const result = await response.json();
    if (result.errors) {
        console.error('GraphQL Errors:', result.errors);
        throw new Error(result.errors[0].message);
    }
    return result.data;
}

async function loadModules() {
    try {
        const query = `{
            allModules { id name grade creditPoints }
            averageGrade
            totalCreditPoints
        }`;
        const data = await graphqlQuery(query);
        
        const tableBody = document.querySelector('#modulesTable tbody');
        tableBody.innerHTML = '';
        
        data.allModules.forEach(module => {
            const row = tableBody.insertRow();
            row.insertCell().textContent = module.id;
            row.insertCell().textContent = module.name;
            row.insertCell().textContent = module.grade.toFixed(1);
            row.insertCell().textContent = module.creditPoints;
            
            const actionCell = row.insertCell();
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Löschen';
            deleteBtn.onclick = () => deleteModule(module.id);
            actionCell.appendChild(deleteBtn);
        });
        
        document.getElementById('statsDiv').innerHTML = `
            <p><strong>Durchschnittsnote:</strong> ${data.averageGrade.toFixed(2)}</p>
            <p><strong>Gesamt-ECTS:</strong> ${data.totalCreditPoints}</p>
        `;
    } catch (error) {
        console.error('Fehler beim Laden:', error);
        document.getElementById('modulesTable').innerHTML = `<tr><td colspan="5" class="error">Fehler beim Laden der Module</td></tr>`;
    }
}

async function addModule() {
    const name = document.getElementById('moduleName').value;
    const grade = parseFloat(document.getElementById('moduleGrade').value);
    const creditPoints = parseFloat(document.getElementById('moduleCreditPoints').value);
    
    if (!name || !grade || !creditPoints) {
        document.getElementById('addMessage').textContent = '❌ Alle Felder erforderlich';
        return;
    }
    
    try {
        const query = `mutation {
            addModule(name: "${name}", grade: ${grade}, creditPoints: ${creditPoints}) {
                id name grade creditPoints
            }
        }`;
        const data = await graphqlQuery(query);
        document.getElementById('addMessage').innerHTML = `<span class="success">✓ Modul hinzugefügt!</span>`;
        document.getElementById('moduleName').value = '';
        document.getElementById('moduleGrade').value = '';
        document.getElementById('moduleCreditPoints').value = '';
        loadModules();
    } catch (error) {
        document.getElementById('addMessage').innerHTML = `<span class="error">❌ ${error.message}</span>`;
    }
}

async function deleteModule(id) {
    if (!confirm('Modul wirklich löschen?')) return;
    
    try {
        const query = `mutation { deleteModule(id: ${id}) }`;
        await graphqlQuery(query);
        loadModules();
    } catch (error) {
        alert('Fehler beim Löschen: ' + error.message);
    }
}

document.addEventListener('DOMContentLoaded', loadModules);
