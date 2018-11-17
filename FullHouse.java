
/**
 * Models a particular type of Hand - FullHouse
 * @author karan
 *
 */
public class FullHouse extends Hand{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates and returns an instance of the class
	 * Calls super constructor of Hand class
	 * @param player
	 * 		Player associated with the hand
	 * @param cards
	 * 		list of cards associated with the hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "FullHouse";
	}
	
	private boolean found1=true,found2=true,found=false;
	
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		CardList cards=new CardList();
		for(int i=0;i<5;i++)
			{cards.addCard(this.getCard(i));}
		cards.sort();
		
		if(this.size()!=5) return false;
		for(int i=0;i<2&&found1;i++) {
			if(cards.getCard(i).getRank()!=cards.getCard(i+1).getRank())
				found1=false;
			
		}
		if (found1) {
			if(cards.getCard(3).getRank()==cards.getCard(4).getRank())
				found=true;
			found2=false;
		}
		for(int i=2;i<4&&found2;i++) {
			if(cards.getCard(i).getRank()!=cards.getCard(i+1).getRank())
				found2=false;
		}
		if (found2) {
			if(cards.getCard(0).getRank()==cards.getCard(1).getRank())
				found=true;
		}
		return found;
	}

	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		CardList cards=new CardList();
		for(int i=0;i<5;i++)
			{cards.addCard(this.getCard(i));}
		cards.sort();
		int idx=0;
		if(found1) {
			for(int i=1;i<3;i++) {
				if(cards.getCard(i).getSuit()>cards.getCard(idx).getSuit())
					idx=i;
			}
			return cards.getCard(idx);
		}
		else {
			idx=2;
			for(int i=3;i<5;i++) {
				if(cards.getCard(i).getSuit()>cards.getCard(idx).getSuit())
					idx=i;
			}
			return cards.getCard(idx);
		}
	}
}
