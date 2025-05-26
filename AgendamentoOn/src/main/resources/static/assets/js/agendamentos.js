const agendamentos = [
    { cliente: "João Silva", data: "2025-05-10", horario: "14:00", servico: "Consulta" },
    { cliente: "Maria Souza", data: "2025-05-11", horario: "10:30", servico: "Retorno" }
  ];
  
  function renderizarAgendamentos() {
    const tabela = document.getElementById("tabelaAgendamentos");
    tabela.innerHTML = "";
  
    agendamentos.forEach((item, index) => {
      const linha = `
        <tr>
          <td>${item.cliente}</td>
          <td>${item.data}</td>
          <td>${item.horario}</td>
          <td>${item.servico}</td>
          <td>
            <button class="btn btn-sm btn-warning me-1">Editar</button>
            <button class="btn btn-sm btn-danger" onclick="remover(${index})">Excluir</button>
          </td>
        </tr>
      `;
      tabela.innerHTML += linha;
    });
  }
  
  function remover(index) {
    agendamentos.splice(index, 1);
    renderizarAgendamentos();
  }
  
  function novoAgendamento() {
    alert("Aqui abriria um modal ou redirecionamento para novo agendamento.");
  }
  
  renderizarAgendamentos();
  