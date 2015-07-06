import java.util.Scanner;
import java.io.*;
import java.lang.*;
import java.text.DecimalFormat;


class algorithm {
	public int[] SHPalgor(int[][] load,char sourNode,char destNode,char[] N){//the the path of SHP 
		int nodeNum=0;//means how many node in this topology
		int[] path = new int[26];//store the path from source node to destination node
		int[] previous = new int[26];// the previous node of this node
		int[] d = new int[26];//mean the distance from this node to the source node
		for(int m = 0;m <26;m++){//set d[] of all other node to 10000(mean max)
			if(N[m] != 0){
				nodeNum++;
			}
			if(m != (int)(sourNode - 'A')){
			
			d[m] = 10000;
			}
			else if('A'<=sourNode && sourNode<= 'Z'){
				d[m] = 0;
			}
			previous[m] = -1;
			path[m] = -1;
		}


		do{//if the old d[x] > d[y]+1(1 mean one link between them)
		for(int m=0;m<26;m++){
			for(int n=0;n<26;n++){
				if (load[m][n] != 0){
					for(int i=0;i<26;i++){
						if (d[i] < 10000){
							if (m ==i){
								if (d[n] == 10000){
									nodeNum--;
								}
								if (d[n] > d[i] + 1){
									d[n] = d[i] + 1;
									previous[n] =i;
								}
							}
							else if (n ==i){
								if (d[m] == 10000){
									nodeNum--;
								}
								if (d[m] > d[i] + 1){
									d[m] = d[i] + 1;
									previous[m] =i;
								}
							}
						}
					}
				}
			}
		}
		
		}while (nodeNum > 1);//do the cycle until the nodenum is less than 2, this mean all node in topology are tested
		path[0]=(int)(destNode-'A');//set the path[0] as the destination node
		
		for(int m =1;m<26;m++){//use previous to set the path
			path[m] = previous[path[m-1]];
			if (path[m] == -1){
				break;
			}
		}
		return path;
		
	}
	
	
	public int[] SDPalgor(int[][] propDelay,char sourNode,char destNode,char[] N){//nearly the same as SHPalgor,only different is  use propdelay instead of 1 as distance
		int nodeNum=0;
		int[] path = new int[26];
		int[] previous = new int[26];
		int[] d = new int[26];
		for(int m = 0;m <26;m++){
			if(N[m] != 0){
				nodeNum++;
			}
			if(m != (int)(sourNode - 'A')){
			
			d[m] = 10000;
			}
			else if('A'<=sourNode && sourNode<= 'Z'){
				d[m] = 0;
			}
			previous[m] = -1;
			path[m] = -1;
		}


		do{
		for(int m=0;m<26;m++){
			for(int n=0;n<26;n++){
				if (propDelay[m][n] != 0){
					for(int i=0;i<26;i++){
						if (d[i] < 10000){
							if (m ==i){
								if (d[n] == 10000){
									nodeNum--;
								}
								if (d[n] > d[m] + propDelay[m][n]){
									d[n] = d[m] + propDelay[m][n];
									previous[n] =i;
								}
							}
							else if (n ==i){
								if (d[m] == 10000){
									nodeNum--;
								}
								if (d[m] > d[n] + propDelay[m][n]){
									d[m] = d[n] + propDelay[m][n];
									previous[m] =i;
								}
							}
						}
					}
				}
			}
		}
		
		}while (nodeNum > 1);
		path[0]=(int)(destNode-'A');
		
		for(int m =1;m<26;m++){
			path[m] = previous[path[m-1]];
			if (path[m] == -1){
				break;
			}
		}
		return path;
		
	}
	
	
	
