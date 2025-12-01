import subprocess
import threading
import os
import shutil
import json
import customtkinter as ctk
from tkinter import messagebox, filedialog

# ===========================================
# WATCHDOG IMPORT
# ===========================================

try:
    from watchdog.observers import Observer
    from watchdog.events import FileSystemEventHandler
    WATCHDOG_AVAILABLE = True
except ImportError:
    WATCHDOG_AVAILABLE = False


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

observer = None
watchdog_enabled = False

# ===========================================
# FUNÇÕES AUXILIARES
# ===========================================

def run_command(cmd):
    """Executa comandos e devolve código, stdout, stderr"""
    process = subprocess.Popen(
        cmd, 
        shell=True, 
        stdout=subprocess.PIPE, 
        stderr=subprocess.PIPE,
        text=True,            # <<< transforma bytes em string automaticamente
        encoding="utf-8",     # ou simplesmente remova encoding
        errors="replace"
    )
    stdout, stderr = process.communicate()
    return process.returncode, stdout, stderr


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

def detect_terminal(cmd, compose_dir):
    """
    Retorna o comando final corretamente formatado por terminal.
    """

    windows_cmd = f'cmd.exe /k "cd /d \\"{compose_dir}\\" && {cmd}"'
    windows_pwsh = f'powershell.exe -NoExit -Command "Set-Location \\"{compose_dir}\\"; {cmd}"'
    windows_wt_pwsh = f'wt.exe powershell.exe -NoExit -Command "Set-Location \\"{compose_dir}\\"; {cmd}"'
    windows_wt_cmd = f'wt.exe cmd.exe /k "cd /d \\"{compose_dir}\\" && {cmd}"'

    terminals = [
        # Linux
        f"x-terminal-emulator -e bash -c \"cd '{compose_dir}' && {cmd}; exec bash\"",
        f"gnome-terminal -- bash -c \"cd '{compose_dir}' && {cmd}; exec bash\"",
        f"xfce4-terminal -e \"bash -c 'cd \"{compose_dir}\" && {cmd}; exec bash'\"",
        f"konsole -e bash -c \"cd '{compose_dir}' && {cmd}; exec bash\"",
        f"xterm -e \"bash -c 'cd \"{compose_dir}\" && {cmd}; exec bash'\"",

        # Windows (CMD)
        windows_cmd,

        # PowerShell
        windows_pwsh,

        # Windows Terminal (WT)
        windows_wt_pwsh,
        windows_wt_cmd,

        # Git Bash
        f"C:\\Program Files\\Git\\bin\\bash.exe -c \"cd '{compose_dir}' && {cmd}; exec bash\"",
        f"C:\\Program Files\\Git\\git-bash.exe -c \"cd '{compose_dir}' && {cmd}; exec bash\"",
    ]

    for t in terminals:
        exe = t.split()[0].replace('"', "")
        if shutil.which(exe):
            return t

    return None


# ===========================================
# WATCHDOG (MONITORAMENTO OPCIONAL)
# ===========================================

def start_watcher():
    global observer, watchdog_enabled

    if not WATCHDOG_AVAILABLE:
        messagebox.showerror("Erro", "Watchdog não instalado!\nUse: pip install watchdog")
        return

    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    class ComposeChangeHandler(FileSystemEventHandler):
        def __init__(self, compose_dir):
            self.compose_file = os.path.join(compose_dir, "docker-compose.yml")

        def on_modified(self, event):
            if event.src_path == self.compose_file:
                output_text.set("🔄 Mudança detectada! Recarregando containers...")
                threading.Thread(target=auto_reload).start()

    def auto_reload():
        compose_dir = config["compose_dir"]
        cmd = f'cd "{compose_dir}" && docker compose up -d --build'
        code, out, err = run_command(cmd)

        if code == 0:
            output_text.set("✔ Reload automático concluído!")
        else:
            output_text.set("❌ Erro no reload automático:\n" + err)

    # Criar observer corretamente
    event_handler = ComposeChangeHandler(compose_dir)
    observer = Observer()
    observer.schedule(event_handler, compose_dir, recursive=False)
    observer.daemon = True
    observer.start()

    watchdog_enabled = True
    watchdog_button_text.set("Desativar Watchdog")
    output_text.set("👀 Watchdog ATIVADO")


def stop_watcher():
    global observer, watchdog_enabled

    if observer:
        observer.stop()
        observer.join()
        observer = None

    watchdog_enabled = False
    watchdog_button_text.set("Ativar Watchdog")
    output_text.set("🛑 Watchdog DESATIVADO")


def toggle_watcher():
    if watchdog_enabled:
        stop_watcher()
    else:
        start_watcher()


# ===========================================
# AÇÕES DO DOCKER
# ===========================================

def docker_up():
    compose_dir = ensure_compose_selected()
    if not compose_dir:
        return

    output_text.set("Rodando: docker compose up -d --build ...")
    cmd = f'cd "{compose_dir}" && docker compose up -d --build'

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
    cmd = f'cd "{compose_dir}" && docker compose up -d'

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
    cmd = f'cd "{compose_dir}" && docker compose down'

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
    cmd = f'cd "{compose_dir}" && docker compose down -v'

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

    cmd = "docker compose logs -f"

    final_cmd = detect_terminal(cmd, compose_dir)
    if not final_cmd:
        messagebox.showerror("Erro", "Nenhum terminal encontrado para abrir os logs.")
        return

    def run():
        try:
            subprocess.Popen(final_cmd, shell=True)
        except Exception as e:
            messagebox.showerror("Erro ao abrir terminal", str(e))

    threading.Thread(target=run, daemon=True).start()


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
app.geometry("500x620+400+170")

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

watchdog_button_text = ctk.StringVar(value="Ativar Watchdog")

watchdog_btn = ctk.CTkButton(
    app,
    textvariable=watchdog_button_text,
    width=260,
    height=40,
    command=lambda: threading.Thread(target=toggle_watcher).start()
)
watchdog_btn.pack(pady=10)

# Output
output_text = ctk.StringVar(value="Aguardando ação...")
output_label = ctk.CTkLabel(
    app, textvariable=output_text, wraplength=450, justify="center"
)
output_label.pack(pady=20)


app.mainloop()
