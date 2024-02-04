
package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class display extends JFrame implements ActionListener {
	private int labelOrder = 7;
	private int sidePot = 0; // keeps track of value of side pot if there is one
	private ImageIcon image1; // place-holders for images
	private ImageIcon image2;
	private JButton call = new JButton("Call / Check");
	private JButton raise = new JButton("Raise");
	private JButton fold = new JButton("Fold");
	private JButton restartButton = new JButton("Play Again?"); // restarts for a new game
	private JButton allInButton = new JButton("All In"); // puts the player all in
	private JButton raise5 = new JButton("5");
	private JButton raise10 = new JButton("10");
	private JButton raise25 = new JButton("25");
	private JButton raise100 = new JButton("100");
	private JButton[] raiseButtons = { raise5, raise10, raise25, raise100, allInButton, raise };
	private JTextField raiseInput = new JTextField("Custom Amount", 50); // used to input how
																			// much a player
																			// wants to raise
	private JLabel previousCard1 = new JLabel();
	private JLabel previousCard2 = new JLabel();
	private JLabel callAmountDisplay = new JLabel();
	private JLabel potAmountDisplay = new JLabel();
	private JLabel currentRoundDisplay = new JLabel();
	private JLabel winnerText = new JLabel();
	private JLabel background = new JLabel();
	private JLabel turnIndicator = new JLabel();
	private JLabel smallBlind = new JLabel();
	private JLabel bigBlind = new JLabel();
	private JLabel[] displays = { callAmountDisplay, potAmountDisplay, currentRoundDisplay };
	private ArrayList<Component> displayedCards = new ArrayList<Component>(); // list of every card displayed
	private ArrayList<String> displayedCardNames = new ArrayList<String>(); // list of every name in same order as
																			// displayed cards
	private ArrayList<String> winningCards = new ArrayList<String>();// holds the names of winning hand
	private ArrayList<JLabel> playerIndicators = new ArrayList<JLabel>();
	private ArrayList<JLabel> playerNames = new ArrayList<JLabel>();
	private ArrayList<JLabel> playerCashes = new ArrayList<JLabel>();

	public void createJpanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		setLayout(null);
		setVisible(true);
		setTitle("Poker Table");
		int xpos = 3;
		int ypos = 200;
		for (JButton x : raiseButtons) {
			x.setBounds(xpos, ypos, 64, 50);
			x.setFont(new Font("Serif", Font.PLAIN, 20));
			x.addActionListener(this);
			add(x);
			ypos += 85;
		}
		raise.setBounds(5, 700, 80, 50);
		allInButton.setBounds(5, 550, 80, 50);
		raiseInput.setBounds(5, 640, 96, 50);
		add(raiseInput);
		call.setBounds(360, 600, 180, 60);
		fold.setBounds(560, 600, 140, 60);
		restartButton.setBounds(820, 28, 120, 30);
		call.setFont(new Font("Serif", Font.PLAIN, 30));
		fold.setFont(new Font("Serif", Font.PLAIN, 40));
		winnerText.setFont(new Font("Serif", Font.PLAIN, 40));
		winnerText.setBounds(150, 10, 800, 60);
		call.addActionListener(this);
		fold.addActionListener(this);
		restartButton.addActionListener(this);
		for (JLabel x : displays) {
			x.setForeground(Color.red);
			x.setFont(new Font("Serif", Font.PLAIN, 24));
			add(x);
		}
		currentRoundDisplay.setFont(new Font("Serif", Font.PLAIN, 40));
		currentRoundDisplay.setBounds(280, 20, 300, 40);
		callAmountDisplay.setFont(new Font("Serif", Font.PLAIN, 40));
		callAmountDisplay.setBounds(480, 20, 320, 40);
		potAmountDisplay.setBounds(420, 230, 300, 40);
		add(call);
		add(fold);
		add(winnerText);
		winnerText.setVisible(false);
		add(restartButton);
		restartButton.setVisible(false);
		background.setBounds(50, 50, 900, 700);
		scaleImage(background, "PokerTable");
		backgroundSetUp();
	}

	public void backgroundSetUp() {
		for (int i = 0; i < Poker.playersInGame.size(); i++) { // displays the circles around
			Player player = Poker.playersInGame.get(i);
			JLabel playerIndicator = new JLabel();
			playerIndicator.setBounds(player.getxpos(), player.getypos(), 100, 100);
			scaleImage(playerIndicator, "player");
			playerIndicators.add(playerIndicator);
		}
		for (int i = 0; i < Poker.playersInGame.size(); i++) { // displays randomized player names
			Player player = Poker.playersInGame.get(i);
			JLabel playerName = new JLabel();
			int namebuffer = 18;
			String name = Poker.playersInGame.get(i).getName();
			if (name.length() < 5) {
				namebuffer += 8;
			}
			playerName.setText(name);
			playerName.setFont(new Font("Serif", Font.PLAIN, 24));
			playerName.setForeground(Color.red);
			playerName.setBounds(player.getxpos() + namebuffer, player.getypos() - 5, 100, 100);
			add(playerName);
			playerNames.add(playerName);
		}
	}

	private void fold() {
		int playerTurn = Poker.currentTurn;
		remove(playerIndicators.get(playerTurn));
		remove(playerNames.get(playerTurn));
		remove(playerCashes.get(playerTurn));
		playerIndicators.remove(playerTurn);
		playerNames.remove(playerTurn);
		playerCashes.remove(playerTurn);
	}

	public void scaleImage(JLabel x, String y) { // used to display a png
		ImageIcon image1 = createImg("/res/" + y + ".png");
		Image scaledImage1 = image1.getImage();
		ImageIcon finalImage = new ImageIcon(
				scaledImage1.getScaledInstance(x.getWidth(), x.getHeight(), Image.SCALE_FAST));
		x.setIcon(finalImage);
		add(x);

	}

	public void displayNewGame() {
		winningCards.clear();
		displayCard("red_back", 220, 300, 2);
		call.setVisible(true);
		fold.setVisible(true);
		for (JButton x : raiseButtons) {
			x.setVisible(true);
		}
		raiseInput.setVisible(true);
		for (JLabel x : displays) {
			x.setVisible(true);
		}
		for (JLabel x : playerCashes) { // these 3 removing for loops make it so I remove the old indicators and labels
										// from past rounds
			remove(x);
		}
		for (JLabel x : playerIndicators) {
			remove(x);
		}
		for (JLabel x : playerNames) {
			remove(x);
		}
		sidePot = 0;
		potAmountDisplay.setBounds(420, 230, 300, 40);
		playerNames.clear(); // clears all lists and resets them (resets after every game)
		playerIndicators.clear();
		playerCashes.clear();
		backgroundSetUp(); // resets the lists
		for (int i = 0; i < Poker.playersInGame.size(); i++) { // displays players cash
			Player player = Poker.playersInGame.get(i);
			JLabel playerCash = new JLabel();
			int cash = Poker.playersInGame.get(i).getCurrentCash();
			playerCash.setText("$" + cash);
			playerCash.setFont(new Font("Serif", Font.PLAIN, 18));
			playerCash.setForeground(Color.red);
			playerCash.setBounds(player.getxpos() + 32, player.getypos() + 55, 100, 30);
			add(playerCash);
			playerCashes.add(i, playerCash);
		}
		remove(bigBlind);
		remove(smallBlind);
		Player player = Poker.playersInGame.get(Poker.BigBlindPos); // displays the initial blind chips
		int x = 70; // depending on what side of the table the player is the chip can be to the left
					// or right
		if (Poker.BigBlindPos > 2) {
			x = -20;
		}
		bigBlind.setBounds(player.getxpos() + x, player.getypos() + 80, 50, 40);
		int smallBlindPos = Poker.BigBlindPos - 1;
		if (Poker.BigBlindPos == 0) {
			smallBlindPos = Poker.currentPlayers.size() - 1;
		}
		if (smallBlindPos > 2) {
			x = -20;
		}
		player = Poker.currentPlayers.get(smallBlindPos);
		smallBlind.setBounds(player.getxpos() + x, player.getypos() + 80, 50, 40);
		turnIndicator.setBounds(170, 150, 100, 100);
		scaleImage(turnIndicator, "turnIndicator");
		scaleImage(bigBlind, "BigBlind");
		scaleImage(smallBlind, "SmallBlind");
		resetDisplayValues();
	}

	public void displayGameEnd(Player winner, ArrayList<Player> tieList, Player sidePotWinner) {
		call.setVisible(false);
		fold.setVisible(false);
		raiseInput.setVisible(false);
		for (JLabel x : displays) {
			x.setVisible(false);
		}
		for (JButton x : raiseButtons) {
			x.setVisible(false);
		}
		if (winner == null) {
			tieDisplay(tieList);
		} else {
			String text = "";
			if (sidePotWinner != null) {
				text = ". side Pot won by " + sidePotWinner.getName();
			}
			potAmountDisplay.setVisible(true);
			String Hand = determineHand(winner.getHandValue()); // finds name of hand value
			switchHands(winner.getCardString(0), winner.getCardString(1)); // displays the winners hand cards
			winnerText.setText(winner.getName() + " Wins that Round with " + Hand + text);
			winnerText.setForeground(Color.red);
			winnerText.setVisible(true);
			restartButton.setVisible(true);
			remove(previousCard1);
			remove(previousCard2);
			endGameCardSetUp(winner, tieList);
			remove(background);
			add(background);
		}
	}

	private void endGameCardSetUp(Player winner, ArrayList<Player> tieList) {
		int xpos = 150;
		double scale = 2.4 - (0.2 * Poker.playersInGame.size());
		for (Player x : Poker.playersInGame) {
			JLabel name = new JLabel();
			xpos += 10;
			int i = 0;
			name.setText(x.getName());
			name.setFont(new Font("Serif", Font.PLAIN, 20));
			name.setForeground(Color.red);
			name.setBounds(xpos, 425, 100, 100);
			add(name);
			playerNames.add(name);
			while (i < 2) {
				displayCard(x.getCardString(i), xpos, 500, scale);
				xpos += (45 * scale);
				i++;
			}
		}
		for (int i = 0; i < winner.getHandCardsize(); i++) { // takes the winners cards and transfers to the
																// winningCards list
			winningCards.add(winner.getHandCardat(i).toString());
		}
		for (int i = 0; i < winningCards.size(); i++) { // finds the winners cards and highlights them
			String card = winningCards.get(i);
			highlightCard(card, scale);
		}
		turnIndicator.setBounds(winner.getxpos(), winner.getypos(), 100, 100);
	}

	private void highlightCard(String card, double scale) { // used to highlight a given card that is displayed
		int sub = 6;
		int listpos = displayedCardNames.indexOf(card);
		if (listpos != -1) {
			int x = displayedCards.get(listpos).getX();
			int y = displayedCards.get(listpos).getY();
			if (y < 400) {
				scale = 1.9;
				sub = 9;
			} // makes scale bigger for the community cards
			JLabel highlight = new JLabel();
			highlight.setBounds(x - sub, y - sub, (int) (55 * scale), (int) (90 * scale));
			scaleImage(highlight, "highlight");
			add(highlight);
			playerNames.add(highlight); // added to list that will be deleted after the round (no real reason its the
										// player names)
		} else {
			System.out.println("Card not found: " + card);
		}
	}

	private void tieDisplay(ArrayList<Player> tieList) {
		String text = null;
		int playerCardDistance = 0; // distance between players displayed cards
		int cardWidth = 120; // width of one card
		String hand = determineHand(tieList.get(0).getHandValue()); // creates a string value for the hand the players
																	// have
		switchHands(tieList.get(0).getCardString(0), tieList.get(0).getCardString(1)); // displays first persons cards
		for (Player x : tieList) { // finds name of hand value
			displayCard(x.getCardString(0), 320 + playerCardDistance, 550, 3);
			displayCard(x.getCardString(1), 320 + cardWidth + playerCardDistance, 550, 3);
			cardWidth = +120;
			playerCardDistance = +40;
			text = text + x.getName();
			if (tieList.indexOf(x) == tieList.size() - 2) {
				text = text + " and ";
			} // adds and before second to last player in the list
			else if (tieList.indexOf(x) != tieList.size() - 1) {
				text = text + ", ";
			}
		}
		text = text + " with " + hand;
		winnerText.setText(text);
		winnerText.setVisible(true);
		restartButton.setVisible(true);
	}

	public String determineHand(int handValue) { // returns the string value for the winners hand
		if (handValue == 1) {
			return "High Card";
		}
		if (handValue == 2) {
			return "a Pair";
		}
		if (handValue == 3) {
			return "a Two Pair";
		}
		if (handValue == 4) {
			return "a 3 of a kind";
		}
		if (handValue == 5) {
			return "a Straight";
		}
		if (handValue == 6) {
			return "a Flush";
		}
		if (handValue == 7) {
			return "a Full House";
		}
		if (handValue == 8) {
			return "a 4 of a Kind";
		}
		if (handValue == 9) {
			return "a Straight Flush";
		}
		if (handValue == 10) {
			return "a Royal Flush";
		}
		return "";
	}

	private ImageIcon createImg(String path) {
		ImageIcon temp = new ImageIcon(getClass().getResource(path));
		return temp;
	}

	public void resetDisplayValues() { // runs every time a new player goes, resets values on the displays
		for (JButton x : raiseButtons) {
			x.setVisible(true);
		}
		callAmountDisplay.setText("Call Amount: $" + String.valueOf(Poker.callAmount));
		potAmountDisplay.setText("Pot Amount: $" + String.valueOf(Poker.pot - sidePot)); // subs side pot if there is
																							// one
		currentRoundDisplay.setText("Round: " + String.valueOf(Poker.round));
		int lastPlayer; // resets the cash display for the current and last player
		int minCash = 100000;
		if (Poker.Allin == true) { // if a person is all in a raise cant happen
			for (JButton x : raiseButtons) {
				x.setVisible(false);
			}
			raiseInput.setVisible(false);
		} else { // this section removes or adds raise buttons if a player can make that action
			for (Player x : Poker.playersInGame) {// finds the player with the least cash for all in, so I player doesnt
													// get
													// out played
				if (x.getCurrentCash() != 0) {// if a player has no money dont account them
					int currentCash = x.getCurrentCash() + x.getPlayerPot();
					if (currentCash < minCash) {
						minCash = currentCash;
					}
				}
			}
			int cash = minCash; // the amount of money a person could raise
			// Cant raise more than the poorest player has so people dont get outbet
			int inPot = Poker.playersInGame.get(Poker.currentTurn).getPlayerPot();
			if (cash < 100 || Poker.callAmount + inPot >= 100) {
				raise100.setVisible(false);
			} // if the player cant afford the raise or the raise is higher then 100 it cant
				// raise by 100 and etc.
			if (cash < 25 || Poker.callAmount + inPot >= 25) {
				raise25.setVisible(false);
			}
			if (cash < 10 || Poker.callAmount + inPot >= 10) {
				raise10.setVisible(false);
			}
			if (cash < 5 || Poker.callAmount + inPot >= 5) {
				raise5.setVisible(false);
			}
			if (Poker.callAmount >= Poker.playersInGame.get(Poker.currentTurn).getCurrentCash()) {
				allInButton.setVisible(false);
			}
		}
		remove(background); // keeps background in the back of the space even when new labels are added
		add(background);
	}

	public void resetCashDisplay() { // resets the cash of a player when they make a move
		playerCashes.get(Poker.currentTurn)
				.setText("$" + String.valueOf(Poker.playersInGame.get(Poker.currentTurn).getCurrentCash()));
	}

	public void switchHands(String card1, String card2) { // runs every time a new player goes, switches the old players
		// // hand cards with the new players hand card
		JLabel cardLabel1 = new JLabel();
		JLabel cardLabel2 = new JLabel();
		cardLabel1.setBounds(110, 550, 120, 210);
		cardLabel2.setBounds(230, 550, 120, 210);
		scaleImage(cardLabel1, card1);
		scaleImage(cardLabel2, card2);
		remove(previousCard1);
		remove(previousCard2);
		add(cardLabel1);
		add(cardLabel2);
		labelOrder = labelOrder + 2;
		previousCard1 = cardLabel1;
		previousCard2 = cardLabel2;
		Player player = Poker.playersInGame.get(Poker.currentTurn);
		turnIndicator.setBounds(player.getxpos(), player.getypos(), 100, 100);
		repaint(); // refreshes image for new cards
	}

	public void displayCard(String card, int x, int y, double scale) { // used whenever a new card is displayed
		JLabel label1 = new JLabel();
		int xscale = (int) (45 * scale); // default (1) is smallest, (2) is on table cards, (3) is current player cards
		int yscale = (int) (78 * scale);
		label1.setBounds(x, y, xscale, yscale);
		scaleImage(label1, card);
		labelOrder++;
		displayedCards.add(label1);
		displayedCardNames.add(card);
		repaint();
	}

	public void removePlayerFromDisplay(int player) {
		remove(playerIndicators.get(player));
		remove(playerNames.get(player));
		remove(playerCashes.get(player));
	}

	public void actionPerformed(ActionEvent e) { // if a button is pressed will run the corresponding method in poker
		if (e.getSource() == fold) {
			fold();
			Poker.fold();
			Poker.AfterChoice();
		} else if (e.getSource() == call) {
			Poker.call();
			Poker.AfterChoice();
		} else if (e.getSource() == raise) { // used to confirm custom raise
			int raiseAmount = Integer.parseInt(raiseInput.getText());
			if (raiseAmount > Poker.playersInGame.get(Poker.currentTurn).getCurrentCash()
					|| raiseAmount <= Poker.callAmount) {
				if (raiseAmount < Poker.callAmount) {
					raiseInput.setText("Less than call");
				} else if (raiseAmount == Poker.callAmount) {
					raiseInput.setText("so you call");
				} else {
					raiseInput.setText("> current cash");
				}
			} else {
				Poker.raise(raiseAmount);
				Poker.AfterChoice();
			}
		} else if (e.getSource() == allInButton) {
			Poker.allIn();
			Poker.AfterChoice();
		} else if (e.getSource() == restartButton) {
			restartButton.setVisible(false);
			winnerText.setVisible(false);
			for (Component x : displayedCards) {
				x.setVisible(false);
			}
			displayedCards.clear();
			displayedCardNames.clear();
			Poker.runGameAgain();
		} else if (e.getSource() == raise5) {
			Poker.raise(5);
			Poker.AfterChoice();
		} else if (e.getSource() == raise10) {
			Poker.raise(10);
			Poker.AfterChoice();
		} else if (e.getSource() == raise25) {
			Poker.raise(25);
			Poker.AfterChoice();
		} else if (e.getSource() == raise100) {
			Poker.raise(100);
			Poker.AfterChoice();
		}
	}

	public void finishProgram() { // most of this just displays all the stuff for the last rich lonely player
		add(winnerText);
		Player player = Poker.playersInGame.get(0);
		int pos = Poker.currentPlayers.indexOf(player);
		for (JLabel x : playerNames) { // removes all names but the winner
			if (playerNames.get(pos) != x) {
				remove(x);
			}
		}
		playerCashes.get(pos).setText("$" + String.valueOf(Poker.currentPlayers.get(pos).getCurrentCash()));
		repaint();
		remove(bigBlind);
		remove(smallBlind);
		winnerText.setVisible(true);
		winnerText.setText("Everyone left.  " + player.getName() + " is rich and alone");
	}

	public void sidePot() {
		JLabel sidePotDisplay = new JLabel();
		sidePotDisplay.setBounds(300, 230, 300, 40);
		add(sidePotDisplay);
		playerIndicators.add(sidePotDisplay); // added to a lists so it can be removed at end of game
		sidePotDisplay.setFont(new Font("Serif", Font.PLAIN, 24));
		sidePotDisplay.setText("Side Pot: $" + String.valueOf(Poker.pot));
		sidePotDisplay.setForeground(Color.red);
		potAmountDisplay.setBounds(530, 230, 300, 40);
		sidePot = Poker.pot;
	}
}
