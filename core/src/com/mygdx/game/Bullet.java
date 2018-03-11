/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author HardnerPL
 */
public class Bullet extends Projectile {
    
    private final boolean up;
    private final int dmg;
    boolean players;
    public Bullet(boolean players, float x, float y, Texture texture, int width, int height, boolean up, int speed, int dmg) {
        super(x, y, texture, width, height, speed);
        this.up = up;
        this.players = players;
        this.dmg = dmg;
    }
    
    @Override
    protected boolean move() {
        if ( y > 720 || y < 0) {
            return false;
        }
        if (up) y += speed * Gdx.graphics.getDeltaTime();
        else y -= speed * Gdx.graphics.getDeltaTime();
        return true;
    }
    
    boolean isPlayers() {
        return players;
    }
    
    public int getDmg() {
        return dmg;
    }
    
    //@Override
    /*public void onCollision(Player player) {
        player.hit();
    } */
}
