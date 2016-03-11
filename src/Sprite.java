import java.awt.Graphics;

public abstract class Sprite implements Comparable<Sprite>{

	int x; //X position
	int y; //Y position
	int z; //Z position -> rendering order
	int height; //Dimensions
	int width;
	int dat; //All sprite subclasses will have a dat member which can be used for storing extra info (like orientation, etc)
	Model mod_ref; //All Sprites should be instantiated with a reference to the containing Model
	
	abstract void update();
	abstract void draw(Graphics g_ref); 
	abstract void leftClick();
	abstract void rightClick();
	abstract void collision();
	abstract Sprite replicate(Model new_mod); //Clone method, requires reference to new model
	
	//Custom comparator, compare Sprites by z-value (rendering order)
	public int compareTo(Sprite other){
		int otherZ = other.z;
		if (this.z > otherZ){
			return 1; 
		}else if (this.z == otherZ){
			return 0;
		}else{
			return -1;
		}
	}
}
