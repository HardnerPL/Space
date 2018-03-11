/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.Gdx;

/**
 *
 * @author HardnerPL
 */
public class Input {
    
    public static int getX() {
        return (int)(Gdx.input.getX() * (405.0f / Gdx.graphics.getWidth()));
    }
    
    public static int getY() {
        return (int)(Gdx.input.getY() * (720.0f / Gdx.graphics.getHeight()));
    }
}
