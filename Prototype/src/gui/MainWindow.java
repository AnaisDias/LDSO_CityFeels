package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener{

	private JPanel contentPane;
	JTextPane instructionsPanel;
	File audioFile;
	String[] instructions = {"Siga em frente.", "Vire a esquerda.",
			"Páre! Está em frente a uma passadeira. Quando ouvir o apito pode avancar.",
			"Vire a direita.", "Chegou à FEUP."};
	String[] audioFiles = {"audio/hello.wav", "audio/hello.wav",
			"audio/hello.wav", "audio/hello.wav", "audio/hello.wav"};
	int step = -1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		instructionsPanel = new JTextPane();
		instructionsPanel.setText("bla bla bla teste pra ver se isto esta bem \r\nagora tem um paragrafo\r\ne  isto \u00E9 outro paragrafo\r\nso quero ver qtas linhas isto da pra ter\r\nsera que tem mais\r\nso mais uma ");
		instructionsPanel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		instructionsPanel.setEditable(false);
		instructionsPanel.setToolTipText("");
		instructionsPanel.setBounds(10, 300, 360, 140);
		contentPane.add(instructionsPanel);

		JButton novaRota = new JButton("Nova rota");
		novaRota.setFont(new Font("SansSerif", Font.PLAIN, 18));
		novaRota.setBounds(10, 13, 110, 40);
		contentPane.add(novaRota);

		JPanel imagePanel = new JPanel();
		imagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		imagePanel.setBounds(10, 66, 360, 221);
		contentPane.add(imagePanel);

		JLabel label = new JLabel();
		label.setIcon(new ImageIcon("images/siaexp.png"));
		imagePanel.add(label);

		JButton newRFID = new JButton("Novo RFID");
		newRFID.setFont(new Font("SansSerif", Font.PLAIN, 18));
		newRFID.setBounds(245, 13, 125, 40);
		newRFID.setActionCommand("newRFID");
		contentPane.add(newRFID);

		JButton repeatSound = new JButton("Replay");
		repeatSound.setFont(new Font("SansSerif", Font.PLAIN, 18));
		repeatSound.setBounds(132, 13, 97, 40);
		repeatSound.setActionCommand("Replay");
		contentPane.add(repeatSound);

		newRFID.addActionListener(this);
		repeatSound.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) { //resolver questao de falar tudo ao mesmo tempo
		if("newRFID".equals(e.getActionCommand()))
		{
			if(step < instructions.length-1)
			{
				step++;
				instructionsPanel.setText(instructions[step]);

				try {
					audioFile = new File(audioFiles[step]);
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch(Exception ex) {
					System.out.println("Error with playing sound.");
					ex.printStackTrace();
				}
			}
		}
		if("Replay".equals(e.getActionCommand()))
		{
			if(step != -1)
			{
				try {
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch(Exception ex) {
					System.out.println("Error with playing sound.");
					ex.printStackTrace();
				}
			}
		}
	}
}
