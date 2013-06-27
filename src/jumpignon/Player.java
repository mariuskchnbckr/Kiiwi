package jumpignon;

import org.newdawn.slick.*;

public class Player extends RenderItem{
    
    private int health;
    private int player_id;
    private int kill_score;
    private int death_score;
    private float y_velocity;
    private float x_velocity;
    private boolean isInAir;
    
    // Infos zum Rendern
    RenderItem follower;
    int z_info;
    private Image image;
    private Image image2;
    
    
    public Player(int h_id, int h_x, int h_y)
    {
        health = 2;
        player_id = h_id;
        kill_score = 0;
        death_score = 0;
        width = 48;
        height = 54;
        isInAir = false;
        y_velocity = 0.0f;
        
        this.respawn(h_x, h_y);
    }
  
    public void setImage(Image a, Image b){image = a;image2 = b;}
    
    public int showKills(){return kill_score;}
    
    public int showDeaths(){return death_score;}
    
    public boolean isFalling()
    {
        if(isInAir == true)
        {
            if(y_velocity > 0)  // Der Spieler springt offensichtlich
            {
                return false;
            }
            else    // Nur wenn eine Abwärtsbewegung stattfindet fällt der Spieler tatsächlich
            {
                return true;
            }
        }
        else{ return false; }
    }
    
    public void jump()
    {
        y_velocity = 12.0f;
    }
    
    public void setFalling()
    {
        isInAir = true;
    }
    
    @Override
    public void renderMe(Graphics g) throws SlickException
    {
        if(health == 2){g.drawImage(image, pos_x, pos_y);}
        else if(health ==1){g.drawImage(image2, pos_x, pos_y);}
        
        if(this.follower != null)
        {
            this.follower.renderMe(g);
        }
    }
    
    public void checkBottomCollisionWithPlayer(Player p1)
    {
        float linkerRandPlayer1 = this.pos_x;
        float rechterRandPlayer1 = this.pos_x + this.width;
        
        float linkerRandPlayer2 = p1.pos_x;
        float rechterRandPlayer2 = p1.pos_x + p1.width;
        
        if(     this.pos_y <= ( p1.get_height() + p1.get_pos_y() )        &&
                this.pos_y >= ( p1.get_height() + p1.get_pos_y() - 25)    &&
                linkerRandPlayer1 <= rechterRandPlayer2                   &&
                rechterRandPlayer1 >= linkerRandPlayer2                   &&
                
                p1.isFalling() == true  ) 
            
        {
            if(this.health == 1){p1.gainKill();}
            p1.jump();
            this.loseHp();
        }
    }
    
    public void gainKill()
    {
        kill_score += 1;
    }
    
    public void bottomCollisionWithObject(RenderItem i1)
    {
        isInAir = false;            // der Spieler muss zuvor gesprungen sein, also wird dieser Zustand gelöscht
        y_velocity = 0.0f;          // die Gravitation greift nicht wenn der Spieler auf einem Objekt steht
        this.pos_y = i1.pos_y - (this.height);      // der Spieler soll auf dem Objekt stehen
    }
    
    public void update(GameContainer container, int delta)
    {
        if(isInAir == true) {x_velocity = 0.35f;}
        else                {x_velocity = 0.5f;}
        
        switch(player_id)
        {
            case(1):
                   
            // [<-] Links bewegung
            if(container.getInput().isKeyDown(Input.KEY_LEFT)){
               pos_x -= x_velocity * delta;
            }
            // [<-] Rechts bewegung
            if(container.getInput().isKeyDown(Input.KEY_RIGHT)){
                pos_x += x_velocity * delta;
            }
            // [↕] Oben bewegung
            if(container.getInput().isKeyPressed(Input.KEY_UP) && isInAir == false){
                y_velocity = 1.0f * delta;
               isInAir = true;
            }
            
            break;
            case(2):
                   
            // [<-] Links bewegung
            if(container.getInput().isKeyDown(Input.KEY_A)){
               pos_x -= 0.5f * delta;
            }
            // [<-] Rechts bewegung
            if(container.getInput().isKeyDown(Input.KEY_D)){
                pos_x += 0.5f * delta;
            }
            // [↕] Oben bewegung
            if(container.getInput().isKeyPressed(Input.KEY_W) && isInAir == false){
                y_velocity = 1.0f * delta;
               isInAir = true;
            }
            
            break;
        }
        
        if(isInAir == true && y_velocity >= -12.0f)
        {
            y_velocity -= 0.05f * delta;
        }
        
        pos_y -= y_velocity;
    }
    
    public void loseHp()
    {
        health -= 1;
        
        if(health == 0)
        {
            death_score++;
            int respawn_x;
            switch(player_id)
            {
                case(1):
                    respawn_x = 100;
                break;
                case(2):
                    respawn_x = 700;
                break;
                default:
                    respawn_x = 450;
                break;
                        
            }
            this.respawn(respawn_x, 420-54);      // PLAYER 1 KONSTANTE!!
        }
    }
    
    public void gainHp()
    {
        if(health != 2)
        {
            health++;
        }
    }
    
    public void respawn(int h_x, int h_y)
    {
        pos_x = h_x;
        pos_y = h_y;
        health = 2;
    }
    
}
