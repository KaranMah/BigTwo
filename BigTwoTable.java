import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUIfor the Big Two card game and handle all user actions.
 * @author karan
 *
 */
public class BigTwoTable implements CardGameTable{

	/**
	 * Creates the basic GUI and returns the instance of the BigTwoTable class
	 * @param game
	 * 		Object of CardGame interface used to pass instances of classes that implement it
	 */
	public BigTwoTable(CardGame game) {
	this.game=game;	
	frame=new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	bigTwoPanel= new BigTwoPanel();	
	bigTwoPanel.setLayout(new BorderLayout());
	//frame.setLayout(new BorderLayout());
	
	
	//make button
	playButton=new JButton("Play");
	passButton=new JButton("Pass");
	
	//add menu 
	restart= new JMenuItem("Connect");
	quit=new JMenuItem("Quit");
	messageMenu= new JMenuItem("Message");
	//restart.setEnabled(false);
	menu=new JMenu("Game");
	bar=new JMenuBar();
	restart.addActionListener(new ConnectMenuItemListener());
	quit.addActionListener(new QuitMenuItemListener());
	messageMenu.addActionListener(new MessageMenuItemListener());
	menu.add(restart);
	menu.add(quit);
	
	bar.add(menu);
	bar.add(messageMenu);
	
	frame.add(BorderLayout.NORTH,bar);
	
	//playButton.setBounds(80, 200, 70, 30);
//	passButton.setBounds(180, 200, 70, 30);
	
	selected=new boolean[13];
	
	JPanel bottom= new JPanel();
	bottom.setLayout(new BoxLayout(bottom,BoxLayout.X_AXIS));
	
	
	
	JPanel msgbox= new JPanel();
	//msgbox.setLayout();
	
	chatbox=new JTextField("");
	chatbox.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if(e.getKeyCode() == KeyEvent.VK_ENTER){
	               String message; 
	               message=chatbox.getText();
	               CardGameMessage mes=new CardGameMessage(7,game.getCurrentIdx(),message);
	               BigTwoClient btc= (BigTwoClient) game;
	               btc.sendMessage(mes);
	               chatbox.setText("");
	            }
	        }

	    });
	
	JLabel message= new JLabel("Message:");
	chatbox.setVisible(true);
	chatbox.setPreferredSize(new Dimension(300,20));
	msgbox.add( message);
	msgbox.add( chatbox);
	//msgbox.setSize(400, 50);
	
	//add buttons
	JPanel buttons= new JPanel();
	buttons.setLayout(new FlowLayout());
	
	buttons.add(playButton);
	buttons.add(passButton);
	playButton.addActionListener(new PlayButtonListener());
	passButton.addActionListener(new PassButtonListener());
	
	
	bottom.add(buttons);
	bottom.add( msgbox);
	
	
	bigTwoPanel.add(BorderLayout.SOUTH, bottom);

	//msgArea
	JPanel messagePanel = new JPanel();
	messagePanel.setLayout(new BorderLayout());
	msgArea=new JTextArea(" ");
	//msgArea.setEditable(false);
	JScrollPane scroll= new JScrollPane(msgArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	scroll.setPreferredSize(new Dimension(400,585));
	scroll.setVisible(true);
	
	
	sysMessage= new JTextArea("  ");
	//sysMessage.setEditable(false);
	JScrollPane messagescroll= new JScrollPane(sysMessage,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	messagePanel.add(scroll,BorderLayout.NORTH);
	messagePanel.add(messagescroll,BorderLayout.SOUTH);
	messagescroll.setPreferredSize(new Dimension(400,300));
	messagescroll.setVisible(true);
	
	bigTwoPanel.add(BorderLayout.EAST,messagePanel);
	
	//add to frame
		
		frame.add(bigTwoPanel);
		frame.setSize(1400,1000);
		frame.setVisible(true);
		
		
	}
	
	private CardGame game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextField chatbox;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	private JMenuItem restart;
	private JMenuItem quit;
	private JMenuItem messageMenu;
	private JMenu menu;
	private JMenuBar bar;
	private JTextArea sysMessage;
	private int ct=1;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		
		this.activePlayer=activePlayer;
		
		if(activePlayer==game.getCurrentIdx()) {
			System.out.println(activePlayer+" "+game.getCurrentIdx());
			enable();
		}
		else
			disable();
	}
	
	public int getcount() {
		return ct;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] getSelected() {
		
		int ctr=0,i;
		for(i=0;i<game.getPlayerList().get(activePlayer).getCardsInHand().size();i++) {
			if(selected[i]) {	
				ctr++;
			}
		}
		int sel[]=new int[ctr];
		ctr=0;
		for(i=0;i<game.getPlayerList().get(activePlayer).getCardsInHand().size();i++) {
			if(selected[i])
			{
				sel[ctr]=i;
				ctr++;
			}
		}
		return sel;
	}
	
	/**
	 * Method to add text message to chat area 
	 * @param msg
	 * 			message to be added to the text area
	 */
	public void setmessage(String msg) {
		sysMessage.append(msg);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetSelected() {
		for(int i=0;i<13;i++) {
			selected[i]=false;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void repaint() {
		
		bigTwoPanel.repaint();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		this.resetSelected();
		this.clearMsgArea();
		this.enable();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * Method to enable connect menu button
	 */
	public void enableConnect() {
		
		
			restart.setEnabled(true);
	}
	/**
	 * Method to disable connect menu button
	 */
	public void disableConnect() {
		
		
			restart.setEnabled(false);
	}
	
	/**
	 * Inner class extends JPanel and implements MouseListener
	 * The listener interface of BigTwoPanel for receiving action events
	 * Overrides paintComponent and MouseListener methods
	 * @author karan
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		private static final long serialVersionUID = 1L;
		
		/**
		 * Creates and returns instance of BigTwoPanel
		 * Adds MouseListener to the object of BigTwoPanel
		 */
		public BigTwoPanel()
		{	

			this.addMouseListener(this);
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void paintComponent(Graphics g) {
			try {
			super.paintComponent(g);
			BigTwoClient big =(BigTwoClient)game;
			//int playerID =big.getLocal();
			
			this.setBackground(Color.green.darker().darker());
			
			//adding images
			char suits[]= {'d','c','h','s'};
			char rank[]={ 'a', '2', '3', '4', '5', '6', '7',
					'8', '9', 't', 'j', 'q', 'k' };
			cardImages= new Image[4][13];
			for(int i=0;i<4;i++) {
				for(int j=0;j<13;j++) {
					String x="src/cards/"+rank[j]+suits[i]+".gif";
					cardImages[i][j]=new ImageIcon(x).getImage();
				}
			}
			cardBackImage= new ImageIcon("src/cards/b.gif").getImage();
			
			
			//adding avatars
			avatars= new Image[4];
			avatars[0]= new ImageIcon("src/003-Character/png/128/batman_128.png").getImage();
			avatars[1]= new ImageIcon("src/003-Character/png/128/flash_128.png").getImage();
			avatars[2]= new ImageIcon("src/003-Character/png/128/green_lantern_128.png").getImage();
			avatars[3]= new ImageIcon("src/003-Character/png/128/superman_128.png").getImage();
			
			
			
			Dimension d = this.getSize();
	         //draw in black
	         g.setColor(Color.BLACK);
	         //draw a centered horizontal line
	         g.drawLine(0,370,d.width,370);
	         g.drawLine(0,190,d.width,190);
	         g.drawLine(0,550,d.width,550);
	         g.drawLine(0,730,d.width,730);
	         
	         
	         for(int i=0;i<4;i++) {
	        	 if(game.getPlayerList().get(i).getName()!=null) {
	        	 if(i==game.getCurrentIdx()) {
	        		 g.setColor(Color.BLUE);
	        		 g.drawString(game.getPlayerList().get(i).getName(), 10, i*180+35);
	        		 
	        		 g.drawImage(avatars[i], 10, (i*180)+40, this);
	        		 CardList list= new CardList();
	        		 list=game.getPlayerList().get(i).getCardsInHand();
	        		 if(i==activePlayer){
	        			 for(int j=0;j<list.size();j++) {
	        			 	int r= list.getCard(j).getRank();
	        			 	int s= list.getCard(j).getSuit();
	        			 	if(!selected[j])
	        				 	g.drawImage(cardImages[s][r], 140+j*40, (i*180)+40, this);
	        			 	else
	        			 		g.drawImage(cardImages[s][r], 140+j*40, (i*180)+20, this);
	        		 	}
	        		 }
	        		 else
	        			 for(int j=0;j<list.size();j++) {
	        			 g.drawImage(cardBackImage, 140+j*40, (i*180)+40, this);
	        			 }
	        		 
	        	 }
	        	 else
	        	 {
	        		 g.setColor(Color.BLACK);
	        		 g.drawString(game.getPlayerList().get(i).getName(), 10, i*180+35);
	        		 g.drawImage(avatars[i], 10, (i*180)+40, this);
	        		 CardList list= new CardList();
	        		 list=game.getPlayerList().get(i).getCardsInHand();
	        		 if(i==activePlayer){
	        			 for(int j=0;j<list.size();j++) {
	        			 	int r= list.getCard(j).getRank();
	        			 	int s= list.getCard(j).getSuit();
	        			 	if(!selected[j])
	        				 	g.drawImage(cardImages[s][r], 140+j*40, (i*180)+40, this);
	        			 	else
	        			 		g.drawImage(cardImages[s][r], 140+j*40, (i*180)+20, this);
	        		 	}
	        		 }
	        		 else
	        			 for(int j=0;j<list.size();j++) {
	        			 g.drawImage(cardBackImage, 140+j*40, (i*180)+40, this);
	        			 }
	        	 }
	         }
	         }
	         
	         if(game.getHandsOnTable().size()!=0) {
	        	 g.setColor(Color.BLACK);
	        	 int a=big.getidx(); 
	        	 
	        	 if(a==5&&activePlayer!=0) a=activePlayer-1;
	        	 else if(a==5&&activePlayer==0) a=3;
	        	 g.drawString("Played by"+game.getPlayerList().get(a).getName(), 10, 4*180+35);
	        	 for(int j=0;j<game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size();j++) {
        			 int r= game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(j).getRank();
        			 int s= game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(j).getSuit();
        			 g.drawImage(cardImages[s][r], 140+j*40, (4*180)+40, this);
        			 
	        	 }
	         }
			}catch (Exception e) {;}
			
		
	         //enableConnect();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			int x=e.getX();
			int y=e.getY(); 
			int i;
			
			if(!game.endOfGame()) {
			for(i=0;i<game.getPlayerList().get(activePlayer).getCardsInHand().size()-1;i++)
			{
				if(!selected[i]&&x>=140+i*40&&x<140+(i+1)*40&&y>=(activePlayer*180)+40&&y<=(activePlayer*180)+40+95) {
					selected[i]=true;
				}
				else if(selected[i]&&x>=140+i*40&&x<140+(i+1)*40&&y>=(activePlayer*180)+20&&y<=(activePlayer*180)+20+95) {
					selected[i]=false;
				}
				else if(selected[i]&&!selected[i+1]&&(x>=180+i*40&&x<210+(i)*40)&&y>=(activePlayer*180)+20&&y<=(activePlayer*180)+40) {
					selected[i]=false;
				}
				else if(selected[i]&&!selected[i+1]&&(x>=180+i*40&&x<210+(i)*40)&&y>=(activePlayer*180)+20&&y<=(activePlayer*180)+40) {
					selected[i]=false;
				}
				else if(!selected[i]&&selected[i+1]&&(x>=180+i*40&&x<210+(i)*40)&&y>=(activePlayer*180)+120&&y<=(activePlayer*180)+135) {
					selected[i]=true;
				}
				
			}
			
			
			int size=game.getPlayerList().get(activePlayer).getCardsInHand().size()-1;
			
			if(!selected[size]&&x>=140+(size)*40&&x<210+size*40&&y>=(activePlayer*180)+40&&y<=(activePlayer*180)+40+95)
				selected[size]=true;
			else if(selected[size]&&x>=140+size*40&&x<210+size*40&&y>=(activePlayer*180)+20&&y<=(activePlayer*180)+20+95)
				selected[size]=false;
			
			
			repaint();	
			}	
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mousePressed(MouseEvent e) {     
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
		      
	    }

		/**
		 * {@inheritDoc}
		 */
		@Override
	    public void mouseEntered(MouseEvent e) {
	    	
	    }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void mouseExited(MouseEvent e) {
		    
	    }
		
		
	}
	/**
	 * The listener interface of Play Button for receiving action events. 
	 * @author karan
	 *
	 */
	class PlayButtonListener implements ActionListener {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			
			BigTwoClient bigTwo=(BigTwoClient) game;
			CardList list=bigTwo.getPlayerList().get(activePlayer).play(getSelected());
			if(list!=null) {
				bigTwo.makeMove(activePlayer,getSelected());
			}
			else
				printMsg("<==Need to select cards!!!\n");
			resetSelected();
			System.out.println(bigTwo.getidx());
			repaint();
		}
	}
	/**
	 *The listener interface of Pass Button for receiving action events
	 * @author karan
	 *
	 */
	class PassButtonListener implements ActionListener {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			
			BigTwoClient bigTwo=(BigTwoClient) game;
			//printMsg("{Pass}");
			bigTwo.makeMove(activePlayer, null);
			resetSelected();
			repaint();
		}
	}
	
	
	/**
	 * The listener interface of Connect Menu Item for receiving action events
	 * @author karan
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			BigTwoClient client = (BigTwoClient)game;
			//BigTwoClient client = new BigTwoClient();
			//ct++;
			client.makeConnection();
			//client.makeConnection();
			repaint();
		}
	}
	/**
	 * The listener interface of Quit Menu Item for receiving action events
	 * 
	 * @author karan
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			BigTwoClient client = (BigTwoClient)game;
			//CardGameMessage message= new CardGameMessage(CardGameMessage.QUIT,-1,null);
			//client.sendMessage(message);
			client.quit();
			
			System.exit(0);
		}
	}
	class MessageMenuItemListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event) {
		chatbox.setText("");
		}
	}
}
