document.querySelector("form").addEventListener("submit", async function(e) {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const senha = document.getElementById("senha").value;

  // Verificação de usuário teste (sem chamar o backend)
  if (email === "teste@teste.com" && senha === "1234") {
    const usuarioTeste = {
      id: 0,
      nome: "Usuário de Teste",
      email: "teste@teste.com",
      tipo: "teste"
    };
    localStorage.setItem("usuario", JSON.stringify(usuarioTeste));
    console.log("Usuário teste logado:", usuarioTeste);
    window.location.href = "agendamentos.html";
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, senha })
    });

    if (response.ok) {
      const usuario = await response.json();
      console.log("Usuário logado:", usuario);
      localStorage.setItem("usuario", JSON.stringify(usuario));
      window.location.href = "agendamentos.html";
    } else {
      const msg = await response.text();
      alert("Erro ao fazer login: " + msg);
    }
  } catch (err) {
    alert("Erro ao conectar com o servidor.");
    console.error(err);
  }
});
