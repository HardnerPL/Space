/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.enemies;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author HardnerPL
 */
public class TankEnemy extends Enemy {
    public TankEnemy(float hpMod, float asMod) {
        super(new Texture("player.png"), 80, 80, 30, (int)(hpMod * 50), asMod * 0.1f);
        bulletWidth = 16;
        bulletHeight = 22;
        bulletSpeed = 120;
    }
    
}
