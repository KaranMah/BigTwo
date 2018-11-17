
/**
 * Models a particular type of Hand - Quad
 * @author karan
 *
 */
public class Quad extends Hand {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Creates and returns an instance of the class
	 * Calls super constructor of Hand class
	 * @param player
	 * 		Player associated with the hand
	 * @param cards
	 * 		list of cards associated with the hand
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}

	private int found=0;
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		
		CardList cards=new CardList();
		for(int i=0;i<5;i++)
			{cards.addCard(this.getCard(i));}
		cards.sort();
		
		boolean valid1=true,valid2=true;
		
		
		if(this.size()!=5) return false;
		
		
		for(int i=0;i<4;i++) {
			if(cards.getCard(i).getRank()!=cards.getCard(i+1).getRank())
			{ 
				valid1=false;
				found= 2;
				
			}
		}
		
		for(int i=1;i<4;i++) {
			if(cards.getCard(i).getRank()!=cards.getCard(i+1).getRank())
			{
				valid2=false;
				found=1;
				
			}
		}
			if(found==1)
				return valid1;
			else 
				return valid2;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Quad";
	}
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard() {
		CardList cards=new CardList();
		for(int i=0;i<5;i++)
			{cards.addCard(this.getCard(i));}
		int idx=0;
		if(found==1) {
			for(int i=1;i<4;i++) {
				if(cards.getCard(i).getSuit()>cards.getCard(idx).getSuit())
					idx=i;
			}
			 return cards.getCard(idx);
		}
		else if (found==2) {
			idx=1;
			for(int i=2;i<5;i++) {
				if(cards.getCard(i).getSuit()>cards.getCard(idx).getSuit())
					idx=i;
			}
		}	
		return cards.getCard(idx);
	}

}
