package Game;

import java.util.ArrayList;

public class Player {
	private int currentCash; // cash the player has at the point
	private int startingCash; // cash that the player started with
	private String name;
	private Card[] cards = new Card[2]; // the two pocket cards the player has
	private ArrayList<Card> handCards = new ArrayList<Card>(); // cards that make up best hand
	private int inPlayerPot; // the amount of money a player put in during a round
	private int handValue; // the type of card combo a player has at the end of the game
	private int kicker; // value of a card that is used to compare in case of similar card combos (In a
						// pair it is the other hand card in everything else it is the highest valued
						// card in the combo)
	private int otherPair; // if a player has a two pair this is the value of the other pair
	private int xpos;
	private int ypos;
	private boolean Sidepot; // if player can only win side pot = true

	public Player(int currentCash, int startingCash, String name, Card[] cards, int inPlayerPot, int handValue,
			int kicker, int otherPair, int xpos, int ypos, ArrayList<Card> handCards, boolean Sidepot) {
		this.currentCash = currentCash;
		this.startingCash = startingCash;
		this.name = name;
		this.handValue = handValue;
		this.inPlayerPot = inPlayerPot;
		this.kicker = kicker;
		this.otherPair = otherPair;
		this.xpos = xpos;
		this.ypos = ypos;
		this.handCards = handCards;
		this.Sidepot = Sidepot;
		for (int i = 0; i < 2; i++) {
			this.cards[i] = cards[i];
		}
	}

	public void sidePot(Boolean x) {
		if (x == true) {
			this.Sidepot = true;
		} else {
			this.Sidepot = false;
		}
	}

	public boolean getSidePot() {
		return this.Sidepot;
	}

	public int getHandValue() {
		return this.handValue;
	}

	public void resetHandValue() {
		this.handValue = 0;
	}

	public boolean setHandValue(int handTypeValue, int largeCardValue) { // if false is returned then a higher hand
																			// value was not found
		if (this.handValue < handTypeValue) {
			this.handValue = handTypeValue;
			this.kicker = largeCardValue;
			this.handCards.clear(); // resets handcards
			return true;
		} // bigger number always replaces
		return false;
	}

	public String getName() {
		return this.name;
	}

	public int getCurrentCash() {
		return this.currentCash;
	}

	public void subCurrentCash(int sub) {
		this.currentCash = this.currentCash - sub;
	}

	public void addCurrentCash(int add) {
		this.currentCash = this.currentCash + add;
	}

	public int getStartingCash() {
		return this.startingCash;
	}

	public int getPlayerPot() {
		return this.inPlayerPot;
	}

	public void addInPlayerPot(int x) {
		this.inPlayerPot = this.inPlayerPot + x;
	}

	public void zeroPlayerPot() {
		this.inPlayerPot = 0;
	}

	public void giveCards(Card[] x) {
		this.cards = x;
	}

	public String getCardString(int x) {
		return this.cards[x].toString();
	}

	public Card getCard(int x) {
		return this.cards[x];
	}

	public int getkicker() {
		return this.kicker;
	}

	public void setkicker(int x) {
		this.kicker = x;
	}

	public int getOtherPair() {
		return this.otherPair;
	}

	public void setOtherPair(int x) {
		this.otherPair = x;
	}

	public int getHighHandCardValue() {// finds highest value card in hand and returns its value
		int highCard = 0;
		for (Card x : this.cards) {
			if (x.getValue() > highCard) {
				highCard = x.getValue();
			}
		}
		return highCard;
	}

	public String toString() {
		return name + " : $" + currentCash + "/" + startingCash + " " + this.cards[0].toString() + ", "
				+ this.cards[1].toString() + ", Other pair: " + this.otherPair + ", kicker: " + this.kicker;

	}

	public int getxpos() {
		return this.xpos;
	}

	public int getypos() {
		return this.ypos;
	}

	public Card getHandCardat(int x) {
		return this.handCards.get(x);
	}

	public void addHandCard(Card x) {
		this.handCards.add(x);
	}

	public int getHandCardsize() {
		return this.handCards.size();
	}

	public void printHandCards() {
		for (Card x : this.handCards) {
			System.out.print(this.name + " ");
			System.out.print(x.toString() + " ");
			System.out.println("");
		}
	}
}
