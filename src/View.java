import javax.swing.JLabel;
import javax.swing.JPanel; 
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Font;

public class View extends JPanel {
	
	Model model;
	Image background;
	Image startMenu;
	Image endMenu;
	
	JTextField enterK;
	JLabel promptK; 
	JTextField enterD;
	JLabel promptD; 
	JTextField enterS;
	JLabel promptS; 
	
	int defaultD = 21;
	int defaultK = 7;
	long defaultS = 678678;

	View(Model m) throws IOException {
		
		//Set font styling
		int fontSize = 16;
        Font f = new Font(this.getFont().getFontName(), Font.BOLD, fontSize);
        
		this.model = m;		
		this.background = ImageIO.read(new File("resources/background.png"));
		this.startMenu = ImageIO.read(new File("resources/start.png"));
		this.endMenu= ImageIO.read(new File("resources/end.png"));
		
		//Menu for adding k
		this.enterK = new JTextField(10);
		this.enterK.setText("7");
		this.promptK = new JLabel("Enter k (recursion thinning): ");
		this.promptK.setFont(f);
		this.promptK.setBackground(Color.white);
		this.promptK.setOpaque(true);
		
		//Component for setting K
		this.enterD = new JTextField(10);
		this.enterD.setText("21");
		this.promptD = new JLabel("Enter d (recursion depth): ");
		this.promptD.setFont(f);
		this.promptD.setBackground(Color.white);
		this.promptD.setOpaque(true);
		
		//Components for setting seed
		this.enterS = new JTextField(10);
		this.enterS.setText("678678");
		this.promptS = new JLabel("Enter random number seed: ");
		this.promptS.setFont(f);
		this.promptS.setBackground(Color.white);
		this.promptS.setOpaque(true);
		
		//Incorporate components
		this.add(promptK);
		this.add(enterK);
		this.add(promptD);
		this.add(enterD);
		this.add(promptS);
		this.add(enterS);
	}

	public void paintComponent(Graphics g) {
		int fontSize = 16;
        Font f = new Font(g.getFont().getFontName(), Font.BOLD, fontSize);
        g.setFont(f);
		if (this.model.start == true){
			this.setComponents(false);
			if (this.model.params == false){
				this.sendParams();
				this.model.params = true;
			}
			g.drawImage(this.background, -10,-10,null); //Draw background
			for (int i = 0; i <this.model.sprites.size(); i++){
				this.model.sprites.get(i).draw(g); 
			}
			g.drawString("Score:", 380, 460);
			g.drawString(Integer.toString(this.model.points), 440, 460);
		}else if (this.model.start == false && this.model.end == false){
			g.drawImage(this.startMenu, -10, -10, null);
		}else if (this.model.end == true){
			this.setComponents(true);
			g.drawImage(this.endMenu, -10,-10,null);
			g.drawString("Your final score:", 170, 370);
			g.drawString(Integer.toString(this.model.points), 320, 370);
			try {
				this.model.reset();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setComponents(boolean set){
		this.promptK.setVisible(set);
		this.enterK.setVisible(set);
		this.promptD.setVisible(set);
		this.enterD.setVisible(set);
		this.promptS.setVisible(set);
		this.enterS.setVisible(set);
	}
	
	public int getK(){
		String text = this.enterK.getText();
		int k = Integer.parseInt(text);
		if (k > 0){
			return k; 
		}else{
			return this.defaultK;
		} 
	}
	
	public long getS(){
		String text = this.enterS.getText();
		long s = Long.parseLong(text);
		if (s > 0){
			return s; 
		}else{
			return this.defaultS;
		} 
	}
	
	public int getD(){
		String text = this.enterD.getText();
		int d = Integer.parseInt(text);
		if (d > 0){
			return d; 
		}else{
			return this.defaultD;
		}
	}
	
	public void sendParams(){
		this.model.maxD = this.getD();
		this.model.thin = this.getK();
		this.model.setRandom(this.getS());
	}
}






