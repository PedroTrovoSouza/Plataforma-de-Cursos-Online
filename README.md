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

## 📈 Escalabilidade & Monitoramento

- Microsserviços independentes garantem modularidade.
- Possibilidade de escalar serviços como `curso-service` e `conteudo-service` separadamente.
- Docker para facilitar deploy em ambientes distribuídos.

---

## 👨‍🏫 Equipe Técnica

- [**Fabio Azevedo**](https://github.com/FabioPojects -> Microserviço de Conteudo e JWT
- [**Fernando Amorim**](https://github.com/FernandoAmoriim -> Microserviço de Curso e JWT
- [**João Rossi**](https://github.com/JoaoRossii) -> Microserviço de Usuario, Certificado e RabbitMQ
- [**Pedro Trovo**](https://github.com/PedroTrovoSouza) -> Microserviço de Matricula, RabbitMQ e testes de carga

