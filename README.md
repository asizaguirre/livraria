# 📚 Livraria: Sistema de Gerenciamento de Biblioteca Moderno

## Visão Geral

O projeto `Livraria` é um sistema de gerenciamento de biblioteca moderno e abrangente, meticulosamente projetado com foco em uma arquitetura robusta, código limpo e melhores práticas. Ele oferece uma experiência fluida para o gerenciamento de livros, autores e assuntos através de uma poderosa API de backend e um frontend Angular intuitivo. Todo o sistema é conteinerizado usando Docker, garantindo fácil configuração, implantação consistente e escalabilidade.

**Agradecimentos:** Gostaríamos de expressar nosso sincero agradecimento a **Paulo Jacques** por fornecer um material excelente e valioso que foi fundamental para a implementação deste sistema. Sua contribuição foi inestimável.

Este sistema incorpora um compromisso com o desenvolvimento de software de alta qualidade, utilizando uma abordagem inspirada em microsserviços para entregar uma solução funcional, manutenível e extensível.

## 🚀 Arquitetura

O sistema segue uma arquitetura desacoplada e inspirada em microsserviços, composta por um frontend robusto, uma poderosa API de backend e um banco de dados PostgreSQL dedicado, tudo orquestrado via Docker Compose e exposto através do Nginx. Este design promove modularidade, escalabilidade e facilidade de manutenção, refletindo as melhores práticas em desenvolvimento de sistemas distribuídos.

### Componentes e Tecnologias

*   **Frontend (Aplicação Web)**
    *   **Framework:** Angular v21.0.0
    *   **Linguagem:** TypeScript
    *   **Estilização:** Bootstrap v5.3.8 para uma interface de usuário responsiva e moderna, complementado por SCSS para estilização personalizada.
    *   **Melhorias na Experiência do Usuário:** `ngx-mask` v20.0.3 para mascaramento de entrada intuitivo.
    *   **Renderização no Lado do Servidor (SSR):** Implementado usando Angular SSR para melhorar o desempenho, otimizar o SEO e proporcionar uma experiência de carregamento inicial mais rápida.
    *   **Implantação:** Empacotado em uma imagem Docker leve e servido eficientemente pelo Nginx.

*   **Backend (API RESTful)**
    *   **Framework:** Spring Boot v3.2.0, fornecendo uma base sólida para aplicações de nível empresarial.
    *   **Linguagem:** Java 17, aproveitando os recursos modernos da linguagem para um código limpo e eficiente.
    *   **Ferramenta de Build:** Maven, gerenciando dependências do projeto e o ciclo de vida da construção.
    *   **Persistência de Dados:** Spring Data JPA com Hibernate para um robusto Mapeamento Objeto-Relacional (ORM), facilitando a interação perfeita com o banco de dados.
    *   **Migrações de Banco de Dados:** Flyway para evolução de esquema de banco de dados confiável e com controle de versão, garantindo consistência entre os ambientes.
    *   **Documentação da API:** Springdoc OpenAPI (integrado com Swagger UI) fornece documentação interativa para todos os endpoints da API, simplificando o desenvolvimento e a integração.
    *   **Mapeamento de Objetos:** MapStruct para mapeamento de DTO (Data Transfer Object) para Entidade de alto desempenho e tipo seguro, minimizando o código boilerplate.
    *   **Geração de Relatórios:** JasperReports para geração de relatórios dinâmicos e personalizáveis (por exemplo, listagens de livros por autor), atendendo a necessidades complexas de business intelligence.
    *   **Validação:** Jakarta Bean Validation garante a integridade e consistência dos dados em todas as requisições da API.
    *   **Qualidade e Produtividade do Código:** Lombok é usado para reduzir o código boilerplate (por exemplo, getters, setters, construtores), promovendo classes mais limpas e legíveis.
    *   **Ferramentas de Desenvolvimento:** Spring Boot DevTools para hot reloading e ciclos de feedback mais rápidos durante o desenvolvimento.

*   **Banco de Dados**
    *   **Tipo:** PostgreSQL v13-alpine, um poderoso banco de dados relacional de código aberto, escolhido por sua confiabilidade e recursos avançados.
    *   **Finalidade:** O repositório central para todos os dados da aplicação, incluindo livros, autores e assuntos.

*   **Proxy Reverso / Servidor Web**
    *   **Tipo:** Nginx (alpine), um servidor web de alto desempenho e proxy reverso.
    *   **Finalidade:** Serve eficientemente os ativos estáticos do frontend Angular. Gerencia o roteamento de URL, garantindo que todas as rotas do lado do cliente sejam corretamente tratadas pelo arquivo `index.html` (roteamento de Single Page Application).

