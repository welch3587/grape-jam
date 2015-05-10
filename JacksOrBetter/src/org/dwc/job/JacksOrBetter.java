package org.dwc.job;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.dwc.job.cards.*;

/**
 * This class manages the game. It keeps track of which phase of the game we're in
 * and the cards in play. It will also manage the number of credits and how
 * much is being bet.
 * 
 * There are 3 phases to the game: Bet (before Deal, =0), Draw (After Deal, =1), Finish (After Draw, =2)
 * Bet is the only point where you can alter the bet you have made.
 * Once the Deal button is pressed, the bet is locked and the choice is
 * what cards will be kept. The Deal button becomes the Draw button.
 * During the Draw phase, the only choices are which cards to keep.
 * Finish will take care of resolving bets with sound and graphics. 
 * Once the flashy stuff is done, it will return the status to Bet
 * 
 * @author Dave Welch
 */
public class JacksOrBetter implements Serializable {
	private static final long serialVersionUID = 5993464;
    private PokerDeck deck = new PokerDeck();
    private PokerCard[] cards = new PokerCard[5];
    private Boolean[] holdFlag = {false, false, false, false, false};
    private DrawPoker game = new DrawPoker();
    private PayoutTable _PoT = new PayoutTable();
    private int bet=1, gamePhase=0;
    private int credits = 1000;
    private int payout=0;
    private Map<String, Integer> basePayout = new HashMap<String, Integer>();
    private static int BET_PHASE = 0;
    private static int DRAW_PHASE = 1;
    private static int FINISH_PHASE = 2;
    
    public static void main(String[] args) {
        new JacksOrBetter();
    }
    
    public JacksOrBetter() {
        //Initialize Game
    	resetCards();
        game.newHand(cards);
    }
    
    /**
     * This method calls the PayoutTable's method for presenting the table
     * in HTML format. It will be necessary to provide a table name (id)
     * if we want to have a specific name for CSS purposes. The default name
     * is "PayoutTable"
     */
    public synchronized String getPayoutTableHMTL() {
    	//System.out.println("Payout Table HTML");
    	//System.out.println(_PoT.htmlOutput());
    	return _PoT.htmlOutput();
    }
    
	/**
	 * This method allows you to stack the deck (choose which cards to deal)
	 * so this class can be tested
	 * @param inCards
	 */
    public synchronized void stackDeck(String[] inCards) {
    	int cardCounter = 0;
    	for(String cardStr: inCards) {
    		cards[cardCounter] = deck.dealSpecificCard(cardStr);
    		cardCounter++;
    		if(cardCounter > 4) break; //In case someone (me?) accidentally sends too many cards
    	}
    }
    
    /**
     * Returns what the action button text should be for each phase
     * @return ActionButtonText
     */
    public synchronized String actionButtonText() {
    	switch(gamePhase) {
			case 0: return "Deal";
			case 1: return "Draw";
			case 2: return "New Hand";
			default: return "Default!?";
    	}
    }
    
    /**
     * No matter what the game phase is, this will force the game
     * to proceed to the next phase.
     */
    public synchronized void goToNextPhase() {
    	switch(gamePhase) {
    		case 0: goToDrawPhase();
    			break;
    		case 1: goToFinishPhase();
				break;
    		case 2: goToBetPhase();
				break;
    	}
    }
    
    /**
     * This method will force the game to go to the bet phase.
     * If the game is already in the bet phase, nothing happens
     */
    public synchronized void goToBetPhase() {
    	if(gamePhase != BET_PHASE) {
	    	gamePhase = BET_PHASE;
	    	payout = 0;
	    	resetCards();
    	}
    }
    
    /**
     * This method will force the game to go to the draw phase.
     * If the game is already in the draw phase, nothing happens
     */
    public synchronized void goToDrawPhase() {
    	if(gamePhase != DRAW_PHASE) {
	    	gamePhase = DRAW_PHASE;
	    	credits -= bet;
	    	dealCards();
	    	listCards();
    	}
    }
    
    /**
     * This method will force the game to go to the finish phase.
     * If the game is already in the finish phase, nothing happens
     */
    public synchronized void goToFinishPhase() {
    	if(gamePhase != FINISH_PHASE) {
	    	gamePhase = FINISH_PHASE;
	    	drawCards();
	    	listCards();
	    	calculatePayout();
	    	credits += payout; 
    	}
    }
    
    /**
     * Simple calculation of bet * the payout amount based on the 
     * players hand.
     */
    private void calculatePayout() {
    	payout = _PoT.getPayoutForHandRank(this.getHandRankName()) * this.bet;
	}

    /**
     * returns the number corresponding to the phase of the game
     * that we are currently in (0, 1 or 2)
     * @return gamePhase
     */
    public synchronized int getGamePhase() {
    	return gamePhase;
    }
    
    /**
     * Returns true if the current phase is the bet phase
     * @return boolean
     */
    public synchronized Boolean isBetPhase() {
    	return gamePhase == BET_PHASE;
    }
    
    /**
     * Returns true if the current phase is the draw phase
     * @return boolean
     */
    public synchronized Boolean isDrawPhase() {
    	return gamePhase == DRAW_PHASE;
    }

    /**
     * Returns true if the current phase is the finish phase
     * @return boolean
     */
    public synchronized Boolean isFinishPhase() {
    	return gamePhase == FINISH_PHASE;
    }

    /** 
     * This will reset and reshuffle the deck
     * and reset the cards. It will also reset
     * the hold flag for all cards to false.
     * Note, the reset cards are not "dealt"
     * so they will cause problems if the 
     * dealCards method is not executed before
     * playing further.
     */
    public synchronized void resetCards() {
    	deck.resetDeck();
        for(int i=0; i<5; i++) {
            cards[i] = new PokerCard();
            holdFlag[i] = false;
        }
    }
    
