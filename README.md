# BackEnd EducaSenai TCC 🚀

[![Coverage Status](https://img.shields.io/codecov/c/github/seu-usuario/seu-repo?style=for-the-badge)](link-para-codecov)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](link-para-licenca)

> API RESTful para gerenciar usuários (Alunos, Mentores), reuniões, feedbacks e trilhas de aprendizado da plataforma EducaSenai. Construído com Java, Spring Boot e MySQL.

---

## 📖 Índice
* [Sobre o projeto](#-sobre-o-projeto)
* [✨ Features](#-features)
* [🛠️ Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [📋 Pré-Requisitos](#-pre-requisitos)
* [⚙️ Instalação e Configuração](#-instalacao-e-configuracao)
* [🚀 Rodando o projeto](#-rodando-o-projeto)
* [📄 Documentação da API](#-documentacao-da-api)
* [📁 Estrutura de pastas](#-estrutura-de-pastas)
* [🧪 Testes](#-testes)
* [☁️ Deploy](#-deploy)
* [🤝 Como contribuir](#-como-contribuir)
* [📜 Licença](#-licenca)
* [🙏 Agradecimentos / Autores](#-agradecimentos--autores)

---

## <a id="-sobre-o-projeto"></a> 💻 Sobre o projeto

Este projeto fornece a API RESTful para a plataforma EducaSenai TCC. Ela é responsável pela autenticação de usuários (Alunos, Mentores), gerenciamento de dados de reuniões, feedbacks, perfis de usuários, trilha de aprendizado e uploads de arquivos. O objetivo é fornecer uma base segura, organizada e perfomática para aplicação frontend e futuras integrações.

---

## <a id="-features"></a> ✨ Features

* ✅ Autenticação e Autorização baseada em JWT (Jason Web Tokens) com Spring Security.
* ✅ Distinção de papéis (Roles): ALUNO e MENTOR.
* ✅ CRUD Completo para usuários (com uploads de avatar).
* ✅ Gerenciamento de Reuniões (agendamentos, aprovação, cancelamento, recusa).
* ✅ Sistemas de Feedbacks entre Mentores e Alunos.
* ✅ Gerenciamento de Trilhas de Aprendizado (se aplicável).
* ✅ Validação de dados de entrada.
* ✅ Tratamentos de erros padronizados.
* ✅ Documentação da API interativa com SWAGGER UI (OpenAPI).

---

## <a id="-tecnologias-utilizadas"></a> 🛠️ Tecnologias Utilizadas
* **Linguagem** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/32b5f793-c7f7-4114-8441-938fcd7b5956" />
Java (v17+)
* **Framework Principal** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/c59302a9-7b8d-41bc-bc00-2a54e6ce80ce" />
Spring Boot (v3.2.4)
* **Banco de Dados** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/574cf14b-2009-4068-b55b-45431fe94a89" />
Mysql (v8.0+)
* **ORM** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/c59302a9-7b8d-41bc-bc00-2a54e6ce80ce" />
Spring Data JPA / Hibernate
* **Autenticação/Segurança** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/8a465678-35cd-405e-8c90-c3bfba3aaf28" />
Spring Security, JWT (com biblioteca como `jjwt`)
* **Documentação API** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/1abf89f6-71a9-4a95-b80c-c9a667e2ab70" />
SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
* **Testes** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/32b5f793-c7f7-4114-8441-938fcd7b5956" />
JUnit 5
* **Build Tool** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/e521aad7-3413-4ba8-8f29-86710678a16a" />
Maven
* **Validação** <img width="20" height="20" alt="image" src="https://github.com/user-attachments/assets/c59302a9-7b8d-41bc-bc00-2a54e6ce80ce" />
Spring Validation (Hibernate Validator)

* **Outras bibliotecas importantes:** (BCrypt, Spring Web, MySQL Connector)

---

## <a id="-pre-requisitos"></a> 📋 Pré-Requisitos

Antes de começar, você vai precisar ter instalado em sua máquina:
* [JDK (Java Development Kit)](https://www.oracle.com/java/technologies/downloads/) (Versão 17 ou superior)
* [Maven](https://maven.apache.org/download.cgi) (ou a IDE já o inclui, como Eclipse/STS/IntelliJ)
* [MySQL Server](https://dev.mysql.com/downloads/mysql/) (Versão 8.0 ou superior) ou Docker com a imagem do MySQL.
* [Git](https://git-scm.com/downloads)
* [Postman](https://www.postman.com/downloads/)

## <a id="-configuracao-e-instalacao"></a> ⚙️ Configuração e Instalação

Siga os passos abaixo para configurar o ambiente de desenvolvimento:

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/DevJulioo/BackEndTCC.git](https://github.com/DevJulioo/BackEndTCC.git)
    cd BackEndTCC
    ```

2.  **Configure o Banco de Dados:**
    * Certifique-se de que seu servidor MySQL esteja rodando.
    * Crie um banco de dados (schema) para o projeto, por exemplo: `educasenai_tcc_db`. Use `utf8mb4` como charset e collation.
    * **Exemplo SQL:** `CREATE DATABASE educasenai_tcc_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`

3.  **Configure as Variáveis de Ambiente (ou `application.properties`):**
    * Localize o arquivo `src/main/resources/application.properties` (ou `application.yml`).
    * Configure as propriedades do banco de dados e a chave secreta do JWT. **NÃO** coloque senhas diretamente no Git se o repositório for público; use variáveis de ambiente se possível para produção.

    **Exemplo de `application.properties`:**
    ```properties
    # Configurações do Banco de Dados MySQL
    spring.datasource.url=jdbc:mysql://localhost:3306/educasenai_tcc_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
    spring.datasource.username=SEU_USUARIO_MYSQL
    spring.datasource.password=SUA_SENHA_MYSQL
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    # Configurações do Hibernate/JPA
    spring.jpa.hibernate.ddl-auto=update # 'update' para desenvolvimento, 'validate' ou 'none' para produção
    spring.jpa.show-sql=true # Mostrar SQL gerado no console (útil para debug)
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect # Dialeto para MySQL 8+

    # Porta da Aplicação (Opcional, padrão é 8080)
    server.port=8080

    # Chave Secreta para JWT (MUITO IMPORTANTE: Use um valor longo e aleatório)
    api.security.token.secret=SUA_CHAVE_SECRETA_SUPER_SEGURA_E_LONGA_AQUI

    # Configuração de Uploads (Exemplo)
    # file.upload-dir=./uploads

    # Configuração Springdoc/Swagger (Opcional, geralmente funciona por padrão)
    # springdoc.api-docs.path=/api-docs
    # springdoc.swagger-ui.path=/swagger-ui.html
    ```
    *(**Importante:** Documente todas as propriedades essenciais)*

4.  **Build do Projeto (Maven):**
    * Abra um terminal na pasta raiz do projeto.
    * Execute o comando Maven para baixar dependências e compilar:
        ```bash
        mvn clean install
        ```
    * (Se estiver usando uma IDE, ela geralmente faz isso automaticamente ao importar o projeto).

---

## <a id="-rodando-o-projeto"></a> 🚀 Rodando o Projeto

Após a instalação e configuração, você pode rodar o projeto:

* **Pela IDE (Recomendado para Desenvolvimento):**
    * Encontre a classe principal da aplicação (geralmente `SeuProjetoApplication.java` com o método `main`) e execute-a como "Java Application".
    * O Spring Boot DevTools (se incluído) geralmente habilita o hot-reload.

* **Via Maven:**
    ```bash
    mvn spring-boot:run
    ```

* **Via JAR (Após Build):**
    ```bash
    # Primeiro, gere o JAR com mvn clean install
    java -jar target/seu-projeto-backend-0.0.1-SNAPSHOT.jar
    ```

O servidor estará disponível em `http://localhost:8080` (ou a porta definida em `server.port`).

---

## <a id="-documentacao-da-api"></a> 📄 Documentação da API

A Documentação interativa da API está disponível via SWAGGER UI:

* **Acesse:** `http://localhost:8080/swagger-ui.html` (após iniciar o servidor).

A Documentação é gerada automaticamente pela biblioteca `springdoc-openapi` com base nos seus controllers e anotações (como `@Operation`, `@ApiResponse`, etc., se você as usar).

**Endpoints Principais (Exemplos)**

* **Autenticação:**
    * `POST /auth/login`: Autentica um usuário e retorna um token JWT.
    * `POST /auth/register`: Registra um novo usuário (Aluno ou Mentor).
* **Usuários:**
    * `GET /api/users/me`: Retorna os dados do usuário logado.
    * `PUT /api/users/me`: Atualiza os dados do usuário logado.
    * `POST /api/users/me/avatar`: Faz upload do avatar do usuário logado.
* **Reuniões:**
    * `POST /api/reunioes`: Aluno solicita uma reunião.
    * `GET /api/reunioes`: Lista reuniões (visão diferente para Aluno e Mentor).
    * `PATCH /api/reunioes/{id}/status`: Mentor aprova/recusa/cancela reunião.
* **Mentores:**
    * `GET /api/mentores`: Lista mentores disponíveis (para Aluno agendar).
* **Feedbacks:**
    * `POST /api/feedbacks`: Aluno envia feedback para Mentor (ou vice-versa).
    * `GET /api/feedbacks`: Lista feedbacks recebidos/enviados.

*(Liste os endpoints mais importantes e seus propósitos gerais)*

---

## <a id="-estrutura-de-pastas"></a> 📁 Estrutura de Pastas

A estrutura de pastas principal do projeto segue as convenções do Spring Boot:
````
seu-repositorio-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/projeto/tcc/
│   │   │       ├── controllers/    # Camada de controle REST (recebe requisições HTTP)
│   │   │       ├── services/       # Lógica de negócio e regras
│   │   │       ├── repositories/   # Interfaces Spring Data JPA (acesso ao DB)
│   │   │       ├── models/         # Entidades JPA (@Entity)
│   │   │       ├── dto/            # Data Transfer Objects (para requisições e respostas)
│   │   │       ├── security/       # Configuração do Spring Security, TokenService, SecurityFilter
│   │   │       ├── config/         # Outras configurações (ex: CORS, Swagger)
│   │   │       ├── exceptions/     # Classes de exceção customizadas
│   │   │       └── TccApplication.java # Classe principal da aplicação
│   │   └── resources/
│   │       ├── static/         # Arquivos estáticos (CSS, JS, Imagens - se houver)
│   │       ├── templates/      # Templates HTML (se usar Thymeleaf, etc.)
│   │       └── application.properties # Arquivo de configuração principal
│   └── test/
│       └── java/
│           └── com/projeto/tcc/ # Testes unitários e de integração
├── .gitignore          # Arquivos ignorados pelo Git
├── mvnw                # Maven Wrapper (executável)
├── mvnw.cmd            # Maven Wrapper (Windows)
├── pom.xml             # Arquivo de configuração do Maven (dependências, build)
└── README.md           # Este arquivo :)

`````

---

## <a id="testes"></a> 🧪 Testes

Para rodar os testes automatizados (Junit), use o comando Maven
```bash
mvn maven
````

---

## <a id="deploy"></a> ☁️ Deploy

* **Instruções Gerais**
Geralmente envolve gerar o JAR ````mvn clean package```` ou fazer um upload em uma plataforma de CI/CD para automatizar o build ou deploy

* **Configuração de produção**
Lembre - se de usar um perfil ```` aplication-properties ```` ou variáveis de ambiente na plataforma de deploy para configurar o banco de dados de produção e a chave secreta JWT de forma segura. Defina ``` spring.jpa.hibernate.ddl-auto=validate ``` ou ``` none ``` em produção.

## <a id="-como-contribuir"></a> 🤝 Como Contribuir

* **Este é um projeto de TCC, mas se houver interesse em contribuir:**

1.  *Faça um Fork do projeto.*

2.  *Crie uma nova branch ` (git checkout -b feature/sua-feature) `*

3.  *Faça commit das suas alterações* ` (git commit -m 'Adiciona nova feature X') `

4.  *Faça push para a sua branch* ` (git push origin feature/sua-feature) `

5.  Abra um **Pull Request**

## <a id="-agradecimentos--autores"></a> 🙏 Agradecimentos / Autores

* **[Júlio Cesar de Souza Moura / EducaSenai]** - Desenvolvedor(es) - [[Link para GitHub/Contato]](https://github.com/DevJulioo)
* Agradecimentos ao **[Fiama e Átila]**, **[SENAI]** pela oportunidade e suporte.


