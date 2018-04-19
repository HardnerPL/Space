/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;

public class Player extends Entity {
    
    boolean alive;
    int money, points;
    int health, maxHealth;
    int startX, touchX;
    int bulletDamage, bulletSpeed;
    int baseDamage, baseSpeed;
    int attackSpeed;
    float doubleShot = 0, speedBoost = 0, damageBoost = 0, shield = 0;
    
    public Player(int x, int y, Texture texture, int width, int height) {
        super(x, y, texture, width, height);
        alive = true;
        maxHealth = 3;
        health = maxHealth;
        touchX = -1;
        points = 0;
        baseDamage = 10;
        bulletDamage = 10;
        bulletSpeed = 250;
        baseSpeed = 10;
        attackSpeed = 10;
        money = 0;
    }
    
    @Override
    public boolean move() {
        if (Gdx.input.isTouched(0)) {
            if (touchX == -1) {
                startX = (int)x;
                touchX = Input.getX();
            }
            System.out.println(startX + " " + touchX + " " + Input.getX());
            x = 1.65f * (Input.getX() - touchX) + startX;
        }
        else touchX = -1;
        
        //x =  5.45f / 3 * (Input.getX() - width / 2) - 125;
        if (x < 0) x = 0;
        if (x > 405 - width) x = 405 - width;
        return true;
    }
    
    public void decay() {
        if (doubleShot > 0) doubleShot -= Gdx.graphics.getDeltaTime();
        if (damageBoost > 0) damageBoost -= Gdx.graphics.getDeltaTime();
        else bulletDamage = baseDamage;
        if (speedBoost > 0) speedBoost -= Gdx.graphics.getDeltaTime();
        else attackSpeed = baseSpeed;
        if (shield > 0) shield -= Gdx.graphics.getDeltaTime();
    }
    
    public void shoot(ArrayList<Bullet> bullets) {
        if (doubleShot > 0) {
            bullets.add(new Bullet(true, x + width / 2 - 30 / 2, y + height + 15, new Texture("bullet.png"), 10, 15, true,  bulletSpeed, (int) (bulletDamage / 1.5f)));
            bullets.add(new Bullet(true, x + width / 2 + 10 / 2, y + height + 15, new Texture("bullet.png"), 10, 15, true,  bulletSpeed, (int) (bulletDamage / 1.5f)));
        }
        else bullets.add(new Bullet(true, x + width / 2 - 10 / 2, y + height + 15, new Texture("bullet.png"), 10, 15, true,  bulletSpeed, bulletDamage));
    }
    
    public boolean getAlive() {
        return alive;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getMoney() {
        return money;
    }
    
    public int getAttackSpeed() {
        return attackSpeed;
    }
    
    public void addPoints(int a) {
        points += a;
    }
    
    public void addMoney(int a) {
        money += a;
    }
    
    public boolean spendMoney(int a) {
        if (money >= a) {
            money -= a;
            return true;
        }
        return false;
    }
    
    public void hit() {
        if (shield <= 0) {
            if (0 < --health) {}
            else alive = false;
        }
    }
    
    public void regenHealth() {
        if (health < maxHealth) health++;
        else money += 200;
    }
    
    public void heal() {
        health = maxHealth;
    }
    
    public void doubleShot() {
        doubleShot = 10;
    }
    
    public void speedBoost() {
        speedBoost = 10;
        attackSpeed = (int) (attackSpeed * 1.5);
    }
    
    public void damageBoost() {
        damageBoost = 10;
        bulletDamage = (int) (bulletDamage * 1.5);
    }
    
    public void shield() {
        shield = 10;
    }
    
    public void upgrade(int a) {
        switch (a) {
            case 0:
                baseDamage *= 1.35;
                bulletSpeed+=50;
                break;
            case 1:
                maxHealth++;
                break;
            case 2:
                baseSpeed *= 1.2;
                break;
        }
    }
}
