import { performRequestWithBody } from "./api.js";
import { setLocalStorageToastProperties, TOAST_VARIANT, showToast } from "./utils.js";

const LOGIN_FORM = document.getElementById('login-form');
const EMAIL_INPUT = document.getElementById('email');
const PASSWORD_INPUT = document.getElementById('password');

LOGIN_FORM.addEventListener('submit', (event) => {
    event.preventDefault();

    const userPayload = {email: EMAIL_INPUT.value, password: PASSWORD_INPUT.value};
    performRequestWithBody('/users/login', 'POST', JSON.stringify(userPayload), onLoginSuccess, onLoginFail);
});

const onLoginSuccess = () => {
    setLocalStorageToastProperties('Successfully logged in', TOAST_VARIANT.SUCCESS)
    document.cookie = "loggedIn=true";
    window.location.replace("/");
}

const onLoginFail = () => {
    showToast('Invalid credentials', TOAST_VARIANT.ERROR);
}