	public int[] LLPalgor(double[][] C,char sourNode,char destNode,char[] N,int[][] load){//find path for LLP,change the distance as Max(d[m],C[m][n]))
		int nodeNum=0;//means how many node in this topology
		int[] path = new int[26];//store the path from source node to destination node
		int[] previous = new int[26];// the previous node of this node
		double[] d = new double[26];//mean the load percentage from this node to the source node
		for(int m = 0;m <26;m++){//set d[] of all other node to 1(mean max)
			if(N[m] != 0){
				nodeNum++;
			}
			if(m != (int)(sourNode - 'A')){
			
			d[m] = 1;
			}
			else if('A'<=sourNode && sourNode<= 'Z'){
				d[m] = 0;
			}
			previous[m] = -1;
			path[m] = -1;
		}


		do{//
		for(int m=0;m<26;m++){
			for(int n=0;n<26;n++){
				if (load[m][n] != 0){
					for(int i=0;i<26;i++){
						if (d[i] < 1){
							if (m ==i){
								if (d[n] == 1){
									nodeNum--;
								}
								if (d[n] > Math.max(d[m],C[m][n])){//C mean the percentage of load in this link
									d[n] = Math.max(d[m],C[m][n]);
									previous[n] =i;
								}
							}
							else if (n ==i){
								if (d[m] == 1){
									nodeNum--;
								}
								if (d[m] > Math.max(d[n],C[m][n])){
									d[m] = Math.max(d[n],C[m][n]);
									previous[m] =i;
								}
							}
						}
					}
				}
			}
		}
		
		}while (nodeNum > 1);
		path[0]=(int)(destNode-'A');
		
		for(int m =1;m<26;m++){
			path[m] = previous[path[m-1]];
			if (path[m] == -1){
				break;
			}
		}
		return path;
		
	}
	
	
	
