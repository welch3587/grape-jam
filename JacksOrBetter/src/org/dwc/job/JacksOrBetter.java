package org.dwc.job;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.dwc.job.cards.*;

/**
 *
 * @author Dave Welch
 */
public class JacksOrBetter implements Serializable {
	private static final long serialVersionUID = 5993464;
    private PokerDeck deck = new PokerDeck();
    private PokerCard[] cards = new PokerCard[5];
    private Boolean[] holdFlag = {false, false, false, false, false};
    private DrawPoker game = new DrawPoker();
    private int bet=1, gamePhase=0;
    private int credits = 1000;
    private int payout=0;
    private Map<String, Integer> basePayout = new HashMap<String, Integer>();
    private static int BET_PHASE = 0;
    private static int DRAW_PHASE = 1;
    private static int FINISH_PHASE = 2;
    
    //3 phases to the game: Bet (before Deal, =0), Draw (After Deal, =1), Finish (After Draw, =2)
    //Bet is the only point where you can alter the bet you have made.
    //Once the Deal button is pressed, the bet is locked and the choice is
    //what cards will be kept. The Deal button becomes the Draw button.
    //During the Draw phase, the only choices are which cards to keep.
    //Finish will take care of resolving bets with sound and graphics. 
    //Once the flashy stuff is done, it will return the status to Bet
    
    public static void main(String[] args) {
        new JacksOrBetter();
    }
    
    public JacksOrBetter() {
        //Initialize Game
    	resetCards();
        game.newHand(cards);
        basePayout.put("JACKS OR BETTER", 1);
        basePayout.put("2 PAIR", 2);
        basePayout.put("3 OF A KIND", 3);
        basePayout.put("STRAIGHT", 4);
        basePayout.put("FLUSH", 6);
        basePayout.put("FULL HOUSE", 9);
        basePayout.put("4 OF A KIND", 25);
        basePayout.put("STRAIGHT FLUSH", 50);
        basePayout.put("ROYAL FLUSH", 250);
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
    
    //Returns what the action button text should be for each phase
    public synchronized String actionButtonText() {
    	switch(gamePhase) {
			case 0: return "Deal";
			case 1: return "Draw";
			case 2: return "New Hand";
			default: return "Default!?";
    	}
    }
    
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
    
    //No cards set. The player bets then hit's the DEAL button and we go to the draw phase
    public synchronized void goToBetPhase() {
    	gamePhase = BET_PHASE;
    	payout = 0;
    	resetCards();
    }
    
    //Player has hit the DEAL button, now we change the phase and draw the cards
    public synchronized void goToDrawPhase() {
    	gamePhase = DRAW_PHASE;
    	credits -= bet;
    	dealCards();
    	listCards();
    }
    
    public synchronized void goToFinishPhase() {
    	gamePhase = FINISH_PHASE;
    	drawCards();
    	listCards();
    	calculatePayout();
    	credits += payout; 
    }
    
    private void calculatePayout() {
    	String handRankName = this.getHandRankName();
    	if(basePayout.containsKey(handRankName)) {
    		payout = basePayout.get(handRankName) * this.bet;
    	} else {
    		payout = 0;
    	}
	}

	public synchronized int getGamePhase() {
    	return gamePhase;
    }
    
    public synchronized Boolean isBetPhase() {
    	return gamePhase == BET_PHASE;
    }
    
    public synchronized Boolean isDrawPhase() {
    	return gamePhase == DRAW_PHASE;
    }

    public synchronized Boolean isFinishPhase() {
    	return gamePhase == FINISH_PHASE;
    }

    public synchronized void resetCards() {
    	deck.resetDeck();
        for(int i=0; i<5; i++) {
            cards[i] = new PokerCard();
            holdFlag[i] = false;
        }
    }
    
    public synchronized void dealCards() {
        for(int i=0; i<5; i++) {
            cards[i] = deck.dealCard();
         }
    }

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

    public synchronized void setBet(int betAmt) {
    	this.bet = betAmt;
    }
    
    public synchronized int getBet() {
    	return bet;
    }
    
    public synchronized void setHoldFlag(int cardNum) {
    	holdFlag[cardNum-1] = true;
    }
    
    public synchronized Boolean getHoldFlag(int cardNum) {
    	return holdFlag[cardNum-1];
    }
    
    public synchronized int getHandRank() {
    	return game.getHandRank(cards);
    }
    
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

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}
	
	private void listCards() {
		System.out.println("Phase " + String.valueOf(gamePhase));
        for(int i=0; i<5; i++) {
            System.out.println("Card " + String.valueOf(i) + " is " + getCardImageName(i+1));
         }
	}

	public int getPayout() {
		return payout;
	}

	public void setPayout(int payout) {
		this.payout = payout;
	}
	
	public String getResultText() {
		if(payout>0) {
			return "Winner!";
		} else {
			return "Better Luck Next Time!";
		}
	}
    
}
