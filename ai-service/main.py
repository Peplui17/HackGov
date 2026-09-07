from fastapi import FastAPI
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from database import get_connection
from analytics import (
    calcular_estatisticas_sla, 
    gerar_metricas_bairro, 
    gerar_histograma_sla, 
    prever_demanda_bairros
)

app = FastAPI(title="HackGov Analítico")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def home():
    return {"status": "Serviço Analítico Operante"}

@app.get("/estatisticas-sla")
def obter_estatisticas():
    return {"indicadores_servico": calcular_estatisticas_sla()}

@app.get("/frequencia-bairros")
def obter_frequencia_bairros():
    return {"demandas_por_bairro": gerar_metricas_bairro()}

@app.get("/grafico-histograma")
def obter_grafico():
    img_b64 = gerar_histograma_sla()
    if not img_b64:
        return JSONResponse(status_code=404, content={"erro": "Dados insuficientes para gerar o gráfico"})
    return {"imagem_base64": img_b64}

@app.get("/predicao-demandas")
def obter_predicao_demandas():
    return {"predicao_bairros": prever_demanda_bairros()}