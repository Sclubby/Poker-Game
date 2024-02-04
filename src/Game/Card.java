package Game;

public class Card {
	private String suit;
	private int num;

	public Card(String s, int n) {
		this.suit = s;
		this.num = n;
	}

	public int getValue() {
		return this.num;
	}

	public String getSuit() {
		return this.suit;
	}

	public String toString() {
		return this.num + this.suit;
	}
}