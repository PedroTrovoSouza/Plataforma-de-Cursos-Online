from locust import HttpUser, task, between
import random
import json
from datetime import datetime

TOKEN_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZWRyb0BwZWRyby5jb20uYnIiLCJpYXQiOjE3NTA3NjgyMTQsImV4cCI6MTc1MDc3MTgxNH0.nOGoEA8pZzoZq8MQ_BjY22v9Cp2QPBdw_4ajcxGkAJw"

matriculas_concluidas = [8, 9, 11, 12, 13]

class CertificadoUser(HttpUser):
    wait_time = between(1, 3)

    @task
    def cadastrar_certificado_valido(self):
        headers = {
            "Authorization": f"Bearer {TOKEN_JWT}",
            "Content-Type": "application/json"
        }

        payload = {
            "dataEmissao": datetime.now().strftime("%Y-%m-%d"),
            "matriculaId": random.choice(matriculas_concluidas)
        }

        with self.client.post(
                "/certificado",
                headers=headers,
                data=json.dumps(payload),
                catch_response=True
        ) as response:
            print(f"[VALOR VÁLIDO] POST /certificado => status {response.status_code}")
            if response.status_code in [200, 201]:
                response.success()
            elif response.status_code == 400:
                response.failure("Erro 400 – matrícula pode não estar CONCLUÍDA")
            else:
                response.failure(f"Erro {response.status_code} - {response.text}")