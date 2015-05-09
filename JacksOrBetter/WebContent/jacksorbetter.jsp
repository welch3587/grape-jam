<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="jorbstyle.css">
<meta name="author" content="Dave Welch" http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Jacks or Better</title>
</head>
<body>

<jsp:useBean id="JorB" class="org.dwc.job.JacksOrBetter" scope="session"/>

<%
String phName = request.getParameter("phasename");
if(phName != null) {
	switch(request.getParameter("phasename")) {
		case "Deal" : 
			JorB.setBet(Integer.valueOf(request.getParameter("betValue")));
			JorB.goToNextPhase();
			break;
		case "Draw" : 
			if(request.getParameter("card1")!=null) JorB.setHoldFlag(1);
			if(request.getParameter("card2")!=null) JorB.setHoldFlag(2);
			if(request.getParameter("card3")!=null) JorB.setHoldFlag(3);
			if(request.getParameter("card4")!=null) JorB.setHoldFlag(4);
			if(request.getParameter("card5")!=null) JorB.setHoldFlag(5);
			JorB.goToNextPhase();
			break;
		case "New Hand" : 
			JorB.goToNextPhase();
			break;
		default: 
			//JorB.goToNextPhase();
			break;
	}
} else {
	JorB.goToBetPhase();
}
%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script>
$(document).ready(function(){
    $("#bet1button").click(function(){
        $("#betVal").val("1");
        $("#betValDisplay").val("1");
        console.log("Setting bet to 1");
   	});
    $("#bet2button").click(function(){
        $("#betVal").val("2");
        $("#betValDisplay").val("2");
        console.log("Setting bet to 2");
    });
    $("#bet3button").click(function(){
        $("#betVal").val("3");
        $("#betValDisplay").val("3");
        console.log("Setting bet to 3");
    });
    $("#bet4button").click(function(){
        $("#betVal").val("4");
        $("#betValDisplay").val("4");
        console.log("Setting bet to 4");
    });
    $("#bet5button").click(function(){
        $("#betVal").val("5");
        $("#betValDisplay").val("5");
        console.log("Setting bet to 5");
    });
    
    if($("#c0hf").val() == "Deal") {
    	$("#bet1button").show();
    	$("#bet2button").show();
    	$("#bet3button").show();
    	$("#bet4button").show();
    	$("#bet5button").show();
    } else if($("#c0hf").val() == "New Hand") {
    	$(".payoutText").show();
    }
    
});

