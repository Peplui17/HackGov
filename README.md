# 🏛️ HackGov — Painel de Gestão Urbana e Relatórios de Zeladoria

[![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-green.svg)]()
[![Tecnologias](https://img.shields.io/badge/stack-Java%20|%20Python%20|%20Oracle%20|%20FastAPI-blue)]()

Sistema integrado de gestão urbana desenvolvido para otimizar o atendimento de chamados de zeladoria (como buracos na via, iluminação pública e poda de árvores). A plataforma combina um **Backend em Java (Spring Boot)** para transações e segurança, um **Microsserviço de IA e Estatística em Python (FastAPI)** para análises preditivas e cálculo de SLAs, e uma interface web moderna.

---

## 🚀 Arquitetura do Sistema

O projeto é dividido em três camadas principais:

1. **Frontend (Interface Web):** Páginas HTML5, CSS3 e JavaScript consumindo as APIs REST.
2. **Backend Principal (Java / Spring Boot - Porta 8080):** Responsável pela autenticação, regras de negócio centrais e gerenciamento do fluxo de solicitações.
3. **Microsserviço Analítico (Python / FastAPI - Porta 8000):** Responsável por processamento estatístico clássico (média, desvio padrão de SLA), geração de gráficos (histogramas) e projeções de Machine Learning para volume de demandas por bairro.
4. **Banco de Dados:** Oracle Database.

---

## 📋 Pré-requisitos e Requisitos da Aplicação

Antes de iniciar a execução do projeto, certifique-se de ter instalado em sua máquina:

* **Java JDK 17** ou superior (para o Spring Boot).
* **Python 3.10+** (para o microsserviço FastAPI).
* **Oracle Database** (ativo localmente ou via container Docker).
* **Gerenciador de Dependências Maven** (geralmente embutido no Spring).
* **Gerenciador Pip** (para bibliotecas Python).
* Extensão **Live Server** (VS Code) ou similar para rodar o frontend estático.

---

## ⚙️ Configuração e Instalação

### 1. Clonando o Repositório
```bash
git clone [https://github.com/Peplui17/HackGov-Enterprise-Challenge.git](https://github.com/Peplui17/HackGov-Enterprise-Challenge.git)
cd HackGov-Enterprise-Challenge
