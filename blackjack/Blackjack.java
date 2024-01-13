package blackjack;

import java.util.*;

public class Blackjack implements BlackjackEngine {

	public static final int DRAW = 1;

	public static final int LESS_THAN_21 = 2;

	public static final int BUST = 3;

	public static final int BLACKJACK = 4;

	public static final int HAS_21 = 5;

	public static final int DEALER_WON = 6;

	public static final int PLAYER_WON = 7;

	public static final int GAME_IN_PROGRESS = 8;
	private final int INITIAL_ACCOUNT_AMOUNT = 200;
	private final int INITIAL_BET_AMOUNT = 5;

	private final Random randomGenerator;
	private final int numberOfDecks;
	private List<Card> gameDeck;
	private List<Card> playerCards;
	private List<Card> dealerCards;
	private int accountAmount;
	private int betAmount;
	private int gameStatus;

	/**
	 * Constructor you must provide. Initializes the player's account to 200 and
	 * the initial bet to 5. Feel free to initialize any other fields. Keep in
	 * mind that the constructor does not define the deck(s) of cards.
	 * 
	 * @param randomGenerator
	 * @param numberOfDecks
	 */

	public Blackjack(Random randomGenerator, int numberOfDecks) {
		this.randomGenerator = randomGenerator;
		this.numberOfDecks = numberOfDecks;
		this.accountAmount = INITIAL_ACCOUNT_AMOUNT;
		this.betAmount = INITIAL_BET_AMOUNT;
		this.gameStatus = GAME_IN_PROGRESS;

		// gameDeck = new ArrayList<>();

	}

	@Override
	public int getNumberOfDecks() {
		return numberOfDecks;
	}

	@Override
	public void createAndShuffleGameDeck() {
		gameDeck = new ArrayList<>();

		List<CardSuit> suits = Arrays.asList(CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.HEARTS, CardSuit.CLUBS);

		for (int i = 0; i < numberOfDecks; i++) {
			for (CardSuit suit : suits) {
				for (CardValue rank : CardValue.values()) {
					gameDeck.add(new Card(rank, suit));
				}
			}
		}

		Collections.shuffle(gameDeck, randomGenerator);
	}

	@Override
	public Card[] getGameDeck() {
		return gameDeck.toArray(new Card[0]);
	}

	public void deal() {

		createAndShuffleGameDeck();
		playerCards = new ArrayList<>();
		dealerCards = new ArrayList<>();

		// gameDeck.clear();

		// createAndShuffleGameDeck();

		// Assign cards to the dealer and player
		playerCards.add(gameDeck.remove(0));
		dealerCards.add(gameDeck.remove(0));
		playerCards.add(gameDeck.remove(0));
		dealerCards.add(gameDeck.remove(0));

		playerCards.get(0).setFaceUp();
		dealerCards.get(0).setFaceDown();

		gameStatus = GAME_IN_PROGRESS;

		accountAmount -= betAmount;

	}

	@Override
	public Card[] getDealerCards() {
		return dealerCards.toArray(new Card[0]);
	}

	// reduces total by 10 when Ace is present
	private int calculateTotal(List<Card> cards) {
		int total = 0;
		int numAces = 0;

		for (Card card : cards) {
			total += card.getValue().getIntValue();
			if (card.getValue() == CardValue.Ace) {
				numAces++;
			}
		} 	

		while (total > 21 && numAces > 0) {
			total -= 10;
			numAces--;
		}

		return total;
	}

	// Case where Ace = 11
	private int specialAceCase(List<Card> cards) {
		int total = 0;

		for (Card card : cards) {
			if (card.getValue() == CardValue.Ace) {
				total += 11;
			} else { 
				total += card.getValue().getIntValue();
			}
		}

		return total;
	}

	@Override
	public int[] getDealerCardsTotal() {
		int total = calculateTotal(dealerCards);
		if (total > 21) {
			return null; // Cards represent a value higher than 21
		} else if (total == 21) {
			return new int[] { 21 }; // Only one value of 21 is possible
		} else {
			int altTotal = specialAceCase(dealerCards);
			if (altTotal != total) {
				return new int[] { Math.min(total, altTotal),
						Math.max(total, altTotal) };
			} else {
				return new int[] { total };
			}
		}
	}

	@Override
	public int getDealerCardsEvaluation() {
		int[] totals = getDealerCardsTotal();

		if (totals == null) {
			return BUST;
			
		} else if (totals.length == 2 && totals[1] == 21 && totals[0] != 21) {
			return BLACKJACK;
			
		} else if (totals[0] == 21) {
			return HAS_21;
			
		} else {
			return LESS_THAN_21;
		}
	}

