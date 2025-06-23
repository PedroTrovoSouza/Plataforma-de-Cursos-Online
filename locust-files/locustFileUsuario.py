from locust import HttpUser, task, between
import random
import string

credenciais_cadastradas = []

def gerar_email_unico():
    sufixo = ''.join(random.choices(string.ascii_lowercase + string.digits, k=6))
    return f"usuario_{sufixo}@teste.com"

class TesteUsuario(HttpUser):
    wait_time = between(1, 3)

    @task(1)
    def logar_usuario(self):
        if not credenciais_cadastradas:
            return

        credenciais = random.choice(credenciais_cadastradas)

        login_payload = {
            "email": credenciais["email"],
            "senha": credenciais["senha"]
        }

        with self.client.post("/usuario/login", json=login_payload, catch_response=True) as response:
            if response.status_code == 200:
                response.success()
            else:
                response.failure(f"Erro no login: {response.status_code} - {response.text}")

    @task(3)
    def cadastrar_usuario(self):
        email = gerar_email_unico()
        senha = "senha123"
        usuario = {
            "nome": "Usuário Teste",
            "email": email,
            "senha": senha,
            "tipo": random.choice(["ALUNO", "PROFESSOR"])
        }

        with self.client.post("/usuario/cadastro", json=usuario, catch_response=True) as response:
            if response.status_code == 200:
                # Armazena as credenciais para uso posterior
                credenciais_cadastradas.append({"email": email, "senha": senha})
                response.success()
            else:
                response.failure(f"Erro no cadastro: {response.status_code} - {response.text}")
