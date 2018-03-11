/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;

public class Entity {
    protected float x, y;
    protected int speed;
    protected int width, height;
    protected Texture texture;
    
    protected Entity(float x, float y, Texture texture, int width, int height) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = width;
        this.height = height;
    }
    
    protected boolean move() {return false; }   
    
        
    protected boolean collide(float x, float y, int width, int height) {
        return this.x < x + width &&
                this.x + this.width > x &&
                this.y < y + height &&
                this.y + this.height > y;
    }
    
// GET // GET // GET //
    
    protected float getX() {
        return x;
    }
    
    protected float getY() {
        return y;
    }
    
    protected Texture getTexture() {
        return texture;
    }
    
    protected int getWidth() {
        return width;
    }
    
    protected int getHeight() {
        return height;
    }
    
// SET // SET // SET //
    protected void setX(int x) {
        this.x = x;
    }
    
    protected void setY(int y) {
        this.y = y;
    }
    
    protected void setTexture(Texture texture) {
        this.texture = texture;
    }
}