function clickCard(cardNum) {
	console.log("Card Number " + cardNum + " clicked!");
	if($("#c0hf").val() == "Draw") {
		switch(cardNum) {
		    case 1:
		    	if($("#c1hf").prop("checked")) {
			    	$("#c1hf").prop("checked", false);
			    	$("#hold1").prop("hidden", true);
		    	} else {
			    	$("#c1hf").prop("checked", true);
			    	$("#hold1").prop("hidden", false);
		    	}
		        break;
		    case 2:
		    	if($("#c2hf").prop("checked")) {
			    	$("#c2hf").prop("checked", false);
			    	$("#hold2").prop("hidden", true);
		    	} else {
			    	$("#c2hf").prop("checked", true);
			    	$("#hold2").prop("hidden", false);
		    	}
		        break;
		    case 3:
		    	if($("#c3hf").prop("checked")) {
			    	$("#c3hf").prop("checked", false);
			    	$("#hold3").prop("hidden", true);
		    	} else {
			    	$("#c3hf").prop("checked", true);
			    	$("#hold3").prop("hidden", false);
		    	}
		        break;
		    case 4:
		    	if($("#c4hf").prop("checked")) {
			    	$("#c4hf").prop("checked", false);
			    	$("#hold4").prop("hidden", true);
		    	} else {
			    	$("#c4hf").prop("checked", true);
			    	$("#hold4").prop("hidden", false);
		    	}
		        break;
		    case 5:
		    	if($("#c5hf").prop("checked")) {
			    	$("#c5hf").prop("checked", false);
			    	$("#hold5").prop("hidden", true);
		    	} else {
			    	$("#c5hf").prop("checked", true);
			    	$("#hold5").prop("hidden", false);
		    	}
		        break;
		} 
	}
}
</script>
<h1>Jacks or Better</h1>
<table class="PayoutTable" border=1 cellspacing=0 cellpadding=0>
 <tr class="PayoutTitleRow">
  <td id="HandRankTitle" class="PayoutTitleData">
  <p class="MsoNormal">HAND RANK</p>
  </td>
  <td class="PayoutTitleData">
  <p class="MsoNormal" align=right>BET 1</p>
  </td>
  <td class="PayoutTitleData">
  <p class="MsoNormal" align=right>BET 2</p>
  </td>
  <td class="PayoutTitleData">
  <p class="MsoNormal" align=right>BET 3</p>
  </td>
  <td class="PayoutTitleData">
  <p class="MsoNormal" align=right>BET 4</p>
  </td>
  <td class="PayoutTitleData">
  <p class="MsoNormal" align=right>BET 5</p>
  </td>
 </tr>
 <tr class="PayoutTableRow">
  <td id="PTD00" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">ROYAL FLUSH</p>
  </td>
  <td id="PTD01" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>250</p>
  </td>
  <td id="PTD02" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>500</p>
  </td>
  <td id="PTD03" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>750</p>
  </td>
  <td id="PTD04" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>1000</p>
  </td>
  <td id="PTD05" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>1250</p>
  </td>
 </tr>
 <tr>
  <td id="PTD10" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">STRAIGHT FLUSH</p>
  </td>
  <td id="PTD11" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>50</p>
  </td>
  <td id="PTD12" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>100</p>
  </td>
  <td id="PTD13" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>150</p>
  </td>
  <td id="PTD14" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>200</p>
  </td>
  <td id="PTD15" class="PayoutTableData" width=100 valign=top >
  <p class=MsoNormal align=right>250</p>
  </td>
 </tr>
 <tr>
  <td id="PTD20" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">4 OF A KIND</p>
  </td>
  <td id="PTD21" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>25</p>
  </td>
  <td id="PTD22" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>50</p>
  </td>
  <td id="PTD23" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>75</p>
  </td>
  <td id="PTD24" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>100</p>
  </td>
  <td id="PTD25" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>125</p>
  </td>
 </tr>
 <tr>
  <td id="PTD30" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">FULL HOUSE</p>
  </td>
  <td id="PTD31" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>9</p>
  </td>
  <td id="PTD32" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>18</p>
  </td>
  <td id="PTD33" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>27</p>
  </td>
  <td id="PTD34" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>36</p>
  </td>
  <td id="PTD35" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>45</p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:4'>
  <td id="PTD40" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">FLUSH</p>
  </td>
  <td id="PTD41" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>6</p>
  </td>
  <td id="PTD42" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>12</p>
  </td>
  <td id="PTD43" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>18</p>
  </td>
  <td id="PTD44" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>24</p>
  </td>
  <td id="PTD45" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>30</p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:5'>
  <td id="PTD50" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">STRAIGHT</p>
  </td>
  <td id="PTD51" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>4</p>
  </td>
  <td id="PTD52" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>8</p>
  </td>
  <td id="PTD53" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>12</p>
  </td>
  <td id="PTD54" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>16</p>
  </td>
  <td id="PTD55" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>20</p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:6'>
  <td id="PTD60" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">3 OF A KIND</p>
  </td>
  <td id="PTD61" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>3</p>
  </td>
  <td id="PTD62" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>6</p>
  </td>
  <td id="PTD63" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>9</p>
  </td>
  <td id="PTD64" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>12</p>
  </td>
  <td id="PTD65" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>15</p>
  </td>
 </tr>
 <tr>
  <td id="PTD70" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">2 PAIR</p>
  </td>
  <td id="PTD71" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>2</p>
  </td>
  <td id="PTD72" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>4</p>
  </td>
  <td id="PTD73" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>6</p>
  </td>
  <td id="PTD74" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>8</p>
  </td>
  <td id="PTD75" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>10</p>
  </td>
 </tr>
 <tr style='mso-yfti-irow:8;mso-yfti-lastrow:yes'>
  <td id="PTD80" class="PayoutTableData" width=200 valign=top >
  <p class="MsoNormal">JACKS OR BETTER</p>
  </td>
  <td id="PTD81" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>1</p>
  </td>
  <td id="PTD82" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>2</p>
  </td>
  <td id="PTD83" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>3</p>
  </td>
  <td id="PTD84" class="PayoutTableData" width=100 valign=top >
  <p class="MsoNormal" align=right>4</p>
  </td>
  <td id="PTD85" class="PayoutTableData" width=100 valign=top >
  <p align=right>5</p>
  </td>
 </tr>
