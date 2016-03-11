import javax.swing.JFrame; 
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class Game extends JFrame implements ActionListener {
	Model model;
	Controller controller;

	
	public Game() throws Exception {
		this.model = new Model();
		controller = new Controller(this.model);
		View view = new View(this.model);
		view.addMouseListener(controller);
		this.setTitle("FloppyBirb");
		this.setSize(500, 500);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		new Timer(12, this).start(); // Indirectly calls actionPerformed at regular intervals
	}

	public void actionPerformed(ActionEvent evt) {
		if (this.model.start == true){
			this.controller.update();
			this.model.update();
			this.repaint(); // Indirectly calls View.paintComponent
		}
	}

	public static void main(String[] args) throws Exception {
		new Game();
	}
	
	public static void observeViewParams(){
		
	}
}
