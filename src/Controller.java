import java.awt.event.MouseListener; 
import java.awt.event.MouseEvent;


class Controller implements MouseListener
{
	
	Model model;
	int frame;
	
	Controller(Model m) {
		this.model = m;
		this.frame = 0;
	}

	public void mousePressed(MouseEvent e) {
		this.model.onClick(e.getButton());
	}

	public void update(){
		int max = 0;
		this.frame++;
		//System.out.print("FRAME: " + frame + "   -   ");
		int temp;
		ControlAction best = ControlAction.DO_NOTHING; //Default to DO_NOTHING
		Model copy = new Model(this.model);
		for (ControlAction action : ControlAction.values()){
			temp = copy.evaluateAction(action, copy, 0);
			System.out.print(action);
			System.out.print("=" + temp + " - ");
			if ( temp > max ){
				max = temp; //Value of current best action
				best = action;//Current best action
			}else if ( temp == max ){
				if ( best.ordinal() >= action.ordinal()){
					best = action;
				}
			}
		}
		System.out.println("Chose action " + best);
		if (best == ControlAction.FLAP){
			this.model.onClick(1);
		}else if (best == ControlAction.THROW){
			this.model.onClick(3);
		}else if (best == ControlAction.FLAP_THROW){
			this.model.onClick(3);
			this.model.onClick(1);
		}
	}
	
    public void mouseReleased(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e) {    }


}