	@Override
	public Card[] getPlayerCards() {
		return playerCards.toArray(new Card[0]);
	}

	@Override
	public int[] getPlayerCardsTotal() {
		// playerCards = new ArrayList<>();
		int total = calculateTotal(playerCards);
		if (total > 21) {
			return null; // Cards represent a value higher than 21
		} else if (total == 21) {
			return new int[] { 21 }; // Only one value of 21 is possible
		} else {
			int altTotal = specialAceCase(playerCards);
			if (altTotal != total) {
				return new int[] { Math.min(total, altTotal),
						Math.max(total, altTotal) };
			} else {
				return new int[] { total };
			}
		}
	}

	@Override
	public int getPlayerCardsEvaluation() {
		int[] totals = getPlayerCardsTotal();

		if (totals == null) {

			return BUST;

		} else if (totals.length == 2 && totals[1] == 21 && totals[0] != 21) {

			return BLACKJACK;

		} else if (totals[0] == 21) {

			return HAS_21;

		} else {

			return LESS_THAN_21;

		}

	}

	private Card retrieveCard() {
		if (gameDeck.isEmpty()) {
			return null;
		}

		Card card = gameDeck.remove(0);
		return card;
	}

	@Override
	public void playerHit() {
		if (gameStatus == DEALER_WON || gameStatus == PLAYER_WON) {
			return;
		}

		if (gameStatus != GAME_IN_PROGRESS) {
			return;
		}

		Card card = retrieveCard();

		playerCards.add(card);

		int[] totals = getPlayerCardsTotal();

		if (totals == null) {
			gameStatus = DEALER_WON;
		} else if (totals.length == 2 && totals[1] == 21) {
			gameStatus = BLACKJACK;
		} else if (totals[0] > 21) {
			gameStatus = DEALER_WON;
		} else {
			gameStatus = GAME_IN_PROGRESS;
		}
	}

	@Override
	public void playerStand() {
		
		int maxDealer;
		int maxPlayer;
		
		int[] totals = getDealerCardsTotal();
		int[] playerTotal = getPlayerCardsTotal();
		if(getPlayerCardsTotal() == null) {
			maxDealer = -1;
		} else {
			maxPlayer = getPlayerCardsTotal()[getPlayerCardsTotal().length -1];
			
		}
		
		if(getPlayerCardsTotal() == null) {
			maxDealer = -1;
		} else {
			maxPlayer = getPlayerCardsTotal()[getPlayerCardsTotal().length -1];
			
		}


		while (totals != null && totals[totals.length - 1] < 16
				&& gameStatus == GAME_IN_PROGRESS) {
			dealerCards.add(gameDeck.get(0));
			totals = getDealerCardsTotal();
			playerTotal = getPlayerCardsTotal(); // Update playerTotal after
													// each hit
		}
		
		dealerCards.get(0).setFaceUp();
		int dealerTotal = totals[0];
		if (playerTotal.length == 2 && playerTotal[1] == 21) { 
				gameStatus = BLACKJACK;									
			if (totals == null || dealerTotal != 21) {
				gameStatus = PLAYER_WON;
				setAccountAmount(getAccountAmount() + (2 * betAmount));
			} else {
				gameStatus = DRAW;
				setAccountAmount(getAccountAmount() + betAmount);

			}
		} else {

			if (totals == null || dealerTotal > 21) {
				gameStatus = PLAYER_WON;
				setAccountAmount(getAccountAmount() + (2 * betAmount));
			} else if (dealerTotal > playerTotal[playerTotal.length - 1]) {
				gameStatus = DEALER_WON;
				setAccountAmount(getAccountAmount() - betAmount);
			} else if (dealerTotal < playerTotal[playerTotal.length - 1]) {
				gameStatus = PLAYER_WON;
				setAccountAmount(getAccountAmount() + (2 * betAmount));
			} else {
				gameStatus = DRAW;
				setAccountAmount(getAccountAmount() + betAmount);
			}
		}
	}

	@Override
	public int getGameStatus() {
		return gameStatus;
	}

	@Override
	public void setBetAmount(int amount) {
		this.betAmount = amount;
	}

	@Override
	public int getBetAmount() {
		return betAmount;
	}

	@Override
	public void setAccountAmount(int amount) {
		this.accountAmount = amount;
	}

	@Override
	public int getAccountAmount() {
		return accountAmount;
	}

	/* Feel Free to add any private methods you might need */
}