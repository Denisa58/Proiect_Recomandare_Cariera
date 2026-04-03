// ==========================================================
// 1. CONFIGURAȚII ȘI VARIABILE GLOBALE
// ==========================================================
const BASE_URL = 'http://localhost:8080';
const API_URL = `${BASE_URL}/api/auth`;
const QUESTIONNAIRE_API_URL = `${BASE_URL}/api/recommendation`;

let CURRENT_USER_ID = null;
let lastRecommendations = []; // Salvăm recomandările pentru a fi folosite de AI

// ==========================================================
// 2. FUNCȚII UI ȘI NAVIGARE
// ==========================================================

function displayMessage(elementId, message, isError = false) {
    const element = document.getElementById(elementId);
    if (!element) return;
    element.textContent = message;
    element.style.color = isError ? 'red' : 'green';
    element.style.display = 'block';
}

function showSection(sectionId) {
    const panels = ['authContainer', 'homePanel', 'coursesPanel', 'questionnaireContainer', 'loginPanel', 'registerPanel'];
    panels.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.style.display = 'none';
    });

    const target = document.getElementById(sectionId);
    if (target) {
        target.style.display = 'block';
        document.querySelector('.main-container')?.classList.add('expanded-view');
    }
}

function toggleDetails(buttonElement) {
    const detailsId = buttonElement.getAttribute('data-details-id');
    const detailsDiv = document.getElementById(detailsId);
    if (detailsDiv) {
        const isHidden = detailsDiv.style.display === 'none';
        detailsDiv.style.display = isHidden ? 'block' : 'none';
        buttonElement.textContent = isHidden ? 'Ascunde detalii' : 'Vezi mai mult';
    }
}

function showLoginForm() { showSection('loginPanel'); document.getElementById('authContainer').style.display = 'block'; }
function showRegisterForm() { showSection('registerPanel'); document.getElementById('authContainer').style.display = 'block'; }

// ==========================================================
// 3. LOGICA DE AUTENTIFICARE
// ==========================================================

