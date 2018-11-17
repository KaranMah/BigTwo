
import java.util.ArrayList;
import java.util.Collections;

/**
 * Models a particular type of hand - Flush
 * @author karan
 *
 */
public class Flush extends Hand{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Creates and returns an instance of the class
	 * Calls super constructor of Hand class
	 * @param player
	 * 		Player associated with the hand
	 * @param cards
	 * 		list of cards associated with the hand
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	/* (non-Javadoc)
	 * @see Hand#isValid()
	 */
	public boolean isValid() {
		CardList cards=new CardList();
		for(int i=0;i<5;i++)
			{cards.addCard(this.getCard(i));}
		cards.sort();
		if(this.size()!=5) return false;
		for(int i=0;i<4;i++) {
			if(cards.getCard(i).getSuit()!=cards.getCard(i+1).getSuit())
				return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see Hand#getType()
	 */
	public String getType() {
		return "Flush";
	}
	/* (non-Javadoc)
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		CardList cards=new CardList();
		for(int i=0;i<5;i++)
			{cards.addCard(this.getCard(i));}
		int idx=0;
		ArrayList<Integer> allcards= new ArrayList<Integer>();
		for(int i=0;i<5; i++) {
			int card=cards.getCard(i).rank;
			allcards.add(card);
		}
		Collections.sort(allcards);
		int x= allcards.get(0);
		if (x==14||x==13) x-=13;
		for(int i=0;i<5; i++) {
			if(cards.getCard(i).getRank()==x)
				idx=i;
		}
		return cards.getCard(idx);
		
	}
}
