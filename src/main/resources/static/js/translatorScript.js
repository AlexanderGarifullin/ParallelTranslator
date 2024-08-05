document.addEventListener('DOMContentLoaded', () => {
    const sourceLangSelect = document.getElementById('sourceLang');
    const targetLangSelect = document.getElementById('targetLang');
    const translateButton = document.getElementById('translateButton');
    const textArea = document.getElementById('text');
    const translatedTextArea = document.getElementById('translatedText');
    const errorMessage = document.getElementById('errorMessage');

    fetch('/api/languages')
        .then(response => response.json())
        .then(languages => {
            populateLanguageSelect(sourceLangSelect, languages);
            populateLanguageSelect(targetLangSelect, languages);
        })
        .catch(error => console.error('Error fetching languages:', error));

    translateButton.addEventListener('click', () => {
        const text = textArea.value;
        const sourceLang = sourceLangSelect.value;
        const targetLang = targetLangSelect.value;

        fetch('/api/translate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ text, sourceLang, targetLang })
        })
            .then(response => {
                if (response.ok) {
                    return response.text(); // Поскольку сервер возвращает текст, используйте response.text()
                } else {
                    return response.text().then(text => { throw new Error(text); });
                }
            })
            .then(data => {
                translatedTextArea.value = data;
                errorMessage.style.display = 'none';
            })
            .catch(error => {
                translatedTextArea.value = '';
                errorMessage.textContent = `Error: ${error.message}`;
                errorMessage.style.display = 'block';
            });
    });

    function populateLanguageSelect(selectElement, languages) {
        selectElement.innerHTML = '';
        languages.forEach(language => {
            const option = document.createElement('option');
            option.value = language.code;
            option.textContent = language.name;
            selectElement.appendChild(option);
        });
    }
});
