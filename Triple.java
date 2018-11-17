
/**
 * 
 * @author karan
 *
 */
public class Triple extends Hand {
	
	private static final long serialVersionUID = 1L;
	/**
	 *  Creates and returns an instance of the class
	 * Calls super constructor of Hand class
	 * @param player
	 * 		Player associated with the hand
	 * @param cards
	 * 		list of cards associated with the hand
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		CardList cards=new CardList();
		for(int i=0;i<3;i++)
			{cards.addCard(this.getCard(i));}
		if(this.size()!=3) return false;
		Card card1= cards.getCard(0);
		Card card2= cards.getCard(1);
		Card card3= cards.getCard(2);
		if(card1.rank==card2.rank&&card3.rank==card2.rank)
			return true;
		else 
			return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Triple";
	}
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		CardList cards=new CardList();
		for(int i=0;i<3;i++)
			{cards.addCard(this.getCard(i));}
		Card card1= cards.getCard(0);
		Card card2= cards.getCard(1);
		Card card3= cards.getCard(2);
		if(card1.suit>card2.suit)
			return card1;
		else if(card2.suit>card3.suit)
			return card2;
		else
			return card3;
	}
}
