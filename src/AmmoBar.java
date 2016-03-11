import java.awt.Image;  
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class AmmoBar extends Sprite{
	
	static Image eggSprite = null; 
	Bird bird_ref;
	int num;
	
	AmmoBar(Bird b_ref) throws IOException{
		this.bird_ref = b_ref;	
		this.num = b_ref.ammo;
		this.x = 10;
		this.y = 430;
		this.z = 4;
		if (eggSprite == null){
			try {
				eggSprite = ImageIO.read(new File("resources/egg.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}
	
	AmmoBar(AmmoBar that, Bird new_bird){
		this.bird_ref = new_bird;
		this.num = that.num;
		this.x = that.x;
		this.y = that.y; 
		this.z = that.z;
	}
	
	void update(){
		this.num = bird_ref.ammo;
	}
	void draw(Graphics g_ref){
		int tempX = this.x;
		for (int i = 0; i < this.num; i++){
			g_ref.drawImage(eggSprite, tempX, this.y, null);
			tempX += 50;
		}
	}
	void leftClick(){}
	void rightClick(){}
	void collision(){}

	Sprite replicate(Model new_mod){
		return new AmmoBar(this, new_mod.bird_ref);
	}
}