</table>

<table class="MsoTableGrid" border=0 cellspacing=0 cellpadding=0>
 <tr class="holdrow">
  <td class="holddata">
  <p id="hold1" class="holdtext"  hidden=true><b>HOLD</b></p>
  </td>
  <td class="holddata">
  <p id="hold2" class="holdtext"  hidden=true><b>HOLD</b></p>
  </td>
  <td class="holddata">
  <p id="hold3" class="holdtext"  hidden=true><b>HOLD</b></p>
  </td>
  <td class="holddata">
  <p id="hold4" class="holdtext"  hidden=true><b>HOLD</b></p>
  </td>
  <td class="holddata">
  <p id="hold5" class="holdtext"  hidden=true><b>HOLD</b></p>
  </td>
 </tr>
 <tr  class="cardrow">
  <td class="cardbox">
  <p align=center>
  <input type="image" id="card1image" class=card src="./images/<%=JorB.getCardImageName(1)%>" onclick="clickCard(1)" width=100 height=136/>
  </p>
  </td>
  <td class="cardbox">
  <p align=center>
  <input type="image" id="card2image" class=card src="./images/<%=JorB.getCardImageName(2)%>" onclick="clickCard(2)" width=100 height=136/>
  </p>
  </td>
  <td class="cardbox">
  <p align=center>
  <input type="image" id="card3image" class=card src="./images/<%=JorB.getCardImageName(3)%>" onclick="clickCard(3)" width=100 height=136/>
  </p>
  </td>
  <td class="cardbox">
  <p align=center>
  <input type="image" id="card4image" class=card src="./images/<%=JorB.getCardImageName(4)%>" onclick="clickCard(4)" width=100 height=136/>
  </p>
  </td>
  <td class="cardbox">
  <p align=center>
  <input type="image" id="card5image" class=card src="./images/<%=JorB.getCardImageName(5)%>" onclick="clickCard(5)" width=100 height=136/>
  </p>
  </td>
 </tr>
</table>

<table class="controlTable">
<tr>
<td class="controls">
 <form id="actionForm" action="jacksorbetter.jsp" method="POST">
 <input id="c0hf" type="text" name="phasename" value="<%=JorB.actionButtonText()%>" hidden=true>
 <input id="c1hf" type="checkbox" name="card1" value="Hold" hidden=true>
 <input id="c2hf" type="checkbox" name="card2" value="Hold" hidden=true>
 <input id="c3hf" type="checkbox" name="card3" value="Hold" hidden=true>
 <input id="c4hf" type="checkbox" name="card4" value="Hold" hidden=true>
 <input id="c5hf" type="checkbox" name="card5" value="Hold" hidden=true>
 <input class="controls" type="submit" value="<%=JorB.actionButtonText()%>">
 <input id="betVal" type="text" name="betValue" value="<%=JorB.getBet()%>" hidden=true>
 </form>
</td> 
</tr>
<tr>
<td>
<input type="button" id="bet1button" class="controls" value="Bet 1"/>
<input type="button" id="bet2button" class="controls" value="Bet 2"/>
<input type="button" id="bet3button" class="controls" value="Bet 3"/>
<input type="button" id="bet4button" class="controls" value="Bet 4"/>
<input type="button" id="bet5button" class="controls" value="Bet 5"/>
</td>
</tr>
<tr>
<td>
<p id="betDisplay"> Bet: <input id="betValDisplay" type="text" name="betValue" value="<%=JorB.getBet()%>"></p>
</td>
<td>
<p class="payoutText"> Payout: <%= JorB.getPayout() %> </p>
</td>
<td>
<p class="payoutText"> <%= JorB.getResultText() %></p>
</td>
</tr>
<tr>
<td>
<p><b>Credits: <%= JorB.getCredits() %> </b></p>
</td>
</tr>
<tr>
<td>
</td>
<!-- <td>
<form id="resetForm" action="jacksorbetter.jsp" method="POST">
  <b class="controls">Reset to Phase 0</b><br>
  <input class="controls" type="submit" value="reset">
</form>
</td> -->
</tr>
</table>
<% request.getSession().setAttribute("JorB", JorB);
 %>
</body>
</html>