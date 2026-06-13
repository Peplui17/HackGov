// src/api/solicitacaoApi.js

export const enviarSolicitacao = async (dados) => {
    // Simulação de delay de rede (1.5 segundos) para visualizar o estado de "Carregando"
    await new Promise(resolve => setTimeout(resolve, 1500));

    // Simulação de erro caso digite a palavra "erro" (para testar a tela de falha)
    if (dados.descricao.toLowerCase().includes("erro")) {
        throw new Error("Falha na comunicação com o servidor. Tente novamente.");
    }

    // Simulação de sucesso (Gera um número de protocolo aleatório)
    return "HCK-" + Math.random().toString(36).substr(2, 8).toUpperCase();
};