
/**
 * this class represents a particular type of class- Single
 * @author karan
 *
 */
public class Single extends Hand {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Creates and returns an instance of the class
	 * Calls super constructor of Hand class
	 * @param player
	 * 		Player associated with the hand
	 * @param cards
	 * 		list of cards associated with the hand
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		if(this.size()==1) return true;
		else
			return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Single";
	}
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		return this.getCard(0);
	}
}
