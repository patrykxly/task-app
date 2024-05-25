export const performRequestWithoutBody = (url, method, onSuccess, onFail) => {
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return onSuccess(response);
            } else {
                const error = new Error(response.statusText);
                error.response = response;
                throw error;
            }
        })
        .catch(error => {
            onFail(error);
        });
}

export const performRequestWithBody = (url, method, data, onSuccess, onFail) => {
    fetch(url, {
        method: method,
        body: data,
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return onSuccess(response);
            } else {
                const error = new Error(response.statusText);
                error.response = response;
                error.data = data;
                throw error;
            }
        })
        .catch(error => {
            onFail(error);
        });
}
