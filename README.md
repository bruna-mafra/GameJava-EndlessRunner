# 🎮 Jogo da Bruxinha - Endless Runner

## 📌 Sobre o jogo
O **Jogo da Bruxinha** é um jogo estilo **Endless Runner** onde o jogador controla uma pequena bruxa que deve **pular obstáculos** e **usar poderes** para destruí-los. O jogo termina em **Game Over** caso perca todas as vidas ou em **Vitória** ao atingir 20 pontos.

## 🛠️ Requisitos
Antes de rodar o jogo, você precisa ter o **Java e JDK** instalados no seu computador.

### 🔹 Instalar o Java e o JDK
Baixe e instale a versão mais recente do **JDK** no site oficial da Oracle:
🔗 [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)

Após instalar e configurar nas variáveis de ambiente, **verifique se o Java foi instalado corretamente** executando no terminal:
```sh
java -version
```
Se aparecer a versão do Java instalada, está tudo certo! Reiniciar seu computador pode ser necessário.

---

## 🚀 Como rodar o jogo


1️⃣ **Abra o prompt de comando, clone o repositório e entre na pasta do jogo:**
```sh
git clone https://github.com/bruna-mafra/GameJava-EndlessRunner.git
cd GameJava-EndlessRunner
```

2️⃣ **Compile o código:**
```sh
javac -cp "lib/*" -d bin src/*.java
```

3️⃣ **Execute o jogo:**
```sh
java -cp "bin;lib/*" Game
```
Agora o jogo será iniciado! 🎮

---

## 🎮 Como jogar
- **Pular:** Pressione **ESPAÇO** para fazer a bruxinha pular e evitar os obstáculos.
- **Soltar Poder:** Pressione **ENTER** para lançar um feitiço contra os obstáculos.
- **Pontuação:** Cada obstáculo ultrapassado soma **+1 ponto** no Score.
- **Ganhando Poder:** A cada 2 pontos acumulados, você ganha um poder adicional para lançar feitiços!
- **Game Over:** Se perder **todas as 3 vidas**, o jogo termina.
- **Vitória:** Se alcançar **20 pontos**, o jogo exibe uma mensagem de vitória!

---

## 🎵 Sons e Trilha Sonora
O jogo conta com **trilha sonora e efeitos sonoros** para maior imersão:

🔊 **Música de Fundo:**
- 🎶 "Lua de Cristal" (arquivo `lua_de_cristal.mp3` na pasta `assets/`)

🔊 **Efeitos Sonoros:**
- 📢 `gameover.mp3` → Quando o jogador perde todas as vidas.
- 🏆 `victory.ogg` → Quando o jogador atinge 20 pontos.
- ⚡ `poder.wav` → Quando o jogador solta um feitiço.

---

## 📂 Estrutura do projeto
```
GameJava/
├── assets/          # Contém imagens e sons do jogo
│   ├── background.png
│   ├── coracao.png
│   ├── lua_de_cristal.wav
│   ├── gameover.wav
│   ├── victory.wav
│   ├── power.wav
├── bin/             # Arquivos compilados
├── lib/             # Bibliotecas externas do jogo
├── src/             # Código-fonte
│   ├── Game.java
│   ├── GameScreen.java
│   ├── Player.java
│   ├── Obstacle.java
│   ├── Shot.java
└── README.md        # Documentação
```

---

## 👨‍💻 Desenvolvido por Bruna Mafra
Este jogo foi desenvolvido como um projeto da disciplina **Desenvolvimento de Jogos** da Universidade, com Java e LibGDX. 🚀✨
