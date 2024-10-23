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
                    handleErrors(errors);
                });
            } else {
                alert('User created successfully!')
            }
        })
        .catch(error => {
            console.error('Error creating user:', error)
        });
}

function handleErrors(errors) {
    // Проверяем, есть ли ошибки для поля "firstName"
    if (errors.firstName) {
        const firstNameErrorDiv = document.querySelector('#firstName-error');
        firstNameErrorDiv.style.display = 'block'; // Показываем блок с ошибкой
        firstNameErrorDiv.innerText = errors.firstName; // Отображаем текск ошибки
    } else {
        document.querySelector('#firstName-error').style.display = 'none'; // Прячем блок с ошибкой, если её нет
    }

    if (errors.lastName) {
        const lastNameErrorDiv = document.querySelector('#lastName-error');
        lastNameErrorDiv.style.display = 'block';
        lastNameErrorDiv.innerText = errors.lastName;
    } else {
        document.querySelector('#lastName-error').style.display = 'none';
    }

    if (errors.age) {
        const ageErrorDiv = document.querySelector('#age-error');
        ageErrorDiv.style.display = 'block';
        ageErrorDiv.innerText = errors.age;
    } else {
        document.querySelector('#age-error').style.display = 'none';
    }

    if (errors.email) {
        const ageErrorDiv = document.querySelector('#email-error');
        ageErrorDiv.style.display = 'block';
        ageErrorDiv.innerText = errors.age;
    } else {
        document.querySelector('#email-error').style.display = 'none';
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
