
/**
 * The class models a deck in the BigTwo game
 * @author karan
 *
 */
public class BigTwoDeck extends Deck{
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see Deck#initialize()
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}
}
