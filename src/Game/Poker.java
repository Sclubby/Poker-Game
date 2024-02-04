package Game;

import java.util.ArrayList;

public class Poker {
	public static String[] symbols = { "D", "H", "S", "C" };
	public static Card[] communityCards = new Card[5];
	public static ArrayList<Card> fullDeck = new ArrayList<Card>(); // complete 52 cards
	public static ArrayList<Card> playingDeck = new ArrayList<Card>(); // deck used during game
	public static ArrayList<Player> currentPlayers = new ArrayList<Player>(); // all players at table
	public static ArrayList<Player> playersInGame = new ArrayList<Player>(); // players not folded removes anyone that
	public static ArrayList<Player> tieList = new ArrayList<Player>(); // keeps track of players that have the same
																		// combination at the end of the game
	public static int BigBlindPos = -1; // player position of big blind
	public static int sidePot = 0; // keeps track of amount of money in the side pot
	public static Player BigBlindPerson = null; // person who is at the big blind position
	public static int pot = 0; // Total amount in the pot
	public static int numOfPlayers = 6;
	public static int bigBlind = 5;
	public static int smallBlind = 2;
	public static int round = 1; // round in play
	public static int currentTurn = 0; // the player who's turn it is
	public static int totalTurn = 0; // total Amount of turns in the round
	public static int callAmount = 0; // amount a player needs to put in to call
	public static int totalCall = bigBlind; // total amount of money need for a person to go on to the next round
	public static display gui = new display(); // the display used throughout the program (only one)
	public static boolean gameEnd = false; // when false game will continue to loop
	public static boolean Allin = false; // if a player at the table goes all in = true

	public static void main(String[] args) {
		BuildDeck(); // creates deck
		GameCreate(); // creates player
		gui.createJpanel(); // creates window with components
		runGameAgain();
	}

	static void runGameAgain() {
		resetGameVar(); // resets any game variables that need to be
		if (playersInGame.size() != 1) { // if there is only one player that can play poker is over
			SetBlinds(); // sets blinds and other table values
			dealCards(); // gives players random cards from deck
			pickCommunityCards(); // decides the community cards for the game
			PrintPlayers(); // Prints players in console
			gui.displayNewGame();
			playerChoice(); // starts first players turn
		} else {
			gui.finishProgram();
		}
	}

	private static void resetGameVar() {
		for (Card x : fullDeck) {
			playingDeck.add(x);
		} // resets playing deck
		playersInGame.clear();
		for (Player x : currentPlayers) {
			playersInGame.add(x);
			x.resetHandValue();
			x.setOtherPair(0);
			x.sidePot(false);
		} // resets players in game list
		for (int i = 0; i < currentPlayers.size(); i++) {
			if (currentPlayers.get(i).getCurrentCash() <= 0) {
				playersInGame.remove(currentPlayers.get(i));
				gui.removePlayerFromDisplay(i);
			}
		} // removes any player with no money
		BigBlindPos++;
		if (BigBlindPos >= playersInGame.size()) {
			BigBlindPos = 0;
		} // resets big blind for next round
		pot = 0;
		round = 1;
		currentTurn = BigBlindPos + 1;
		if (currentTurn >= playersInGame.size()) {
			currentTurn = 0;
		}
		totalTurn = 0;
		totalCall = bigBlind;
		tieList.clear();
	}

	public static void displayHand() {
		String card1 = playersInGame.get(currentTurn).getCardString(0);
		String card2 = playersInGame.get(currentTurn).getCardString(1);
		gui.switchHands(card1, card2);
	}

	public static void pickCommunityCards() {
		for (int i = 0; i < 5; i++) {
			communityCards[i] = PickRandCard();
		}
	}