*   **Conteinerização e Orquestração**
    *   **Ferramenta:** Docker & Docker Compose
    *   **Finalidade:** Cada componente da aplicação (frontend, backend, banco de dados) é encapsulado em seu próprio contêiner Docker, garantindo consistência ambiental do desenvolvimento à produção. O Docker Compose orquestra esses contêineres, gerenciando seu ciclo de vida, configurações de rede e intercomunicação, simplificando a implantação de toda a aplicação multi-serviço.

### Fluxo de Comunicação

1.  **Acesso do Usuário:** Os usuários interagem com a aplicação através de seus navegadores web, que se conectam ao servidor web Nginx. O Nginx serve eficientemente os arquivos estáticos do frontend Angular.
2.  **Interação Frontend-Backend:** O frontend Angular realiza chamadas assíncronas (AJAX) para a API de backend Spring Boot para recuperar, criar, atualizar e excluir dados (por exemplo, buscar uma lista de livros, adicionar um novo autor).
3.  **Interação Backend-Banco de Dados:** O backend Spring Boot processa essas requisições da API, interagindo com o banco de dados PostgreSQL via Spring Data JPA para realizar as operações de dados necessárias.
4.  **Rede Interna:** Dentro do ambiente Docker Compose, os serviços se comunicam de forma segura através de uma rede Docker interna. O frontend, ao fazer chamadas de API, endereça o serviço de backend usando seu nome de serviço Docker (`api`).

## ⚙️ Configuração e Execução

Este projeto utiliza Docker para conteinerização e Docker Compose para orquestração, proporcionando um ambiente consistente de desenvolvimento e implantação. Você pode executar toda a pilha da aplicação com um único comando, ou configurar componentes individuais localmente para desenvolvimento.

### Pré-requisitos

Certifique-se de ter o seguinte instalado em seu sistema:

