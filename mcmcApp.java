import javax.swing.*;



public class mcmcApp {
	public static void main(String[] args){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						JFrame appFrame = new mainFrame("MCMC Application: Probability Calculator");
						appFrame.setVisible(true);
						appFrame.setSize(800, 600);
						appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				}
		);
		
		
		
	}
	
}
