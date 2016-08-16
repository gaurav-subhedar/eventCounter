import javax.swing.event.AncestorEvent;

public class AVLTreeNode {
	
	/* declare class variables, values to be stored at each node 
	 */
	 
	int eventId;
	int count;
	int bf;
	AVLTreeNode leftChild;
	AVLTreeNode rightChild;
	AVLTreeNode parent;
	
	//constructor to initialize values	
	
	AVLTreeNode(int eventId, int count){
		this.eventId=eventId;
		this.count=count;
		this.bf=0;
		this.leftChild=null;
		this.rightChild=null;
		this.parent=null;
	}
	
	public void display(AVLTreeNode root){
		int level=1;
		AVLTreeNode temp=root;
		while(temp!=null){
			System.out.println(level + " : " + temp.eventId + ", " + temp.count +  ", " + temp.bf + ", ");			
			if(temp.leftChild!=null)
				System.out.println(" left : " + temp.leftChild.eventId);
			if(temp.rightChild!=null)
				System.out.println(" right : " + temp.rightChild.eventId);
			if(temp.leftChild!=null)
				display(temp.leftChild);
			if(temp.rightChild!=null)
				display(temp.rightChild);
			temp=null;
		}
	}
		
	
	/* Count method to get the count of node with eventId = eventID
	 * 
	 */
	public int Count(AVLTreeNode root, int eventId){
		while(root!=null){
			if(eventId==root.eventId)
				return root.count;
			else if(eventId < root.eventId)
				root=root.leftChild;
			else
				root=root.rightChild;
		}
		return 0;
	}
	
	/*Builds tree from two event and count arrays. Mid element becomes the root, left to mid-1 becomes left subtree,
	 *  mid+1 to right becomes right subtree.
	 */
	
	public AVLTreeNode buildTree(int[] eventIdArr, int[] countArr, int l, int r){
		if(l>r)
			return null;
		int mid = (l+r)/2;
		AVLTreeNode root=new AVLTreeNode(eventIdArr[mid],countArr[mid]);
		root.leftChild=buildTree(eventIdArr, countArr, l, mid-1);
		root.rightChild=buildTree(eventIdArr, countArr,mid+1,r);
		return root;		
	}
	
	/*Establishes pointers to parents for each node. Called after initial buildTree()
	 * 
	 */
	public void establishParentPointers(AVLTreeNode root){
		if(root.leftChild!=null){
			root.leftChild.parent=root;
			establishParentPointers(root.leftChild);
		}
		if(root.rightChild!=null){
			root.rightChild.parent=root;
			establishParentPointers(root.rightChild);
		}
		return;
	}
	
	/*
	 * Establishes balance factors recursively for all nodes. Called after buildTree() and establishParantPointers
	 */
	public void establishBalanceFactors(AVLTreeNode root){
		
		if(root!=null){
			calculateBalanceFactor(root);			
			establishBalanceFactors(root.leftChild);
			establishBalanceFactors(root.rightChild);
		}
		return;
	}
	
	/*
	 * Counts the number of nodes with eventId : lId<=eventID<=rId
	 */
	public int countInRange(AVLTreeNode root, int lId, int rId){
		if(root==null)
			return 0;
		else if(lId <= root.eventId && rId >= root.eventId)
			return (1 + countInRange(root.leftChild, lId, rId) + countInRange(root.rightChild, lId, rId));
		else if(lId < root.eventId)
				return countInRange(root.leftChild, lId, rId);
		else
			return countInRange(root.rightChild, lId, rId);
	}
	
	/*
	 * Gives the inorder successor of the node with eventID. 
	 */
	
	public AVLTreeNode Next(AVLTreeNode root, int eventID){
		
		AVLTreeNode temp=root,temp2=null,p=null;
		String tag="";
		while(temp!=null && temp.eventId!=eventID){
			if(eventID < temp.eventId){
				p=temp;
				temp=temp.leftChild;				
				tag="L";
			}
			else{
				p=temp;
				temp=temp.rightChild;
				tag="R";
			}
		}
		if(temp==null){
			if(tag.equalsIgnoreCase("L"))
				return p;
			if(tag.equalsIgnoreCase("R"))
				return(Next(root,p.eventId));
		}			
		if(temp.rightChild!=null){
			temp2=temp.rightChild;
			while(temp2.leftChild!=null)
				temp2=temp2.leftChild;
			return temp2;
		}
		else if(temp.rightChild==null && temp.parent==null)
			return null;
		else{
			temp=temp.parent;
			while(temp!=null && temp.eventId<eventID)
				temp=temp.parent;
			return temp;
		}
	}
	