	public void SP(int[][] propDelay,int[][] load,char[] sourNode,char[] destNode,double[] startTime,double[] duration,char[] N,boolean DorH,int Packet_rate,String mode){// for both SHP andSDP
		int[][] path = new int[sourNode.length][26];//the path for all input
		boolean[] isblockc = new boolean[sourNode.length];//is all input blocked,false mean not blocked, true mean blocked
		boolean[][] isblock = new boolean[sourNode.length][1000];//is all input blocked,false mean not blocked, true mean blocked
		int[] delay = new int[sourNode.length];//the delay for all input
		int blocknum=0,unblocknum=0;
		for(int m =0;m< sourNode.length;m++){
			if(DorH == true){//DorH decide is SHP or SDP
				path[m] = SHPalgor(load,sourNode[m],destNode[m],N);
			}
			else if (DorH == false){
				path[m] = SDPalgor(propDelay,sourNode[m],destNode[m],N);
			}
			isblockc[m] = false;
			for(int k = 0; k<1000; k++){
				isblock[m][k] = false;
			}
		}

		if(mode.equals("CIRCUIT")){
		for(int m = 0;m<sourNode.length;m++){//test each input line, get result for each source node and destination node
			int[][] tmpload = new int[26][26]; //load count for each request
			for(int n = 0; n <=m;n++){//test all the input before it
				if((startTime[n]+duration[n])>startTime[m])
					if(isblock[n][(int)((startTime[m]-startTime[n])*Packet_rate)] == false){//if it blocked or finished before this start,do not need to test
					for(int i =0;i<25;i++){
						if(path[n][i+1] != -1){//find availble path
							//System.out.println(n+ "and" + i + ": " +path[n][i] );
							if(path[n][i] >path[n][i+1] ){//only search the load[1][2],1<=2
							
							tmpload[path[n][i+1]][path[n][i]]++;//if a path find,increase the count 1
							
							if (tmpload[path[n][i+1]][path[n][i]] > load[path[n][i+1]][path[n][i]] ){//if the count of this path over the the load,it will be blocked
								isblockc[m] =true;
								isblock[m][0] =true;
								break;
							}
							}
							else if(path[n][i] <=path[n][i+1] ){
							
							tmpload[path[n][i]][path[n][i+1]]++;
							if (tmpload[path[n][i]][path[n][i+1]] > load[path[n][i]][path[n][i+1]] ){
								
								isblockc[m] =true;
								isblock[m][0] =true;
								break;
							}
							}
						}
						
						
					}
				}	
			}
			
			if (isblockc[m] == false){
				isblock[m][0]=false;
				//unblocknum++;
			for (int h = 0;h <= (int)(Packet_rate*duration[m]);h++){
				int[][] tmpload1 = new int[26][26];
				for(int n = 1; n <=m;n++){//test all the input before it
					if((startTime[n]+duration[n])>(startTime[m]+h/Packet_rate))
						if(isblock[n][(int)((startTime[m]+h/Packet_rate-startTime[n])*Packet_rate)] == false){//if it blocked or finished before this start,do not need to test
						//System.out.println(n+ "and" + m  );
						for(int i =0;i<25;i++){
							if(path[n][i+1] != -1){//find availble path
								//System.out.println(n+ "and" + i + ": " +path[n][i] );
								if(path[n][i] >path[n][i+1] ){//only search the load[1][2],1<=2
								
								tmpload1[path[n][i+1]][path[n][i]]++;//if a path find,increase the count 1
								
								if (tmpload1[path[n][i+1]][path[n][i]] > load[path[n][i+1]][path[n][i]] ){//if the count of this path over the the load,it will be blocked
									isblock[m][h] =true;
									break;
								}
								}
								else if(path[n][i] <=path[n][i+1] ){
								
								tmpload1[path[n][i]][path[n][i+1]]++;
								if (tmpload1[path[n][i]][path[n][i+1]] > load[path[n][i]][path[n][i+1]] ){
									
									isblock[m][h] =true;
									break;
								}
								}
							}
							
							
						}
					}	
				}
				if(isblock[m][h] ==true){
					blocknum++;
				}else{
					unblocknum++;
				}
			}
			
			}
			else{
				for (int h = 0;h <= (int)(Packet_rate*duration[m]);h++){
					isblock[m][h]=true;
					blocknum++;
					
				}
			}
			
			
		}
		}
		else if(mode.equals("PACKET")){
			for(int m = 0;m<sourNode.length;m++){
				//int[][] tmpload = new int[26][26];
				for (int h = 0;h <= (int)(Packet_rate*duration[m]);h++){
					int[][] tmpload = new int[26][26];
					for(int n = 0; n <= m;n++){//test all the input before it
						//for(int hn = 0;hn <= (int)(Packet_rate*duration[n]);hn++)
						if((startTime[n]+duration[n])>(startTime[m]+h/Packet_rate) )
							if(isblock[n][(int)((startTime[m]+h/Packet_rate-startTime[n])*Packet_rate)] == false){//if it blocked or finished before this start,do not need to test
							
							for(int i =0;i<25;i++){
								if(path[n][i+1] != -1){//find availble path
									//System.out.println(n+ "and" + i + ": " +path[n][i] );
									if(path[n][i] >path[n][i+1] ){//only search the load[1][2],1<=2
									
									tmpload[path[n][i+1]][path[n][i]]++;//if a path find,increase the count 1
									
									if (tmpload[path[n][i+1]][path[n][i]] > load[path[n][i+1]][path[n][i]] ){//if the count of this path over the the load,it will be blocked
										isblock[m][h] =true;
										break;
									}
									}
									else if(path[n][i] <=path[n][i+1] ){
									
									tmpload[path[n][i]][path[n][i+1]]++;
									if (tmpload[path[n][i]][path[n][i+1]] > load[path[n][i]][path[n][i+1]] ){
										
										isblock[m][h] =true;
										break;
									}
									}
								}
							}
						}	
						
						
						
					}
					if(isblock[m][h] ==true){
						//System.out.println(m+ "and" + h  );
						blocknum++;
					}else{
						unblocknum++;
					}
				}
			}
		}
		

		if(mode.equals("CIRCUIT")){
			System.out.println("total number of virtual circuit requests: " + sourNode.length);
		}
		else{
			System.out.println("total number of virtual circuit requests: " + (blocknum+unblocknum));
		}
		


		System.out.println("total number of packets: " + (blocknum+unblocknum));
		System.out.println("number of successfully routed packets: " + unblocknum);
		DecimalFormat df1 = new DecimalFormat("##.00");
		String successpercent = df1.format((double)unblocknum/(double)(blocknum+unblocknum)*100);
		System.out.println("percentage of successfully routed packets: " + successpercent);
		System.out.println("number of blocked packets: " + blocknum);
		String blockpercent = df1.format(((double)blocknum)/(double)(blocknum+unblocknum)*100);
		System.out.println("percentage of blocked packets: " + blockpercent);
		int totalhop=0,pathnum=0;
		for(int m =0;m<sourNode.length;m++){//get the delay,and number of node for each one depend on their path
			if(isblockc[m] == false){
			
			for(int i =0;i<25;i++){
				if(path[m][i] != -1){
					totalhop++;
				}
				if(path[m][i+1] != -1){
					if(path[m][i] >= path[m][i+1]){
						delay[m] = delay[m]+propDelay[path[m][i+1]][path[m][i]];
					}
					else{
						delay[m] = delay[m]+propDelay[path[m][i]][path[m][i+1]];
					}
				}
			}
			pathnum++;
			//System.out.println(hopnum[m]);
			}
		}
		String hoppercent = df1.format(((double)totalhop-(double)pathnum)/(double)pathnum);//the number of hop = node -1
		System.out.println("average number of hops per circuit:  " + hoppercent);
		int totaldelay = 0;
		for(int m =0;m<sourNode.length;m++){//get the toral delay
			totaldelay = totaldelay +delay[m];
		}
		String delaypercent = df1.format((double)totaldelay/(double)pathnum);
		System.out.println("average cumulative propagation delay per circuit:  " + delaypercent);
		
	}
	
	
	
