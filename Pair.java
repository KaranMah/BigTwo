
/**
 * Subclass of Hand class to represent a particular type of hand - the Pair
 * @author karan
 *
 */
public class Pair extends Hand{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor to create an instance of this class.
	 * It calls the super constructor by using super command.
	 * @param player
	 * 		Player of this hand
	 * @param cards
	 * 		list of cards in this hand
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		CardList cards=new CardList();
		if (this.size()!=2) return false;
		for(int i=0;i<2;i++)
			{cards.addCard(this.getCard(i));}
		if(cards.getCard(0).getRank()==cards.getCard(1).getRank())
			return true;
		else 
			return false;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Pair";
	}
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		CardList cards=new CardList();
		for(int i=0;i<2;i++)
			{cards.addCard(this.getCard(i));}
		if(cards.getCard(0).suit>cards.getCard(1).suit)
			return cards.getCard(0);
		else 
			return cards.getCard(1);
	}
}