	/*
	 * Gives inorder predecessor of the node with eventID.
	 */
	
	
	public AVLTreeNode Previous(AVLTreeNode root, int eventID){
		
		AVLTreeNode temp=root, temp2=null, p=null;
		String tag="";
		while(temp!=null && temp.eventId!=eventID){
			if(eventID < temp.eventId){
				p=temp;
				temp=temp.leftChild;
				tag="L";
			}
			else{
				p=temp;
				temp=temp.rightChild;
				tag="R";
			}
		}
		if(temp==null){
			if(tag.equalsIgnoreCase("R"))
				return p;
			if(tag.equalsIgnoreCase("L"))
				return Previous(root, p.eventId);			
			return null;
		}
			
		if(temp.leftChild!=null){
			temp2=temp.leftChild;
			while(temp2.rightChild!=null)
				temp2=temp2.rightChild;
			return temp2;
		}
		else if(temp.leftChild==null && temp.parent==null)
			return null;
		else{
			temp=temp.parent;
			while(temp!=null && temp.eventId>eventID)
				temp=temp.parent;
			return temp;
		}
				
	}
	
	/*
	 * Reduces the count of node by "count" with eventID=eventId. If the count goes below 1, removes that node from tree. 
	 * Flag = 1 or 0. 1 denotes the call is an Original delete call, 0 denotes the call is a recursive call, called if the node to 
	 * be deleted is not a leaf node. Returns new root after deletion and rotation
	 */
	
	public AVLTreeNode delete(AVLTreeNode root, int eventId, int count, int flag){
		
		AVLTreeNode temp1=root,temp2=null,temp3=null;
		int tempEventId, tempCount;
		String tag="";
		
		//if root is the given node
		if(root.eventId==eventId && root.leftChild==null && root.rightChild==null){
			root.count = root.count - count;
			if(root.count<=0){
				System.out.println("0");
				root=null;
			}
			System.out.println(root.count);
			return root;
		}
		
		//find the node
		while(temp1!=null){
			if(temp1.eventId==eventId)
				break;
			if(eventId > temp1.eventId)
				temp1=temp1.rightChild;
			else
				temp1=temp1.leftChild;
		}
		
		//if node not found, print 0 and return
		if(temp1==null){
			System.out.println("0");
			return root;
		}
		
		if(temp1!=null && temp1.eventId==eventId){
			
			temp1.count=temp1.count-count;
			if(temp1.count<=0){
				/*check if the call to delete is an Original call or a Recursive call. Recursive call will
				 * be made when the node to be deleted is not a  leaf node. Print only if call is Originally for delete.
				 */
				if(flag==1)
					System.out.println("0");
				
				//node is leaf node
				if(temp1.leftChild==null && temp1.rightChild==null){
					
					temp2=temp1.parent;
					if(temp1.eventId<temp2.eventId){
						temp2.leftChild=null;
						tag="L";
					}
					else{
						temp2.rightChild=null;
						tag="R";
					}
					//adjust balance factors after deletion
					AVLTreeNode subTreeRoot = adjustBalanceFactorsForDelete(temp2,tag);
					if(subTreeRoot!=null && subTreeRoot.parent==null)
						root=subTreeRoot;
					return root;
					
				}
				else{
					/*
					 * if node to be deleted is not a leaf node, then find the max in left subtree, delete that node and replace current
					 * node's eventId and count by that node
					 */
					
					AVLTreeNode maxInLeft = findMaxInLeftSubTree(temp1);				
					if(maxInLeft==null){
						temp2=temp1.parent;
						if(temp1.eventId<temp2.eventId){
							temp2.leftChild=temp1.rightChild;
							temp2.leftChild.parent=temp2;
						}
						else{
							temp2.rightChild=temp1.rightChild;
							temp2.rightChild.parent=temp2;
						}
						return root;
					}
					else{
						tempEventId=maxInLeft.eventId;
						tempCount=maxInLeft.count;
						//set flag to 0, since we don't need to print this delete
						root=delete(root, maxInLeft.eventId, maxInLeft.count,0);
						temp2=root;
						while(temp2.eventId!=eventId){
							if(eventId>temp2.eventId)
								temp2=temp2.rightChild;
							else
								temp2=temp2.leftChild;
						}
						temp2.eventId=tempEventId;
						temp2.count=tempCount;
						return root;
					}
						
				}
			}
			else{
				System.out.println(temp1.count);
				return root;
			}
		}
		
			
		return root;
	}
	
