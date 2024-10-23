// function updateUser() {
//     const userId = document.querySelector('#id').value;
//     const formData = {
//         id: userId,
//         firstName: document.querySelector('#firstName').value,
//         lastName: document.querySelector('#lastName').value,
//         // остальные поля
//     };
//
//     fetch("/api/v1/admin/users/${userId}", {
//         method: 'PUT',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify(formData)
//     }
//     )
//         .then(response => response.json())
//         .then(data => {
//             // Обработка успешного ответа
//             console.log('User updated:', data);
//             // Можно обновить данные на странице, если требуется
//         })
//         .catch(error => console.error('Error updating user:', error));
// }