from locust import HttpUser, task, between
import random
import string

TOKEN_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZWRyb0BwZWRyby5jb20uYnIiLCJpYXQiOjE3NTA2ODk1NzMsImV4cCI6MTc1MDY5MzE3M30.4lw8DxSojWZrWoLFKDz_oJDY61k1ZPNRrtKmRr-Mrzk"

def gerar_sufixo_unico():
    return ''.join(random.choices(string.ascii_lowercase + string.digits, k=6))

class TesteCadastroCurso(HttpUser):
    wait_time = between(1, 3)

    @task
    def cadastrar_curso(self):
        sufixo = gerar_sufixo_unico()

        curso = {
            "titulo": f"Curso Teste {sufixo}",
            "descricao": f"Descrição única do curso {sufixo}",
            "categoria": random.choice(["Programação", "Negócios", "Design", "Marketing"]),
            "preco": round(random.uniform(49.90, 499.90), 2)
        }

        headers = {
            "Authorization": f"Bearer {TOKEN_JWT}"
        }

        with self.client.post("/cursos", json=curso, headers=headers, catch_response=True) as response:
            if response.status_code == 201:
                response.success()
            else:
                response.failure(f"Erro {response.status_code}: {response.text}")