*   **Git:** Para clonar o repositório.
*   **Docker & Docker Compose:** Essenciais para executar a pilha da aplicação em contêineres.
    *   [Instalar Docker Engine](https://docs.docker.com/engine/install/linux/)
    *   [Instalar Docker Compose](https://docs.docker.com/compose/install/)
*   **Java Development Kit (JDK) 17 ou superior:** Necessário para o desenvolvimento local do backend.
    *   [Baixar JDK](https://adoptium.net/temurin/releases/)
*   **Maven:** Ferramenta de build para o backend Java.
    *   [Instalar Maven](https://maven.apache.org/install.html)
*   **Node.js (versão LTS, por exemplo, 20.x) & npm:** Necessário para o desenvolvimento local do frontend.
    *   [Baixar Node.js](https://nodejs.org/en/download/)
*   **Angular CLI:** Interface de linha de comando para projetos Angular.
    ```bash
    npm install -g @angular/cli
    ```

### Executando com Docker Compose (Recomendado)

Este método constrói e executa toda a pilha da aplicação (banco de dados PostgreSQL, API Spring Boot e frontend Angular servido pelo Nginx) usando contêineres Docker, proporcionando o ambiente mais consistente.

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/asizaguirre/livraria.git
    cd livraria
    ```
2.  **Compile o JAR do Backend Spring Boot:**
    O serviço `api` em `docker-compose.yml` espera um arquivo JAR pré-compilado. Navegue até a raiz do projeto e compile o backend:
    ```bash
    mvn clean install -DskipTests
    ```
    Este comando criará o `biblioteca-0.0.1-SNAPSHOT.jar` (ou similar) no diretório `target/`.

3.  **Construa e Inicie a Pilha Docker Compose:**
    A partir do diretório raiz do projeto onde `docker-compose.yml` está localizado:
    ```bash
    docker-compose up --build -d
    ```
    *   `--build`: Reconstrói as imagens se houver alterações nos Dockerfiles ou contextos.
    *   `-d`: Executa os contêineres em modo "detached" (em segundo plano).

4.  **Verifique o Status da Aplicação:**
    Verifique o status dos contêineres em execução:
    ```bash
    docker-compose ps
    ```
    Certifique-se de que todos os serviços (`db`, `api`, `web`) estão saudáveis e em execução.

5.  **Acesse a Aplicação:**
    *   **Frontend:** Abra seu navegador web e navegue para `http://localhost:4200`
    *   **API de Backend (Swagger UI):** Acesse a documentação da API em `http://localhost:8080/swagger-ui/index.html`

6.  **Pare a Aplicação:**
    Para parar e remover todos os contêineres, redes e volumes criados por `docker-compose up`:
    ```bash
    docker-compose down -v
    ```
    *   `-v`: Remove volumes nomeados declarados na seção `volumes` do arquivo `docker-compose.yml` (por exemplo, `db_data`), o que é útil para um banco de dados limpo, mas use com cautela, pois ele exclui todos os dados persistentes.

### Executando Componentes Localmente (Modo de Desenvolvimento)

Esta seção detalha como executar os serviços de frontend e backend independentemente, sem o Docker Compose, o que pode ser útil para desenvolvimento e depuração focados.

#### 1. Inicie o Banco de Dados PostgreSQL (via Docker)

É altamente recomendado executar o banco de dados em Docker mesmo para desenvolvimento local, a fim de manter a consistência com o ambiente de produção e simplificar a configuração.

```bash
docker-compose up -d db
```
Isso iniciará apenas o serviço `db`. Você pode pará-lo com `docker-compose down db`.

#### 2. Execute o Backend Spring Boot Localmente

1.  **Certifique-se de que o banco de dados esteja em execução** (localmente ou via Docker, conforme acima).
2.  **Navegue até a raiz do projeto backend:**
    ```bash
    cd /home/alam/Área de trabalho/Workspace/Livraria/ # (ou o diretório contendo pom.xml)
    ```
3.  **Compile e execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação Spring Boot será iniciada e estará acessível em `http://localhost:8080`.

#### 3. Execute o Frontend Angular Localmente

1.  **Navegue até o diretório do projeto frontend:**
    ```bash
    cd biblioteca-web
    ```
2.  **Instale as dependências (se ainda não o fez):**
    ```bash
    npm install
    ```
3.  **Inicie o servidor de desenvolvimento Angular:**
    ```bash
    ng serve
    ```
    O servidor de desenvolvimento Angular será iniciado, e a aplicação estará acessível em `http://localhost:4200`.

## 🧪 Testes

Este projeto adere a altos padrões de qualidade através de uma estratégia de testes abrangente para sua API de backend e aplicação frontend. Testes automatizados garantem a confiabilidade, correção e manutenibilidade da base de código, seguindo as melhores práticas de garantia de qualidade de software.

### Testes de Backend

O backend Spring Boot utiliza uma abordagem de testes em múltiplas camadas, incluindo testes unitários, de integração e de repositório, aproveitando frameworks de teste Java padrão. Isso garante uma validação completa da lógica de negócios, acesso a dados e endpoints da API.

*   **Frameworks:** JUnit 5 (Jupiter) para escrever testes, Mockito para mocking de dependências e Spring Boot Test para testes de integração com o contexto Spring.
*   **Ferramentas:** JaCoCo para análise abrangente de cobertura de código, fornecendo insights sobre a eficácia dos testes.

#### Como Executar Testes de Backend

Navegue até o diretório raiz do projeto (contendo `pom.xml`):

1.  **Execute todos os testes (unitários e de integração):**
    ```bash
    mvn test
    ```
    Este comando executa todos os testes encontrados no projeto.
2.  **Execute testes e gere o relatório de cobertura de código JaCoCo:**
    ```bash
    mvn clean verify
    ```
    Após a execução, um relatório JaCoCo detalhado será gerado. Você pode visualizá-lo abrindo `target/site/jacoco/index.html` em seu navegador web. Este relatório destaca as linhas e branches cobertos pelos testes.
3.  **Pule os testes durante a compilação (por exemplo, para criação mais rápida de imagens Docker ou implantação):**
    ```bash
    mvn clean install -DskipTests
    ```

### Testes de Frontend

O frontend Angular é equipado com uma configuração de testes moderna para garantir a confiabilidade dos componentes, a integridade da interface do usuário e a funcionalidade geral da aplicação.

*   **Framework:** Vitest, um framework de teste rápido e moderno, é utilizado (conforme indicado em `package.json`).
*   **Ferramentas:** O Vitest normalmente se integra com ambientes como o JSDOM para simular um ambiente de navegador no Node.js, permitindo testes eficientes de componentes e serviços sem um navegador completo.

#### Como Executar Testes de Frontend

Navegue até o diretório `biblioteca-web`:

```bash
cd biblioteca-web
```

1.  **Execute todos os testes de frontend:**
    ```bash
    npm test
    ```
    Este comando executa os testes configurados no script `test` de `package.json`, tipicamente iniciando o Vitest em modo de observação (`watch mode`), fornecendo feedback imediato sobre as alterações no código.

## 🤝 Contribuindo

(Seção opcional: Adicione diretrizes para contribuir com o projeto, por exemplo, padrões de codificação, processo de pull request, etc.)

## 📄 Licença

(Seção opcional: Especifique as informações de licenciamento do projeto.)