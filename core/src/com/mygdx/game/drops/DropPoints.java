/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.drops;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Player;

/**
 *
 * @author HardnerPL
 */
public class DropPoints extends Drop {
    int value;
    public DropPoints(float x, float y) {
        super(x, y, new Texture("bullet.png"));
        float random = MathUtils.random();
        value = 100;
        if (random > 0.5f) value = 200;
        if (random > 0.75f) value = 500;
        if (random > 0.90f) value = 1000;
        if (random > 0.99f) value = 5000;
    }
    @Override
    public void onCollision(Player player) {
        player.addPoints(value);
    }
}
