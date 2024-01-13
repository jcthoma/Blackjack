# Blackjack

In this blackjack version we have a dealer (person who shuffles and distribute cards) and only one player.  The game's objective is for the player to beat the dealer by generating a hand of cards whose value is higher than the dealer's hand without exceeding a total value of 21.  The game starts by the dealer shuffling cards (one or more decks) and dealing two cards to the player and herself/himself.  One of the dealer's cards will be face down.  At this point the player will ask for cards until he/she believes it can beat the dealer with the current hand and as long the cards' total value does not exceed 21.  If the hand of cards does not exceed 21, and the player stops requesting cards (what is referred to as "stand"), then the dealer will flip the card that was face down, and proceed to deal cards to herself/himself as long as the cards' value is less than 16 and does not exceed 21.   If a value greater than 21 is generated the player wins.  Otherwise whoever (player or dealer) has the hand with the highest value will win the game.  The following provides additional information about the game:

The term "bust" refers to the scenario where the player or dealer cards' value exceeds 21.
The term "blackjack" refers to the scenario where in a hand of cards we have an Ace("1") along with a "10", Jack, Queen or King. 
In our blackjack version the player cannot split a hand of cards.  If you don't know what splitting is, disregard this comment.
The value of cards "2" through "10" correspond to the numeric value associated with the card face value.
"1" (Ace) could be worth either 1 or 11.
The Jack ("J"),  Queen("Q")  and King("K") are worth 10 points each.
The card's suit (i.e., "SPADES", "DIAMONDS", "HEARTS", "CLUBS") has no bearing on a card's value.
