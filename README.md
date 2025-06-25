# 🎓 Plataforma de Cursos Online

*Microsserviço Educacional para Gerenciamento de Cursos, Usuários e Conteúdos*

---

## 📘 Descrição Técnica

A **Plataforma de Cursos Online** é um sistema educacional moderno, modular e escalável, projetado para permitir que professores como João possam vender seus próprios cursos, além de permitir que outros educadores façam o mesmo.

Baseada em arquitetura de **microserviços**, a plataforma visa facilitar a manutenção, o crescimento e a segurança da aplicação, com foco na separação clara de responsabilidades entre os serviços.

---

## 🎯 Objetivos

- Oferecer uma plataforma robusta para criação e venda de cursos.
- Permitir que professores e alunos acessem recursos com segurança e facilidade.
- Garantir escalabilidade e flexibilidade para expansão de funcionalidades.
- Gerenciar autenticação e autorização de forma centralizada e eficiente.

---

## 📐 Arquitetura do Sistema

- **Spring Boot (Java 17)**: Base de todos os serviços (REST API, segurança, regras de negócio).
- **Spring Security + JWT**: Autenticação e autorização segura entre os microsserviços.
- **WebClient**: Comunicação assíncrona entre os microsserviços.
- **MySQL**: Persistência dos dados.
- **Maven**: Gerenciamento de dependências e build.
- **RabbitMQ / MailHog (extensões)**: Simulação de envio de certificados.

---

## ⚙️ Microsserviços

### 1. `usuario-service` – Serviço de Usuários
- Registro e login de alunos e professores.
- Armazenamento de nome, e-mail, senha e tipo de usuário.
- Geração de JWT para autenticação.
- Diferenciação de permissões entre professores e alunos.

### 2. `curso-service` – Serviço de Cursos
- Criação de cursos por professores (título, descrição, preço, categoria).
- Listagem e busca de cursos por nome ou categoria.
- Edição apenas por professores criadores do curso.

### 3. `matricula-service` – Serviço de Matrículas
- Matrícula de alunos em cursos.
- Registro de data da matrícula.
- Listagem de cursos por aluno e de alunos por curso.

### 4. `conteudo-service` – Serviço de Conteúdo
- Criação de aulas por curso (vídeo, título e material em PDF).
- Acesso restrito ao conteúdo apenas por alunos matriculados.

### 5. `gateway-service` – API Gateway
- Roteamento de requisições para os microsserviços.
- Validação de JWT e controle de acesso por perfil.
- Regras de proteção: só professores criam cursos e só alunos se matriculam.

---

## 🔐 Regras de Segurança

- Acesso ao conteúdo apenas com JWT válido.
- Um aluno só vê conteúdo dos cursos em que está matriculado.
- Proteção de endpoints sensíveis via roles (`ALUNO`, `PROFESSOR`).

---

## 📡 Exemplos de Endpoints REST

| **Serviço**         | **Recurso**                    | **Método** | **Descrição**                                   |
|--------------------|--------------------------------|------------|-------------------------------------------------|
| `usuario-service`  | `/usuarios`                    | `POST`     | Cadastro de novo usuário                        |
|                    | `/login`                       | `POST`     | Autenticação e geração de token JWT             |
| `curso-service`    | `/cursos`                      | `POST`     | Criação de novo curso (somente professor)       |
|                    | `/cursos`                      | `GET`      | Listagem geral de cursos                        |
|                    | `/cursos?categoria=tecnologia` | `GET`      | Buscar cursos por categoria                     |
| `matricula-service`| `/matriculas`                  | `POST`     | Matrícula de aluno em curso                     |
|                    | `/matriculas/aluno/{id}`       | `GET`      | Listar cursos do aluno                          |
|                    | `/matriculas/curso/{id}`       | `GET`      | Listar alunos matriculados                      |
| `conteudo-service` | `/cursos/{id}/aulas`           | `POST`     | Criar aula em um curso                          |
|                    | `/cursos/{id}/aulas`           | `GET`      | Listar aulas (apenas se aluno estiver matriculado) |

---

## 🚀 Guia de Instalação

