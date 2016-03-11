import java.util.Collections;   
import java.util.LinkedList;
import java.io.IOException;
import java.util.ListIterator;

class Model
{
    View view_ref;
    
	LinkedList<Sprite> sprites; //ArrayList of sprite objects
    Bird bird_ref;
    Random rand; 
    int points; //Number of obstacles cleared
    int maxD;//Maxmimum depth of decision tree
    int thin; //Thinning of decision tree
    int seed; //To seed RNG
    boolean end; //End model
    boolean start; //Start model
    boolean kill; //Kill sequence
    boolean quiet;
    boolean params;
    ListIterator<Sprite> it;
    
// How far to set back first tree

    
    //Default constructor, initialize member objects
	Model() {
		try {
			reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}

	Model(Model that){
		this.seed = that.seed;
		this.rand = new Random(that.rand);
		this.points = that.points;
		this.start = true;
		this.end = that.end;
		this.maxD = that.maxD;
		this.thin = that.thin;
		this.kill = that.kill;
		this.sprites = new LinkedList<Sprite>();
		this.it = this.sprites.listIterator();
		ListIterator<Sprite> temp = that.sprites.listIterator();
		while (temp.hasNext()){
			Sprite spr = temp.next();
			//Skip Sprites which are not needed for simulation
			if (spr instanceof AmmoBar || spr instanceof Plant){
				continue;
			}
			this.it.add(spr.replicate(this));
		}
		//this.it = this.sprites.listIterator();
		//Capture reference to bird
		for (int i=0; i < this.sprites.size(); i++){
			if (this.sprites.get(i) instanceof Bird){
				this.bird_ref = (Bird)this.sprites.get(i);
			}
		}
		//Make sure we have a bird
		if (bird_ref == null || !(bird_ref instanceof Bird)){
			throw new IllegalArgumentException("Bird reference broken in Model copy constructor!");
		}
	}
	
	
	public void update() {	
		if (this.end == false){			
			Collections.sort(sprites);
		    this.it = this.sprites.listIterator(); //Reset iterator
			while (this.it.hasNext()){
				Sprite current = this.it.next(); //Set current to next value in list
				current.update(); //Update all sprites
				//System.out.println("Number of elements = " + this.sprites.size());
			}
		}else{
			this.start = false;
			//if (this.quiet == false)
				//System.out.println("Game Over...");
		}
	}
	
	
	public void onClick(int type){
		if (this.start == false){
			this.start = true;
		}else{
			if (type == 1){
				for (int i = 0; i <this.sprites.size(); i++){
					this.sprites.get(i).leftClick(); 
				}
			}else if (type == 3){
				this.bird_ref.rightClick(); 
			}
		}
	}
	
	
	//Heuristic search of paths for called action
	//depth=current depth; d=depth limit; k=how often to call recursion
	int evaluateAction(ControlAction a, Model local, int depth){
		int next = depth+1;
		int d = this.maxD;
		int k = this.thin;		
		
		//If max recursion depth
		if (depth == d ){
			//If bird is dead
			if (local.bird_ref.dead == true){
				return 0;
				//If bird is alive
			}else{
				//This works
				int ret = (500 - (Math.abs(local.bird_ref.y - 250)));
				return ret;
				//System.out.println(ret);
			}
		//If not reach maximum recursion depth
		}else{
			Model copy = new Model(local);
			//If not, update the model
			//Switch/case seems to work
			switch (a){
			case DO_NOTHING:
				break;
			case FLAP:
				copy.onClick(1);//left
				break;
			case THROW:
				copy.onClick(3);//right
				break;
			case FLAP_THROW:
				copy.onClick(1);
				copy.onClick(3);
				break;
			}
			copy.update();
			//System.out.println(a);
			if (depth % k == 0){
				int best = 0;
				int temp = 0;
				//System.out.println("   Check paths   ");
				for (ControlAction b : ControlAction.values()){
					temp = evaluateAction(b, copy, next);	
					//System.out.print(b);
					//System.out.print("="+temp+" - ");
					if ( temp > best ){
						best = temp; //Value of current best action
					}
				}
				//System.out.println("Returning " + best);
				return best;
			}else{
				int ret = evaluateAction(ControlAction.DO_NOTHING, copy, next);
				return ret;

			}
		}
	}
	
	public void reset() throws IOException{
		
		this.seed = 678678;
		this.rand = new Random(this.seed);
		this.points = 0;
		this.maxD = 21;
		this.thin = 7;
		this.start = false;
		this.end = false;
		this.kill = false;
		this.quiet = false;
		this.params = false;
		this.sprites = new LinkedList<Sprite>();
		this.sprites.add( new Bird(this));
		this.sprites.add( new Tree(this));
		//Capture reference to bird
		this.it = this.sprites.listIterator(); //Reset iterator
		//while ( this.it.hasNext()){
		for (int i=0; i < this.sprites.size(); i++){
			if (this.sprites.get(i) instanceof Bird){
				this.bird_ref = (Bird)this.sprites.get(i);
			}
		}
		//Make sure we have a bird
		if (bird_ref == null || !(bird_ref instanceof Bird)){
			throw new IllegalArgumentException("Bird reference broken in Model.reset()!");
		}
	}
	
	public void setView(View ref){
		this.view_ref = ref;
	}
	
	public void setRandom(Long s){
		this.rand = new Random(s);
	}
}
