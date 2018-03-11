package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Projectile extends Entity{
    protected int speed;
    
    public Projectile(float x, float y, Texture texture, int width, int height, int speed) {
        super(x, y, texture, width, height);
        this.speed = speed;
    }
    
    public void onCollision(Player player) {}
}