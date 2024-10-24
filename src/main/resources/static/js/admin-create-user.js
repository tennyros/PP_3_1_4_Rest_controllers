function createUser() {
    const formData = {
        firstName: document.querySelector('#user-firstname').value,
        lastName: document.querySelector('#user-lastname').value,
        age: document.querySelector('#user-age').value,
        email: document.querySelector('#user-email').value,
        password: document.querySelector('#user-password').value,
        passwordConfirm: document.querySelector('#user-passwordConfirm').value,
        roles: getSelectedRoles()
    };

    // Проверка на наличие выбранных ролей
    if (formData.roles.length === 0) {
        document.querySelector('#roles-error').style.display = 'block';
        return;
    } else {
        document.querySelector('#roles-error').style.display = 'none';
    }

    // Отправляем данные на сервер через fetch
    fetch('/api/v1/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errors => {
                    handleValidationErrors(errors);
                    throw new Error('Validation or server error!')
                });
            }
            return response.json();
        })
        .then(data => {
            alert('User created successfully!');
            clearForm();
        })
        .catch(error => {
            console.error('Error creating user:', error)
        });
}

function handleValidationErrors(errors) {
    clearErrors();

    for (let field in errors.fieldErrors) {
        const errorDiv = document.querySelector(`#${field}-error`);
        if (errorDiv) {
            errorDiv.style.display = 'block';
            errorDiv.textContent = errors.fieldErrors[field];
        }
    }
}

function getSelectedRoles() {
    const selectedRoles = [];
    const rolesSelect = document.querySelector('#roles');

    // Проходим по всем выбранным элементам <option>
    for (let option of rolesSelect.options) {
        if (option.selected) {
            selectedRoles.push({roleName: option.value});
        }
    }
    return selectedRoles;
}

function clearErrors() {
    document.querySelectorAll('.error-message').forEach(errorDiv => {
        errorDiv.style.display = 'none';
        errorDiv.textContent = '';
    })
}