async function handleRegister(username, email, password) {
    try {
        const response = await fetch(`${API_URL}/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        if (response.status === 201) {
            displayMessage('registerMessage', 'Înregistrare reușită!');
            CURRENT_USER_ID = 1;
            setTimeout(() => {
                document.getElementById('authContainer').style.display = 'none';
                document.getElementById('welcomeUsername').textContent = username;
                document.getElementById('welcomePanel').style.display = 'block';
                showSection('questionnaireContainer');
                loadSkillsForQuestionnaire();
            }, 1000);
        } else {
            const result = await response.text();
            displayMessage('registerMessage', `Eroare: ${result}`, true);
        }
    } catch (e) { displayMessage('registerMessage', 'Eroare backend.', true); }
}

async function handleLogin(username, password) {
    try {
        const response = await fetch(`${API_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        if (response.status === 200) {
            CURRENT_USER_ID = 1;
            displayMessage('loginMessage', 'Login reusit');
            setTimeout(() => {
            document.getElementById('authContainer').style.display = 'none';
            document.getElementById('welcomeUsername').textContent = username;
            document.getElementById('welcomePanel').style.display = 'block';
            showSection('homePanel');
            },1000);
        } else {
            displayMessage('loginMessage', 'Eroare la autentificare.', true);
        }
    } catch (e) { displayMessage('loginMessage', 'Eroare de rețea.', true); }
}

// ==========================================================
// 4. LOGICA CHESTIONAR ȘI RECOMANDĂRI CARIERE
// ==========================================================

async function loadSkillsForQuestionnaire() {
    try {
        const response = await fetch(`${QUESTIONNAIRE_API_URL}/skills`);
        const skills = await response.json();
        displaySkillQuestionnaire(skills);
    } catch (e) { console.error("Eroare skills:", e); }
}

function displaySkillQuestionnaire(skills) {
    const container = document.getElementById('questionnaireContainer');
    container.innerHTML = '<h3>Evaluează-ți competențele (1-5)</h3>';
    const form = document.createElement('form');
    form.id = 'skillsForm';

    skills.forEach(skill => {
        const div = document.createElement('div');
        div.className = 'skill-item';
        div.innerHTML = `
            <label>${skill.skillName}:</label>
            <div class="skill-row">
                <input type="range" min="1" max="5" value="3" data-skill-id="${skill.id}" oninput="this.nextElementSibling.value=this.value">
                <output>3</output>
            </div>
        `;
        form.appendChild(div);
    });

    const btn = document.createElement('button');
    btn.type = 'submit';
    btn.className = 'action-button';
    btn.textContent = 'Trimite Evaluarea';
    form.appendChild(btn);
    container.appendChild(form);
    form.addEventListener('submit', handleSubmitQuestionnaire);
}

async function handleSubmitQuestionnaire(event) {
    event.preventDefault();
    const assessments = Array.from(event.target.querySelectorAll('input[type="range"]')).map(input => ({
        skillId: parseInt(input.dataset.skillId),
        level: parseInt(input.value)
    }));
    try {
        const response = await fetch(`${QUESTIONNAIRE_API_URL}/submit/${CURRENT_USER_ID}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ assessments })
        });
        if (response.ok) fetchAndDisplayRecommendations(CURRENT_USER_ID);
    } catch (e) { console.error(e); }
}

async function fetchAndDisplayRecommendations(userId) {
    try {
        const response = await fetch(`${QUESTIONNAIRE_API_URL}/${userId}`);
        const recommendations = await response.json();
        lastRecommendations = recommendations; // Salvăm pt AI
        displayRecommendations(recommendations);
    } catch (e) { console.error(e); }
}

function displayRecommendations(recommendations) {
    const container = document.getElementById('questionnaireContainer');
    let htmlContent = `
        <div class="navigation-options" style="margin-bottom:20px;">
            <button onclick="showSection('homePanel')" class="action-button">Mergi la Home</button>
            <button id="recommendCoursesBtn" class="action-button secondary">Recomandă Cursuri </button>
        </div>
        <h2>Recomandările Tale de Carieră</h2>
        <div class="recommendation-list">`;

    recommendations.forEach((rec, index) => {
        const percentage = Math.round(rec.matchScore * 100);
        htmlContent += `
            <div class="career-item" style="border: 1px solid #ddd; padding: 20px; margin: 15px 0; border-radius: 12px; background: white;">
                <h3>${rec.careerName}</h3>
                <p>${rec.careerDescription}</p>
                <div style="background: #eee; height: 10px; border-radius: 5px; margin: 10px 0;">
                    <div style="width: ${percentage}%; background: #28a745; height: 100%; border-radius: 5px;"></div>
                </div>
                <p><b>${rec.matchPercentage} potrivire</b></p>
                <div id="det-${index}" style="display:none; margin-top:10px; padding-top:10px; border-top: 1px solid #eee;">
                    ${rec.detailedDescription || 'Fără detalii suplimentare.'}
                </div>
                <button class="action-button small-action-button" data-details-id="det-${index}" onclick="toggleDetails(this)">Vezi mai mult</button>
            </div>`;
    });

    container.innerHTML = htmlContent + '</div>';
    document.getElementById('recommendCoursesBtn').onclick = handleAIRecommendation;
}

// ==========================================================
// 5. MOTORUL AI DE CURSURI ȘI DOCUMENTAȚII (EXTERN + INTERN)
// ==========================================================

function getExternalResources(careerName) {
    const query = encodeURIComponent(careerName);
    return [
        {
            title: `Curs Complet: ${careerName}`,
            platform: "YouTube Education",
            url: `https://www.youtube.com/results?search_query=${query}+full+course+free`,
            description: `Tutoriale video gratuite și cursuri pas cu pas pentru ${careerName}.`,

        },
        {
            title: `Documentație și Roadmap ${careerName}`,
            platform: "Google / Learning",
            url: `https://www.google.com/search?q=${query}+learning+roadmap+documentation+free`,
            description: "Ghiduri oficiale și documentația necesară pentru a începe studiul.",

        }
    ];
}

async function handleAIRecommendation() {
    if (!lastRecommendations || lastRecommendations.length === 0) {
        alert("Finalizează chestionarul mai întâi!");
        return;
    }


    const searchInput = document.getElementById('courseSearchInput');
    if (searchInput) searchInput.value = "";


    const topCareer = lastRecommendations[0].careerName;
    showSection('coursesPanel');
    const coursesList = document.getElementById('coursesList');
    coursesList.innerHTML = `<p style="text-align:center;"> Se analizează resursele pentru <b>${topCareer}</b>...</p>`;

    try {
        const response = await fetch(`${BASE_URL}/api/courses/recommend-ai?careerName=${encodeURIComponent(topCareer)}`);
        let courses = await response.json();

        if (!courses || courses.length === 0) {
            courses = getExternalResources(topCareer);
            const info = document.createElement('p');
            info.style = "color: #856404; background: #fff3cd; padding: 10px; border-radius: 5px; font-size: 13px; grid-column: 1 / -1;";
            info.innerHTML = "ℹ️ Am selectat resurse externe gratuite deoarece nu s-au găsit cursuri în baza de date locală.";
            coursesList.appendChild(info);
        }

        renderCourseCards(courses);
    } catch (e) {
        renderCourseCards(getExternalResources(topCareer));
    }
}


function renderCourseCards(courses) {
    const coursesList = document.getElementById('coursesList');


    const infoMsg = coursesList.querySelector('p[style*="background: #fff3cd"]');
    coursesList.innerHTML = '';
    if (infoMsg) coursesList.appendChild(infoMsg);

    if (!courses || courses.length === 0) {
        coursesList.innerHTML += '<p style="grid-column: 1/-1; text-align:center;">Nu s-au găsit cursuri.</p>';
        return;
    }

    courses.forEach(course => {
        const card = document.createElement('div');
        card.className = 'course-card';


        card.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                <h4 style="margin: 0; color: #0056b3;">${course.title}</h4>
            </div>
            <p style="margin-top: 10px; font-size: 14px; color: #555;">
                ${course.description || 'Fără descriere disponibilă.'}
            </p>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px;">
                <span class="platform-badge" style="background: #eef2f7; color: #444; padding: 4px 8px; border-radius: 4px; font-size: 12px;">
                    ${course.platform || 'Curs Online'}
                </span>
                <a href="${course.url}" target="_blank" class="action-button" style="text-decoration: none; font-size: 12px; padding: 8px 12px; background-color: #28a745; color: white; border-radius: 5px;">
                    Deschide Resursa
                </a>
            </div>
        `;
        coursesList.appendChild(card);
    });
}
// ==========================================================
// 6. CĂUTARE MANUALĂ ȘI SINCRONIZARE
// ==========================================================

async function handleSearch() {
    const keyword = document.getElementById('courseSearchInput')?.value || "";
    const list = document.getElementById('coursesList');
    list.innerHTML = '<p>Se încarcă...</p>';
    try {
        const response = await fetch(`${BASE_URL}/api/courses/search?keyword=${encodeURIComponent(keyword)}`);
        const courses = await response.json();
        list.innerHTML = '';
        renderCourseCards(courses);
    } catch (e) { list.innerHTML = 'Eroare la căutare.'; }
}

async function handleSync() {
    if(!confirm("Sincronizezi cursurile de pe Exercism?")) return;
    try {
        const response = await fetch(`${BASE_URL}/api/courses/sync`);
        const resText = await response.text();
        alert(resText);
        handleSearch();
    } catch (e) { alert("Eroare la sincronizare."); }
}

// ==========================================================
// 7. EVENIMENTE DOM
// ==========================================================

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('registerForm')?.addEventListener('submit', (e) => {
        e.preventDefault();
        handleRegister(document.getElementById('reg_username').value, document.getElementById('reg_email').value, document.getElementById('reg_password').value);
    });

    document.getElementById('loginForm')?.addEventListener('submit', (e) => {
        e.preventDefault();
        handleLogin(document.getElementById('login_username').value, document.getElementById('login_password').value);
    });

    document.getElementById('showLogin')?.addEventListener('click', showLoginForm);
    document.getElementById('showRegister')?.addEventListener('click', showRegisterForm);
    document.getElementById('goToQuestionnaireBtn')?.addEventListener('click', () => {
        showSection('questionnaireContainer');
        loadSkillsForQuestionnaire();
    });

    document.getElementById('logoutBtn')?.addEventListener('click', () => location.reload());

    const recommendationsBtn = document.getElementById('showRecommendationsBtn');
        if (recommendationsBtn) {
            recommendationsBtn.onclick = (e) => {
                e.preventDefault();
                if (CURRENT_USER_ID) {
                    showSection('questionnaireContainer');
                    fetchAndDisplayRecommendations(CURRENT_USER_ID);
                }
                else {
                    alert("Te rugăm să te autentifici.");
                }
            };
        }
    });