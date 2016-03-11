import java.awt.Image;   
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class Plant extends Sprite{

	Bird bird_ref;
	Image currentSprite;
	int tAnimate; //Animation counter	
	double vVert;
	double vHor; 
	int y_max; //When to stop ascending/descending
	int y_min; //POsition for exit sequence
	boolean exit; //Set to true when exit
	
	static boolean loaded = false;
	static Image frame1 = null;
	static Image frame2 = null;
	static Image frame3 = null;
	static Image frame4 = null;
	static Image frame5 = null;
	static Image frame6 = null;
	static Image rframe1 = null;
	static Image rframe2 = null;
	static Image rframe3 = null;
	static Image rframe4 = null;
	static Image rframe5 = null;
	static Image rframe6 = null;
	
	Plant(Model m_ref, Bird b_ref){
		this.mod_ref = m_ref;
		this.bird_ref = b_ref;
		this.x = bird_ref.x - 100;
		this.tAnimate = 0;
		this.vHor = 0.0;
		this.exit = false;
		this.z = 4; //Render top layer
		this.y = 500;
		this.vVert = -16;
		this.y_max = 200; 
		this.y_min = 300;

		//Load sprites
		if (loaded == false){
			loaded = true;
			this.loadImages();
		}
		
	}
	
	Plant(Plant that, Model new_mod){
		//Most variables aren't needed by AI 
		this.mod_ref = new_mod;
		this.bird_ref = null;
		this.x = that.x;
		this.tAnimate = that.tAnimate;
		this.vHor = that.vHor; 
		this.exit = that.exit;
		this.z = that.z;
		this.y = that.y;
		this.vVert = that.vVert;
		this.y_max = that.y_max;
		this.y_min = that.y_min;
	}
	
	void update(){
		this.y += this.vVert;
		this.x += this.vHor;
		if (this.y < this.y_max){
			this.vVert = 8.0;
			this.exit = true;
		}else if (this.exit == true && this.y > this.y_min){
			this.vVert = 0.0;
			this.vHor = 8.0;
		}
		//When plantSprite exits screen, call Game Over
		if (this.x > 500){
			bird_ref.killGame();
		}
	}
	void draw(Graphics g_ref){
		this.tAnimate++;
		this.currentSprite = this.detectSprite();
		g_ref.drawImage(this.currentSprite, this.x, this.y, null);
	}
	void leftClick(){}
	void rightClick(){}
	void collision(){}
	
	void loadImages(){
		frame1 = this.loadImage("resources/plant1.png");
		frame2 = this.loadImage("resources/plant2.png");
		frame3 = this.loadImage("resources/plant3.png");
		frame4 = this.loadImage("resources/plant4.png");
		frame5 = this.loadImage("resources/plant5.png");
		frame6 = this.loadImage("resources/plant6.png");
	}
	
	Image loadImage(String file){
		Image img = null;
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return img;
	}
	
	Image detectSprite(){
		int t = this.tAnimate;
		Image img = null;
		//if upwards facing
		if (t <= 20){
			img = frame1;
		}else if (t <= 23){
			img = frame2;
		}else if (t <= 26){
			img = frame3;
		}else if (t <= 29){
			img = frame4;
		}else if (t <= 32){
			img = frame5;
		}else{
			img = frame6;
		}							
		return img;
	}
	
	Sprite replicate(Model new_mod){
		return new Plant(this, new_mod);
	}
}