	public static void revealCards(boolean revealAllCards) {
		int numOfCards = 1;
		if (revealAllCards == true) { // reveals all cards at once
			for (int i = 0; i < 5; i++) {
				gui.displayCard(communityCards[i].toString(), (90 * numOfCards) + 220, 300, 2);
				numOfCards++;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				gui.displayCard(communityCards[i].toString(), (90 * numOfCards) + 220, 300, 2);
				numOfCards++;
			} // flop
			if (round == 3) {
				gui.displayCard(communityCards[3].toString(), 580, 300, 2);
			} // turn
			if (round == 4) {
				gui.displayCard(communityCards[4].toString(), 670, 300, 2);
			} // river
		}
	}

	public static void dealCards() {
		for (Player y : currentPlayers) {
			Card[] cardsInHand = new Card[2];
			for (int x = 0; x < 2; x++) {
				cardsInHand[x] = PickRandCard();
			}
			y.giveCards(cardsInHand);
		}
	}

	public static void BuildDeck() {
		int cardValue = 2, suitNum = 0;
		for (int i = 0; i < 52; i++) {
			fullDeck.add(new Card(symbols[suitNum], cardValue));
			if (cardValue == 14) {
				cardValue = 2;
				suitNum++;
			} else {
				cardValue++;
			}
		}
	}

	public static Card PickRandCard() {
		int randCard = (int) (Math.random() * playingDeck.size() - 1);
		Card card = playingDeck.get(randCard);
		playingDeck.remove(randCard);
		return card;
	}

	public static void printCommunityCards() {
		for (Card x : communityCards) {
			System.out.println(x.toString());
		}
	}

	public static void printDeck() {
		for (Card x : playingDeck) {
			System.out.println(x.toString());
		}
	}

	public static void PrintPlayers() {
		for (Player x : playersInGame) {
			System.out.println(x.toString());
		}
		System.out.println("Num of players: " + currentPlayers.size());
	}

	public static void GameCreate() {
		int xpos[] = { 100, 170, 353, 536, 720, 780 };
		int ypos[] = { 300, 150, 120, 120, 150, 300 };
		// int cash = 100; // starting amount of cash for each player
		int cash[] = { 100, 200, 50, 20, 50, 100 }; // used for testing
		Card blank = new Card(null, 0); // place holder cards
		String[] playerNames = { "Steven", "Joe", "Carl", "Jimbo", "Cathy", "Shaun", "Tracie", "Sam", "Nelson", "Stacy",
				"John", "Quin", "James", "Paxton", "Chuck", "Will", "Ruben", "Anna", "Jackie", "Alex", "Max", "Jack",
				"Kym", "Kurt", "Abby" }; // every name that a player can randomly get
		Card[] cardsInHand = new Card[2];
		int nameListSize = playerNames.length;
		for (int i = 0; i < numOfPlayers; i++) {
			ArrayList<Card> HandCards = new ArrayList<Card>();
			String name = null;
			boolean loop = true;
			while (loop == true) { // finds a random name in the list that is not already taken
				int randName = (int) (Math.random() * nameListSize);
				if (playerNames[randName] == null) {
				} else {
					name = playerNames[randName];
					playerNames[randName] = null;
					loop = false;
				}
			}
			nameListSize--;
			for (int x = 0; x < 2; x++) {
				cardsInHand[x] = blank;
			}
			Player newPlayer = new Player(cash[i], cash[i], name, cardsInHand, 0, 0, 0, 0, xpos[i], ypos[i], HandCards,
					false);
			currentPlayers.add(newPlayer);
			playersInGame.add(newPlayer);
		}
		BigBlindPos = currentPlayers.size() - 1;
	}

	public static void SetBlinds() {
		round = 1;
		BigBlindPerson = playersInGame.get(BigBlindPos);
		int smallBlindPos = BigBlindPos - 1;
		if (BigBlindPos == 0) {
			smallBlindPos = currentPlayers.size() - 1;
		}
		callAmount = bigBlind;
		putInChips(BigBlindPos, bigBlind);
		putInChips(smallBlindPos, smallBlind);

		System.out.println("Big Blind: " + playersInGame.get(BigBlindPos).getName());
		System.out.println("Small Blind: " + playersInGame.get(smallBlindPos).getName());
		System.out.println();
	}

