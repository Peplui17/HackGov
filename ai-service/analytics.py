import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import io
import base64
from database import get_connection

def calcular_estatisticas_sla():
    conn = get_connection()
    if not conn:
        return {"erro": "Sem conexão com o banco"}

    query = """
        SELECT TIPO_OCORRENCIA_ID_TIPO, DATA_ABERTURA, STATUS_ATUAL, BAIRRO 
        FROM SOLICITACAO
    """
    
    try:
        df = pd.read_sql(query, con=conn)
    except Exception as e:
        return {"erro": f"Falha ao executar query: {str(e)}"}
    finally:
        conn.close()

    if df.empty:
        return {"mensagem": "Nenhum dado encontrado para análise"}

    df['DATA_ABERTURA'] = pd.to_datetime(df['DATA_ABERTURA'])
    df['DIAS_ABERTO'] = (pd.Timestamp.today() - df['DATA_ABERTURA']).dt.days

    estatisticas = df.groupby('TIPO_OCORRENCIA_ID_TIPO')['DIAS_ABERTO'].agg(
        total_chamados='count',
        media_dias='mean',
        variancia='var',
        desvio_padrao='std'
    ).fillna(0).round(2)

    return estatisticas.to_dict(orient='index')

def gerar_metricas_bairro():
    conn = get_connection()
    if not conn:
        return {"erro": "Sem conexão com o banco"}

    query = "SELECT BAIRRO, TIPO_OCORRENCIA_ID_TIPO FROM SOLICITACAO"
    
    try:
        df = pd.read_sql(query, con=conn)
    finally:
        conn.close()

    if df.empty:
        return {}

    frequencia_bairro = df['BAIRRO'].value_counts().head(10).to_dict()
    return frequencia_bairro

def gerar_histograma_sla():
    conn = get_connection()
    if not conn:
        return None

    query = "SELECT TIPO_OCORRENCIA_ID_TIPO, DATA_ABERTURA FROM SOLICITACAO"
    try:
        df = pd.read_sql(query, con=conn)
    finally:
        conn.close()

    if df.empty:
        return None

    df['DATA_ABERTURA'] = pd.to_datetime(df['DATA_ABERTURA'])
    df['DIAS_ABERTO'] = (pd.Timestamp.today() - df['DATA_ABERTURA']).dt.days

    plt.figure(figsize=(8, 5))
    sns.histplot(df['DIAS_ABERTO'], bins=10, kde=True, color='blue')
    plt.title('Histograma de Frequência - Prazos de Atendimento')
    plt.xlabel('Dias em Aberto')
    plt.ylabel('Frequência')
    
    buf = io.BytesIO()
    plt.savefig(buf, format='png', bbox_inches='tight')
    buf.seek(0)
    plt.close()

    image_base64 = base64.b64encode(buf.read()).decode('utf-8')
    return image_base64

from sklearn.linear_model import LinearRegression
import numpy as np

def prever_demanda_bairros():
    conn = get_connection()
    if not conn:
        return {"erro": "Sem conexão"}
    
    query = "SELECT BAIRRO, DATA_ABERTURA FROM SOLICITACAO"
    try:
        df = pd.read_sql(query, con=conn)
    finally:
        conn.close()

    if df.empty:
        return {"mensagem": "Dados insuficientes para predição"}

    df['DATA_ABERTURA'] = pd.to_datetime(df['DATA_ABERTURA'])
    df['MES'] = df['DATA_ABERTURA'].dt.month

    # Agrupa histórico por bairro e mês
    df_grouped = df.groupby(['BAIRRO', 'MES']).size().reset_index(name='VOLUME')

    predicoes = {}
    for bairro in df_grouped['BAIRRO'].unique():
        df_bairro = df_grouped[df_grouped['BAIRRO'] == bairro]
        if len(df_bairro) > 1:
            X = df_bairro[['MES']]
            y = df_bairro['VOLUME']
            model = LinearRegression()
            model.fit(X, y)
            
            # Estima o volume para o próximo mês
            proximo_mes = np.array([[ (pd.Timestamp.today().month % 12) + 1 ]])
            pred = model.predict(proximo_mes)[0]
            predicoes[bairro] = max(0, int(round(pred)))
        else:
            predicoes[bairro] = int(df_bairro['VOLUME'].values[0])

    return predicoes