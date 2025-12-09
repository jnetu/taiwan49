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

**JDK 21** (Obrigatório para compilar e usar o jpackage);

**Eclipse IDE** (O projeto já contém os metadados de importação);

* *Nota:* Não é necessário instalar bibliotecas externas, os .jars do lwjgl e etc já estão na pasta `/lib`. importe na sua IDE

## macete do Eclipse

Abra o Eclipse e vá em `File > Open Projects from File System...`;

Selecione a pasta raiz deste projeto;

O Eclipse deve reconhecer automaticamente as bibliotecas em `lib/` por causa do `.classpath` incluso;


## instalador do executável em windows:

```powershell
build.bat
```

O main está na classe `main.Game` em src/

as libs .jar estão em lib/

e os assets em res/