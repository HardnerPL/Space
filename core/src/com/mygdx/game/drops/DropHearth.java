/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game.drops;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Player;

/**
 *
 * @author HardnerPL
 */
public class DropHearth extends Drop {
    
    public DropHearth(float x, float y) {
        super(x, y, new Texture("hearth.png"));
    }
    @Override
    public void onCollision(Player player) {
        player.regenHealth();
    }
}