	public static void playerChoice() {
		boolean loop = true;
		while (loop == true) { // skips any player with no money
			if (currentTurn >= playersInGame.size()) { // loops turn order
				currentTurn = 0;
			}
			if (playersInGame.get(currentTurn).getCurrentCash() == 0) {
				currentTurn++;
			} else {
				loop = false;
			}
		}
		callAmount = totalCall - playersInGame.get(currentTurn).getPlayerPot(); // finds how much a single player needs
		System.out.println(" current Turn = " + currentTurn); // to put in
		displayHand();
		gui.resetDisplayValues();
		// input from player needed here
	}

	public static void AfterChoice() { // After players move checks for next round, move on, or end game
		gui.resetCashDisplay();
		totalTurn++;
		int playersWithCall = 0;
		int brokePlayers = 0;
		for (Player x : playersInGame) {// checks the whole table to see how many people have the call amount in
			if (x.getPlayerPot() == totalCall) {
				playersWithCall++;
			}
			if (x.getCurrentCash() == 0 && x.getPlayerPot() == 0) { // players that arent in round are subtracted
				brokePlayers++;
			}
		}
		int playersStillPlaying = playersInGame.size() - brokePlayers;
		if (callAmount == 0) {
			playersWithCall = playersStillPlaying;
		}
		System.out.println("P " + playersStillPlaying);
		System.out.println("pc " + playersWithCall);
		if (playersWithCall == playersStillPlaying && totalTurn >= playersStillPlaying || playersInGame.size() == 1) {
			// if all players have put in the same amount of money not including ones that
			// dont have rights to the main pot next round
			totalCall = 0;
			BigBlindPos = currentPlayers.indexOf(BigBlindPerson);
			currentTurn = BigBlindPos + 1; // resets turn to first player
			if (currentTurn >= currentPlayers.size()) {
				currentTurn = 0;
			}
			boolean found = false;
			while (found == false) { // finds the next person after the bigblind for the beginning of the round
				if (playersInGame.indexOf(currentPlayers.get(currentTurn)) == -1) {
					currentTurn++;
					if (currentTurn >= currentPlayers.size()) {
						currentTurn = 0;
					}
				} else {
					found = true;

					currentTurn = playersInGame.indexOf(currentPlayers.get(currentTurn));
				}
			}
			callAmount = 0;
			round++;
			totalTurn = 0;
			for (Player y : playersInGame) {
				y.zeroPlayerPot();
			} // zeros all players personal pots for next round
			if (Allin == true && brokePlayers != playersInGame.size()) { // displays a side pot if one or more
																			// players are all in
				gui.sidePot();
				Allin = false;
			}
			revealCards(false); // reveals next community card(s)
			System.out.println(round);
			System.out.println(brokePlayers);
			if (round > 4 || playersInGame.size() == 1 || brokePlayers == currentPlayers.size()) { // if this is true
																									// game ends
				if (round < 4) {
					revealCards(true);
				}
				gui.resetDisplayValues();
				determineWinner();
			} else {
				playerChoice();
			}
		} else { // runs if not a new round or end of game
			currentTurn++;
			playerChoice();
		}
	}

	public static void fold() {
		totalTurn--; // does count a fold as a turn so the logic remains intact
		String foldingPlayer = playersInGame.get(currentTurn).getName();
		for (int i = 0; i < playersInGame.size(); i++) {
			if (foldingPlayer.equals(playersInGame.get(i).getName())) {
				playersInGame.remove(i);
				currentTurn--; // counteracts removal of player so a player isn't skipped
			}
		} // removes player that folded from in game list
	}

	public static void call() {
		putInChips(currentTurn, callAmount);
		if (playersInGame.get(currentTurn).getCurrentCash() == 0) {
			Allin = true;
		} // if a player has zero cash after their turn they are all in
	}

