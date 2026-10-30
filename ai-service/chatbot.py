from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI(title="HackGov AI Chatbot Service", version="1.0")

# Habilita o CORS para permitir requisições do frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class MensagemRequest(BaseModel):
    texto_cidadao: str
    id_cidadao: int

@app.post("/api/ia/triagem")
def processar_triagem_chatbot(payload: MensagemRequest):
    texto = payload.texto_cidadao.lower()
    
    # Lógica de NLP / Classificação baseada no escopo do HackGov
    if "árvore" in texto or "poda" in texto or "caindo" in texto:
        tipo_ocorrencia_id = 1
        nome_tipo = "Poda de Árvore"
    elif "buraco" in texto or "asfalto" in texto:
        tipo_ocorrencia_id = 2
        nome_tipo = "Tapa-buraco"
    else:
        tipo_ocorrencia_id = 3
        nome_tipo = "Iluminação Pública"

    return {
        "bairro": "Lagoas",
        "logradouro": "Rua Principal",
        "tipo_ocorrencia_id": tipo_ocorrencia_id,
        "nome_tipo": nome_tipo,
        "descricao": payload.texto_cidadao,
        "status_atual": "ABERTO"
    }