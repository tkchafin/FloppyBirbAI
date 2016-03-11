import java.awt.Image;    
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class Bird extends Sprite{


	double vFall;
	double vHor;
	int tFlap;
	int maxY;
	int minY;
	int maxAmmo;
	int defAmmo;
	int offset; //Account for droplets in hit detection
	int ammo; //Control number of eggs to throw
	double gravity; //Gravity simulation- "acceleration"
	boolean dead; //Are we dead?
	boolean monsterCalled; //Have we called death sequence?
	boolean ammoUpdate;
	Image currentSprite;
	static Image frame1 = null;
	static Image frame2 = null;
	static Image frame3 = null;
	static Image frame4 = null;
	static Image deadSprite = null;
	static Image ammoSprite = null;
	
	//Default constructor
	Bird(Model ref) throws IOException{
		this.mod_ref = ref;
		this.height=70;
		this.width=60;
		this.x = 100;
		this.y = 0;
		this.z = 1; //Bird renders at level 1
		this.maxAmmo = 6;
		this.defAmmo = 3;
		this.ammo = this.defAmmo;
		this.dead = false;
		this.vFall = 0.1;
		this.vHor = 0.0;
		this.tFlap = 0;
		this.maxY = 650;
		this.minY = -200;
		this.offset = 20;
		this.gravity = 0.1;
		//this.initAmmo(this);
		this.ammoUpdate = false;
		if (frame1 == null){
			frame1 = ImageIO.read(new File("resources/frame1.png"));
		}
		if (frame2 == null){
			frame2 = ImageIO.read(new File("resources/frame2.png"));
		}
		if (frame3 == null){
			frame3 = ImageIO.read(new File("resources/frame3.png"));
		}
		if (frame4 == null){
			frame4 = ImageIO.read(new File("resources/frame4.png"));
		}
		if (ammoSprite == null){
			ammoSprite = ImageIO.read(new File("resources/egg.png"));
		}
		if (deadSprite == null){
			deadSprite = ImageIO.read(new File("resources/deadbird.png"));
		}
		this.currentSprite = frame1;
	}
	
	Bird(Bird that, Model new_mod){
		this.mod_ref = new_mod; //Initialize mod_ref to null
		this.ammo = that.ammo;
		this.height=that.height;
		this.width = that.width;
		this.x = that.x;
		this.y = that.y;
		this.z = that.z;
		this.dead = that.dead;
		this.ammoUpdate = that.ammoUpdate;
		this.vFall = that.vFall;
		this.vHor = that.vHor;
		this.tFlap = that.tFlap;
		this.minY = that.minY;
		this.maxY = that.maxY;
		this.maxAmmo = that.maxAmmo;
		this.defAmmo = that.defAmmo;
		this.offset = that.offset;
		this.gravity = that.gravity;
	}
	
	public void update(){

		//If bird out of bounds, set model to END state
		if (this.y > this.maxY || this.y < this.minY){
			this.collision();
		}
		this.vFall += this.gravity; 
		this.y += this.vFall;
		this.x += this.vHor;
		if (this.dead == false){
			//Control interval for flapping
			if (this.tFlap <= 35){
				this.tFlap++;
			}else{
				this.tFlap = 0;
			}	
			//Loop through Tree objects in mod_ref.sprites LinkedList
			for (int i = 0; i < mod_ref.sprites.size(); i++){
				if (mod_ref.sprites.get(i) instanceof Tree){			
					int bR= this.x+this.width;
					int tR= mod_ref.sprites.get(i).x + mod_ref.sprites.get(i).width;
					int bB= this.y + this.height;
					int tB= mod_ref.sprites.get(i).y + mod_ref.sprites.get(i).height;
					//Check for orientation of tree, make appropriate check for overlap
					if (mod_ref.sprites.get(i).dat == 0){
						if ((bR > mod_ref.sprites.get(i).x) && (this.x < tR) && (bB > mod_ref.sprites.get(i).y)){ //14 adjusts for bird "sweat" in sprite
							this.collision();
						}
					}else{
						if (bR > mod_ref.sprites.get(i).x && (this.x < tR) && (this.y + this.offset < tB)){
							this.collision();
						}	
					}
				}
				//Update ammo
				this.updateAmmo();
			}
		}else{
			//System.out.println("Dead Bird");
			//When bird falls off screen, call exit sequence
			if (this.y > 350){
				if (this.monsterCalled == false){
					this.callMonster(mod_ref);
					this.currentSprite = null;
					this.monsterCalled = true;
				}
			//When bird exits view, stop changing position
			}else if (this.y > 550){
				this.vFall = 0.0; 
				this.vHor = 0.0;
			}
		}
	}
	
	public void draw(Graphics g_ref){
		//Animate bird by choosing different frame at set intervals which are incremented in this.update()
		if (this.tFlap < 15){
			this.currentSprite = frame1;
		}else if (this.tFlap <21){
			this.currentSprite = frame2;
		}else if (this.tFlap <29){
			this.currentSprite = frame3;
		}else{
			this.currentSprite = frame4;
		}
		if (mod_ref.kill == true){
			this.currentSprite = deadSprite;
		}	
		g_ref.drawImage(currentSprite, this.x, this.y, null);
	}
	
	public void leftClick(){
		if (this.dead == false){
			this.vFall = -6;
		}
	}
	
	public void collision(){
		mod_ref.kill = true;
		this.killBird();
	}
	
	public void killBird(){
		this.dead = true; 
		this.vHor = 2;
		this.currentSprite = deadSprite;
		this.z = 3;
		//if (this.y > 500){
		//	this.killGame();
		//}
	}
	
	//Made as a method, in case need to add additional end behaviour later
	public void killGame(){
		mod_ref.end = true; //Bird is only Sprite with power to kill game
	}
	
	public void rightClick(){
		if (this.dead == false){
			if (this.ammo > 0){
				try {
					mod_ref.it.add( new Egg(mod_ref, this) );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(System.err);
					System.exit(1);
				}
				//this.ammo--;
			}
		}
	}
	
	public void callMonster(Model ref){
		mod_ref.it.add(new Plant(ref, this));
	}
	
	void initAmmo(Bird b_ref){
		try {
			mod_ref.it.add(new AmmoBar(b_ref));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	void updateAmmo(){
		if (this.mod_ref.points > 0){
			if (this.mod_ref.points % 10.0 == 0.0){
				if (this.ammo < this.maxAmmo && this.ammoUpdate == false){
					this.ammo++;
					this.ammoUpdate = true;
				}
			}else{
				this.ammoUpdate = false;
			}
		}
	}
	
	Sprite replicate(Model new_mod){
		return new Bird(this, new_mod);
	}
}