	public static void raise(int raiseAmount) {
		totalCall = raiseAmount;
		callAmount = raiseAmount - playersInGame.get(currentTurn).getPlayerPot();
		putInChips(currentTurn, callAmount);
		if (playersInGame.get(currentTurn).getCurrentCash() == 0) {
			Allin = true;
		} // if a player has zero cash after their turn they are all in
	}

	public static void allIn() {
		int AllinCash = playersInGame.get(currentTurn).getCurrentCash();
		playersInGame.get(currentTurn).sidePot(true);
		int minCash = 100000;
		for (Player x : playersInGame) {// the all in amount is = to the player still in with the lowest cash
			if (x.getCurrentCash() != 0) { // does not account for players with no money but are still in
				int currentCash = x.getCurrentCash() + x.getPlayerPot(); // includes money on hand and in pot
				if (currentCash < AllinCash && currentCash < minCash) {
					minCash = currentCash;
				}
			}
		}
		if (minCash != 100000) {
			AllinCash = minCash;
		}
		totalCall += AllinCash - callAmount; // sets the totalcall to the correct
												// amount
		putInChips(currentTurn, AllinCash);
		Allin = true;
		// AllInCash is the lowest amount a player still in the game has on hand so no
		// one gets outbet
	}

	public static void putInChips(int playerPos, int chipsIn) {
		playersInGame.get(playerPos).subCurrentCash(chipsIn);
		playersInGame.get(playerPos).addInPlayerPot(chipsIn);
		pot += chipsIn;
	}

	public static void determineWinner() {
		Player sidePotWinner = null;
		// If 1 player is left he wins
		if (playersInGame.size() > 1) {
			for (int index = 0; index < playersInGame.size(); index++) { // combines players hand with community
																			// cards
				int counter = 1;
				ArrayList<Card> tempList = new ArrayList<Card>();
				Player x = playersInGame.get(index);
				tempList.add(x.getCard(0));
				tempList.add(x.getCard(1));
				for (Card y : communityCards) {
					tempList.add(y);
				}
				sortCardsSuit(tempList);
				for (Card a : tempList) {
					System.out.print(a.toString() + " ");
				}
				for (int i = 0; i < tempList.size() - 1; i++) { // Checks for flush
					if (tempList.get(i).getSuit().equals(tempList.get(i + 1).getSuit())) {// increments every time a
																							// card
																							// with the same suit if
																							// found,
																							// resets if not
						counter++;
						if (i == 5 && counter >= 5) {
							foundFlush(tempList, i, x);
						}
					} else {
						if (counter >= 5) {
							foundFlush(tempList, i, x);
							break;
						}
						counter = 1;
					}

				}

				if (x.getHandValue() <= 8) { // if no royal flush or Straight Flush are found this is run
					PairChecker(tempList, x);
					StraightChecker(tempList, x, false);
				}
				int highCard = 0;
				for (Card card : tempList) {
					if (card.getValue() > highCard) {
						highCard = card.getValue();
					}
				}
				CardComboCheck(x, tempList, 1, highCard); // runs every time but a lower value cannot overpass a higher
															// value
				System.out.println("Player: " + x.toString() + " Hand Value " + x.getHandValue());
			}
		}
		Player winner = compareHandValues(); // finds person in with the highest Hand Value if more then one people
												// have
												// the same handvalue then determine tie is run
		if (winner == null) {
			for (Player x : tieList) {
				System.out.println("Player: " + x.toString() + " Wins");
				x.addCurrentCash(pot / tieList.size());
			}
		} else {
			if (winner.getSidePot() == true) { // if the winner only gets a side pot
				winner.addCurrentCash(sidePot);
				playersInGame.remove(winner);
				pot -= sidePot;
				sidePotWinner = winner;
				winner.sidePot(false);
				System.out.println("Side Pot goes to " + winner);
				winner = null;
				determineWinner(); // runs program again with the regular pot and with the winner out of the
									// running
			} else {
				winner.addCurrentCash(pot);
			} // add the pot cash to the winners cash amount
			System.out.println("Player: " + winner.toString() + " Wins the pot");
		}
		for (Player x : currentPlayers) {
			x.printHandCards();
		}
		gui.displayGameEnd(winner, tieList, sidePotWinner); // runs method in display that displays all the text for the
		// winner/winners
	}