    /**
     * This method deals 5 cards from the deck.
     */
    public synchronized void dealCards() {
        for(int i=0; i<5; i++) {
            cards[i] = deck.dealCard();
         }
    }

    /**
     * This method cycles through the 5 cards and when it
     * determines that the hold flag for a card is false,
     * it will discard that card, deal a new card and set
     * the hold flag to false. I also have some output in 
     * here for debugging purposes.
     */
    public synchronized void drawCards() {
        for(int i=0; i<5; i++) {
        	if(!holdFlag[i]) {
        		deck.discardCard(cards[i]);
        		cards[i] = deck.dealCard();
        		holdFlag[i] = false;
        		System.out.println("Replacing card " + String.valueOf(i));
        	} else {
        		System.out.println("Card " + String.valueOf(i) + " was held!");
        	}
         }
    }

    /**
     * This sets the bet variable to the betAmt passed as a parameter
     * @param betAmt
     */
    public synchronized void setBet(int betAmt) {
    	if(betAmt < 1) betAmt = 1; //Allow no monkey business with the bet amount!
    	this.bet = betAmt;
    }
    
    /** 
     * Returns an integer corresponding to the current bet amount
     * @return int
     */
    public synchronized int getBet() {
    	return bet;
    }
    
    /**
     * Sets the hold flag for the card corresponding to the parameter to true
     * @param cardNum
     */
    public synchronized void setHoldFlag(int cardNum) {
    	holdFlag[cardNum-1] = true;
    }
    
    /**
     * Sets the hold flag for the card corresponding to the parameter to false
     * @param cardNum
     */
    public synchronized void resetHoldFlag(int cardNum) {
    	holdFlag[cardNum-1] = false;
    }

    /**
     * This method returns the boolean value corresponding to the hold flag
     * associated with the card number supplied as a parameter
     * @param cardNum
     * @return boolean
     */
    public synchronized Boolean getHoldFlag(int cardNum) {
    	return holdFlag[cardNum-1];
    }
    
    /**
     * returns an integer corresponding to the rank of the hand currently in play
     * For more information on how this number is arrived at, please visit
     * http://www.suffecool.net/poker/evaluator.html.
     * @return integer
     */
    public synchronized int getHandRank() {
    	return game.getHandRank(cards);
    }
    
    /**
     * This method returns the string value of the hand rank name corresponding
     * to the hand rank integer value. Most of the hand rank names are set in
     * the DrawPoker.java class, but I check for a few non-standard ranks
     * here that are specific to this game.
     * @return String
     */
    public synchronized String getHandRankName() {
    	//Since Draw Poker doesn't care about distinguishing
    	//a pair of jacks from any other pair, we'll look for 
    	//that scenario here.
    	//We'll also look for a Royal Flush scenario (rank 1)
    	//because Draw Poker doesn't distinguish that from 
    	//any other straight flush
    	String handRankName = game.getHandRankName(getHandRank());
    	if(handRankName.equalsIgnoreCase("ONE PAIR")) {
    		if(this.getHandRank() < 4206) {
    			return "JACKS OR BETTER";
    		}
    	} else if(handRankName.equalsIgnoreCase("STRAIGHT FLUSH")) {
    		if(this.getHandRank() == 1) {
    			return "ROYAL FLUSH";
    		}
    	}
    	return handRankName;
    }
    
    /**
     * Returns a string value for the file name of the card number
     * supplied as an input parameter. IMPORTANT: If you change the 
     * set of card graphics in use, you must also change the calculation
     * of getCardGraphicName in the PokerCard.java class.
     * @param int cardNum
     * @return String card graphic file name
     */
    public synchronized String getCardImageName(int cardNum) {
    	//So we can keep the same indexing in the JSP
    	//I am going to subtract 1 from the cardNum.
    	//This means that card number 1 in the JSP page
    	//corresponds to card number 0 here.
    	cardNum--;
    	if(cards[cardNum]!=null) {
    		return cards[cardNum].getCardGraphicName();
    	}
    	else {
    		return "Null Card Detected!";
    	}
    	
   	}

    /**
     * returns the int value corresponding to the amount of credits the player
     * currently has.
     * @return int
     */
	public int getCredits() {
		return credits;
	}

	/**
	 * This method sets the number of credits to the amount supplied as a parameter
	 * @param int
	 */
	public void setCredits(int credits) {
		this.credits = credits;
	}
	
	/**
	 * I created this method for debugging purposes. It sends to stdout the current phase of the game
	 * as well as the graphic file name or each card. I was having a problem at one point where cards
	 * were not being handled correctly and this allowed me to narrow down where that was happening.
	 * I will use it as a sanity check when I deal the cards.
	 */
	private void listCards() {
		System.out.println("Phase " + String.valueOf(gamePhase));
        for(int i=0; i<5; i++) {
            System.out.println("Card " + String.valueOf(i) + " is " + getCardImageName(i+1));
         }
	}

	/**
	 * returns the int value for the current amount calculated for the payout
	 * @return int
	 */
	public int getPayout() {
		return payout;
	}

	/**
	 * This method provides one way to set the amount of payout. It is unused at this point
	 * because I set the payout within this class, but it might be useful later. 
	 * @param int
	 */
	public void setPayout(int payout) {
		this.payout = payout;
	}
	
	/**
	 * This method provides feedback on the results in the finish phase. I might try to make
	 * it a little more interesting at a later time.
	 * 
	 * @return String
	 */
	public String getResultText() {
		if(payout>0) {
			return "Winner!";
		} else {
			return "Better Luck Next Time!";
		}
	}
    
}
