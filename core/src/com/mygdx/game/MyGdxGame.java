package com.mygdx.game;

import com.mygdx.game.drops.Drop;
import com.mygdx.game.enemies.Enemy;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.*;
import com.mygdx.game.enemies.MediumEnemy;
import com.mygdx.game.enemies.NormalEnemy;
import com.mygdx.game.enemies.TankEnemy;

public class MyGdxGame extends ApplicationAdapter {
    static final int WORLD_WIDTH = 405;
    static final int WORLD_HEIGHT = 720;
    OrthographicCamera camera;
    
    SpriteBatch batch;
    BitmapFont textPoints, textLives, textShield, textTime;
    BitmapFont textShopDmg, textShopExit;
    Rectangle recShopDmg, recShopExit;
    Texture shield, hearth;
    
    Player player;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Drop> drops = new ArrayList<Drop>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    List<Entity> toRemove = new ArrayList<Entity>();
    List<Enemy> toRemoveE = new ArrayList<Enemy>();
    
    boolean isWave = true;
    
    float playerShoot = 0, playerShield = 0, enemiesShoot = 0, enemiesSpawn = 0, gameTime = 40, nextWave = 0;
    float invurnerable = 0;
    
    float  hpMod = 1, dmgMod = 1, asMod = 1;
    float normChance = 1, medChance = 0, tankChance = 0;
    float spawnSpeed = 1f;
    int enemiesLeft;
    int enemiesToSpawn;
    int lastEnemies;
    int wave = 0;
    
    
    
    
    @Override
    public void create () {
        camera = new OrthographicCamera(405, 720);
        batch = new SpriteBatch();
        shield = new Texture("shield.png");
        hearth = new Texture("hearth.png");
        player = new Player(100, 20, new Texture("ship.png"), 50, 35);
        textPoints = new BitmapFont();
        textLives = new BitmapFont();
        textTime = new BitmapFont();
        textShield = new BitmapFont();
        textShopDmg = new BitmapFont();
        textShopExit = new BitmapFont();
        textPoints.getData().scale(1.5f);
        textLives.getData().scale(1.5f);
        textShield.getData().scale(1.5f);
        textShopDmg.getData().scale(1.5f);
        textShopExit.getData().scale(2f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }
    
    @Override
    public void render () {
        batch.setProjectionMatrix(camera.combined);
        
        if (isWave) {
            move();
            collide();
            shoot();
            spawn();
            decay();
            wave();
        }
        
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        
        if (isWave) {
            if (player.getAlive()) batch.draw(player.getTexture(), player.getX(), player.getY(), player.width, player.height);
            for ( Bullet bullet : bullets) {
                batch.draw(bullet.getTexture(), bullet.getX(), bullet.getY(), bullet.width, bullet.height);
            }
            for ( Drop drop : drops) {
                batch.draw(drop.getTexture(), drop.getX(), drop.getY(), drop.width, drop.height);
            }
            for (Enemy enemy : enemies) {
                batch.draw(enemy.getTexture(), enemy.getX(), enemy.getY(), enemy.width, enemy.height);
            }
            textPoints.draw(batch, "Points: " + player.getPoints(), 10, 710);
            batch.draw(hearth, 10, 640, 30, 30);
            textLives.draw(batch, "" + player.getHealth(), 50, 670);
            batch.draw(shield, 10, 600, 30, 30);
            textShield.draw(batch, "" + player.getShield(), 50, 630);
            //textTime.draw(batch, "Enemies left: " + enemiesLeft, 10, 650);
        } else {
            textShopExit.draw(batch, "EXIT", 160, 700);
            textShopDmg.draw(batch, "DMG", 50, 630);
            if (Input.getX() < 160 + 90 &&
                    Input.getX() > 160 &&
                    Input.getY() > 720 - 700 &&
                    Input.getY() < 720 - 700 + 40 &&
                    Gdx.input.isTouched(0)) {
                isWave = true;
            }
            if (Input.getX() < 50 + 50 &&
                    Input.getX() > 50 &&
                    Input.getY() > 720 - 630 &&
                    Input.getY() < 720 - 630 + 30 &&
                    Gdx.input.isTouched(0)) {
                player.buy();
            }
        }
        batch.end();
        camera.update();
    }
    
    public void move() {
        player.move();
        for (Enemy enemy : enemies) {
            enemy.move();
        }
        enemies.removeAll(toRemove);
        for (Bullet bullet : bullets) {
            if (!bullet.move()) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
        for (Drop drop : drops) {
            if (!drop.move()) {
                toRemove.add(drop);
            }
        }
        drops.removeAll(toRemove);
        toRemove.clear();
    }
    
    public void collide() {
        for ( Bullet bullet : bullets) {
            if (!bullet.isPlayers()) {
                if (bullet.collide(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                    toRemove.add(bullet);
                    player.hit(bullet.getDmg());
                    //invurnerable = 1.5f;
                }
            }
            else {
                for (Enemy enemy : enemies) {
                    if (bullet.collide(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight())) {
                        toRemove.add(bullet);
                        if (enemy.hit(bullet.getDmg())) {
                            enemiesLeft--;
                            toRemoveE.add(enemy);
                        }
                    }
                }
            }
        }
        //if (invurnerable > 0) invurnerable -= Gdx.graphics.getDeltaTime();
        //else invurnerable = 0;
        
        for (Enemy enemy : enemies) {
            if (invurnerable == 0) {
                if (enemy.collide(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                    enemiesLeft--;
                    toRemoveE.add(enemy);
                    player.hit(enemy.getHealth());
                    //invurnerable = 1.5f;
                }
            }
        }
        
        for ( Drop drop : drops) {
            if (drop.collide(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                toRemove.add(drop);
                drop.onCollision(player);
            }
        }
        bullets.removeAll(toRemove);
        drops.removeAll(toRemove);
        for (Enemy enemy : toRemoveE) {
            enemy.die(bullets, drops);
            player.addPoints(enemy.getPoints());
        }
        enemies.removeAll(toRemoveE);
        toRemoveE.clear();
        toRemove.clear();
    }
    
    public void shoot() {
        playerShoot += Gdx.graphics.getDeltaTime();
        enemiesShoot += Gdx.graphics.getDeltaTime();
        if (playerShoot > 0.3 && player.getAlive()) {
            player.shoot(bullets); //bullets.add(new Bullet(true, player.getX() + player.getWidth() / 2 - 5, player.getY() + player.getHeight(), new Texture("bullet.png"), 10, 15, true, 400));
            playerShoot = 0;
        }
        if (enemiesShoot > 0.1) {
            for (Enemy enemy : enemies) {
                enemy.shoot(bullets);
            }
            enemiesShoot = 0;
        }
    }
    
    public void spawn() {
        enemiesSpawn += Gdx.graphics.getDeltaTime();
        if (enemiesSpawn > 1 / spawnSpeed && enemiesToSpawn > 0) {
            if (MathUtils.random(normChance + medChance) > (normChance + medChance) - normChance)
                enemies.add(new NormalEnemy(hpMod, asMod));
            else if (MathUtils.random(medChance + tankChance) > (medChance + tankChance) - medChance) {
                enemies.add(new MediumEnemy(hpMod, asMod));
            }
            else enemies.add(new TankEnemy(hpMod, asMod));
            enemiesSpawn = 0;
            enemiesToSpawn--;
        }
    }
    
    public void decay() {
        if (player.getShield() < 1) playerShield += Gdx.graphics.getDeltaTime();
        else playerShield = 0;
        if (playerShield > 15) {
            if (player.regenShield()) playerShield = 0;
            
        }
        player.decay();
    }
    
    public void wave() {
        gameTime += Gdx.graphics.getDeltaTime();
        if (enemiesLeft == 0 ) nextWave += Gdx.graphics.getDeltaTime();
        else nextWave = 0;
        if (wave == 0) {
            enemiesToSpawn = 20;
            enemiesLeft = 20;
            wave = 1;
        }
        if (wave == 1 && nextWave >= 3) {
            enemiesToSpawn = 20;
            enemiesLeft = 20;
            normChance = 16;
            medChance = 4;
            wave = 2;
        }
        if (wave == 2 && nextWave >= 3) {
            enemiesToSpawn = 25;
            enemiesLeft = 25;
            normChance = 18;
            medChance = 7;
            wave = 3;
        }
        if (wave == 3 && nextWave >= 3) {
            enemiesToSpawn = 25;
            enemiesLeft = 25;
            normChance = 16;
            medChance = 6;
            tankChance = 3;
            wave = 4;
        }
        if (wave == 4 && nextWave >= 3) {
            lastEnemies = 30;
            enemiesToSpawn = 30;
            enemiesLeft = 30;
            normChance = 18;
            medChance = 8;
            tankChance = 4;
            wave = 5;
        }
        if (wave >= 5 && nextWave >= 3) {
            spawnSpeed += 0.1f;
            lastEnemies += 5;
            enemiesToSpawn = lastEnemies;
            enemiesLeft = enemiesToSpawn;
            normChance = 4;
            medChance = 2;
            tankChance = 1;
        }
    }
    
    @Override
    public void dispose () {
        batch.dispose();
    }
}
