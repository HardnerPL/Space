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
    public NormalEnemy(int x, int hp, float as) {
        super(x, 720, new Texture("player.png"), 50, 50, 50, hp, as);
        bulletWidth = 10;
        bulletHeight = 15;
        bulletSpeed = 150;
        bulletDamage = 1;
    }
}
