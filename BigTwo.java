import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface.
 * It is used to model a Big Two card game.
 * This class contains the main function and other functions to run the game
 * @author karan
 *
 */
public class BigTwo implements CardGame{

	private Deck deck=new BigTwoDeck();
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentIdx;
	private BigTwoTable bigTwoTable;
	
	//self declared global variables
	private int idx=5;
	private boolean refresh=false,repaint=true;
	
	
	/**
	 * Creates and returns the instance of BigTwo class
	 * 
	 */
	public BigTwo() {
		playerList = new ArrayList<CardGamePlayer>();
		for(int i=0;i<4;i++) {
			CardGamePlayer player =new CardGamePlayer();
			playerList.add(player);
		}
		handsOnTable=new ArrayList<Hand>();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumOfPlayers() {
		return playerList.size();
	}
	
	/**
	 * Returns the deck of cards.
	 * @return
	 * 		returns instance of BigTwoDeck which has the deck of cards
	 */
	public Deck getDeck() {
		return this.deck;
	}
	/**
	 * Returns the ArrayList containing the list of players
	 * @return
	 * 		the list of payers
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}
	/**
	 *Returns the ArrayList containing the hands on table.
	 * @return
	 * 		The list of hands on table.
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}
	
	/**
	 * To return the current index
	 * @return
	 * 		returns the current index
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * Method to return idx of the last player who played the hand
	 * @return
	 * 	integer value containing index of player who played the last hand
	 */
	public int getidx() {
		return idx;
	}
	
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Deck deck) {
		idx=5;
		
		for(int i=0;i<this.getNumOfPlayers();i++) {
			playerList.get(i).removeAllCards();
		}
		
		handsOnTable.clear();
		int x=0;
		for(int i=0;i<4;i++) {
			for(int j=0;j<13;j++) {
				playerList.get(i).addCard(deck.getCard(i+j+x));
			}
			x+=12;
		}
		Card card=new Card(0,2);
		for(int i=0;i<4;i++) {
			if(playerList.get(i).getCardsInHand().contains(card)) {
				bigTwoTable.setActivePlayer(i);
				currentIdx=i;
			}
		}
		bigTwoTable.printMsg("Player "+currentIdx+"'s turn:\n");
	}
	
	
	/**
	 * Make move played by the player and call checkMove()
	 * @param playerID
	 * 	the playerID of the player who makes the move
	 * @param cardIdx
	 * 	the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID,int[] cardIdx) {
		checkMove(playerID, cardIdx);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkMove(int playerID,int[] cardIdx) {
		
		CardList list=playerList.get(currentIdx).play(cardIdx);
		
		Card card=new Card(0,2);
		
		
		if(list!=null) {
			Hand hand=composeHand(playerList.get(currentIdx),list);
			if(hand!=null) {
				
				if(handsOnTable.size()==0){
					if(hand.contains(card)){
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						repaint=true;
						bigTwoTable.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						bigTwoTable.printMsg("\n");
					}
					else{
						bigTwoTable.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						bigTwoTable.printMsg("<==Not a legal move!!!\n");
						if(currentIdx>0)
							currentIdx=currentIdx-1;
						else
							currentIdx=3;
						repaint=false;
					} 
				}
				else {
					if(refresh) {
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						refresh=false;
						idx=5;
						repaint=true;
						bigTwoTable.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						bigTwoTable.printMsg("\n");
					}
					else if(hand.size()!=handsOnTable.get(handsOnTable.size()-1).size()) {
						bigTwoTable.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						bigTwoTable.printMsg("<==Not a legal move!!!\n");
						if(currentIdx>0)
							currentIdx=currentIdx-1;
						else
							currentIdx=3;
						repaint=false;
							
					}
					else if(hand.beats(handsOnTable.get(handsOnTable.size()-1))) {
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						idx=5;
						repaint=true;
						bigTwoTable.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						bigTwoTable.printMsg("\n");
					}
					else {
						bigTwoTable.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						bigTwoTable.printMsg("<==Not a legal move!!!\n");
						if(currentIdx>0)
							currentIdx=currentIdx-1;
						else
							currentIdx=3;
						repaint=false;
					}
				}
				
			}
			else if(hand==null&&handsOnTable.size()==0) {
				print(list,true,false);
				bigTwoTable.printMsg("<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
			}
			else {
				print(list,true,false);
				bigTwoTable.printMsg("<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
			}
			currentIdx+=1;
			if(currentIdx>3) 
				currentIdx=0;
			
			
		}	
		
		
		else {
			if(handsOnTable.size()==0) {
				bigTwoTable.printMsg("<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
			}
			
			else if(idx==5) {
				repaint=true;
				if(currentIdx!=0)
					idx=currentIdx-1;
				else
					idx=3;
				bigTwoTable.printMsg("\n");
			}
			else if(idx==currentIdx){
				bigTwoTable.printMsg("<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
				refresh=true;
			}
			
			else{
				bigTwoTable.printMsg("\n");
				repaint=true;
			}
			if(idx==(currentIdx+1)%4){
				//repaint=true;
				refresh=true;
				
			}
			currentIdx+=1;
			if(currentIdx>3) 
				currentIdx=0;
				
		}
	
		//game ends
		if(endOfGame()) {
			bigTwoTable.printMsg("Game ends");
			for(int i=0;i<4;i++) {
				if(playerList.get(i).getNumOfCards()!=0)
					bigTwoTable.printMsg(playerList.get(i).getName()+" has "+playerList.get(i).getNumOfCards()+" cards in hand.\n");
				else
					bigTwoTable.printMsg(playerList.get(i).getName()+" wins the game.\n");
			}
			bigTwoTable.disable();
			if(currentIdx>0)
				currentIdx=currentIdx-1;
			else
				currentIdx=3;
		}
		bigTwoTable.setActivePlayer(currentIdx);
		//repaint
		if(repaint&&!endOfGame())
			bigTwoTable.printMsg("Player "+currentIdx+"'s turn:\n");
		
	}
	
	
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean endOfGame() {
		for(int i=0;i<getNumOfPlayers();i++) {
			if(playerList.get(i).getNumOfCards()==0) {
				return true;
			}
		}
		return false;
	}
	
	private void print(CardList hand,boolean printFront, boolean printIndex) {
		ArrayList<Card> cards= new ArrayList<Card>();
		
		for(int i=0;i<hand.size();i++)
			cards.add(hand.getCard(i));
		for (int i = 0; i < cards.size(); i++) {
			String string = "";
			if (printIndex) {
				string = i + " ";
			}
			if (printFront) {
				string = string + "[" + cards.get(i) + "]";
			} else {
				string = string + "[  ]";
			}
			if (i % 13 != 0) {
				string = " " + string;
			}
			bigTwoTable.printMsg(string);
			if (i % 13 == 12 || i == cards.size() - 1) {
				bigTwoTable.printMsg("");
			}
		}
	
	}
	
	/**
	 * Creates an instance of the BigTwo class and calls the start method
	 * @param args
	 * 		The arguments passed to main. Not being used in the application
	 */
/*	public static void main(String[] args) {
		BigTwo bigTwo= new BigTwo();
		bigTwo.getDeck().initialize();
		bigTwo.getDeck().shuffle();
		bigTwo.start((BigTwoDeck)bigTwo.getDeck());
	}*/
	/**
	 * This is a static method to return the object of the type of hand 
	 * @param player
	 * 		The active player that plays the hand
	 * @param cards
	 * 		The hand played by player
	 * @return
	 * 		Object of type of hand played
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		
		Single new5= new Single(player, cards);
		Pair new6=new Pair(player, cards);
		Triple new7=new Triple(player, cards);
		Quad new0=new Quad(player, cards);
		Straight new1=new Straight(player, cards);
		Flush new2=new Flush(player, cards);
		FullHouse new3=new FullHouse(player, cards);
		StraightFlush new4=new StraightFlush(player, cards);
		if(new0.isValid())
			{return new0;}
		else if(new1.isValid())
			{return new1;}
		else if(new2.isValid())
			{return new2;}
		else if(new3.isValid())
			{return new3;}	
		else if(new4.isValid()) 
			{return new4;}
		else if(new5.isValid()) 
			{return new5;}
		else if(new6.isValid()) 
			{return new6;}
		else if(new7.isValid()) 
			{return new7;}
		else return null;
	}
	
}
