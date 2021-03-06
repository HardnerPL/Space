/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.mygdx.game.enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Bullet;
import com.mygdx.game.drops.Drop;
import com.mygdx.game.Entity;
import com.mygdx.game.drops.DropDamage;
import com.mygdx.game.drops.DropDoubleShot;
import com.mygdx.game.drops.DropHearth;
import com.mygdx.game.drops.DropMoney;
import com.mygdx.game.drops.DropShield;
import com.mygdx.game.drops.DropSpeed;
import java.util.ArrayList;

public class Enemy extends Entity{
    
    protected int health;
    protected int maxHealth;
    protected int points;
    protected int bulletWidth, bulletHeight, bulletSpeed;
    protected float shootChance;
    
    public Enemy(Texture texture, int width, int height, int speed, int lives, float shootChance) {
        super((int)(MathUtils.random() * 355.0), 720, texture, width, height);
        this.speed = speed;
        maxHealth = lives;
        health = maxHealth;
        this.shootChance = shootChance;
        points = 100 + 50 * ( lives - 1);
    }
    
    @Override
    public boolean move() {
        if ( y < 0 - height ) {
            y = 900;
            x = (int)(MathUtils.random() * 355.0);
        }
        y -= speed * Gdx.graphics.getDeltaTime();
        return true;
    }
    
    public void shoot(ArrayList<Bullet> bullets) {
        if (MathUtils.random() < shootChance / 2) bullets.add(new Bullet(false, x + width / 2 - bulletWidth / 2, y - bulletHeight, new Texture("bullet.png"), bulletWidth, bulletHeight, false,  150, 1));
    }
    
    public boolean hit(int dmg) {
        for (int i = 0; i < dmg; i++) {
            if (0 == --health) return true;
        }
        return false;
    }
    
    public void die(ArrayList<Bullet> bullets, ArrayList<Drop> drops) {
        /*if (MathUtils.random() > 0.9f) {
        bullets.add(new Bullet(false, x + width / 2 - 5, y - 10, new Texture("bullet.png"), 10, 15, false, 150));
        bullets.add(new Bullet(false, x + 50 + width / 2 - 5, y - 10, new Texture("bullet.png"), 10, 15, false, 150));
        bullets.add(new Bullet(false, x - 50 + width / 2 - 5, y - 10, new Texture("bullet.png"), 10, 15, false, 150));
        } */
        if (MathUtils.random() > 0.9f) {
            drops.add(new DropMoney(x + width / 2 - 10, y - 20));
        }

        else if (MathUtils.random() > 0.97f) {
            drops.add(new DropDoubleShot(x + width / 2 - 10, y - 20));
        }
        
        else if (MathUtils.random() > 0.97f) {
            drops.add(new DropDamage(x + width / 2 - 10, y - 20));
        }
        
        else if (MathUtils.random() > 0.97f) {
            drops.add(new DropSpeed(x + width / 2 - 10, y - 20));
        }
        
        else if (MathUtils.random() > 0.97f) {
            drops.add(new DropShield(x + width / 2 - 10, y - 20));
        }
        
        else if (MathUtils.random() > 0.99f) {
            drops.add(new DropHearth(x + width / 2 - 10, y - 20));
        }
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
}
