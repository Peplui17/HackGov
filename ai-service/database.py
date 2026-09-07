import os
import oracledb

# Dados extraídos do seu application.properties
DB_USER = os.getenv("DB_HACKGOV_USER", "RM561537")
DB_PASSWORD = os.getenv("DB_HACKGOV_PASSWORD", "301106")

# Tradução da URL JDBC para o formato DSN do Python oracledb
# Formato: host:porta/servico (ou usando a string de conexão com SID)
DB_DSN = "oracle.fiap.com.br:1521/ORCL"

def get_connection():
    try:
        connection = oracledb.connect(
            user=DB_USER,
            password=DB_PASSWORD,
            dsn=DB_DSN
        )
        return connection
    except Exception as e:
        print(f"Erro ao conectar no Oracle da FIAP: {e}")
        return None