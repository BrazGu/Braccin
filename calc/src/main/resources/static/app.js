document.getElementById('validarBtn').addEventListener('click', function () {
    const tipo = document.getElementById('tipo').value;
    const documento = document.getElementById('documento').value.trim();

    if (!documento) {
        alert("Por favor, insira um documento.");
        return;
    }

    fetch(`http://localhost:8080/api/validar/${tipo}/${documento}`)
        .then(response => response.json())
        .then(isValido => {
            const resultadoDiv = document.getElementById('resultado');
            if (isValido) {
                resultadoDiv.textContent = `Documento ${documento} é válido.`;
                resultadoDiv.style.color = 'green';
            } else {
                resultadoDiv.textContent = `Documento ${documento} é inválido.`;
                resultadoDiv.style.color = 'red';
            }
        })
        .catch(error => {
            console.error("Erro na requisição", error);
            alert("Erro ao validar documento.");
        });
});
