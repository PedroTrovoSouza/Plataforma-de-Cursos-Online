from locust import HttpUser, task, between
import random

TOKEN_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZWRyb0BwZWRyby5jb20uYnIiLCJpYXQiOjE3NTA2ODk1NzMsImV4cCI6MTc1MDY5MzE3M30.4lw8DxSojWZrWoLFKDz_oJDY61k1ZPNRrtKmRr-Mrzk"

cursos_existentes = ["Curso Teste qp29wc", "Curso Teste wgok1t", "Curso Teste vot693"]
emails_existentes = ["pedro@trovo.com", "jose@trovo.com", "pedro.tsouza@solutis.com.br", "pedro@pedro.com.br"]

matriculas_existentes = [
    6, 8, 9, 10, 11, 12, 13, 14, 15,
    16, 17, 18, 19, 20, 21, 22, 23, 24
]

class MatriculaUser(HttpUser):
    wait_time = between(1, 3)

    def on_start(self):
        # Opcional: algo para executar quando o usuário inicia (ex: login)
        pass

    @task
    def fluxo_de_matricula(self):
        # Primeiro faz o DELETE
        self.cancelar_matricula()

        # Depois faz o POST
        self.cadastrar_matricula()

    def cancelar_matricula(self):
        id_matricula = random.choice(matriculas_existentes)
        headers = {"Authorization": f"Bearer {TOKEN_JWT}"}

        with self.client.delete(
                f"/matricula/cancelar-matricula/{id_matricula}",
                headers=headers,
                catch_response=True
        ) as response:
            print(f"DELETE /matricula/cancelar-matricula/{id_matricula} => status {response.status_code}, body: {response.text}")
            if response.status_code == 200:
                response.success()
            elif response.status_code == 404:
                response.failure(f"Matrícula {id_matricula} não encontrada.")
            else:
                response.failure(f"Erro {response.status_code}: {response.text}")

    def cadastrar_matricula(self):
        nome_curso = random.choice(cursos_existentes)
        email_usuario = random.choice(emails_existentes)

        headers = {
            "Authorization": f"Bearer {TOKEN_JWT}",
            "Content-Type": "text/plain"
        }

        with self.client.post(
                f"/matricula/cadastrar/{nome_curso}",
                data=email_usuario,
                headers=headers,
                catch_response=True
        ) as response:
            if response.status_code == 201:
                response.success()
            else:
                response.failure(f"Erro {response.status_code}: {response.text}")
