
/**
 * This class represents the hand played by a person in general card games.
 * This class also finds the wining hand in a card game by comparing 
 * @author karan
 *
 */
public abstract class Hand extends CardList {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates and returns the instance of Hand class
	 * @param player
	 * 		The player associated with this hand
	 * @param cards
	 * 		the list of cards associated with the hand and the player
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player=player;
		int x=cards.size();
		for (int i=0;i<x;i++) 
		{
			this.addCard(cards.getCard(i));
		}
		
	}
	private CardGamePlayer player;
	
	/**
	 * Method to return the player of this hand
	 * @return
	 * 		returns player instance of this hand class
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	/**
	 * Method to get Top Card. 
	 * Method overridden in subclasses
	 * @return
	 * 		Returns the top card of the hand
	 */
	public Card getTopCard() {
		Card card= new Card(0,0);
		return card;
	}

	/**
	 * Method to check if this hand beats the hand given as parameter
	 * @param hand
	 * 		The hand to be compared with this hand
	 * @return
	 * 		Returns true if beats the hand in parameter otherwise returns false
	 */
	public boolean beats(Hand hand) {
		int handSize=hand.size();
		if(handSize==this.size())
		{
		if(handSize==1||handSize==2||handSize==3) {
			int x=this.getTopCard().rank;
			int y=hand.getTopCard().rank;
			if(x==0||x==1) {x=x+13;}
			if(y==0||y==1) {y=y+13;}
			if(x>y)
				return true;
			else if(x==y) {
				if(this.getTopCard().suit>hand.getTopCard().suit)
					return true;
				else 
					return false;
			}
			else
				return false;
		}
		else if(handSize==5) {
			String type;
			type=this.getType();
			if(type=="Quad") {
				if(hand.getType()=="Straight") return true;
				else if(hand.getType()=="Flush") return true;
				else if(hand.getType()=="FullHouse")return true;
				else {
					int x=this.getTopCard().rank;
					int y=hand.getTopCard().rank;
					if(x==0||x==1) {x+=13;}
					if(y==0||y==1) {y+=13;}
				
					if(x>y)
						return true;
					else if(x==y) {
						if(this.getTopCard().suit>hand.getTopCard().suit)
							return true;
						else 
							return false;
					}
					else
						return false;
				} 
			}
			
			else if(type=="Straight") {
				int x=this.getTopCard().rank;
				int y=hand.getTopCard().rank;
				if(x==0||x==1) { x+=13;}
				if(y==0||y==1) { y+=13;}
				
				if(x>y)
					return true;
				else if(x==y) {
					if(this.getTopCard().suit>hand.getTopCard().suit)
						return true;
					else 
						return false;
				}
				else
					return false;
			}
			else if(type=="StraightFlush") {
				if(hand.getType()=="Straight") return true;
				else if(hand.getType()=="FullHouse") return true;
				else if(hand.getType()=="Quad") return true;
				else if(hand.getType()=="Flush") return true;
				else {
					
					int x=this.getTopCard().suit;
					int y=hand.getTopCard().suit;
				
					if(x>y)
						return true;
					else if(x==y) {
						int a =this.getTopCard().rank;
						int b=hand.getTopCard().rank;
						if(a==0||a==1) {a+=13;}
						if(b==0||b==1) {b+=13;}
						if(a>b)
							return true;
						else 
							return false;
					}
					else
						return false;
				}
			}
			else if(type=="Flush") {
				if(hand.getType()=="Straight") { return true;}
				else {
					int x=this.getTopCard().suit;
					int y=hand.getTopCard().suit;
					
					if(x>y)
						return true;
					else if(x==y) {
						int a =this.getTopCard().rank;
						int b=hand.getTopCard().rank;
						if(a==0||a==1) {a+=13;}
						if(b==0||b==1) {b+=13;}
						if(a>b)
							return true;
						else 
							return false;
					}
					else
						{return false;}
				}
			}
		
			else if(type=="FullHouse") {
				if(hand.getType()=="Straight") { return true;}
				else if(hand.getType()=="Flush") { return true;}
				else {
					int x=this.getTopCard().rank;
					int y=hand.getTopCard().rank;
					if(x==0||x==1) {x+=13;}
					if(y==0||y==1) {y+=13;}
					
					if(x>y)
						{return true;}
					else if(x==y) {
						if(this.getTopCard().suit>hand.getTopCard().suit)
							{return true;}
						else 
							{return false;}
					}
					else
						{return false;}
				}
			}
			else
				{return false;}

		}
		else
			{return false;}
		}
		else
			{return false;}
	}
	/**
	 * Method to check if the hand is valid or not
	 * Method overriden in subclasses
	 * @return
	 * 		returns boolean value true if valid else returns false
	 */
	abstract boolean isValid();
	/**
	 * Method to return the type of the hand.
	 * Method Overridden in Subclasses
	 * @return
	 * 		returns string value - name of subclass
	 */
	abstract String getType();
	
}
