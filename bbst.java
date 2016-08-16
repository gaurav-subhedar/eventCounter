import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class bbst {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//check if arguments are present or not
		if(args.length!=1){
			System.out.println("Invalid arguments!");
			System.exit(0);
		}
		else{
			//read the initial event ids and counts into separate arrays
			
			BufferedReader br = null;
			try{
				br = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));				
				String newLine;
				int lineCount=0;
				newLine = br.readLine();
				if(newLine==null){
					System.out.println("Invalid line count!");
					System.exit(1);
				}
				else{
					
					lineCount = Integer.parseInt(newLine);
					
					int[] eventArr = new int [lineCount];
					int[] countArr = new int [lineCount];
					
					StringTokenizer tokenizer;
					
					for(int i=0;i<lineCount;i++){
						newLine = br.readLine();
						if(newLine!=null){
							tokenizer = new StringTokenizer(newLine, " ");
							eventArr[i] = Integer.parseInt(tokenizer.nextToken());
							countArr[i] = Integer.parseInt(tokenizer.nextToken());							
						}
					}
					
					/*calculate mid, create root node with eventId and count with index mid
					 * all elements with index<mid will be in left subtree, all elements with index > mid will be in right subtree
					 */
					int length = eventArr.length-1;
					int mid = length/2;
					
					AVLTreeNode root = new AVLTreeNode(eventArr[mid], countArr[mid]);
					root.leftChild = root.buildTree(eventArr, countArr, 0, mid-1);
					root.rightChild = root.buildTree(eventArr, countArr, mid+1, length);

					//establish parent pointers and balance factors
					root.establishParentPointers(root);		
					
					root.establishBalanceFactors(root);
					
					if(root!=null){
						//read commands from standard input, call respective functions of AVLTreeNode class
						Scanner scanner = new Scanner(System.in);
						StringTokenizer tokenizer2;
						String line, command;
						int arg1,arg2,resInt;
						AVLTreeNode res=null;
						
						while(scanner.hasNext()){
							line = scanner.nextLine();
							tokenizer2 = new StringTokenizer(line, " ");
							command = tokenizer2.nextToken();
							if(command.equals("increase")){
								arg1 = Integer.parseInt(tokenizer2.nextToken());
								arg2 = Integer.parseInt(tokenizer2.nextToken());
								root = root.insert(root,arg1, arg2);
							}
							else if(command.equals("reduce")){
								arg1 = Integer.parseInt(tokenizer2.nextToken());
								arg2 = Integer.parseInt(tokenizer2.nextToken());
								root = root.delete(root, arg1, arg2,1);
							}
							else if(command.equals("next")){
								arg1 = Integer.parseInt(tokenizer2.nextToken());
								res = root.Next(root, arg1);
								if(res!=null)
									System.out.println(res.eventId + " " + res.count);
								else
									System.out.println("0 0");
							}
							else if(command.equals("count")){
								arg1 = Integer.parseInt(tokenizer2.nextToken());
								resInt = root.Count(root, arg1);
								System.out.println(resInt);
							}
							else if(command.equals("previous")){
								arg1 = Integer.parseInt(tokenizer2.nextToken());
								res = root.Previous(root, arg1);
								if(res!=null)
									System.out.println(res.eventId + " " + res.count);
								else
									System.out.println("0 0");
							}
							else if(command.equals("inrange")){
								arg1 = Integer.parseInt(tokenizer2.nextToken());
								arg2 = Integer.parseInt(tokenizer2.nextToken());
								resInt = root.countInRange(root, arg1, arg2);
								System.out.println(resInt);
							}
							else if(command.equals("quit")){
								System.exit(0);
							}
							else{
								System.out.println("Invalid Command!");
							}
						}
					}
					
					
				}
			}
			catch(FileNotFoundException exc){
				System.out.println("File not found!");
				exc.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}



}
