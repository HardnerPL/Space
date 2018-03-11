/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.drops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Player;
import com.mygdx.game.Projectile;

/**
 *
 * @author HardnerPL
 */
public class Drop extends Projectile {
    
    public Drop( float x, float y, Texture texture) {
        super(x, y, texture, 20, 20, 80);
    }
    @Override
    public boolean move() {
        if ( y > 720 || y < 0) {
            return false;
        }
        y -= speed * Gdx.graphics.getDeltaTime();
        return true;
    }
    
    @Override
    public void onCollision(Player player) {}
}
