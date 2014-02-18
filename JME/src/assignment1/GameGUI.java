/**
 * Display the frame, and the user can click the button.
 * Three are main operations: Player, Set, Help and Enter.
 * <ol>
 * <li>Player: choose the number of players and play with friends or AI.</li>
 * <li>Set: set the sound, turn on or turn off.</li>
 * <li>Help: give some hints about how to play.</li>
 * <li>Enter: enter the game and play now.</li>
 * </ol>
 * 
 * @author <a href="mailto:Y.Li38@student.liverpool.ac.uk">Li Yiming</a>
 * @version 1.0
 * 
 */

package assignment1;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class GameGUI {

	/* ------------------ Fields ------------------------ */

	private static JFrame frame;

	private static JDialog player;

	private static JDialog set;

	private static JDialog help;

	private final static Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

	private static int initialSound = 1;

	private static int choose = 1;

	/* ------------------ Methods ------------------------ */

	/**
	 * Display the frameGUI, playerGUI, setGUI and helpGUI.
	 * 
	 * @param args
	 *            it comes from the CMD or terminal
	 */
	public static void main(String args[]) {
		frameGUI();
		playerGUI();
		setGUI();
		helpGUI();
	}

	/**
	 * Display the main frame.
	 */
	private static void frameGUI() {
		// create components and set the attributes for them
		frame = new JFrame("Carom Billiards Game");

		final JButton buttonPlayer = new JButton("Player");
		buttonPlayer.setToolTipText("Choose the number of players.");
		buttonPlayer.setPreferredSize(new Dimension(100, 42));

		final JButton buttonSet = new JButton("Set");
		buttonSet.setToolTipText("Set for the game.");
		buttonSet.setPreferredSize(new Dimension(100, 42));

		final JButton buttonHelp = new JButton("Help");
		buttonHelp.setToolTipText("Help contents.");
		buttonHelp.setPreferredSize(new Dimension(100, 42));

		final JButton buttonEnter = new JButton("Enter");
		buttonEnter.setToolTipText("Play now.");
		buttonEnter.setPreferredSize(new Dimension(100, 42));

		ImageIcon icon = new ImageIcon("bin/assignment1/data/texture/icon.jpg");
		JLabel label = new JLabel("Play Carom Billiards, Enjoy Yourself.",
				icon, JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);

		final JButton buttonExit = new JButton("Exit");
		buttonExit.setPreferredSize(new Dimension(70, 30));

		JPanel jpanel1 = new JPanel();
		JPanel jpanel2 = new JPanel();
		JPanel jpanel3 = new JPanel();
		JPanel jpanel = new JPanel();

		// layout and listener of frame
		jpanel1.setLayout(new GridLayout(4, 1));
		jpanel1.add(buttonPlayer);
		jpanel1.add(buttonSet);
		jpanel1.add(buttonHelp);
		jpanel1.add(buttonEnter);
		// set the border of jpanel
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "");
		jpanel1.setBorder(border);

		jpanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
		jpanel2.add(label);
		jpanel2.add(jpanel1);
		border = BorderFactory.createTitledBorder(etched, "");
		jpanel2.setBorder(border);

		jpanel3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpanel3.add(buttonExit);

		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
		jpanel.add(jpanel2);
		jpanel.add(jpanel3);

		ActionListener buttonListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton buttonS = (JButton) e.getSource();
				if (buttonS == buttonPlayer) {
					player.setVisible(true);
				} else if (buttonS == buttonSet) {
					set.setVisible(true);
				} else if (buttonS == buttonHelp) {
					help.setVisible(true);
				} else if (buttonS == buttonEnter) {
					// start the game
					BilliardsGame app = new BilliardsGame(initialSound, choose);
					// we can not set
					// "app.setConfigShowMode(ConfigShowMode.AlwaysShow);",
					// because this dialog has another thread,
					// it will affect deadlock with my GUI.
					// In future, if we want to set, we should know more
					// information about JME GUI
					app.start();
				} else {
					System.exit(0);
				}
			}
		};

		buttonPlayer.addActionListener(buttonListen);
		buttonSet.addActionListener(buttonListen);
		buttonHelp.addActionListener(buttonListen);
		buttonEnter.addActionListener(buttonListen);
		buttonExit.addActionListener(buttonListen);

		frame.setContentPane(jpanel);
		// the exit application default window close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set in the middle of screen
		int x = (screenSize.width - 360) / 2;
		int y = (screenSize.height - 300) / 2;
		frame.setLocation(x, y);
		// set the size of frame and make it fixed
		frame.setSize(360, 300);
		frame.setResizable(false);
		frame.setVisible(true);

	}

	/**
	 * Display the player dialog.
	 */
	private static void playerGUI() {
		// create components and set the attributes for them
		player = new JDialog(frame, "Choose the number of players");
		final JButton buttonConfirmPlayer = new JButton("Confirm");
		buttonConfirmPlayer.setToolTipText("Confirm and close.");

		JLabel labelPlayer = new JLabel("Please choose the players:");

		final JRadioButton radioa = new JRadioButton("Two players", true);
		final JRadioButton radiob = new JRadioButton(
				"Player VS Virtual Player1", false);
		final JRadioButton radioc = new JRadioButton(
				"Player VS Virtual Player2", false);

		ButtonGroup groupPlayer = new ButtonGroup();
		groupPlayer.add(radioa);
		groupPlayer.add(radiob);
		groupPlayer.add(radioc);

		JPanel jpanel1 = new JPanel();
		JPanel jpanel2 = new JPanel();
		JPanel jpanel3 = new JPanel();
		JPanel jpanel = new JPanel();

		// layout and listener of player dialog
		jpanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jpanel1.add(radioa);
		jpanel1.add(radiob);
		jpanel1.add(radioc);
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "");
		jpanel1.setBorder(border);

		jpanel2.setLayout(new GridLayout(2, 1));
		jpanel2.add(labelPlayer);
		jpanel2.add(jpanel1);
		jpanel2.setBorder(border);

		jpanel3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpanel3.add(buttonConfirmPlayer);

		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
		jpanel.add(jpanel2);
		jpanel.add(jpanel3);

		ActionListener radioListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButton radioS = (JRadioButton) e.getSource();
				if (radioS == radioa) {
					choose = 1;
				} else if (radioS == radiob) {
					choose = 2;
				} else {
					choose = 3;
				}
			}
		};

		radioa.addActionListener(radioListen);
		radiob.addActionListener(radioListen);
		radioc.addActionListener(radioListen);

		ActionListener buttonListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton buttonS = (JButton) e.getSource();
				if (buttonS == buttonConfirmPlayer) {
					player.setVisible(false);
				}
			}
		};

		buttonConfirmPlayer.addActionListener(buttonListen);

		// set it in the middle of screen
		player.setContentPane(jpanel);
		int x = (screenSize.width - 500) / 2;
		int y = (screenSize.height - 185) / 2;
		player.setLocation(x, y);
		// set the size and make it fixed
		player.setResizable(false);
		player.setSize(500, 185);
	}

	/**
	 * Display the set dialog.
	 */
	private static void setGUI() {
		// create components and set the attributes for them
		set = new JDialog(frame, "Set sound");
		final JButton buttonConfirmSet = new JButton("Confirm");
		buttonConfirmSet.setToolTipText("Confirm and close.");

		JLabel labelPlayer = new JLabel("Please set sound:");

		final JRadioButton radioOn = new JRadioButton("Turn on", true);
		final JRadioButton radioOff = new JRadioButton("Turn off", false);

		ButtonGroup groupSetSound = new ButtonGroup();
		groupSetSound.add(radioOn);
		groupSetSound.add(radioOff);

		JPanel jpanel1 = new JPanel();
		JPanel jpanel2 = new JPanel();
		JPanel jpanel3 = new JPanel();
		JPanel jpanel = new JPanel();

		// layout and listener of set dialog
		jpanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jpanel1.add(radioOn);
		jpanel1.add(radioOff);
		Border etched = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etched, "");
		jpanel1.setBorder(border);

		jpanel2.setLayout(new GridLayout(2, 1));
		jpanel2.add(labelPlayer);
		jpanel2.add(jpanel1);
		jpanel2.setBorder(border);

		jpanel3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jpanel3.add(buttonConfirmSet);

		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
		jpanel.add(jpanel2);
		jpanel.add(jpanel3);

		ActionListener radioListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButton radioS = (JRadioButton) e.getSource();
				if (radioS == radioOn) {
					initialSound = 1;
				} else {
					initialSound = 0;
				}
			}
		};

		radioOn.addActionListener(radioListen);
		radioOff.addActionListener(radioListen);

		ActionListener buttonListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton buttonS = (JButton) e.getSource();
				if (buttonS == buttonConfirmSet) {
					set.setVisible(false);
				}
			}
		};

		buttonConfirmSet.addActionListener(buttonListen);

		// set it in the middle of screen
		set.setContentPane(jpanel);
		int x = (screenSize.width - 240) / 2;
		int y = (screenSize.height - 185) / 2;
		set.setLocation(x, y);
		// set the size and make it fixed
		set.setResizable(false);
		set.setSize(240, 185);
	}

	/**
	 * Display the help dialog.
	 */
	private static void helpGUI() {
		// create components and set the attributes for them
		help = new JDialog(frame, "Help", true);
		final JButton buttonConfirmHelp = new JButton("Confirm");
		buttonConfirmHelp.setToolTipText("Confirm and close.");

		JLabel label1 = new JLabel(
				"*********************Help*********************");
		JLabel label2 = new JLabel("  [1/2/left-click]:    change direction");
		JLabel label22 = new JLabel("  [9/0/right-click]:  change force");
		JLabel label3 = new JLabel("  [Space]:  kick ball");
		JLabel label4 = new JLabel("  [F5]:    change camera");
		JLabel label5 = new JLabel("  [F6]:    sound on/off");

		JPanel jpanel1 = new JPanel();
		JPanel jpanel2 = new JPanel();
		JPanel jpanel22 = new JPanel();
		JPanel jpanel3 = new JPanel();
		JPanel jpanel4 = new JPanel();
		JPanel jpanel5 = new JPanel();
		JPanel jpanel6 = new JPanel();
		JPanel jpanel = new JPanel();

		// layout and listener of help dialog
		jpanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jpanel1.add(label1);

		jpanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpanel2.add(label2);

		jpanel22.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpanel22.add(label22);

		jpanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpanel3.add(label3);

		jpanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpanel4.add(label4);

		jpanel5.setLayout(new FlowLayout(FlowLayout.LEFT));
		jpanel5.add(label5);

		jpanel6.setLayout(new FlowLayout(FlowLayout.CENTER));
		jpanel6.add(buttonConfirmHelp);

		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
		jpanel.add(jpanel1);
		jpanel.add(jpanel2);
		jpanel.add(jpanel22);
		jpanel.add(jpanel3);
		jpanel.add(jpanel4);
		jpanel.add(jpanel5);
		jpanel.add(jpanel6);

		ActionListener buttonListen = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton buttonS = (JButton) e.getSource();
				if (buttonS == buttonConfirmHelp) {
					help.setVisible(false);
				}
			}
		};

		buttonConfirmHelp.addActionListener(buttonListen);

		help.setContentPane(jpanel);
		// set in the middle of screen
		int x = (screenSize.width - 270) / 2;
		int y = (screenSize.height - 250) / 2;
		help.setLocation(x, y);
		// set the size and make it fixed
		help.setResizable(false);
		help.setSize(270, 250);
	}
}
