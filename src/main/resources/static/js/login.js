function login(event) {
    event.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/login", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    })
        .then(response => {
            if (response.ok) return response.text();
            else throw new Error("Credenciais inválidas");
        })
        .then(text => {
            alert(text);
            // Redireciona para painel, se quiseres
            // window.location.href = "/painel.html";
        })
        .catch(() => alert("Login falhou! Verifica os dados."));
}
