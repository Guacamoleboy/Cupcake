export function callJavaMethod(url, element) {
    const data = {};
    for (let newData of element.attributes) {
        if (newData.name.startsWith('data-')) {
            const key = newData.name.substring(5);
            data[key] = newData.value;
        }
    }

    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams(data)
    })
        .then(response => response.text())
        .then(result => {
            console.log('Response:', result);
        })
        .catch(err => console.error(err));
}

window.callJavaMethod = callJavaMethod;