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
    int health;
    int shield;
    int startX, touchX;
    int bulletDamage;
    float doubleShot = 0;
    
    public Player(int x, int y, Texture texture, int width, int height) {
        super(x, y, texture, width, height);
        alive = true;
        health = 3;
        shield = 1;
        touchX = -1;
        points = 0;
        bulletDamage = 1;
    }
    
    @Override
    public boolean move() {
        if (Gdx.input.isTouched(0)) {
            if (touchX == -1) {
                startX = (int)x;
                touchX = Input.getX();
            }
            System.out.println(startX + " " + touchX + " " + Input.getX());
            x = 1.7f * (Input.getX() - touchX) + startX;
        }
        else touchX = -1;
        
        //x =  5.45f / 3 * (Input.getX() - width / 2) - 125;
        if (x < 0) x = 0;
        if (x > 405 - width) x = 405 - width;
        return true;
    }
    
    public void decay() {
        if (doubleShot > 0) doubleShot -= Gdx.graphics.getDeltaTime();
    }
    
    public void shoot(ArrayList<Bullet> bullets) {
        if (doubleShot > 0) {
            bullets.add(new Bullet(true, x + width / 2 - 30 / 2, y + height + 15, new Texture("bullet.png"), 10, 15, true,  250, bulletDamage));
            bullets.add(new Bullet(true, x + width / 2 + 10 / 2, y + height + 15, new Texture("bullet.png"), 10, 15, true,  250, bulletDamage));
        }
        else bullets.add(new Bullet(true, x + width / 2 - 10 / 2, y + height + 15, new Texture("bullet.png"), 10, 15, true,  250, bulletDamage));
    }
    
    public boolean getAlive() {
        return alive;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getShield() {
        return shield;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void addPoints(int a) {
        points += a;
    }
    
    public void buy() {
        bulletDamage++;
    }
    
    public void addMoney(int a) {
        money += a;
    }
    
    public void hit(int dmg) {
        for (int i = 0; i < dmg; i++) {
            if (shield > 0) shield--;
            else if (0 < --health) {}
            else alive = false;
        }
    }
    
    public boolean regenShield() {
        if (shield < 1) {
            shield++;
            return true;
        }
        else return false;
    }
    
    public void regenHealth() {
        if (health < 3) health++;
    }

    public void doubleShot() {
        doubleShot = 10;
    }
}
