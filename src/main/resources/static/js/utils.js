export const TOAST_VARIANT = {
    SUCCESS: 'success',
    ERROR: 'error'
};

const TOAST_VARIANT_TO_COLOR = {
    [TOAST_VARIANT.SUCCESS]: 'green',
    [TOAST_VARIANT.ERROR]: 'red'
};

export const showToast = (message, variant) => {
    const toastContainer = document.getElementById('toast-container');
    if (!toastContainer) {
        const container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.classList.add('toast');
    toast.classList.add('show');
    toast.style.backgroundColor = TOAST_VARIANT_TO_COLOR[variant];
    toast.style.position = 'absolute';

    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');

    const toastBody = document.createElement('div');
    toastBody.classList.add('toast-body');
    toastBody.innerText = message;

    toast.appendChild(toastBody);
    document.getElementById('toast-container').appendChild(toast);

    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, 3000);
}

export const setLocalStorageToastProperties = (message, variant) => {
    localStorage.setItem('toastMessage', message);
    localStorage.setItem('toastVariant', variant);
}

export const deleteLocalStorageToastProperties = () => {
    localStorage.removeItem('toastMessage');
    localStorage.removeItem('toastVariant');
}