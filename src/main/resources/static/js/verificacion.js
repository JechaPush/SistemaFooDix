// Función para mostrar el formulario de correo
function showEmailForm() {
    document.getElementById('emailForm').style.display = 'block';
    document.getElementById('verificationForm').style.display = 'none';
}

// Función para mostrar el formulario de verificación
function showVerificationForm() {
    document.getElementById('emailForm').style.display = 'none';
    document.getElementById('verificationForm').style.display = 'block';
}

// Manejar el envío del formulario de correo
document.getElementById('emailForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const email = document.getElementById('email').value;
    const submitBtn = this.querySelector('button[type="submit"]');
    
    // Validar email
    if (!email || !isValidEmail(email)) {
        showAlert('Por favor ingresa un correo electrónico válido', 'danger');
        return;
    }
    
    // Mostrar el modal con la animación
    const codeModal = new bootstrap.Modal(document.getElementById('codeModal'));
    codeModal.show();
    
    // Deshabilitar botón
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
    
    try {
        // Llamada REAL al backend
        const formData = new URLSearchParams();
        formData.append('email', email);
        
        const response = await fetch('/auth/send-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        });
        
        const result = await response.text();
        const data = JSON.parse(result);
        
        if (data.success) {
            // Éxito: mostrar formulario de verificación
            codeModal.hide();
            showVerificationForm();
            showAlert('Código enviado exitosamente', 'success');
        } else {
            // Error: mostrar mensaje
            codeModal.hide();
            showAlert(data.error || 'Error al enviar el código', 'danger');
        }
        
    } catch (error) {
        console.error('Error:', error);
        codeModal.hide();
        showAlert('Error de conexión. Intenta nuevamente.', 'danger');
    } finally {
        // Rehabilitar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Enviar código';
    }
});

// Manejar el envío del formulario de verificación
document.getElementById('verificationForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const code = document.getElementById('code').value;
    const submitBtn = this.querySelector('button[type="submit"]');
    
    if (!code || code.length !== 6) {
        showAlert('Por favor ingresa un código válido de 6 dígitos', 'danger');
        return;
    }
    
    // Deshabilitar botón
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    
    try {
        const formData = new URLSearchParams();
        formData.append('code', code);
        
        const response = await fetch('/auth/verify-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        });
        
        const result = await response.text();
        const data = JSON.parse(result);
        
        if (data.success) {
            showAlert('Código verificado exitosamente', 'success');
            // Obtener el correo ingresado (sigue presente en el DOM aunque el formulario esté oculto)
            const email = document.getElementById('email') ? document.getElementById('email').value : '';
            // Redirigir después de 1 segundo incluyendo el email como query param para que registro.html lo use
            setTimeout(() => {
                const target = (data.redirectUrl || '/registro');
                const url = email ? `${target}?email=${encodeURIComponent(email)}` : target;
                window.location.href = url;
            }, 1000);
        } else {
            showAlert(data.error || 'Código inválido', 'danger');
        }
        
    } catch (error) {
        console.error('Error:', error);
        showAlert('Error de conexión. Intenta nuevamente.', 'danger');
    } finally {
        // Rehabilitar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-check"></i> Verificar código';
    }
});

// Función para mostrar alertas
function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    // Insertar después del título
    const cardBody = document.querySelector('.card-body');
    const title = cardBody.querySelector('h2').parentElement;
    title.parentNode.insertBefore(alertDiv, title.nextSibling);
    
    // Auto-remover después de 5 segundos
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.remove();
        }
    }, 5000);
}

// Función para validar email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    showEmailForm();
});