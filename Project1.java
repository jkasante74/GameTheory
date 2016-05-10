package ipd;
import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;


public class Project1{
public static int gameRound = 0;
private static Scanner input;	
static char [][] grimTriggerHist;
static char [][] titForTatHist;
static char [][] pavlovChoiceHist;
static int [][] pavlovScoreHist;
@SuppressWarnings("static-access")
public static void main(String[] args) {

    //obtain the number of teams from user input

    int teams, tourn;
    input = new Scanner(System.in);
    System.out.print("How many players are in the game?");
    teams = input.nextInt();
    input = new Scanner(System.in);
    System.out.print("Enter number of tournaments to be played?");
    tourn = input.nextInt();
    
    //Declaration and Initializations
    ArrayList<Object> myList = new ArrayList<Object>();
    ArrayList<Object> myHomeList = new ArrayList<Object>();
    ArrayList<Object> myAwayList = new ArrayList<Object>();
   
    grimTriggerHist = new char[teams][teams];
    titForTatHist = new char[teams][teams];
    pavlovChoiceHist = new char[teams][teams];
    pavlovScoreHist = new int[teams][teams];
    String [] Strategy = new String[teams];
    char [] agentsActions = new char[2];
    int [] agentsMatchScores = new int[2];
    int []agentsTotalScores = new int[teams];
    
    final String[][] data  = new String[teams][3]; // To store the final output of agents name, strategies and scores
    
    //Create an object in each of these ArrayList and let them pick Strategy
    for(int i = 0; i < teams; i++){
    	myList.add(new Project1());
    	Project1 obj = (Project1) myList.get(i);
        Strategy[i] = obj.pickStrategy(i, obj, gameRound);
    }
    System.out.println("Randomly chosen Strategies ");
	System.out.println("=============================");
    // Print the randomly selected Strategies
    for(int t = 0; t < teams; t++){
    	
    	System.out.println("Player "+ (t+1) + "\t"+Strategy[t]);
    }
    
    for(int t = 0; t < tourn; t++){
    	System.out.println("TOURNAMENT "+ (t+1));
    	System.out.println("=============================");
    // Generate the schedule using Round Robin algorithm.
    int totalRounds = (teams - 1);	 // Set total rounds to determine how many games each team plays in a tournament
    int matchesPerRound = teams / 2; //Set total matches per round (Note: Total matches in tournament = 0.5n(n-1) ie totalRounds * total matches)
  
    
    // Create the two list to match pairs
    for(int i = 0; i < matchesPerRound; i++){
    	myHomeList.add(new Project1());
    	myAwayList.add(new Project1());
    } 

    for (int round = 0; round < totalRounds; round++) {
    	gameRound = round;
        
    	// Create the matched pairs of Agents into two groups
    	for (int match = 0; match < matchesPerRound; match++) {
            int home = (round + match) % (teams - 1);
            int away = (teams - 1 - match + round) % (teams - 1);

            // Last team stays in the same place while the others rotate around it.
            if (match == 0)
                away = teams - 1;
        	
        	myHomeList.set(match,String.valueOf(home + 1) );
        	myAwayList.set(match, String.valueOf(away + 1));
        }
    
    	// Display the match pairing and selected strategy for each rounds    
    	System.out.println("Round " + (round + 1));
    	System.out.println();
    
    	for (int j = 0; j < matchesPerRound; j++){ 
    		System.out.println("Player "+myHomeList.get(j)+ "\t vrs \t " + "Player " +myAwayList.get(j));
		   
    		// Play the game
    		agentsActions = getAgentsActions(round, myHomeList.get(j), myAwayList.get(j), Strategy);  
	
    		// Calculate the agents match score	   
    		agentsMatchScores = calcAgentsScores(agentsActions);
	
    		// Calculate the total score for each player
    		int scA = Integer.parseInt((String)myHomeList.get(j));
    		int scB = Integer.parseInt((String)myAwayList.get(j));
    		agentsTotalScores[(scA - 1)] =  agentsTotalScores[(scA - 1)] + agentsMatchScores[0];
    		agentsTotalScores[(scB - 1)] =  agentsTotalScores[(scB - 1)] + agentsMatchScores[1];
    		System.out.println();
    		
    		
    		//Update the Grim Trigger History for all Agents
    		if(grimTriggerHist[(scA-1)][(scB-1)] !='D') //Update opponent history action only if existing action is Coorporate
    			if(Strategy[(scA-1)].equalsIgnoreCase("GrimTrigger")){
    				grimTriggerHist[(scA-1)][(scB-1)]=agentsActions[1];
    		}
    		
    		if(grimTriggerHist[(scB-1)][(scA-1)] !='D')
    		if(Strategy[(scB-1)].equalsIgnoreCase("GrimTrigger")){
    				grimTriggerHist[(scB-1)][(scA-1)]=agentsActions[0];
    		}
    		
    		//Update the TitForTat History for all Agents
    		if(Strategy[(scA-1)].equalsIgnoreCase("TitForTat")){
    			titForTatHist[(scA-1)][(scB-1)]=agentsActions[1];
    		}
    		
    		if(Strategy[(scB-1)].equalsIgnoreCase("TitForTat")){
    			titForTatHist[(scB-1)][(scA-1)]=agentsActions[0];
    		}
    		
    		//Update the PAVLOV History for all Agents
    		if(Strategy[(scA-1)].equalsIgnoreCase("Pavlov")){
    			pavlovChoiceHist[(scA-1)][(scB-1)]=agentsActions[0];
    			pavlovScoreHist[(scA-1)][(scB-1)]=agentsMatchScores[0];
    		}
    		
    		if(Strategy[(scB-1)].equalsIgnoreCase("Pavlov")){
    			pavlovChoiceHist[(scB-1)][(scA-1)]=agentsActions[1];
    			pavlovScoreHist[(scB-1)][(scA-1)]=agentsMatchScores[1];
    		}
    		
    		
    	}
	   
    	
    	
	   System.out.println("\n");
    }
    }
    
   // Create a 3-dimensional array that has the Player's name, Strategies, and Score
    for(int  i = 0; i < Strategy.length; i++){
    	 data[i][0] = "Player "+(i +1);
    	 data[i][1] = Strategy[i];
    	 data[i][2] = String.valueOf((agentsTotalScores[i]));
    }
  
    // Sort the arrays based on the 3rd Column the total score
    Arrays.sort(data, new Comparator<String[]>() {
        @Override
        public int compare(final String[] entry1, final String[] entry2) {
            final String time1 = entry1[2];
        	 final String time2 = entry2[2];
            return Integer.valueOf(time1).compareTo(Integer.valueOf(time2));
        }
    });
    // Print the leader Board
    System.out.println("LEADER BOARD");
   	System.out.println("=============================");
    for(int i = (data.length-1); i >= 0;i--){
    	System.out.printf("%-20s%-30s%-30s", data[i][0], data[i][1], data[i][2]);
    	System.out.println();
    }
 
  
    
    
    
    
    
    
}




/**
 * This method calculates the payoffs associated with the selected actions
 * @param agentsActions
 * @return
 */

private static int [] calcAgentsScores(char[] agentsActions) {
	int scoreA = 0, scoreB = 0;
	int []matchScores = new int[2];
	
	if((agentsActions[0]=='C')&&(agentsActions[1]=='C')){
		scoreA = 3; 
		scoreB = 3;
	}
	
	if((agentsActions[0]=='C')&&(agentsActions[1]=='D')){
		scoreA = 0; 
		scoreB = 5;
	}
	
	if((agentsActions[0]=='D')&&(agentsActions[1]=='C')){
		scoreA = 5; 
		scoreB = 0;
	}
	
	if((agentsActions[0]=='D')&&(agentsActions[1]=='D')){
		scoreA = 1; 
		scoreB = 1;
	}
	
	matchScores[0] = scoreA;
	matchScores[1] = scoreB;
	
	System.out.println(matchScores[0] +"\t \t vrs \t \t" + matchScores[1]);
	return matchScores;
}

/**
 * Get the actions associated with the strategies of the agents in the current match
 * @param object
 * @param object2
 * @param strategy
 */
private static char[] getAgentsActions(int cRound, Object object, Object object2, String[] strategy) {
	char [] actions = new char[2];
	String a = strategy[(Integer.parseInt((String)object) - 1)];
	String b = strategy[(Integer.parseInt((String)object2) - 1)];
	int agentId = (Integer.parseInt((String)object) - 1);
	int agentOppId = (Integer.parseInt((String)object2) - 1);
	System.out.println(a +"\t vrs \t" + b);
	
	// get the Action
	char actionA, actionB;
		 
	switch (a) {
		case "CooperateAll": actionA = cooperateAll(); break;
		case "DefectAll": 	actionA = defectAll(); break;
		case "TitForTat":	actionA = TitForTat(cRound, agentId, agentOppId); break;
		case "Random": actionA = random() ; break;
		case "Pavlov": actionA = PAVLOV(cRound, agentId,agentOppId); break;
		case "GrimTrigger": actionA = grimTrigger(agentId,agentOppId);  break;
		default: throw new RuntimeException("Bad argument passed to makePlayer");
	}
		 
	switch (b) {
	case "CooperateAll": actionB = cooperateAll(); break;
	case "DefectAll": 	actionB = defectAll(); break;
	case "TitForTat":	actionB = TitForTat(cRound, agentOppId, agentId); break;
	case "Random": actionB = random() ; break;
	case "Pavlov": actionB = PAVLOV(cRound, agentOppId,agentId); break;
	case "GrimTrigger": actionB = grimTrigger(agentOppId,agentId);  break;
	default: throw new RuntimeException("Bad argument passed to makePlayer");
	}	 
		 actions[0] = actionA;
		 actions[1] = actionB;
		 
		 System.out.println(actions[0] +"\t \t vrs \t \t" + actions[1]);
	return actions;
}

/**
 * In this method the agents in the game will randomly pick one strategy 
 * and play the tournament with that strategy
 * @param i
 * @param objs
 * @param round
 * @return
 */
public static String pickStrategy(int i, Object objs, int round) {
	
	// Generate random number to select Strategy 
	Random randomGenerator = new Random();
	int randomInt = randomGenerator.nextInt(6);
	String chosenStrategy = "";
	
	// Select Strategy 
	 switch (randomInt) {
		case 0: chosenStrategy = "CooperateAll"; break;
		case 1: chosenStrategy = "DefectAll"; break;
		case 2:	chosenStrategy= "TitForTat"; break;
		case 3: chosenStrategy = "Random"; break;
		case 4: chosenStrategy = "Pavlov"; break;
		case 5: chosenStrategy = "GrimTrigger";  break;
		default: throw new RuntimeException("Bad argument passed to makePlayer");
	 }
	
	 	return chosenStrategy;
}


/**
 * Initially, a player using grim trigger will cooperate, 
 * but as soon as the opponent defects (thus satisfying the 
 * trigger condition), the player using grim trigger will 
 * defect for the remainder of the iterated game. 
 * @return
 */
public static char grimTrigger(int agentid, int oppid ) {
// create an array of strategies
// before selection of c or d get the opponent id
	char grimSelection;
	if (grimTriggerHist[agentid][oppid]=='D')
		grimSelection = 'D';
		
	else
		grimSelection = 'C';
		
	return grimSelection;
}
	
/**
 * (repeat last choice if good outcome) - 
 * If 5 or 3 points scored in the last round then repeat 
 * last choice.
 * @param n
 * @param oppId
 * @return
 */
	private static char PAVLOV(int n, int agentId, int oppId) {
		char pavlovChoioce;
		if ((n==0)&&(pavlovChoiceHist[agentId][oppId]== 0)){
			pavlovChoioce = 'C';	//cooperate by default
			return pavlovChoioce;
		}
		if(pavlovScoreHist[agentId][oppId] >= 3){
			pavlovChoioce = pavlovChoiceHist[agentId][oppId];
			return pavlovChoioce;
		}
		
		else{
			if(pavlovChoiceHist[agentId][oppId]=='C')
					pavlovChoioce = 'D';
			else
					pavlovChoioce = 'C';
		
			return pavlovChoioce;
		}
	}

	
	/**
	 * Cooperate at the first play; then,
	 * on subsequent plays, mimic the action 
	 * the other player chose on the immediately 
	 * preceding play
	 * @param n
	 * @param oppId
	 * @return
	 */
	private static char TitForTat(int n, int agentId, int oppId) {
		char TFT;
		if (titForTatHist[agentId][oppId]=='D')
			TFT = 'D';
			
		else
			TFT= 'C';
			
		return TFT;
		
}

	
	/**
	 * Cooperate at all times no matter what the opponent does
	 * @return
	 */
	static char cooperateAll(){
		return 'C';
	}
	
	/**
	 * Defect at all times no matter what the opponent does
	 * @return
	 */
	static char defectAll(){
		return 'D';
	}
	
	
	/**
	 * Choose to Cooperate or Defect at based on a probability distribution
	 * @return
	 */
	static char random(){
		if (Math.random() < 0.5)
			return 'C';  //cooperates half the time
		else
			return 'D';  //defects half the time
	}


}