package com.mygdx.game;

import com.mygdx.game.drops.Drop;
import com.mygdx.game.enemies.Enemy;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
    
    Input input = new Input();
    
    SpriteBatch batch;
    BitmapFont font1, font2, font3;
    BitmapFont textPoints, textLives, textTime, textMoney;
    BitmapFont priceDmg, priceHp, priceAs, textDmg, textHp, textAs, textExit;
    Rectangle recShopDmg, recShopExit;
    Texture hearth, background, shop;
    
    Player player;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Drop> drops = new ArrayList<Drop>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    List<Entity> toRemove = new ArrayList<Entity>();
    List<Enemy> toRemoveE = new ArrayList<Enemy>();
    
    boolean isWave = false;
    
    float playerShoot = 0, playerShield = 0, enemiesShoot = 0, enemiesSpawn = 0, gameTime = 0, nextWave = 0;
    float invurnerable = 0;
    
    float  hpMod = 1, dmgMod = 1, asMod = 1;
    float normChance = 1, medChance = 0, tankChance = 0;
    float spawnSpeed = 0.7f;
    int enemiesLeft;
    int enemiesToSpawn;
    int lastEnemies;
    int wave = 0;
    
    int shopInWaves = 5;
    int shopMaxLvl = 6;
    int[] bought = {0, 0, 0};
    int[] price = {200, 500, 1000, 2000, 5000, 10000, 20000};
    boolean touched;
    
    
    
    
    @Override
    public void create () {
        camera = new OrthographicCamera(405, 720);
        batch = new SpriteBatch();
        
        hearth = new Texture("hearth.png");
        background = new Texture("background.jpg");
        shop = new Texture("shop.png");
        player = new Player(100, 20, new Texture("ship.png"), 50, 35);
        
        font1 = new BitmapFont();
        font2 = new BitmapFont();
        textPoints = new BitmapFont();
        textLives = new BitmapFont();
        textTime = new BitmapFont();
        textMoney = new BitmapFont();
        textExit = new BitmapFont();
        textDmg = new BitmapFont();
        textHp = new BitmapFont();
        textAs = new BitmapFont();
        priceDmg = new BitmapFont();
        priceHp = new BitmapFont();
        priceAs = new BitmapFont();
        
        font1.getData().scale(1.5f);
        font2.getData().scale(2.25f);
        textPoints.getData().scale(1.5f);
        textLives.getData().scale(1.5f);
        textMoney.getData().scale(1.5f);
        
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
    }
    
    @Override
    public void render () {
        if (player.alive == false) {
            player = new Player(100, 20, new Texture("ship.png"), 50, 35);
            enemies.clear();
            drops.clear();
            bullets.clear();
            shopInWaves = 5;
            hpMod = 1; dmgMod = 1; asMod = 1;
            normChance = 1; medChance = 0; tankChance = 0;
            spawnSpeed = 0.7f;
            wave = 0;
        }
        batch.setProjectionMatrix(camera.combined);
        if (!Gdx.input.justTouched()) touched = false;
        
        if (isWave) {
            move();
            collide();
            shoot();
            spawn();
            decay();
            wave();
        }
        
        batch.begin();
        batch.draw(background, 0, 0);
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
            font1.draw(batch, "" + wave, 180, 710);
            font1.draw(batch, "$" + player.getMoney(), 10, 710);
            batch.draw(hearth, 315, 680, 30, 30);
            font1.draw(batch, "" + player.getHealth(), 350, 710);
            //textTime.draw(batch, "Wave" + wave, 10, 650);
        } else {
            player.heal();
            batch.draw(shop, 0, 0);
            font2.setColor(1, 1, 1, 1);
            font2.draw(batch, "$" + player.getMoney(), 50, 690);
            font2.setColor(0, 0, 0, 1);
            font2.draw(batch, "DMG", 55, 560);
            if (bought[0] > shopMaxLvl) font2.draw(batch, "MAX", 220, 560);
            else font2.draw(batch, "$" + price[bought[0]], 220, 560);
            font2.draw(batch, "HP", 77, 390);
            if (bought[1] > shopMaxLvl) font2.draw(batch, "MAX", 220, 390);
            else font2.draw(batch, "$" + price[bought[1]], 220, 390);
            font2.draw(batch, "AS", 80, 220);
            if (bought[2] > shopMaxLvl) font2.draw(batch, "MAX", 220, 220);
            else font2.draw(batch, "$" + price[bought[2]], 220, 220);
            //batch.draw(priceTexture.get(bought[0]), 203, 720 - 144 - 72);
            //batch.draw(priceTexture.get(bought[1]), 203, 720 - 314 - 72);
            //batch.draw(priceTexture.get(bought[2]), 203, 720 - 482 - 72);
            if (Input.getX() < 109 + 183 && //EXIT
                    Input.getX() > 109 &&
                    Input.getY() > 603 &&
                    Input.getY() < 603 + 72 &&
                    Gdx.input.justTouched()) {
                shopInWaves = 5;
                isWave = true;
            }
            if (Input.getX() < 203 + 183 && //DMG
                    Input.getX() > 203 &&
                    Input.getY() > 144 &&
                    Input.getY() < 144 + 72 &&
                    Gdx.input.justTouched() && !touched && bought[0] <= shopMaxLvl) {
                if (player.spendMoney(price[bought[0]])) {
                    bought[0]++;
                    player.upgrade(0);
                }
            }
            if (Input.getX() < 203 + 183 && //HP
                    Input.getX() > 203 &&
                    Input.getY() > 314 &&
                    Input.getY() < 314 + 72 &&
                    Gdx.input.justTouched() && !touched && bought[1] <= shopMaxLvl) {
                if (player.spendMoney(price[bought[1]])) {
                    bought[1]++;
                    player.upgrade(1);
                }
            }
            if (Input.getX() < 203 + 183 && //AS
                    Input.getX() > 203 &&
                    Input.getY() > 482 &&
                    Input.getY() < 482 + 72 &&
                    Gdx.input.justTouched() && !touched && bought[2] <= shopMaxLvl) {
                if (player.spendMoney(price[bought[2]])) {
                    bought[2]++;
                    player.upgrade(2);
                }
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
                    player.hit();
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
                    player.hit();
                    //invurnerable = 1.5f;
                }
            }
        }
        
        for ( Drop drop : drops) {
            if (drop.collide(player.getX(), player.getY(), player.getWidth(), player.getHeight()) || enemiesLeft == 0) {
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
        if (enemiesLeft == 0) {
            bullets.clear();
        }
    }
    
    public void shoot() {
        playerShoot += Gdx.graphics.getDeltaTime();
        enemiesShoot += Gdx.graphics.getDeltaTime();
        if (playerShoot > 1.0 / player.getAttackSpeed() * 4 && player.getAlive()) {
            player.shoot(bullets); //bullets.add(new Bullet(true, player.getX() + player.getWidth() / 2 - 5, player.getY() + player.getHeight(), new Texture("bullet.png"), 10, 15, true, 400));
            playerShoot = 0;
        }
        if (enemiesShoot > 0.5) {
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
        player.decay();
    }
    
    public void wave() {
        gameTime += Gdx.graphics.getDeltaTime();
        if (enemiesLeft == 0 ) {
            if (shopInWaves == 0) isWave = false;
            else nextWave += Gdx.graphics.getDeltaTime();
        }
        else nextWave = 0;
        if (wave == 0) {
            enemiesToSpawn = 20;
            enemiesLeft = 20;
            wave = 1;
            shopInWaves--;
        }
        else if (wave == 1 && nextWave >= 3) {
            enemiesToSpawn = 20;
            enemiesLeft = 20;
            normChance = 16;
            medChance = 4;
            wave = 2;
            shopInWaves--;
        }
        else if (wave == 2 && nextWave >= 3) {
            enemiesToSpawn = 25;
            enemiesLeft = 25;
            normChance = 18;
            medChance = 7;
            wave = 3;
            shopInWaves--;
        }
        else if (wave == 3 && nextWave >= 3) {
            enemiesToSpawn = 25;
            enemiesLeft = 25;
            normChance = 16;
            medChance = 6;
            tankChance = 3;
            wave = 4;
            shopInWaves--;
        }
        else if (wave == 4 && nextWave >= 3) {
            lastEnemies = 30;
            enemiesToSpawn = 30;
            enemiesLeft = 30;
            normChance = 18;
            medChance = 8;
            tankChance = 4;
            wave = 5;
            shopInWaves--;
        }
        else if (wave >= 5 && nextWave >= 3) {
            spawnSpeed += 0.1f;
            lastEnemies += 5;
            enemiesToSpawn = lastEnemies;
            enemiesLeft = enemiesToSpawn;
            normChance = 4;
            medChance = 2;
            tankChance = 1;
            shopInWaves--;
            wave++;
        }
    }
    
    @Override
    public void dispose () {
        batch.dispose();
    }
}
