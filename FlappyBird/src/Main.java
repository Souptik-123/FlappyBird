import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
public class Main {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		int width=360;
		int height=640;
		
		JFrame frame=new JFrame("Flappy Bird");
		frame.setVisible(true);
		frame.setSize(width,height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GamePanel gp=new GamePanel();
		frame.add(gp);
		frame.pack();
		gp.requestFocus();
		frame.setVisible(true);
	}

}
