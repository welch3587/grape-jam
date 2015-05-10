package org.dwc.job;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * I'm constructing this class to provide a more dynamic way of generating
 * the HTML to display on the Jacks or Better page. Additionally, I want to 
 * experiment with using nested selectors for both css and JQuery.
 * 
 * @author Dave Welch
 *
 */

public class PayoutTable implements Serializable {
	private static final long serialVersionUID = 6002564;
	private int _minBet = 1;
	private int _maxBet = 5;
	private String[] _handRankNames = new String[20]; //more than we need, I hope...
    private Map<String, Integer> _basePayout = new HashMap<String, Integer>();

	
	public PayoutTable(int inMinBet, int inMaxBet, String[] inHandRanks) {
		int countRank = 0;
		_minBet = inMinBet;
		_maxBet = inMaxBet;
		
		for(String rank: inHandRanks) {
			_handRankNames[countRank] = rank;
			countRank++;
		}
	}
	
	public PayoutTable() {
        _handRankNames[8] = "JACKS OR BETTER";
        _handRankNames[7] = "2 PAIR";
        _handRankNames[6] = "3 OF A KIND";
        _handRankNames[5] = "STRAIGHT";
        _handRankNames[4] = "FLUSH";
        _handRankNames[3] = "FULL HOUSE";
        _handRankNames[2] = "4 OF A KIND";
        _handRankNames[1] = "STRAIGHT FLUSH";
        _handRankNames[0] = "ROYAL FLUSH";
        
        _basePayout.put("JACKS OR BETTER", 1);
        _basePayout.put("2 PAIR", 2);
        _basePayout.put("3 OF A KIND", 3);
        _basePayout.put("STRAIGHT", 4);
        _basePayout.put("FLUSH", 6);
        _basePayout.put("FULL HOUSE", 9);
        _basePayout.put("4 OF A KIND", 25);
        _basePayout.put("STRAIGHT FLUSH", 50);
        _basePayout.put("ROYAL FLUSH", 250);

	}
	
	public synchronized void setPayoutForHandRank(String handRank, int payout) {
		_basePayout.put(handRank, payout);
	}
	
	public synchronized int getPayoutForHandRank(String handRank) {
		if(_basePayout.containsKey(handRank)) {
			return _basePayout.get(handRank);
		} else {
			return 0;
		}
	}
	
	public synchronized String htmlOutput() {
		return htmlOutput("PayoutTable");
		
	}
	
	public synchronized String htmlOutput(String tableName) {
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<table id=\"" + tableName +"\" border=1 cellspacing=0 cellpadding=0>");
		htmlBuilder.append(System.getProperty("line.separator"));
		
		//I am going to setup a title row which just shows the bet amounts. We will always have this row
		//After this row, I'll cycle through the handRankNames.
		htmlBuilder.append("<tr id=\"TitleRow\">");
		htmlBuilder.append(System.getProperty("line.separator"));
		
		//This is the column header for the hand rank
		htmlBuilder.append("<td class=\"RowIndex\">");
		htmlBuilder.append("HAND RANK");
		htmlBuilder.append("</td>");
		htmlBuilder.append(System.getProperty("line.separator"));
		
		//Now we need to loop through integer values starting with minBet and ending with maxBet
		for(int i=_minBet; i<=_maxBet; i++) {
			htmlBuilder.append("<td id=\"TitleData\" class=\"BET" + String.valueOf(i) + "\">");
			htmlBuilder.append("BET " + String.valueOf(i));
			htmlBuilder.append("</td>");
			htmlBuilder.append(System.getProperty("line.separator"));
		}
		
		htmlBuilder.append("</tr>");
		htmlBuilder.append(System.getProperty("line.separator"));
		
		//loop through hand ranks here
		//Something to keep in mind is that some of our handranks may be blank. So we need to check for that
		//before we start a particular table row
		
		for(String handRank:_handRankNames) {
			if(handRank != "" && handRank != null) {
				htmlBuilder.append("<tr class=\"" + handRank.replaceAll("\\s+","") + "\">");
				htmlBuilder.append(System.getProperty("line.separator"));
				
				//This is the first column of the row, which contains the name of the rank being displayed
				htmlBuilder.append("<td class=\"RowIndex\">");
				htmlBuilder.append(handRank);
				htmlBuilder.append("</td>");
				htmlBuilder.append(System.getProperty("line.separator"));
				
				for(int i=_minBet; i<=_maxBet; i++) {
					htmlBuilder.append("<td id=\"DetailData\" class=\"BET" + String.valueOf(i) + "\">");
					htmlBuilder.append(String.valueOf(i*getPayoutForHandRank(handRank)));
					htmlBuilder.append("</td>");
					htmlBuilder.append(System.getProperty("line.separator"));
				}
				
				htmlBuilder.append("</tr>");
				htmlBuilder.append(System.getProperty("line.separator"));
			}
		}
		htmlBuilder.append("</table>");
		htmlBuilder.append(System.getProperty("line.separator"));
		return htmlBuilder.toString();
	}

}