	public void LLP(int[][] propDelay,int[][] load,char[] sourNode,char[] destNode,double[] startTime,double[] duration,char[] N,int Packet_rate,String mode){
		int[][] path = new int[sourNode.length][26];
		boolean[][] isblock = new boolean[sourNode.length][1000];
		boolean[] isblockc = new boolean[sourNode.length];
		//int[] d = new int[26];
		double[][] MaxC = new double[sourNode.length][1000];
		int[] delay = new int[sourNode.length];
		int blocknum=0,unblocknum=0;
		
		
		
		for(int m = 0;m<sourNode.length;m++){//test each input line, get result for each source node and destination node
			int[][] tmpload = new int[26][26]; //load count for each request
			double[][] C = new double[26][26];//C means the percentage of use for each link
			for(int n = 0; n <m;n++){//test all the input before it
				if(isblockc[n] == false && (startTime[n]+duration[n])>startTime[m]){//if it blocked or finished before this start,do not need to test
					for(int i =0;i<25;i++){
						if(path[n][i+1] != -1){//find availble path
							//System.out.println(n+ "and" + i + ": " +path[n][i] );
							if(path[n][i] >path[n][i+1] ){
							
							tmpload[path[n][i+1]][path[n][i]]++;//if the path find ,increase the count 1
							C[path[n][i+1]][path[n][i]] = 	(double)tmpload[path[n][i+1]][path[n][i]]/(double)load[path[n][i+1]][path[n][i]];
							
						}
						else if(path[n][i] <=path[n][i+1] ){
							
							tmpload[path[n][i]][path[n][i+1]]++;
							C[path[n][i]][path[n][i+1]] = 	(double)tmpload[path[n][i]][path[n][i+1]]/(double)load[path[n][i]][path[n][i+1]];
							//System.out.println(tmpload[path[n][i]][path[n][i+1]] +":"+C[path[n][i]][path[n][i+1]]+"|"+load[path[n][i]][path[n][i+1]]);
							}
						}
						
						
					}
				}
			}
			path[m] = LLPalgor(C,sourNode[m],destNode[m],N,load);
			//System.out.println(m);
			if(mode.equals("CIRCUIT")){
				isblockc[m]= true;
				for (int h = 0;h <= (int)(Packet_rate*duration[m]);h++){
					for(int i =0;i<25;i++){//calculate the max load percentage for each one 
						if(path[m][i+1] != -1){
							
							if(path[m][i] >path[m][i+1] ){
								//tmpload[path[m][i+1]][path[m][i]]++;
								MaxC[m][h]=Math.max(MaxC[m][h],((double)tmpload[path[m][i+1]][path[m][i]]+1)/(double)load[path[m][i+1]][path[m][i]]);		
							}
							else if(path[m][i] <=path[m][i+1] ){
								//tmpload[path[m][i+1]][path[m][i]]++;
								MaxC[m][h]=Math.max(MaxC[m][h],((double)tmpload[path[m][i]][path[m][i+1]]+1)/(double)load[path[m][i]][path[m][i+1]]);
								//System.out.println(tmpload[path[m][i]][path[m][i+1]] +":"+C[path[m][i]][path[m][i+1]]+"|"+load[path[m][i]][path[m][i+1]]);
							}
						}
					}
					//System.out.println(MaxC[m]);
					if(MaxC[m][h] > 1 || MaxC[m][h] == 1){// if Max ==1 ,it means blocked
						isblock[m][h] = true;
						blocknum++;
					}else{
						isblock[m][h] = false;
						unblocknum++;
						isblockc[m]= false;
					}

				}
			}else if (mode.equals("PACKET")){
				
				
				isblockc[m]= true;
				for (int h = 0;h <= (int)(Packet_rate*duration[m]);h++){
					
					
					for(int i =0;i<25;i++){//calculate the max load percentage for each one 
						if(path[m][i+1] != -1){
							
							if(path[m][i] >path[m][i+1] ){
								
								MaxC[m][h]=Math.max(MaxC[m][h],((double)tmpload[path[m][i+1]][path[m][i]]+1)/(double)load[path[m][i+1]][path[m][i]]);		
							}
							else if(path[m][i] <=path[m][i+1] ){
								
								MaxC[m][h]=Math.max(MaxC[m][h],((double)tmpload[path[m][i]][path[m][i+1]]+1)/(double)load[path[m][i]][path[m][i+1]]);
								//System.out.println(tmpload[path[m][i]][path[m][i+1]] +":"+C[path[m][i]][path[m][i+1]]+"|"+load[path[m][i]][path[m][i+1]]);
							}
						}
					}
					//System.out.println(MaxC[m]);
					if(MaxC[m][h] > 1 || MaxC[m][h] == 1){// if Max ==1 ,it means blocked
						isblock[m][h] = true;
						blocknum++;
					}else{
						isblock[m][h] = false;
						unblocknum++;
						isblockc[m]= false;
					}
				
					for(int i =0;i<25;i++){
						if(path[m][i+1] != -1){//find availble path
							//System.out.println(n+ "and" + i + ": " +path[n][i] );
							if(path[m][i] >path[m][i+1] ){
							
							//tmpload[path[m][i+1]][path[m][i]]++;//if the path find ,increase the count 1
							C[path[m][i+1]][path[m][i]] = 	(double)(tmpload[path[m][i+1]][path[m][i]]+1)/(double)load[path[m][i+1]][path[m][i]];
							
						}
						else if(path[m][i] <=path[m][i+1] ){
							
							//tmpload[path[m][i]][path[m][i+1]]++;
							C[path[m][i]][path[m][i+1]] = 	(double)(tmpload[path[m][i+1]][path[m][i]]+1)/(double)load[path[m][i]][path[m][i+1]];
							//System.out.println(tmpload[path[n][i]][path[n][i+1]] +":"+C[path[n][i]][path[n][i+1]]+"|"+load[path[n][i]][path[n][i+1]]);
							}
						}
						
						
					}
					path[m] = LLPalgor(C,sourNode[m],destNode[m],N,load);
					
			}
			
			}
			
		
		}
		
		if(mode.equals("CIRCUIT")){
			System.out.println("total number of virtual circuit requests: " + sourNode.length);
		}
		else{
			System.out.println("total number of virtual circuit requests: " + (blocknum+unblocknum));
		}
		


		System.out.println("total number of packets: " + (blocknum+unblocknum));
		System.out.println("number of successfully routed packets: " + unblocknum);
		DecimalFormat df1 = new DecimalFormat("##.00");
		String successpercent = df1.format((double)unblocknum/(double)(blocknum+unblocknum)*100);
		System.out.println("percentage of successfully routed packets: " + successpercent);
		System.out.println("number of blocked packets: " + blocknum);
		String blockpercent = df1.format(((double)blocknum)/(double)(blocknum+unblocknum)*100);
		System.out.println("percentage of blocked packets: " + blockpercent);
		int totalhop=0,pathnum=0;
		for(int m =0;m<sourNode.length;m++){//calculate the delay
			//if(isblock[m] == false){
			
			for(int i =0;i<25;i++){
				if(path[m][i] != -1){
					totalhop++;
				}
				if(path[m][i+1] != -1){
					if(path[m][i] >= path[m][i+1]){
						delay[m] = delay[m]+propDelay[path[m][i+1]][path[m][i]];
					}
					else{
						delay[m] = delay[m]+propDelay[path[m][i]][path[m][i+1]];
					}
				}
			}
			pathnum++;
			//System.out.println(hopnum[m]);
			//}
		}
		String hoppercent = df1.format(((double)totalhop-(double)pathnum)/(double)pathnum);
		System.out.println("average number of hops per circuit:  " + hoppercent);
		int totaldelay = 0;
		for(int m =0;m<sourNode.length;m++){
			totaldelay = totaldelay +delay[m];
		}
		String delaypercent = df1.format((double)totaldelay/(double)pathnum);
		System.out.println("average cumulative propagation delay per circuit:  " + delaypercent);
		
		

	}
}


