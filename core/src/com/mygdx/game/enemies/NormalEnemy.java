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
public class NormalEnemy extends Enemy {
    public NormalEnemy(float hpMod, float asMod) {
        super(new Texture("player.png"), 50, 50, 50, (int)(hpMod * 2), asMod * 0.2f);
        bulletWidth = 10;
        bulletHeight = 15;
        bulletSpeed = 150;
        bulletDamage = 1;
    }
}
