function createUser() {
    const formData = {
        firstName: document.querySelector('#user-firstname').value,
        lastName: document.querySelector('#user-lastname').value,
        email: document.querySelector('#user-email').value,
        password: document.querySelector('#user-password').value,
        passwordConfirm: document.querySelector('#user-passwordConfirm').value,
        age: document.querySelector('#user-age').value,
        roles: getSelectedRoles()
    };

    fetch('/api/v1/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())  // Парсим JSON ответ
        .then(data => {
            if (data.fieldErrors) {
                // Если есть ошибки валидации
                handleValidationErrors(data.fieldErrors);
            } else {
                // Успешное создание пользователя
                alert('User created successfully!');
                clearForm();  // Очищаем форму после успешного создания пользователя
            }
        })
        .catch(error => console.error('Error:', error));
}

function handleValidationErrors(errors) {
    // Очищаем предыдущие ошибки
    clearErrors();

    // Пробегаемся по полям с ошибками и отображаем их
    for (let field in errors) {
        const errorDiv = document.querySelector(`#${field}-error`);
        if (errorDiv) {
            errorDiv.style.display = 'block';
            errorDiv.textContent = errors[field];
        }
    }
}

function clearErrors() {
    // Прячем все сообщения об ошибках
    document.querySelectorAll('.error-message').forEach(errorDiv => {
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';
    });
}

function clearForm() {
    // Очищаем все поля формы
    document.querySelector('#user-form').reset();
}

function getSelectedRoles() {
    // Пример получения выбранных ролей
    return Array.from(document.querySelectorAll('#roles-select option:checked')).map(option => option.value);
}