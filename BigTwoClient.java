import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BigTwoClient implements CardGame, NetworkGame{
	
	public BigTwoClient(){
		playerName=JOptionPane.showInputDialog(playerName);
		//System.out.println(playerName);
		deck= new BigTwoDeck();
		playerList = new ArrayList<CardGamePlayer>();
		for(int i=0;i<4;i++) {
			CardGamePlayer player =new CardGamePlayer();
			player.setName("player"+i);
			playerList.add(player);
		}
		handsOnTable=new ArrayList<Hand>();
		
		
		table=new BigTwoTable(this);
		makeConnection();
		table.disable();
	}
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	private ObjectInputStream is;
	
	//private int ct=0;
	//private int localID;
	
	
	//self declared global variables
		private int idx=5;
		private boolean refresh=false,repaint=true;
	
		/**
		 * {@inheritDoc}
		 */
		@Override
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	
	/**
	 * To return index of the last player who played a hand
	 * @return
	 * 		player index of the last player to play a hand 
	 */
	public int getidx() {
		return idx;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCurrentIdx() {
		if(playerID==currentIdx) {
			//System.out.println(playerID+" "+currentIdx);
			table.enable();
		}
		else
			table.disable();
		return currentIdx;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start (Deck deck) {
		idx=5;
		table.enable();
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
				
				currentIdx=i;
				
				//playerID=currentIdx;
				
			}
		}
		table.setActivePlayer(playerID);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage message= new CardGameMessage(6,playerID,cardIdx);
		sendMessage(message);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		CardList list=playerList.get(playerID).play(cardIdx);
		
		Card card=new Card(0,2);
		
		
		if(list!=null) {
			Hand hand=composeHand(playerList.get(currentIdx),list);
			if(hand!=null) {
				
				if(handsOnTable.size()==0){
					if(hand.contains(card)){
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						repaint=true;
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else{
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("<==Not a legal move!!!\n");
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
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else if(hand.size()!=handsOnTable.get(handsOnTable.size()-1).size()) {
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("<==Not a legal move!!!\n");
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
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else {
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("<==Not a legal move!!!\n");
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
				table.printMsg("<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
			}
			else {
				print(list,true,false);
				table.printMsg("<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					playerID=3;
				repaint=false;
			}
			currentIdx+=1;
			if(currentIdx>3) 
				currentIdx=0;
			
			
		}	
		
		
		else {
			if(handsOnTable.size()==0) {
				table.printMsg("{Pass}<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
			}
			
			else if(idx==5) {
				repaint=true;
				if(playerID!=0)
					idx=currentIdx-1;
				else
					idx=3;
				table.printMsg("{Pass}\n");
			}
			else if(idx==playerID){
				table.printMsg("{Pass}<==Not a legal move!!!\n");
				if(currentIdx>0)
					currentIdx=currentIdx-1;
				else
					currentIdx=3;
				repaint=false;
				refresh=true;
			}
			
			else{
				table.printMsg("{Pass}\n");
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
			
			
			String msg="\nGame ends\n";
			for(int i=0;i<4;i++) {
				if(playerList.get(i).getNumOfCards()==0)
					msg+=playerList.get(i).getName()+" wins the game.\n";
				else
					msg+=playerList.get(i).getName()+" has "+playerList.get(i).getNumOfCards()+" cards\n";
				//System.out.println(msg);
			}
			table.disable();
			
			int input = JOptionPane.showOptionDialog(null, msg, "Game ends", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

			if(input == JOptionPane.OK_OPTION)
			{
				CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(ready);
				
			}
			
			if(currentIdx>0)
				currentIdx=currentIdx-1;
			else
				currentIdx=3;
			
		}
		//table.setActivePlayer(currentIdx);
		//repaint
		
		if(repaint&&!endOfGame())
			table.printMsg(playerList.get(currentIdx).getName()+"'s turn:\n");
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean endOfGame() {
		
		int id=currentIdx;
		if(id>0)
			id--;
		else
			id=3;
			if(playerList.get(id).getCardsInHand().size()==0) {
				//System.out.println("hi");
				return true;
			}
		
		return false;
	}
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPlayerID(){
		return playerID;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID=playerID;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName=playerName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServerIP() {
		return serverIP;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServerIP( String serverIP) {
		this.serverIP=serverIP;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort=serverPort;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public  void makeConnection() {
		
		try {
			
			sock = new Socket("127.0.0.1",2396);
			oos= new ObjectOutputStream(sock.getOutputStream());
			
			
			
			CardGameMessage join = new CardGameMessage(CardGameMessage.JOIN,-1,playerName);
			sendMessage(join);
			CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
			sendMessage(ready);
			
			
			
			
			Runnable inThread = new ServerHandler();
			Thread inputThread = new Thread(inThread);
			inputThread.start();
			
			System.out.println("Network established");
			table.disableConnect();
			
			
		}catch (Exception e) {e.printStackTrace();
		table.printMsg("Error in connecting to the server!\n");
		table.enableConnect();}
		
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void parseMessage(GameMessage message) {
		if(message.getType()==CardGameMessage.PLAYER_LIST) {
			table.disable();
			playerID=message.getPlayerID();
			String names[]=new String[4];
			names=(String[])message.getData();
			for(int i=0;i<4;i++) {
				playerList.get(i).setName(names[i]);
			}
		}
		if(message.getType()==CardGameMessage.JOIN) {
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.repaint();
			table.enableConnect();
		}
		if(message.getType()==CardGameMessage.FULL) {
			table.printMsg("Server full\nCannot join the game");
			quit();
		}
		if(message.getType()==CardGameMessage.QUIT) {
			table.printMsg(playerList.get(message.getPlayerID()).getName()+" ("+message.getData()+") left the game!\n");
			playerList.get(message.getPlayerID()).setName(null);
			
			if(!endOfGame()) {
				table.disable();
				//playerList.remove(message.getPlayerID());
				for(int i=0;i<playerList.size();i++) {
					playerList.get(i).removeAllCards();
				}
				handsOnTable.clear();
				table.clearMsgArea();
				
				CardGameMessage mess= new CardGameMessage(4,-1,null);
				sendMessage(mess);
			}
			//table.enableConnect();
			
			table.repaint();
		}
		if(message.getType()==CardGameMessage.READY) {
			table.printMsg(playerList.get(message.getPlayerID()).getName()+": Ready!\n");
			//System.out.println("ready received");
			table.repaint();
		}
		if(message.getType()==CardGameMessage.START) {
			start((BigTwoDeck)message.getData());
			//System.out.println("start received");
			table.repaint();
		}
		if(message.getType()==CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(),(int[])message.getData());
			table.repaint();
		}
		if(message.getType()==CardGameMessage.MSG) {
			
			table.setmessage((String)message.getData()+"\n");
			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		try {
			
			oos.writeObject(message);
	
		}catch (Exception e) {e.printStackTrace();}
		
	}
	
	
	/**
	 * This inner class is used for receiving incoming messages from a server
	 * @author karan
	 *
	 */
	public class ServerHandler implements Runnable{
		
		/**
		 * Creates and returns an instance of the ServerHandler class.
		 * 
		 * @param clientSocket
		 *            the socket connection to the client
		 */
		public  ServerHandler() {
			
			try {
				is = new ObjectInputStream(sock.getInputStream());
				
				
				
			}catch (Exception ex) {
				//System.out.println("add line here ");
				//table.enableConnect();
				System.out.println("exception at server handler");ex.printStackTrace();table.enableConnect();}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			GameMessage message;
			try {
				
				message =(CardGameMessage)is.readObject();
				while(message!=null) {
				 parseMessage(message);
				 message =(CardGameMessage)is.readObject();
				 table.disableConnect();
				 }
			}catch (Exception e) {table.printMsg("Error in communicating with the server!\n");table.enableConnect();}
			
		}
		
	}
	
	/**
	 * Method to close the socket
	 */
	public void quit() {
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			System.out.println("Error in sleeping before closing the client socket at "
					+ sock.getRemoteSocketAddress());
			ex.printStackTrace();
		}

		// closes the socket
		try {
			//oos.close();
			//is.close();
			oos.flush();
			
			sock.close();
		} catch (Exception ex) {
			System.out.println("Error in closing the client socket at "
					+ sock.getRemoteSocketAddress());
			ex.printStackTrace();
		}
	}
	
	
	
	/**
	 * Creates an instance of the BigTwoClient class 
	 * @param args
	 * 		The arguments passed to main. Not being used in the application
	 */
	public static void main(String[] args) {
		BigTwoClient bigTwo= new BigTwoClient();
		//bigTwo.getDeck().initialize();
		//bigTwo.getDeck().shuffle();
		//bigTwo.start((BigTwoDeck)bigTwo.getDeck());
	}
	
	/**
	 * This is a static method to return the object of the type of hand 
	 * @param player
	 * 		The active player that plays the hand
	 * @param cards
	 * 		The hand played by player
	 * @return
	 * 		Object of type of hand played
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards ) {
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
			table.printMsg(string);
			if (i % 13 == 12 || i == cards.size() - 1) {
				table.printMsg("");
			}
		}
	
	}
	

}
