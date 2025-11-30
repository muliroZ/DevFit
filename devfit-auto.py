import subprocess
import threading
import os
import json
import customtkinter as ctk
from tkinter import messagebox, filedialog

# ===========================================
# CONFIGURAÇÃO / ARQUIVO DE SALVAMENTO
# ===========================================

CONFIG_FILE = os.path.expanduser("~/.docker_controller_config.json")

def load_config():
    if os.path.exists(CONFIG_FILE):
        with open(CONFIG_FILE, "r") as f:
            return json.load(f)
    return {"compose_dir": ""}

def save_config(config):
    with open(CONFIG_FILE, "w") as f:
        json.dump(config, f, indent=4)

config = load_config()

# ===========================================
# FUNÇÕES AUXILIARES
# ===========================================

def run_command(cmd):
    """Executa comandos e devolve código, stdout, stderr"""
    process = subprocess.Popen(
        cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE
    )
    stdout, stderr = process.communicate()
    return process.returncode, stdout.decode(), stderr.decode()


def ensure_compose_selected():
    """Valida se há um docker-compose.yml selecionado"""
    compose_dir = config.get("compose_dir", "")

    if not compose_dir:
        messagebox.showerror("Erro", "Selecione um projeto Docker primeiro!")
        return None

    compose_path = os.path.join(compose_dir, "docker-compose.yml")
    if not os.path.exists(compose_path):
        messagebox.showerror("Erro", f"O diretório não contém docker-compose.yml:\n{compose_dir}")
        return None

    return compose_dir


# ===========================================
# AÇÕES DO DOCKER
# ===========================================

def docker_up():
    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    output_text.set("Rodando: docker compose up -d --build ...")
    cmd = f"cd '{compose_dir}' && docker compose up -d --build"

    code, out, err = run_command(cmd)

    if code == 0:
        output_text.set("✔ Containers iniciados!")
    else:
        output_text.set("❌ Erro ao iniciar containers:\n" + err)

def docker_only_up():
    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    output_text.set("Rodando: docker compose up -d ...")
    cmd = f"cd '{compose_dir}' && docker compose up -d"

    code, out, err = run_command(cmd)

    if code == 0:
        output_text.set("✔ Containers iniciados!")
    else:
        output_text.set("❌ Erro ao iniciar containers:\n" + err)

def docker_down():
    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    output_text.set("Derrubando containers...")
    cmd = f"cd '{compose_dir}' && docker compose down"

    code, out, err = run_command(cmd)

    if code == 0:
        output_text.set("✔ Containers desligados!")
    else:
        output_text.set("❌ Erro ao derrubar containers:\n" + err)

def docker_down_with_volumes():
    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    output_text.set("Derrubando containers...")
    cmd = f"cd '{compose_dir}' && docker compose down -v"

    code, out, err = run_command(cmd)

    if code == 0:
        output_text.set("✔ Containers desligados! (com volumes)")
    else:
        output_text.set("❌ Erro ao derrubar containers:\n" + err)

def docker_restart():
    output_text.set("Reiniciando containers...")
    docker_down()
    docker_only_up()


def show_logs():
    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    output_text.set("Abrindo logs em um terminal externo...")

    cmd = f"cd '{compose_dir}' && docker compose logs -f"

    # lista de terminais possíveis no Linux Mint / Ubuntu
    terminals = [
        f"x-terminal-emulator -e bash -c \"{cmd}; exec bash\"",
        f"gnome-terminal -- bash -c \"{cmd}; exec bash\"",
        f"xfce4-terminal -e \"bash -c '{cmd}; exec bash'\"",
        f"konsole -e bash -c \"{cmd}; exec bash\"",
        f"xterm -e \"bash -c '{cmd}; exec bash'\"",
    ]

    for t in terminals:
        try:
            subprocess.Popen(t, shell=True)
            return
        except FileNotFoundError:
            pass

    messagebox.showerror("Erro", "Nenhum terminal encontrado para abrir os logs.")


# ===========================================
# SELEÇÃO DO PROJETO DOCKER
# ===========================================

def select_compose_directory():
    directory = filedialog.askdirectory()

    if not directory:
        return

    compose_path = os.path.join(directory, "docker-compose.yml")
    if not os.path.exists(compose_path):
        messagebox.showerror("Erro", "Esse diretório NÃO contém docker-compose.yml!")
        return

    config["compose_dir"] = directory
    save_config(config)

    selected_dir_var.set("Projeto atual: " + directory)
    output_text.set("✔ Projeto Docker selecionado!")


# ===========================================
# GUI
# ===========================================

ctk.set_appearance_mode("dark")
ctk.set_default_color_theme("blue")

app = ctk.CTk()
app.title("Docker Controller")
app.geometry("500x600")

# Título
title_label = ctk.CTkLabel(app, text="Docker Controller", font=("Arial", 26, "bold"))
title_label.pack(pady=10)

# Path selecionado
selected_dir_var = ctk.StringVar(
    value=f"Projeto atual: {config.get('compose_dir', '(nenhum)')}"
)
dir_label = ctk.CTkLabel(app, textvariable=selected_dir_var, wraplength=450)
dir_label.pack(pady=5)

select_btn = ctk.CTkButton(
    app, text="Selecionar Projeto Docker", width=260, height=40,
    command=select_compose_directory
)
select_btn.pack(pady=10)


# Botões
btn_frame = ctk.CTkFrame(app)
btn_frame.pack(pady=15)

buttons = [
    ("Start (Up + Build)", docker_up),
    ("Start (Only Up)", docker_only_up),
    ("Stop (Down)", docker_down),
    ("Stop (Down + Volumes)", docker_down_with_volumes),
    ("Restart", docker_restart),
    ("Ver Logs", show_logs),
]

for i, (label, action) in enumerate(buttons):
    btn = ctk.CTkButton(
        btn_frame, text=label, width=200, height=40,
        command=lambda act=action: threading.Thread(target=act).start()
    )
    btn.grid(row=i, column=0, padx=10, pady=5)


# Output
output_text = ctk.StringVar(value="Aguardando ação...")
output_label = ctk.CTkLabel(
    app, textvariable=output_text, wraplength=450, justify="center"
)
output_label.pack(pady=20)


app.mainloop()
