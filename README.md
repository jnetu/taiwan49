# Easy LWJGL setup - Boost Performance

- last Lwjgl
- vsync
- viewport
- texture & inputs simple code
- resizable window - pixelperfect (any WIDTH HEIGHT) 
- ajustable Tick per Second TPS
- ajustable Framerate
- Low RAM consumption free memoryLeak
- CPU await system - CPU Saver consumption when nothing is rendering

projeto simples para setups iniciais de games usando LWJGL3

## Requisitos

**JDK 21+** (Obrigatório para compilar e usar o jpackage);

**Eclipse IDE** (O projeto já contém os metadados de importação);

* *Nota:* Não é necessário instalar bibliotecas externas, os .jars do lwjgl e etc já estão na pasta `/lib`. importe na sua IDE

## Rodando pelo terminal

### macOS / Linux

```bash
chmod +x run.sh
./run.sh
```

> **macOS:** o GLFW exige `-XstartOnFirstThread` — o script já passa essa flag automaticamente via `exec java -XstartOnFirstThread ...`. Não use `sudo`.

> **Linux:** roda normalmente, sem flags extras.

### Windows

```bat
run.bat
```

---

## macete do Eclipse

Abra o Eclipse e vá em `File > Open Projects from File System...`;

Selecione a pasta raiz deste projeto;

O Eclipse deve reconhecer automaticamente as bibliotecas em `lib/` por causa do `.classpath` incluso;

### macOS — configuração extra no Eclipse

O Eclipse no macOS precisa da flag `-XstartOnFirstThread` para o GLFW funcionar.

Vá em `Run > Run Configurations > Java Application` → selecione a config do `main.Game` → aba **Arguments** → campo **VM arguments**:

```
-XstartOnFirstThread
```

Salve e rode normalmente.

---

## instalador do executável

### Windows

```bat
build.bat
```

Gera um instalador `.exe` via `jpackage` em `dist/build/`.

### macOS / Linux *(em breve)*

O `Launcher.java` já está preparado para re-lançar a JVM com `-XstartOnFirstThread` automaticamente quando empacotado via `jpackage` no macOS — sem necessidade de flags manuais pelo usuário final.

---

O main está na classe `main.Game` em src/

as libs .jar estão em lib/

e os assets em res/
