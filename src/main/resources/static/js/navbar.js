import { deleteLocalStorageToastProperties, showToast } from "./utils.js";

const NAV_LOGIN = document.getElementById('nav-login');
const NAV_LOGOUT = document.getElementById('nav-logout');
const NAV_TASKS = document.getElementById('nav-tasks');

NAV_LOGOUT.addEventListener('click', () => {
    document.cookie = "loggedIn=false";
    toggleNav();
});

const toggleNav = () => {
    const isLoggedIn = document.cookie === "loggedIn=true";
    NAV_LOGIN.style.display = isLoggedIn ? "none" : "block";
    NAV_TASKS.style.display = isLoggedIn ? "block" : "none";
    NAV_LOGOUT.style.display = isLoggedIn ? "block" : "none";
}

document.addEventListener('DOMContentLoaded', () => {
    const toastMessage = localStorage.getItem('toastMessage');
    const toastVariant = localStorage.getItem('toastVariant');
    if (toastMessage && toastVariant) {
        showToast(toastMessage, toastVariant);
        deleteLocalStorageToastProperties();
    }
});

(() => {
    toggleNav();
})();