	private static void CardComboCheck(Player y, ArrayList<Card> tempList, int HandValue, int largeCardValue) {
		// checks to see that a hand card is used to make a card combo
		boolean found = false;
		for (Card x : tempList) {
			for (int i = 0; i < communityCards.length; i++) {
				if (x == communityCards[i]) {
					found = true;
				}
			}
			if (found == false) {
				y.setHandValue(HandValue, largeCardValue); // assigns a handvalue and a kicker card
				break;
			} // if a card is used that isnt a community card then the combo works
			found = false;
		}
	}

	private static void foundFlush(ArrayList<Card> tempList, int i, Player x) { // only runs if flush is found and the
																				// last card is found
		String flushSuit = tempList.get(i).getSuit();
		ArrayList<Card> newList = new ArrayList<Card>(); //used to seperate the flush
		int maxSuitCardValue = 0;
		sortCardsNum(tempList);
		for (int a = tempList.size() -1; a > tempList.size(); a--) { // goes through the flush and finds 
			//largest valued card while removing cards that are not part of the flush
			Card card = tempList.get(a);
			if (newList.size() >= 5) {break; }
			if (flushSuit.equals(card.getSuit())) {
				newList.add(card);
				if (card.getValue() > maxSuitCardValue) {
					maxSuitCardValue = card.getValue();
				}
			}
		}
		CardComboCheck(x, newList, 6, maxSuitCardValue); // gives a player a flush with a their highest card for
		// a tie
		for (Card card : newList) { // adds all flush cards to hand cards list
			if (flushSuit.equals(card.getSuit())) {
				x.addHandCard(card);
			}
		}
		RoyalFlushChecker(newList, x, flushSuit);
	}

	private static Player compareHandValues() {
		int maxHandValue = 0;
		for (Player x : playersInGame) { // finds max hand value in list
			if (x.getHandValue() > maxHandValue) {
				maxHandValue = x.getHandValue();
			}
		}
		for (Player x : playersInGame) { // checks over list. If more than one person has the same hand value the
											// determineTie method is run
			if (maxHandValue == x.getHandValue()) {
				tieList.add(x);
			}
		}
		if (tieList.size() == 1) {
			return tieList.get(0);
		}
		return determineTie(tieList);
	}

	private static Player determineTie(ArrayList<Player> tieList) {
		int counter = 0;
		int mode = 0; // determines which card is compared depending on what the matching players have
		int compareCard = 0;
		Player winner = null;
		int maxCard = 0;
		int allHandValue = tieList.get(0).getHandValue();
		while (counter < 2) {
			for (int i = 0; i < tieList.size(); i++) {
				Player x = tieList.get(i);
				if (mode == 0) { // checks card value in win Card (changes depending on what kind of combination
									// the winner has)
					compareCard = x.getkicker();
				}
				if (mode == 1) { // checks other hand card that is not used in pair or 3 of a kind
					if (x.getCard(0).getValue() == x.getkicker()) {
						compareCard = x.getCard(1).getValue();
					} else {
						compareCard = x.getCard(0).getValue();
					}
				}
				if (mode == 2) { // Checks lowest hand card
					if (x.getCard(0).getValue() == x.getHighHandCardValue()) {
						compareCard = x.getCard(1).getValue();
					} else {
						compareCard = x.getCard(0).getValue();
					}
				}
				if (mode == 3) {
					compareCard = x.getOtherPair();
				}

				if (compareCard > maxCard) {
					maxCard = compareCard;
					if (winner != null) {
						tieList.remove(winner); // the last winner is removed from list
						System.out.println("removed -" + winner.toString());
						i--;
					}
					winner = x;
					System.out.println("winner " + x);
				}

				if (winner != x) {// removes any player that has a less valued compare card
					tieList.remove(x);
					System.out.println("removed " + x.toString());
					i--;
				}
			}
			if (tieList.size() == 1) { // if only one player has the highest determine card they win
				return winner;
			}
			if (allHandValue == 4 || allHandValue == 2) { // 3 of a kind or 1 pair will use other hand card
				mode = 1;
			}
			if (allHandValue == 1) { // high card will use lowest hand card
				mode = 2;
			}
			if (allHandValue == 7 || allHandValue == 3) {// full house or 2 pair will use other pair
				mode = 3;
			}
			maxCard = 0;
			counter++;
			winner = null;
		}
		System.out.println("tie");
		return winner;
	}