public class RoutingPerformance { 

public static void main (String args[ ])throws Exception{ 

	
	
	String mode = args[0];
	int Packet_rate = Integer.parseInt(args[4]);
	
	
	

	
	
	
	
	
	algorithm alg1 = new algorithm();
	int[][] propDalay = new int[26][26];//store the input propagation delay
	int[][] load = new int[26][26];//store the input capacity of each link
	int flag = 0,startNode=0,lastNode=0;
	for (int n =0;n<26;n++){
		for(int m =0;m<26;m++){
			propDalay[n][m] =0;
			load[n][m] = 0;
		}
	}
	
	int count = 0;//get the line number of the input file
	File f = new File(args[3]);
	InputStream input = new FileInputStream(f);
	BufferedReader b = new BufferedReader(new InputStreamReader(input));
	String value = b.readLine();
	if(value != null)
	while(value !=null){
  		count++;
  		value = b.readLine();
  	}
	b.close();

	
	int num=0;
	char[] sourNode = new char[count];//store the input source node
	char[] destNode = new char[count];//store the input destination node
	double[] startTime = new double[count];//store the input start time
	double[] duration = new double[count];//store the input duration
	char[] N = new char[26];
	
	
	File file = new File(args[2]);  //open the file topology
	BufferedReader reader = null;  
	try {  
		
		reader = new BufferedReader(new FileReader(file));  
		String tempString = null;  
 
		while ((tempString = reader.readLine()) != null){  

			//System.out.println(tempString + "|" +flag ); 
			flag++;
			String[] stringarray =tempString.split(" ");
			startNode = (int)(stringarray[0].toCharArray()[0] - 'A');
			lastNode = (int)(stringarray[1].toCharArray()[0] - 'A');
			if(startNode <=lastNode){//because propDalay and load is n*n,but the link is bi-direcional,so only use propDalay[1][2].1<=2
			
				propDalay[startNode][lastNode] = Integer.parseInt(stringarray[2]);
				load[startNode][lastNode] = Integer.parseInt(stringarray[3]);
			}
			else{
				propDalay[lastNode][startNode] = Integer.parseInt(stringarray[2]);
				load[lastNode][startNode] = Integer.parseInt(stringarray[3]);
			}
			//System.out.print(startNode+ ":" + lastNode + ":" + propDalay[startNode][lastNode] + ":"+ load[startNode][lastNode]);
			//System.out.println((int)(stringarray[0].toCharArray()[0] - 'A'));
			
		}  
		reader.close();  
	} catch (IOException e) {  
		e.printStackTrace();  
	} finally {  
		if (reader != null){  
			try {  
				reader.close();  
			} catch (IOException e1) {  
			}  
		}  
	}  
	
	
	
	
	
	
	
	
	
	
	File file1 = new File(args[3]);  //open the file workload
	BufferedReader reader1 = null;  
	try {  

		reader1 = new BufferedReader(new FileReader(file1));  
		String tempString = null;  
 
		while ((tempString = reader1.readLine()) != null){  

			//System.out.println(tempString + "|" +flag ); 
			flag++;
			String[] stringarray =tempString.split(" ");
			sourNode[num] = stringarray[1].toCharArray()[0];
			destNode[num] = stringarray[2].toCharArray()[0];
			startTime[num] = Double.parseDouble(stringarray[0]);
			duration[num] = Double.parseDouble(stringarray[3]);
			//System.out.print(sourNode[num]+ ":" + destNode[num] + ":" + startTime[num] + ":"+ duration[num]);
			num++;
			}
			  
		reader1.close();  
	} catch (IOException e) {  
		e.printStackTrace();  
	} finally {  
		if (reader1 != null){  
			try {  
				reader1.close();  
			} catch (IOException e1) {  
			}  
		}  
	}  
	
	int Packet_num = 0;
	for(int s = 0; s< sourNode.length; s++)
	{
		Packet_num = Packet_num+ (int)(duration[s]*Packet_rate) +1;
	}
	

	
	
	
	for(int m=0;m<26;m++){			//get the N(all nodes in topology)
			for(int n=0;n<26;n++){
				if(load[m][n] != 0)
				{
					int nflag = 0;
					for(int i =0;i<26;i++){
						if (N[i] == (char)(m+'A'))
						{
							nflag =1;
						}
					}
					if (nflag==0){
						for(int j=0;j<26;j++){
							if(N[j] == 0){
								N[j] = (char)(m+'A');
								break;
							}
						}
					}
					nflag = 0;
					for(int i =0;i<26;i++){
						if (N[i] == (char)(n+'A'))
						{
							nflag =1;
						}
					}
					if (nflag==0){
						for(int j=0;j<26;j++){
							if(N[j] == 0){
								N[j] = (char)(n+'A');
								break;
							}
						}
					}
				}
			}
		}
		
		for(int m = 0; m<26;m++){
			//System.out.print(N[m]);
		}
		boolean DorH;//this is a flag that decide the input is SHP or SDP
		if(args[1].equals("SHP")){
			DorH = true;
			alg1.SP(propDalay,load,sourNode,destNode,startTime,duration,N,DorH,Packet_rate,mode);
		}
		else if (args[1].equals("SDP")){
			DorH = false;
			alg1.SP(propDalay,load,sourNode,destNode,startTime,duration,N,DorH,Packet_rate,mode);
		}
		else if (args[1].equals("LLP")){
			alg1.LLP(propDalay,load,sourNode,destNode,startTime,duration,N,Packet_rate,mode);
		}

	
		
}
}
