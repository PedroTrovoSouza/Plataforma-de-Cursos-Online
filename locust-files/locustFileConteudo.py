from locust import HttpUser, task, between
import random
import string
import json

TOKEN_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZWRyb0BwZWRyby5jb20uYnIiLCJpYXQiOjE3NTA3NjgyMTQsImV4cCI6MTc1MDc3MTgxNH0.nOGoEA8pZzoZq8MQ_BjY22v9Cp2QPBdw_4ajcxGkAJw"

conteudos_existentes = [4, 5, 6]
cursos_existentes = [1, 2, 3, 4, 5]

class ConteudoUser(HttpUser):
    wait_time = between(1, 3)

    @task(1)
    def deletar_conteudo(self):
        conteudo_id = random.choice(conteudos_existentes)
        headers = {"Authorization": f"Bearer {TOKEN_JWT}"}

        with self.client.delete(
                f"/conteudos/{conteudo_id}",
                headers=headers,
                catch_response=True
        ) as response:
            print(f"DELETE /conteudos/{conteudo_id} => status {response.status_code}")
            if response.status_code in [200, 204]:
                response.success()
            elif response.status_code == 404:
                response.failure(f"Conteúdo {conteudo_id} não encontrado.")
            else:
                response.failure(f"Erro ao deletar conteúdo: {response.status_code}")

    @task(3)
    def salvar_conteudo(self):
        titulo = "Aula " + ''.join(random.choices(string.ascii_uppercase + string.digits, k=5))
        url_video = "https://youtube.com/watch?v=" + ''.join(random.choices(string.ascii_lowercase + string.digits, k=11))
        curso_id = random.choice(cursos_existentes)

        headers = {
            "Authorization": f"Bearer {TOKEN_JWT}",
            "Content-Type": "application/json"
        }

        payload = {
            "titulo": titulo,
            "url_video": url_video,
            "cursoId": curso_id
        }

        with self.client.post(
                "/conteudos",
                data=json.dumps(payload),
                headers=headers,
                catch_response=True
        ) as response:
            print(f"POST /conteudos => status {response.status_code}")
            if response.status_code in [200, 201]:
                response.success()
            else:
                response.failure(f"Erro ao salvar conteúdo: {response.status_code} - {response.text}")
