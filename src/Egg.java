import java.awt.Image;   
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class Egg extends Sprite{

	static Image eggSprite = null;
	static Image splatSprite = null;
	Image currentSprite;
	double vHor;
	double vFall;
	boolean splat;
	
	//Constructor
	Egg(Model ref, Bird bird_ref) throws IOException{
		this.x = bird_ref.x;
		this.y = bird_ref.y; 
		this.z = 3; //Eggs render at layer 3
		this.vFall = bird_ref.vFall - 4;
		this.vHor = 12;
		this.mod_ref = ref; 
		this.splat = false;
		this.height = 30; 
		this.width = 0; // Set to zero, so collision not evaluated until full instersection with tree.
		
		if (eggSprite == null){
			eggSprite = ImageIO.read(new File("resources/egg.png"));
		}
		if (splatSprite == null){
			splatSprite = ImageIO.read(new File("resources/splat.png"));
		}
		this.currentSprite = eggSprite;
	}
	
	Egg(Egg that, Model new_mod){
		this.x = that.x;
		this.y = that.y;
		this.z = that.z;
		this.vFall = that.vFall;
		this.vHor = that.vHor;
		this.mod_ref = new_mod;
		this.splat = that.splat;
		this.height = 30;
		this.width = 0; 
		
	}
	
	public void update(){
		if (this.splat == false){
			this.x += this.vHor;
			this.vFall += 0.5;
			this.y += this.vFall;
		
			//Loop through Tree objects in mod_ref.sprites LinkedList
			for (int i = 0; i < mod_ref.sprites.size(); i++){
				if (mod_ref.sprites.get(i) instanceof Tree){			
					int eR= this.x+this.width;
					int tR= mod_ref.sprites.get(i).x + mod_ref.sprites.get(i).width;
					int eB= this.y + this.height;
					int tB= mod_ref.sprites.get(i).y + mod_ref.sprites.get(i).height;
					//Check for orientation of tree, make appropriate check for overlap
					if (mod_ref.sprites.get(i).dat == 0){
						if ((eR > mod_ref.sprites.get(i).x) && (this.x < tR) && (eB > mod_ref.sprites.get(i).y)){ //14 adjusts for bird "sweat" in sprite
							this.collision(i);	
						}
					}else{
						if (eR > mod_ref.sprites.get(i).x && (this.x < tR) && (this.y < tB)){
							this.collision(i);
						}
					}
				}
			}
		}else{
			this.x += this.vHor;
			this.y += this.vFall;
		}
		if (this.y > 650 || this.y < -200 || this.x > 600 || this.x < -100){
			mod_ref.it.remove();
		}
		if (mod_ref.kill == true && this.splat == true){
			this.vHor = 0.0;
		}
	}
	public void draw(Graphics g_ref){
		g_ref.drawImage(this.currentSprite, this.x, this.y, null);
	}
	

	public void collision(){
		this.currentSprite = splatSprite;
		this.vFall = 0.0;
		this.vHor = -4.0;
		this.splat = true;
	}
	
	public void collision(int i){
		this.collision();
		//System.out.println("Splat");
		this.dat = this.mod_ref.sprites.get(i).dat;
		this.mod_ref.sprites.get(i).collision();
		if (this.dat == 0){
			this.vFall = 14.0;
		}else{
			this.vFall = -14.0;
		}
	}
	public void leftClick(){}
	public void rightClick(){}
	
	Sprite replicate(Model new_mod){
		return new Egg(this, new_mod);
	}
}
