package org.dwc.job;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dwc.job.cards.*;



/**
 * This class is the repository for the Draw Poker rules and calculations
 * that I will need for the Jacks or Better game.
 * 
 * I considered putting this code into the PokerDeck class, but since there
 * are a number of different games that you can play with a poker deck, I 
 * decided to separate the game rules and calculations from the cards in use.
 * 
 * If I ever create an actual poker game, I will need to modify this to 
 * handle multiple hands. As it is, all I really need this class to do in this
 * context is calculate the hand value for the player and then generate
 * a value for a hand that contains a pair of Jacks with three 2s. This 
 * represents the lowest value hand that contains a pair of jacks.
 * 
 * @author Dave Welch
 */
public class DrawPoker implements Serializable {
	private static final long serialVersionUID = 6958464;
    private PokerCard[] cards = new PokerCard[5];
    private int[] values = new int[4888];
    private int[] products = new int[4888];
    private int[] unique5 = new int[7937];
    private int[] flushes = new int[7937];
    private int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };
    
    public DrawPoker() {
        initArrays();
    }
    
    public DrawPoker(PokerCard[] inCards) {
        cards = inCards;
        initArrays();
    }
    
    public synchronized void newHand(PokerCard[] inCards) {
        cards = inCards;
    }
    
    public synchronized int getJacksOrBetterValue() {
        //I'm hard coding this because it will always be 
        //the same value
        return 4205;
    }
    
    public synchronized int getHandRank(PokerCard[] inCards) {
        //LogWriter outLog = new LogWriter("./job.log");
    	cards = inCards;
        
        int s, q, c1, c2, c3, c4, c5;
        c1 = cards[0].getValue();
        //outLog.writeLine("Initial Value of c1:"+String.valueOf(c1));
        c2 = cards[1].getValue();
        //outLog.writeLine("Initial Value of c2:"+String.valueOf(c2));
        c3 = cards[2].getValue();
        //outLog.writeLine("Initial Value of c3:"+String.valueOf(c3));
        c4 = cards[3].getValue();
        //outLog.writeLine("Initial Value of c4:"+String.valueOf(c4));
        c5 = cards[4].getValue();
        //outLog.writeLine("Initial Value of c5:"+String.valueOf(c5));

        q = (c1|c2|c3|c4|c5) >> 16;
        //outLog.writeLine("Initial Value of q:"+String.valueOf(q));

        /* check for Flushes and StraightFlushes
        */
        s =  (c1 & c2 & c3 & c4 & c5 & 0xF000);
        //outLog.writeLine("Value of s in Check Flushes:"+String.valueOf(s));
        if (s > 0) return(flushes[q]);

        /* check for Straights and HighCard hands
        */
        s = unique5[q];
        //outLog.writeLine("Value of s in Straights and High Cards:"+String.valueOf(s));
        if ( s > 0 )  return ( s );

        /* let's do it the hard way
        */
        q = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
        //outLog.writeLine("Value of c1 & 0xFF:"+String.valueOf(c1 & 0xFF));
        //outLog.writeLine("Value of c2 & 0xFF:"+String.valueOf(c2 & 0xFF));
        //outLog.writeLine("Value of c3 & 0xFF:"+String.valueOf(c3 & 0xFF));
        //outLog.writeLine("Value of c4 & 0xFF:"+String.valueOf(c4 & 0xFF));
        //outLog.writeLine("Value of c5 & 0xFF:"+String.valueOf(c5 & 0xFF));
        
        //outLog.writeLine("Value of q in doing it the hard way:"+String.valueOf(q));
        //outLog.closeLog();
        q = findit( q );

        return( values[q] );        
    }
    
    // perform a binary search on a pre-sorted array
    //
    private synchronized int findit( int key ) {
        int low = 0, high = 4887, mid;

        while ( low <= high )
        {
            mid = (high+low)/2;      // divide by two
            if ( key < products[mid] )
                high = mid - 1;
            else if ( key > products[mid] )
                low = mid + 1;
            else
                return( mid );
        }
        //fprintf( stderr, "ERROR:  no match found; key = %d\n", key );
        return( -1 );
    }

    public synchronized String getHandRankName(int handRank) {
        if (handRank > 6185) return("HIGH CARD");        // 1277 high card
        if (handRank > 3325) return("ONE PAIR");         // 2860 one pair
        if (handRank > 2467) return("2 PAIR");         //  858 two pair
        if (handRank > 1609) return("3 OF A KIND");  //  858 three-kind
        if (handRank > 1599) return("STRAIGHT");         //   10 straights
        if (handRank > 322)  return("FLUSH");            // 1277 flushes
        if (handRank > 166)  return("FULL HOUSE");       //  156 full house
        if (handRank > 10)   return("4 OF A KIND");   //  156 four-kind
        return("STRAIGHT FLUSH");                   //   10 straight-flushes
    }
    
    public synchronized int getHandValue() {
        int handValue = (cards[0].getPrime()*cards[1].getPrime()*cards[2].getPrime()*cards[3].getPrime()*cards[4].getPrime());
        int flushCheck = (cards[0].getValue() & cards[1].getValue() & cards[2].getValue() & cards[3].getValue() & cards[4].getValue() & 0xF000);
        return handValue;
    }

    public synchronized void initArrays() {
        /* When I originally wrote this, I had a terrible time trying to populate the arrays because
         * the resulting .java file size was too large. So I put the array values into CSV files and
         * read them into the array.
         * Now that I'm re-writing this to operate as a JSP solution, I'm finding that populating
         * the arrays from the files is also proving to be a pain. So, I'm creating a static
         * class for each of the arrays that does nothing more than populate the array.
         * At the very least, I should never have to solve this problem again...
         */
        flushes = Flushes.flushes;
        products = Products.products;
        unique5 = Unique5.unique5;
        values = Values.values;
    	
        //Flushes
        /*
        BufferedReader br = null;
        List<String> inString;
        int countVar = 0;
        String sCurrentLine;
        
        try {
            br = new BufferedReader(new FileReader("./flushes.dat"));
            while ((sCurrentLine = br.readLine()) != null) {
                inString = Arrays.asList(sCurrentLine.split(","));
                for(String eaString : inString) {
                    flushes[countVar] = Integer.parseInt(eaString.trim());
                    countVar++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Products
        countVar = 0;
        try {
            br = new BufferedReader(new FileReader("./WebContent/data/products.dat"));
            while ((sCurrentLine = br.readLine()) != null) {
                inString = Arrays.asList(sCurrentLine.split(","));
                for(String eaString : inString) {
                    products[countVar] = Integer.parseInt(eaString.trim());
                    countVar++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Unique5
        countVar = 0;
        try {
            br = new BufferedReader(new FileReader("./WebContent/data/unique5.dat"));
            while ((sCurrentLine = br.readLine()) != null) {
                inString = Arrays.asList(sCurrentLine.split(","));
                for(String eaString : inString) {
                    unique5[countVar] = Integer.parseInt(eaString.trim());
                    countVar++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Values
        countVar = 0;
        try {
            br = new BufferedReader(new FileReader("./WebContent/data/values.dat"));
            while ((sCurrentLine = br.readLine()) != null) {
                inString = Arrays.asList(sCurrentLine.split(","));
                for(String eaString : inString) {
                    values[countVar] = Integer.parseInt(eaString.trim());
                    countVar++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        */
        
  /*      int perm7[21][5] = {
  { 0, 1, 2, 3, 4 },
  { 0, 1, 2, 3, 5 },
  { 0, 1, 2, 3, 6 },
  { 0, 1, 2, 4, 5 },
  { 0, 1, 2, 4, 6 },
  { 0, 1, 2, 5, 6 },
  { 0, 1, 3, 4, 5 },
  { 0, 1, 3, 4, 6 },
  { 0, 1, 3, 5, 6 },
  { 0, 1, 4, 5, 6 },
  { 0, 2, 3, 4, 5 },
  { 0, 2, 3, 4, 6 },
  { 0, 2, 3, 5, 6 },
  { 0, 2, 4, 5, 6 },
  { 0, 3, 4, 5, 6 },
  { 1, 2, 3, 4, 5 },
  { 1, 2, 3, 4, 6 },
  { 1, 2, 3, 5, 6 },
  { 1, 2, 4, 5, 6 },
  { 1, 3, 4, 5, 6 },
  { 2, 3, 4, 5, 6 }
}*/
    }
/*    hand_rank( short val )
{
    if (val > 6185) return(HIGH_CARD);        // 1277 high card
    if (val > 3325) return(ONE_PAIR);         // 2860 one pair
    if (val > 2467) return(TWO_PAIR);         //  858 two pair
    if (val > 1609) return(THREE_OF_A_KIND);  //  858 three-kind
    if (val > 1599) return(STRAIGHT);         //   10 straights
    if (val > 322)  return(FLUSH);            // 1277 flushes
    if (val > 166)  return(FULL_HOUSE);       //  156 full house
    if (val > 10)   return(FOUR_OF_A_KIND);   //  156 four-kind
    return(STRAIGHT_FLUSH);                   //   10 straight-flushes
}*/
}