	/*
	 * adjusts the balance factors of subtree after deletion. Rotate functions are called, new root of the subtree after
	 * rotation is returned 
	 */
	public AVLTreeNode adjustBalanceFactorsForDelete(AVLTreeNode currNode, String tag){
		
		AVLTreeNode BNode;
		AVLTreeNode ANode;
		AVLTreeNode CNode;
		AVLTreeNode temp2 = null,temp3=null;
		AVLTreeNode temp=currNode;
		
		int p=0,gp=0;
		//find ancestor with bf=2 or -2
		while(temp!=null){
			p=calculateBalanceFactor(temp);			
			if(p==-2 || p==2)
				break;
			temp=temp.parent;			
		}
		
		if(p==2 || p==-2){
			BNode=temp;
			if(tag.equalsIgnoreCase("L"))
				ANode=BNode.rightChild;
			else
				ANode=BNode.leftChild;
						
			//call rotations depending on the sibling's bf and tag, recalculate balance factors after rotations
			if((ANode.bf==0 || ANode.bf==1) && tag.equalsIgnoreCase("L")){
				temp2=rotateLL(ANode);
				establishBalanceFactors(BNode);
				return temp2;
			}
			else if((ANode.bf==0 || ANode.bf==1) && tag.equalsIgnoreCase("R")){
				temp2=rotateRR(ANode);				
				establishBalanceFactors(BNode);
				return temp2;
			}
			else if(ANode.bf==-1 && tag.equals("L")){
				CNode=ANode.leftChild;
				temp3=rotateRR(CNode);
				temp2=rotateLL(temp3);
				establishBalanceFactors(BNode);
				return temp2;
			}
			else if(ANode.bf==-1 && tag.equals("R")){
				CNode=ANode.rightChild;
				temp3=rotateLL(CNode);
				temp2=rotateRR(temp2);
				establishBalanceFactors(BNode);
				return temp2;
			}
			
		}			
	
		return temp;
	}

	/*
	 * returns the node with max eventid in the left subtree of given node
	 * 
	 */
	
	public AVLTreeNode findMaxInLeftSubTree(AVLTreeNode node){
		
		AVLTreeNode temp=node.leftChild;
		if(temp==null){
			return null;
		}
		while(temp.rightChild!=null){
			temp=temp.rightChild;			
		}
		return temp;
		
	}
	
	/*
	 * increase the count of given node by "count". if node is not present, then create new node and insert into tree.
	 * balance the tree and calculate balance factors after insertion of new node. 
	 */
	public AVLTreeNode insert(AVLTreeNode root, int eventId, int count){
		
		AVLTreeNode newNode=new AVLTreeNode(eventId, count);
				
		if(root==null){			
			root=newNode;
			return root;
		}
		AVLTreeNode temp=root,parent=null;
		//find the node
		while(temp!=null){
			//if node is present, increase count and return
			if(newNode.eventId==temp.eventId){
				temp.count=temp.count+count;
				System.out.println(temp.count);
				return root;
			}
			parent=temp;
			if(newNode.eventId<temp.eventId){				
				temp=temp.leftChild;
			}
			else{
				temp=temp.rightChild;
			}
		}
		//if node not present, create new node and establish child and parent pointers
		newNode.parent=parent;
		
		if(newNode.eventId>parent.eventId){
			parent.rightChild=newNode;
		}			
		else{
			parent.leftChild=newNode;
		}
		//calculate balance factors, if tree is not balanced then perform corresponding rotations
		AVLTreeNode subTreeRoot = adjustBalanceFactorsForInsert(newNode);
		if(subTreeRoot!=null && subTreeRoot.parent==null)
			root=subTreeRoot;
		System.out.println(newNode.count);
		return root;
	}
	
