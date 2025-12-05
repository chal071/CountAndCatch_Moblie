import json
import io
import base64

import pandas as pd
import matplotlib.pyplot as plt


def fig_to_base64(plot_func):
    buf = io.BytesIO()
    plt.clf()
    plot_func()
    plt.tight_layout()
    plt.savefig(buf, format="png")
    buf.seek(0)
    return base64.b64encode(buf.read()).decode("utf-8")


def run_analysis(json_str):
    # 1) JSON -> DataFrame
    data = json.loads(json_str)
    df = pd.DataFrame(data)

    if df.empty:
        return {
            "metrics": {
                "player_count": 0,
                "average_session_length": 0.0,
                "retention_rate": 0.0,
                "churn_rate": 0.0,
            },
            "hist_session_length": "",
            "dau": "",
            "completion_by_game": "",
        }

    # 2) 解析时间
    if "fecha_hora" in df.columns:
        df["fecha_hora"] = pd.to_datetime(df["fecha_hora"])
        df["fecha"] = df["fecha_hora"].dt.date

    # 3) 基本指标
    player_count = df["nombre"].nunique() if "nombre" in df.columns else 0
    avg_session_length = (
        df["tiempo_partida"].mean() if "tiempo_partida" in df.columns else 0.0
    )

    # returning_player：同一 nombre 有 >=2 条记录视为“回流”
    if "nombre" in df.columns and "fecha_hora" in df.columns:
        sessions_per_player = df.groupby("nombre")["fecha_hora"].count()
        returning = (sessions_per_player >= 2).astype(int)
        retention_rate = float((returning == 1).mean()) if len(returning) > 0 else 0.0
    else:
        retention_rate = 0.0

    churn_rate = 1.0 - retention_rate

    metrics = {
        "player_count": int(player_count),
        "average_session_length": float(avg_session_length),
        "retention_rate": float(retention_rate),
        "churn_rate": float(churn_rate),
    }

    # 4) 图1：tiempo_partida 直方图
    hist_session_length_b64 = ""
    if "tiempo_partida" in df.columns:
        def plot_hist():
            df["tiempo_partida"].hist(bins=8)
            plt.xlabel("Tiempo de partida (s)")
            plt.ylabel("Frecuencia")
            plt.title("Distribución de tiempo de partida")

        hist_session_length_b64 = fig_to_base64(plot_hist)

    # 5) 图2：DAU 折线（fecha vs jugadores únicos）
    dau_b64 = ""
    if "fecha" in df.columns and "nombre" in df.columns:
        dau_series = df.groupby("fecha")["nombre"].nunique()

        def plot_dau():
            plt.plot(dau_series.index, dau_series.values, marker="o")
            plt.xlabel("Fecha")
            plt.ylabel("Jugadores únicos (DAU)")
            plt.title("Jugadores activos por día")
            plt.xticks(rotation=45)

        dau_b64 = fig_to_base64(plot_dau)

    # 6) 图3：每个 juego 的通关率（terminada==1 的比例）
    completion_b64 = ""
    if "juego" in df.columns and "terminada" in df.columns:
        comp = df.groupby("juego")["terminada"].mean() * 100

        name_map = {
            1: "Juego Count",
            2: "Juego Catch",
        }
        labels = [name_map.get(int(idx), str(idx)) for idx in comp.index]
        values = comp.values

        def plot_completion():
            plt.bar(labels, values)
            plt.ylabel("% partidas terminadas")
            plt.ylim(0, 100)
            plt.title("Tasa de finalización por juego")

        completion_b64 = fig_to_base64(plot_completion)

    return {
        "metrics": metrics,
        "hist_session_length": hist_session_length_b64,
        "dau": dau_b64,
        "completion_by_game": completion_b64,
    }