	private static void PairChecker(ArrayList<Card> list, Player currentPlayer) {
		int threeOfAKind = 0;
		int counter = 1; // counts number of = valued cards
		int threeOfAKindMax = 0; // finds Highest 3 of a kind
		int maxPair = 0;
		int secondPair = 0;
		ArrayList<Card> newList = new ArrayList<Card>(); //used to seperate the pairs
		ArrayList<Integer> pairValues = new ArrayList<Integer>();
		sortCardsNum(list);
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).getValue() != list.get(i + 1).getValue() || i == 5) { // forces the if to run at the last
																					// card
				if (i == 5) {
					if (list.get(i).getValue() == list.get(i + 1).getValue()) { // if before = after, counter++
						counter++;
					}
				} // checks last two cards for match
				if (counter == 2) {
					pairValues.add(list.get(i).getValue());
				}
				if (counter == 3) {
					threeOfAKind++;
					if (list.get(i).getValue() > threeOfAKindMax) {
						threeOfAKindMax = list.get(i).getValue();
					} // keeps track of highest 3 of a kind in hand
				}
				int OGcounter = counter;
				int OGi = i;
				while (counter != 0  && counter > 1) { //removes all non pair cards
					newList.add(list.get(i));
					i--;
					counter--;
					}
				i = OGi;
				if (OGcounter == 4) {
					CardComboCheck(currentPlayer, newList, 8, list.get(i).getValue());
					for (int y = 0; y < 4; y++) { // adds all cards into hand card
						currentPlayer.addHandCard(list.get(i));
						i--;
					}
					i += 4;
					break;
				}
				counter = 1;
			} else {
				counter++;
			}
		}
		int pairAmount = pairValues.size();
		if (pairAmount == 1) {
			maxPair = pairValues.get(0);
		}
		if (pairAmount >= 2) { // if more than one pair determines value of both of them
			for (int x : pairValues) {
				if (x > maxPair) {
					maxPair = x;
				}
			} // finds Max pair
			pairValues.remove(pairValues.indexOf(maxPair)); // removes highest pair from list
			for (int y : pairValues) {
				if (y > secondPair) {
					secondPair = y;
				}
			} // finds second Highest pair
		}
		if (pairAmount >= 1 && threeOfAKind == 1 || threeOfAKind >= 2) { // full house
			CardComboCheck(currentPlayer, newList, 7, threeOfAKindMax);
			if (pairAmount >= 1) {
				currentPlayer.setOtherPair(pairValues.get(0));
			} // keeps track of pair in case of a tie
			for (Card y : newList) { // adds all cards into hand card
				if (y.getValue() == threeOfAKindMax || y.getValue() == maxPair) {
					currentPlayer.addHandCard(y);
				}
			}
		} else if (threeOfAKind == 1) { // Three of a kind
			CardComboCheck(currentPlayer, newList, 4, threeOfAKindMax);
			for (Card y : newList) { // adds all cards into hand card
				if (y.getValue() == threeOfAKindMax) {
					currentPlayer.addHandCard(y);
				}
			}
		} else if (pairAmount >= 2) {
			CardComboCheck(currentPlayer, newList, 3, maxPair); // two pair
			currentPlayer.setOtherPair(secondPair); // Keeps track of other pair in case of tie
			for (Card y : newList) { // adds all cards into hand card
				if (y.getValue() == maxPair || y.getValue() == secondPair) {
					currentPlayer.addHandCard(y);
				}
			}
		} else if (pairAmount == 1) {
			CardComboCheck(currentPlayer, list, 2, pairValues.get(0)); // pair
			for (Card y : newList) { // adds all cards into hand card
				if (y.getValue() == pairValues.get(0)) {
					currentPlayer.addHandCard(y);
				}
			}
		}
	}

	private static void RoyalFlushChecker(ArrayList<Card> list, Player CurrentPlayer, String flushSuit) { // flush
		int counter = 1;
		for (int i = 0; i < list.size(); i++) { // removes all cards that dont have the same suit or are less than 10
			if (list.get(i).getSuit() != flushSuit || list.get(i).getValue() < 10) {
				list.remove(i);
			}
		}
		sortCardsNum(list); // Sort cards by Numeric order
		for (int i = 0; i < list.size() - 1; i++) {
			if (counter == 5) {
				CardComboCheck(CurrentPlayer, list, 10, 14);
				for (Card x : list) {// add winning cards to handcard array
					CurrentPlayer.addHandCard(x);
				}
				break;
			} // found royal flush
			if (list.get(i).getValue() == (list.get(i + 1).getValue() + 1)) {
				counter++;
			} else {
				counter = 1;
			}
		}
		StraightChecker(list, CurrentPlayer, true);// checks to see if the flush is a straight flush
	}

	private static void StraightChecker(ArrayList<Card> list, Player CurrentPlayer, boolean flush) {
		int counter = 1;
		sortCardsNum(list);
		ArrayList<Card> newList = new ArrayList<Card>(); //used to separate the straight from the rest of the list
		for (int i = 0; i < list.size() - 1; i++) {
			if (list.get(i).getValue() == (list.get(i + 1).getValue() - 1)) {
				counter++;
			} else {
				if (counter >= 5) {
					while (counter != 0) { //removes all non pair cards
						newList.add(list.get(i));
						i--;
						counter--;
						}
					if (flush == false) {
						CardComboCheck(CurrentPlayer, newList, 5, list.get(i).getValue());
					} else {
						CardComboCheck(CurrentPlayer, newList, 9, list.get(i).getValue());
					}
					for (int y = 0; y < 5; y++) { // adds all cards into hand card
						CurrentPlayer.addHandCard(list.get(i));
						i--;
					}
					i += 5;
				}
				if (list.get(i).getValue() != (list.get(i + 1).getValue())) {// if the next card is equal dont change
																				// the counter
					counter = 1;
				}
			}
		}
	}

	public static void sortCardsSuit(ArrayList<Card> list) {
		boolean sorted = false;
		Card temp;
		int x = list.size();
		while (sorted == false) {
			int lowestLetter = 0;
			for (int i = 1; i < x; i++) {
				if (list.get(i).getSuit().compareTo(list.get(lowestLetter).getSuit()) > -1) {
					lowestLetter = i;
				}
			}
			temp = list.get(lowestLetter);
			list.remove(list.get(lowestLetter));
			list.add(x - 1, temp);
			x--;
			if (x == 1) {
				sorted = true;
			}
		}
	}

	public static void sortCardsNum(ArrayList<Card> list) { // sorts list (smallest - largest) in num
		boolean sorted = false;
		Card temp;
		int x = list.size();
		while (sorted == false) {
			int posOfLarge = 0;
			for (int i = 1; i < x; i++) {
				if (list.get(i).getValue() > list.get(posOfLarge).getValue()) {
					posOfLarge = i;
				}
			}
			temp = list.get(posOfLarge);
			list.remove(list.get(posOfLarge));
			list.add(x - 1, temp);
			x--;
			if (x == 1) {
				sorted = true;
			}
		}
	}

}
