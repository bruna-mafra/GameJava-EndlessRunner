import java.util.Iterator;
import org.lwjgl.opengl.GL20;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music; // Importa sons: trilha de fundo
import com.badlogic.gdx.audio.Sound; // Importa sons: efeitos sonoros
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;  // Imagem de fundo
    private Player player;  // Bruxinha
    private Array<Obstacle> obstacles;  // Lista de obstáculos
    private long lastObstacleTime;
    private int score;  // Pontuação
    private int powers;  // Controle de poderes
    private int lives;  // Nova variável para contar as vidas
    private boolean isGameOver; // Controle de fim de jogo
    private boolean isVictory;  // Controle de vitória
    private Array<Shot> shots;  // Controle de feitiços
    private BitmapFont font;
    private Music backgroundMusic;
    private Texture heartTexture; //  Ícone de coração
    private Sound gameOverSound; // Som para Game Over
    private Sound victorySound; // Som para Vitória
    private Sound powerSound; // Som para o poder lançado

    @Override
    public void create(){  // função de inicialização
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("assets/background.png"));
        heartTexture = new Texture(Gdx.files.internal("assets/coracao.png")); 
        player = new Player();
        obstacles = new Array<>();
        spawnObstacle();

        score = 0;
        powers = 0;
        lives = 3; // Jogador começa com 3 vidas
        isGameOver = false; // Inicialmente o jogo está ativo
        isVictory = false; // Inicialmente o jogo não foi vencido
        shots = new Array<>();  // Array de feitiços vazio
        font = new BitmapFont();

        // Música de fundo
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/lua_de_cristal.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

          // Efeitos sonoros
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("assets/game_over.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("assets/victory.ogg"));
        powerSound = Gdx.audio.newSound(Gdx.files.internal("assets/poder.wav"));
    }

    @Override
    public void render(){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (!isGameOver && !isVictory) { // Só atualiza se o jogo não acabou ou não foi vencido
            update();
        }

        batch.begin();
        batch.enableBlending();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.disableBlending();
        
        player.draw(batch);

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(batch);
        }
        for (Shot shot : shots) {
            shot.draw(batch);
        }

        // Ajustar a posição dos corações
        int heartY = Gdx.graphics.getHeight() - 70; 
        for (int i = 0; i < lives; i++) {
            batch.draw(heartTexture, 10 + (i * 40), heartY, 32, 32);
        }

        // Ajustar a posição do Score e indicador do número de Feitiços disponíveis
        int scoreY = heartY - 30;  
        font.draw(batch, "Score: " + score, 10, scoreY);
        font.draw(batch, "Feitiços: " + powers, 10, scoreY - 30);

        // Se for game over, exibe a mensagem
        if (isGameOver) {
            font.draw(batch, "GAME OVER!", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
            
        // Se for vitória, exibe a mensagem
        } else if (isVictory) {
            font.draw(batch, "VITÓRIA!", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        }

        // Finaliza o jogo
        batch.end();        
    }

    private void update(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){  // Espaço: pulo
            player.jump();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && powers > 0){  // Enter: usa feitiço
            userPower();
        }
        player.update();

        // Verifica se o jogador venceu quando o score chega a 20 pontos
        if (score >= 20) {
            victory();
            return;
        }

        // Criar obstáculos a cada 2 segundos
        if (TimeUtils.nanoTime() - lastObstacleTime > 2000000000){
            spawnObstacle();
        }

        // Verificar colisões e remover obstáculos
        for (Iterator<Obstacle> iterator = obstacles.iterator(); iterator.hasNext();){
            Obstacle obstacle = iterator.next();
            obstacle.update();

            if (obstacle.getBounds().overlaps(player.getBounds())) {
                lives--; // Perde uma vida ao colidir com um caldeirão (obstáculo)
                Gdx.app.log("Game", "O jogador perdeu uma vida! Vidas restantes: " + lives);
                iterator.remove(); // Remove o obstáculo atingido

                if (lives <= 0) {
                    gameOver();
                    return;
                }
            }

            if (obstacle.getPosition().x < -obstacle.getWidth()) {
                iterator.remove();
                score++;
                if (score % 2 == 0) {
                    powers++;
                }
            }
        }

        // Verificar colisões entre tiros e obstáculos
        for (Iterator<Shot> shotIterator = shots.iterator(); shotIterator.hasNext();) {
            Shot shot = shotIterator.next();
            shot.update();

            for (Iterator<Obstacle> obsIterator = obstacles.iterator(); obsIterator.hasNext();) {
                Obstacle obstacle = obsIterator.next();

                if (shot.getBound().overlaps(obstacle.getBounds())) {
                    shotIterator.remove();
                    obsIterator.remove();
                    break;
                }
            }
        }

        // Remover tiros que saem da tela
        for (Iterator<Shot> iterator = shots.iterator(); iterator.hasNext();){
            Shot shot = iterator.next();
            if (shot.getX() > Gdx.graphics.getWidth()){
                iterator.remove();
            }
        }
    }

    private void victory() {
        Gdx.app.log("Game", "VITÓRIA! O jogador atingiu 20 pontos.");
        isVictory = true;
        backgroundMusic.stop();
        victorySound.play(); // Toca o som de vitória
    }

    private void gameOver() {
        Gdx.app.log("Game", "GAME OVER! O jogador perdeu todas as vidas.");
        isGameOver = true;
        backgroundMusic.stop();
        gameOverSound.play(); // Toca o som de game over
    }

    private void spawnObstacle(){
        obstacles.add(new Obstacle(Gdx.graphics.getWidth(), 100));
        lastObstacleTime = TimeUtils.nanoTime();
    }

    @Override
    public void dispose(){
        batch.dispose();
        background.dispose();
        heartTexture.dispose(); 
        player.dispose();
        font.dispose();
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
        for (Shot shot : shots) {
            shot.dispose();
        }
        backgroundMusic.dispose(); // Libera memória da música de fundo
        gameOverSound.dispose(); // Libera memória do som de game over
        victorySound.dispose(); // Libera memória do som de vitória
        powerSound.dispose(); // Libera memória do som de poder
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    private void userPower(){
        if (powers > 0){
            Shot shot = new Shot(
                player.getPosition().x + player.getBounds().width,
                player.getPosition().y + player.getBounds().height / 2
            );
            shots.add(shot);
            powers--;  // Remove 1 do número de feitiços
            Gdx.app.log("Game", "Poder utilizado!");
            powerSound.play(); // Toca o som de poder ao usar o feitiço
        }
    }
}