	/*
	 * adjust balance factors of all nodes of subtree with root=given node
	 */
	public AVLTreeNode adjustBalanceFactorsForInsert(AVLTreeNode currNode){
		
		AVLTreeNode BNode;
		AVLTreeNode ANode;
		AVLTreeNode temp2 = null,temp3=null;
		AVLTreeNode temp=currNode;
		
		int p=0,p1=0;
		//find ancestor with balance factor = 2 or -2
		while(temp!=null){
			p=calculateBalanceFactor(temp);			
			if(p==-2 || p==2)
				break;
			temp=temp.parent;			
		}
		
		if(p==2 || p==-2){
			BNode=temp;
			if(currNode.eventId>BNode.eventId)
				ANode=BNode.rightChild;
			else
				ANode=BNode.leftChild;
			
			//perform rotations depending on new node's position in tree, recalculate balance factors after rotations		
			if(currNode.eventId>BNode.eventId && currNode.eventId>ANode.eventId){
				//RR
				System.out.println("LL");
				temp2=rotateLL(ANode);
				p1=calculateBalanceFactor(temp2);
			}
			else if(currNode.eventId>BNode.eventId && currNode.eventId<ANode.eventId){
				//LR rotation, call RR follwed by LL
				System.out.println("RL");
				temp2=rotateRR(currNode.parent);
				temp2=rotateLL(temp2);
				p1=calculateBalanceFactor(temp2);
			}
			else if(currNode.eventId<BNode.eventId && currNode.eventId>ANode.eventId){
				//RL rotation, call LL followed by RR
				System.out.println("LR");
				temp2=rotateLL(currNode.parent);
				temp2=rotateRR(temp2);
				p1=calculateBalanceFactor(temp2);
			}
			else if(currNode.eventId<BNode.eventId && currNode.eventId<ANode.eventId){
				//LL
				System.out.println("RR");
				temp2=rotateRR(ANode);
				p1=calculateBalanceFactor(temp2);
			}	
			return temp2;
		}		
	
		return temp;
	}
	
	//left rotation at node p, return root of subtree after rotation
	
	public AVLTreeNode rotateLL(AVLTreeNode p){
		AVLTreeNode gp=p.parent;
		AVLTreeNode ggp=gp.parent;
		
		gp.rightChild=p.leftChild;
		if(gp.rightChild!=null)
			gp.rightChild.parent=gp;
		
		p.leftChild=gp;
		if(p.leftChild!=null)
			p.leftChild.parent=p;
		
		if(ggp!=null){
			if(p.eventId<ggp.eventId)
				ggp.leftChild=p;
			else
				ggp.rightChild=p;
		}
		
		p.parent=ggp;
		
		return p;
	}
	
	//right rotation at p, return root of subtree after rotation
	
	public AVLTreeNode rotateRR(AVLTreeNode p){
		AVLTreeNode gp=p.parent;
		AVLTreeNode ggp=gp.parent;
		
		gp.leftChild=p.rightChild;
		if(gp.leftChild!=null)
			gp.leftChild.parent=gp;
		
		p.rightChild=gp;
		if(p.rightChild!=null)
			p.rightChild.parent=p;
		
		if(ggp!=null){
			if(p.eventId<ggp.eventId)
				ggp.leftChild=p;
			else
				ggp.rightChild=p;
		}
		p.parent=ggp;
		
		return p;
		
	}
	
	//calculate balance factor of given node
	public int calculateBalanceFactor(AVLTreeNode currNode){
		currNode.bf=getHeightOfSubtree(currNode.leftChild)-getHeightOfSubtree(currNode.rightChild);
		return(currNode.bf);
	}
	
	//calculate height of subtree with root = given node
	public int getHeightOfSubtree(AVLTreeNode node){
		if(node==null)
			return 0;
		else{
			return (1 + Math.max(getHeightOfSubtree(node.leftChild), getHeightOfSubtree(node.rightChild)));
		}
	}
	
}
