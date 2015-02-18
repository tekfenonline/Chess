package OYMACI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Frame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Chess game = new Chess();
	private final JPanel panel = new JPanel();
	private final JButton btnNew = new JButton("New");
	private final JButton btnExit = new JButton("Exit");
	private final JButton btnSave = new JButton("Save");
	private final JButton btnLoad = new JButton("Load");
	private JButton[][] stones = new JButton[8][8];
	private ImageIcon bp = new ImageIcon("bp.png");
	private ImageIcon bb = new ImageIcon("bb.png");
	private ImageIcon bh = new ImageIcon("bh.png");
	private ImageIcon br = new ImageIcon("br.png");
	private ImageIcon bq = new ImageIcon("bq.png");
	private ImageIcon bk = new ImageIcon("bk.png");
	private ImageIcon wp = new ImageIcon("wp.png");
	private ImageIcon wb = new ImageIcon("wb.png");
	private ImageIcon wh = new ImageIcon("wh.png");
	private ImageIcon wr = new ImageIcon("wr.png");
	private ImageIcon wq = new ImageIcon("wq.png");
	private ImageIcon wk = new ImageIcon("wk.png");
	private ImageIcon black = new ImageIcon("black.png");
	private ImageIcon white = new ImageIcon("white.png");
	private boolean firstClick = true;
	private int sourceX = 0;
	private int sourceY = 0;
	private String color = "White";
	private final JLabel lblTurnblack = new JLabel("Turn:Black");
	private final JLabel lblPoweredByHalil = new JLabel("powered by Halil Oymac\u0131");
	private final JTextArea txtrError = new JTextArea();
	private int count = 0;
	private JLabel lblTimer = new JLabel("00 : 00 : 00");
	@SuppressWarnings("unused")
	private Timer timer;

	Frame()
	{
		super("CHESS");
		initGUI();
	}

	private void initGUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(686, 655);
		setResizable(false);
		// frame create center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		setVisible(true);
		getContentPane().setLayout(null);
		panel.setBackground(SystemColor.scrollbar);
		panel.setBounds(83, 59, 539, 533);
		panel.setLayout(new GridLayout(8, 8));

		lblTurnblack.setText("Turn: " + color + " - Step: " + count);

		// add timer
		Timer timer = new Timer(1000, new ActionListener()
		{
			private int second = 0;
			private int minute = 0;
			private int hour = 0;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String stopwatch = String.format("%02d : %02d : %02d", hour, minute, second);
				lblTimer.setText(stopwatch);

				if (second > 0 && second % 59 == 0)
				{
					minute++;
				}
				if (minute > 1 && minute % 59 == 0)
				{
					hour++;
					minute = 0;
				}
				if (hour > 23)
				{
					hour = 0;
				}
				if (second > 59)
				{
					second = 0;
				}
				second++;
			}
		});
		timer.start();
		// add buttons
		for (int i = 0; i < stones.length; i++)
		{
			for (int j = 0; j < stones.length; j++)
			{
				if (i == 1)
					stones[i][j] = new JButton(bp);
				else if (i == 6)
					stones[i][j] = new JButton(wp);
				else
					stones[i][j] = new JButton();
				if ((i + j) % 2 == 0)
					stones[i][j].setBackground(Color.orange);
				else
					stones[i][j].setBackground(Color.magenta);
				stones[i][j].addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						int sx = 0, sy = 0, ax = 0, ay = 0;
						loop: for (int j2 = 0; j2 < stones.length; j2++)
						{
							for (int j3 = 0; j3 < stones.length; j3++)
							{
								if (arg0.getSource() == stones[j2][j3])
								{
									if (firstClick)
									{
										sx = j2;
										sy = j3;
									} else
									{
										ax = j2;
										ay = j3;
									}
									break loop;
								}
							}
						}
						if (!(clicks(sx, sy, ax, ay)))
							return;
					}
				});
			}
		}
		setIcons();

		for (int i = 0; i < stones.length; i++)
		{
			for (int j = 0; j < stones.length; j++)
			{
				panel.add(stones[i][j]);
			}
		}

		getContentPane().add(panel);
		btnNew.setBounds(83, 11, 89, 23);

		btnNew.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				game = new Chess();
				firstClick = true;
				txtrError.setText(null);
				count = 0;
				color = "White";
				lblTurnblack.setText("Turn: " + color + " - Step: " + count);
				setIcons();
				defaultButtonsBackground();
			}
		});
		getContentPane().add(btnNew);
		btnExit.setBounds(533, 11, 89, 23);

		btnExit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(1);

			}
		});
		getContentPane().add(btnExit);
		btnSave.setBounds(220, 11, 89, 23);

		getContentPane().add(btnSave);
		btnLoad.setBounds(374, 11, 89, 23);

		getContentPane().add(btnLoad);
		lblTurnblack.setBounds(83, 592, 189, 14);
		lblTurnblack.setIcon(white);

		getContentPane().add(lblTurnblack);
		lblPoweredByHalil.setBounds(463, 592, 159, 23);

		getContentPane().add(lblPoweredByHalil);
		txtrError.setBackground(UIManager.getColor("Button.background"));
		txtrError.setBounds(10, 90, 59, 171);
		txtrError.setEditable(false);
		getContentPane().add(txtrError);

		lblTimer.setBounds(543, 45, 79, 14);
		getContentPane().add(lblTimer);
	}

	void setIcons()
	{
		for (int i = 0; i < stones.length; i++)
		{
			for (int j = 0; j < stones.length; j++)
			{
				if (game.getBoard(i, j) == 0)
					stones[i][j].setIcon(null);
				else if (game.getBoard(i, j) == 1)
					stones[i][j].setIcon(bp);
				else if (game.getBoard(i, j) == -1)
					stones[i][j].setIcon(wp);
				else if (game.getBoard(i, j) == 2)
					stones[i][j].setIcon(bb);
				else if (game.getBoard(i, j) == -2)
					stones[i][j].setIcon(wb);
				else if (game.getBoard(i, j) == 3)
					stones[i][j].setIcon(bh);
				else if (game.getBoard(i, j) == -3)
					stones[i][j].setIcon(wh);
				else if (game.getBoard(i, j) == 4)
					stones[i][j].setIcon(br);
				else if (game.getBoard(i, j) == -4)
					stones[i][j].setIcon(wr);
				else if (game.getBoard(i, j) == 5)
					stones[i][j].setIcon(bq);
				else if (game.getBoard(i, j) == -5)
					stones[i][j].setIcon(wq);
				else if (game.getBoard(i, j) == 6)
					stones[i][j].setIcon(bk);
				else if (game.getBoard(i, j) == -6)
					stones[i][j].setIcon(wk);
				else
				{
					System.out.println("error!");
					System.exit(1);
				}
			}
		}
	}

	boolean clicks(int sx, int sy, int ax, int ay)
	{
		if (firstClick)
		{
			if ((color.contentEquals("Black") && (game.getBoard(sx, sy) > 0))
					|| (color.contentEquals("White") && (game.getBoard(sx, sy) < 0)))
			{
				int temp[][] = new int[32][2];
				sourceX = sx;
				sourceY = sy;
				stones[sx][sy].setBackground(Color.green);
				temp = game.findCanMoveField(color, sx, sy);
				for (int i = 0; temp[i][0] != -99; i++)
				{
					stones[temp[i][0]][temp[i][1]].setBackground(Color.blue);
				}
			} else
			{
				return false;
			}
		} else
		// second click
		{
			if (ax == sourceX && ay == sourceY)
			{
				firstClick = !firstClick;
				if ((sourceX + sourceY) % 2 == 0)
					stones[sourceX][sourceY].setBackground(Color.orange);
				else
					stones[sourceX][sourceY].setBackground(Color.magenta);
				txtrError.setText(null);
				defaultButtonsBackground();
				return false;
			}

			// castling
			if ((color.contentEquals("White") && stones[sourceX][sourceY].getIcon() == wk
					&& stones[7][7].getIcon() == wr && sourceX == 7 && sourceY == 4 && ax == 7 && ay == 6
					&& stones[7][5].getIcon() == null && stones[7][6].getIcon() == null)
					|| (color.contentEquals("Black") && stones[sourceX][sourceY].getIcon() == bk
							&& stones[0][7].getIcon() == br && sourceX == 0 && sourceY == 4 && ax == 0 && ay == 6
							&& stones[0][5].getIcon() == null && stones[0][6].getIcon() == null))
			{
				game.castling(color, "Small");
				changeTurn();
			}

			else if ((color.contentEquals("White") && sourceX == 7 && sourceY == 4 && ax == 7 && ay == 2
					&& stones[7][4].getIcon() == wk && stones[7][3].getIcon() == null && stones[7][2].getIcon() == null
					&& stones[7][1].getIcon() == null && stones[7][0].getIcon() == wr)
					|| (color.contentEquals("Black") && sourceX == 0 && sourceY == 4 && ax == 0 && ay == 2
							&& stones[0][4].getIcon() == bk && stones[0][3].getIcon() == null
							&& stones[0][2].getIcon() == null && stones[0][1].getIcon() == null && stones[0][0]
							.getIcon() == br))
			{
				game.castling(color, "Big");
				changeTurn();
			} else
			{
				game.backupBoard();
				String error = game.play(color, sourceX, sourceY, ax, ay);
				if (error == null)
				{
					if ((game.checkKing("Black") && (color.contentEquals("White")))
							|| (game.checkKing("White") && (color.contentEquals("Black"))))
					{
						txtrError.setText("Check King!\nCannot move!");
						game.restoreBoard();
						return false;
					}
					defaultButtonsBackground();
					++count;
					if (game.checkKing(color))
					{
						txtrError.setText("Check King!");
						loop: for (int i = 0; i < stones.length; i++)
						{
							for (int j = 0; j < stones.length; j++)
							{
								if ((color.contentEquals("Black") && stones[i][j].getIcon() == wk)
										|| (color.contentEquals("White") && stones[i][j].getIcon() == bk))
								{
									stones[i][j].setBackground(Color.red);
									if (game.checkMate(color, i, j))
										JOptionPane.showMessageDialog(null, "CHECKMATE!");
									game.restoreBoard();
									break loop;
								}
							}
						}
					}
					changeTurn();
				} else
				{
					if (color.contentEquals("White"))
						game.reverseBoard();
					txtrError.setText(error);
					firstClick = !firstClick;
				}
			}
		}
		firstClick = !firstClick;
		lblTurnblack.setText("Turn: " + color + " - Step: " + count);
		setIcons();
		return true;
	}

	void defaultButtonsBackground()
	{
		for (int i = 0; i < stones.length; i++)
		{
			for (int j = 0; j < stones.length; j++)
			{
				if ((i + j) % 2 == 0)
					stones[i][j].setBackground(Color.orange);
				else
					stones[i][j].setBackground(Color.magenta);
			}
		}
	}
	
	void changeTurn()
	{
		if (color.contentEquals("Black"))
		{
			color = "White";
			lblTurnblack.setIcon(white);
			lblTurnblack.setBounds(83, 592, 189, 14);
		} else
		{
			color = "Black";
			lblTurnblack.setIcon(black);
			lblTurnblack.setBounds(83, 45, 189, 14);
		}
	}
}