###💡 Clone o repositório

| git clone [Cursos_Online](https://github.com/PedroTrovoSouza/Plataforma-de-Cursos-Online.git) |
| cd Plataforma-de-Cursos-Online                                                                |


✅ Requisitos
- Java 17 ou superior
- Maven
- MySQL Workbench
- Insomnia (ou Postman)
- Git

### 🐇 Instalação do RabbitMQ

- Baixar o RabbitMQ Server - https://github.com/rabbitmq/rabbitmq-server/releases/download/v4.1.0/rabbitmq-server-4.1.0.exe
 
- Baixar o Erlang - https://github.com/erlang/otp/releases/download/OTP-28.0/otp_win64_28.0.exe
 
- Iniciar o Servidor
rabbitmq-service.bat
 
- Verificar se existe processo rodando
netstat -ano | findstr :25672
 
- Finalizar a tarefa para rodar:
taskkill /PID <PID> /F
 
- Verificar se esta rodando como serviço
Get-Service | Where-Object {$_.DisplayName -like "*RabbitMQ*"}
 
- Se estiver rodando, pare
Stop-Service -Name RabbitMQ
 
- Instalar Pluggins para Projeto
rabbitmq-plugins enable rabbitmq_management
 
- Rodar a aplicação
rabbitmq-server.bat

- Parar a Mensageria 
rabbitmqctl stop (deve ser usado em outro prompt de comando)

### 🛢️ Criação do Banco de Dados no MySQL Workbench
- 1. Abra o MySQL Workbench.
- 2. Conecte-se ao servidor local.
- 3. Execute o seguinte script SQL para criar o banco:
----------------------------------
| create database cursos_online; |
| use database cursos_online;    |
----------------------------------
- 4. (Opcional) Crie o usuário e dê permissões:
-------------------------------------------------------------------------
| CREATE USER 'cursos_user'@'localhost' IDENTIFIED BY 'senha_segura';   |
| GRANT ALL PRIVILEGES ON cursos_online.* TO 'cursos_user'@'localhost'; |
| FLUSH PRIVILEGES;                                                     |
-------------------------------------------------------------------------
- 5. Atualize o application.properties da aplicação com suas credenciais:
-------------------------------------------------------------------
| spring.datasource.url=jdbc:mysql://localhost:3306/cursos_online |
| spring.datasource.username=cursos_user                          |
| spring.datasource.password=senha_segura                         |
-------------------------------------------------------------------

### 🌐 Criação dos Endpoints no Insomnia - Trilha Padrão
- Cadastrar Usuário
http://localhost:8085/api/usuarios/usuario/cadastro
- Login de Usuário
http://localhost:8085/api/usuarios/usuario/login
- Cadastrar Curso
http://localhost:8085/api/cursos/cursos
- Cadastrar Conteúdo
http://localhost:8085/api/conteudos/conteudos
- Realizar Matricula em Curso
http://localhost:8085/api/matriculas/matricula/cadastrar/curso_para_matricular
- Concluir Matricula (finalizar curso)
http://localhost:8085/api/matriculas/matricula/concluir-curso/{idUsuario}
- Emitir Certificado do Curso
http://localhost:8085/api/certificados/certificado
- Avaliar Curso
http://localhost:8085/api/cursos/avaliacoes

---

## 📈 Escalabilidade & Monitoramento

- Microsserviços independentes garantem modularidade.
- Possibilidade de escalar serviços como `curso-service` e `conteudo-service` separadamente.
- Docker para facilitar deploy em ambientes distribuídos.

---

## 👨‍🏫 Equipe Técnica

- [**Fabio Azevedo**](https://github.com/FabioPojects -> Microsserviço de Conteudo e JWT
- [**Fernando Amorim**](https://github.com/FernandoAmoriim -> Microsserviço de Curso e JWT
- [**João Rossi**](https://github.com/JoaoRossii) -> Microsserviço de Usuario, Certificado e RabbitMQ
- [**Pedro Trovo**](https://github.com/PedroTrovoSouza) -> Microsserviço de Matricula, RabbitMQ e testes de carga

