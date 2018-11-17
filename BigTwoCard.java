/**
 * Class  models a card in the BigTwo game
 * @author karan
 *
 */
public class BigTwoCard extends Card{
	private static final long serialVersionUID = 1L;
	/**
	 * Creates and returns the instance of the class
	 * explicitly calls the super constructor of parent class
	 * @param suit
	 * 		Suit of card to be created
	 * @param rank
	 * 		Rank of the card to be created
	 */
	public BigTwoCard(int suit , int rank) {
			super(suit, rank);
	}
	/* (non-Javadoc)
	 * @see Card#compareTo(Card)
	 */
	public int compareTo(Card card) {
		int card1=this.rank;
		int card2=card.rank;
		if(this.rank==0) card1+=13;
		else if(this.rank==1) card1+=13;
		if(card.rank==0) card2+=13;
		else if(card.rank==0) card2+=13;
		
		if (card1 > card2) {
			return 1;
		} else if (card1 < card2) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
