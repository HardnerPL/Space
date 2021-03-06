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
public class MediumEnemy extends Enemy {
    
    public MediumEnemy(float hpMod, float asMod) {
        super(new Texture("player.png"), 60, 60, 40, (int)(hpMod * 30), asMod * 0.2f);
        bulletWidth = 10;
        bulletHeight = 15;
        bulletSpeed = 150;
    }
    
}
