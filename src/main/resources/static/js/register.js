import { performRequestWithBody } from "./api.js";
import { setLocalStorageToastProperties, TOAST_VARIANT } from "./utils.js";

const REGISTER_FORM = document.getElementById('register-form');
const REGISTER_BUTTON = document.getElementById('register-button');
const PASSWORD_INPUT = document.getElementById('password');
const PASSWORD_FEEDBACK = PASSWORD_INPUT.nextElementSibling;
const REPEAT_PASSWORD_INPUT = document.getElementById('repeat-password');
const REPEAT_PASSWORD_FEEDBACK = REPEAT_PASSWORD_INPUT.nextElementSibling;
const EMAIL_INPUT = document.getElementById('email');
const EMAIL_FEEDBACK = EMAIL_INPUT.nextElementSibling;

REGISTER_BUTTON.addEventListener('click', (event) => {
    clearValidity();
})

REGISTER_FORM.addEventListener('submit', (event) => {
    event.preventDefault();

    const userPayload = {email: EMAIL_INPUT.value, password: PASSWORD_INPUT.value};
    validatePassword();
    validateRepeatPassword();
    performRequestWithBody('/users/register', 'POST', JSON.stringify(userPayload), onRegisterSuccess, onRegisterFail);

    if (REGISTER_FORM.checkValidity() === false) {
        event.stopPropagation();
    }
    REGISTER_FORM.classList.add('was-validated');
});

const onRegisterSuccess = (data) => {
    REGISTER_FORM.reset();
    REGISTER_FORM.classList.remove('was-validated');
    setLocalStorageToastProperties('Successfully registered', TOAST_VARIANT.SUCCESS)
    window.location.replace("/login");
}

const onRegisterFail = () => {
    EMAIL_INPUT.setCustomValidity("Email already exists in database");
    EMAIL_FEEDBACK.innerText = "Email already exists in database";
    EMAIL_INPUT.reportValidity();
}

const validatePassword = () => {
    let valid = true;
    let message = '';

    if (PASSWORD_INPUT.value.length < 8) {
        valid = false;
        message = "Password must be at least 8 characters long.";
    } else if (!/[A-Z]/.test(PASSWORD_INPUT.value)) {
        valid = false;
        message = "Password must contain at least one uppercase letter.";
    } else if (!/[a-z]/.test(PASSWORD_INPUT.value)) {
        valid = false;
        message = "Password must contain at least one lowercase letter.";
    } else if (!/[0-9]/.test(PASSWORD_INPUT.value)) {
        valid = false;
        message = "Password must contain at least one number.";
    } else if (!/[!@#$%^&+=]/.test(PASSWORD_INPUT.value)) {
        valid = false;
        message = "Password must contain at least one special character (@#$%^&+=).";
    }

    if (!valid) {
        PASSWORD_INPUT.setCustomValidity(message);
        PASSWORD_FEEDBACK.innerText = message;
        PASSWORD_INPUT.reportValidity();
    }
}

const validateRepeatPassword = () => {
    if(PASSWORD_INPUT.value != REPEAT_PASSWORD_INPUT.value) {
        REPEAT_PASSWORD_INPUT.setCustomValidity('Passwords do not match.');
        REPEAT_PASSWORD_FEEDBACK.innerText = 'Passwords do not match.';
        REPEAT_PASSWORD_INPUT.reportValidity();
    }
}

const clearValidity = () => {
    clearEmailValidity();
    clearPasswordValidity();
    clearRepeatPasswordValidity();
}

const clearEmailValidity = () => {
    EMAIL_INPUT.setCustomValidity('');
    EMAIL_FEEDBACK.innerText = '';
    EMAIL_INPUT.reportValidity();
}

const clearPasswordValidity = () => {
    PASSWORD_INPUT.setCustomValidity('');
    PASSWORD_FEEDBACK.innerText = '';
    PASSWORD_INPUT.reportValidity();
}

const clearRepeatPasswordValidity = () => {
    REPEAT_PASSWORD_INPUT.setCustomValidity('');
    REPEAT_PASSWORD_FEEDBACK.innerText = '';
    REPEAT_PASSWORD_INPUT.reportValidity();
}