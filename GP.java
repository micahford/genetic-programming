import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

//represents a tree.
public class GP {
	private Node<String> root;
	private static int maxdepth = 2;
	private static Stack<String> valueStack;
	private static int x = 2;

	public GP(String rootName) {
		root = new Node<String>();
		root.value = rootName;
		root.children = new Node[2];
	}

	public static class Node<T> {
		private String value;
		private Node<T> parent;
		private Node<String>[] children = new Node[2];
		private int depthFromRoot = 0;
	}

	private static String randOperator() {
		String[] ops = { "+", "-", "*", "/" };
		Random r = new Random();
		return ops[r.nextInt(4)];
	}
	
	//returns a string representation of a random double between -5 and +5, or x.
	private static String randNum() {
		Random r = new Random();
		if(r.nextDouble()<0.5){
			return Integer.toString(x);
		}
		else {
			return String.format("%.2f",r.nextInt(10)-5 + r.nextDouble());
		}
		
	}

	public Node<String> createBinaryTree(){
		Node<String> rootNode = new Node<String>();
		rootNode.depthFromRoot = 0;
		rootNode.value =randOperator();
		
		Node<String> curr = rootNode;
		treeHelper(curr, curr.depthFromRoot);
		return rootNode;
		
	}
	
	public static void treeHelper(Node<String> currentNode, int depth){
		if(depth == maxdepth){
			return;
		}else{
			Node<String> n1 = new Node<String>();  
			if(depth == maxdepth-1){n1.value = randNum();}
			else {n1.value = randOperator();}	
			n1.parent = currentNode;
			n1.depthFromRoot = currentNode.depthFromRoot+1;
			currentNode.children[0] = n1;
			treeHelper(n1, n1.depthFromRoot);
			
			Node<String> n2 = new Node<String>();
			if(depth == maxdepth-1){n2.value = randNum();}
			else {n2.value = randOperator();}			
			n2.parent = currentNode;
			n2.depthFromRoot = currentNode.depthFromRoot+1;
			currentNode.children[1] = n2;
			treeHelper(n2, n2.depthFromRoot);
		
		}
	}

	//calls helper method (recursive method) to print tree.
	public static void printTree(Node<String> n){
		System.out.println("Current Tree: \n\n");
		printHelper(n, 0);
	}
	
	//performs inorder traversal, prints off (sideways).
	public static void printHelper(Node<String> currentNode, int depth){
		if(currentNode == null){
			return;
		}else{
			String spacing = "";
			for(int i = 0; i < depth; i++){spacing+=" ";}
			//print right child
			printHelper(currentNode.children[1], depth+1);
			for(int i = 0; i < depth; i++){spacing+=" ";}
			
			System.out.println(spacing + currentNode.value);
			//now go to left child
			printHelper(currentNode.children[0], depth+1);
		}
	}

	//This method actually calculates the value of a tree. It takes the head node of the tree as input.
	private static double valueOfTree(Node<String> head){
		valueStack = new Stack<String>(); //resets the "valueStack"
		valueHelper(head, 0); //Now, "loads" the value stack.
		reverseStack(); //Now, reverses the value stack.

		double total = calculateStack(); //now, convert from postfix (valueStack) to an infix and calculate.
		return total;
	}
	
	//reverses valueStack. very simple.
	private static void reverseStack(){
		Queue<String> tempQueue = new LinkedList<String>();
		int size = valueStack.size();
		for(int i = 0; i< size; i ++){
			tempQueue.add(valueStack.pop());
		}
		for(int i = 0; i < size; i++){
			valueStack.push(tempQueue.poll());
		}
	}
	
	/*
	 *Here is where the bulk of the calculations take place. Essentially, 
	 *you run through the valueStack (which contains everything in the tree).
	 *If you get to a number, you add it to a helper stack. If you get to an operator,
	 *you pop the top two values of the helper stack, perform the operation on those
	 *two values, and the push that new value back on top of the helper stack. At the 
	 *end of this algorithm, there will be only one value in the helper stack--the final
	 *answer.
	 */
	private static double calculateStack(){
		Stack<Double> values = new Stack<Double>(); //temp stack--only holds numbers (no operators).
		int size = valueStack.size();
		for(int i = 0; i < size;i++){
			String s = valueStack.pop();
			if(isOperator(s)) {
				double n1 = values.pop();
				double n2 = values.pop();
				double n3 = calc(n1,n2,s);
				values.add(n3);
			} else{
				values.add(Double.parseDouble(s));
			}
		}
		return values.peek();
	}
	
	//takes 2 double values, and a string representation of an operator, and returns value of the equation (val1 __ val2)
	private static double calc(double val1, double val2, String op){
		if(op == "+"){
			return val1+val2;
		}
		else if (op == "-"){
			return val2-val1;
		}
		else if (op == "*"){
			return val1*val2;
		}
		else return val2/val1;
	}
	
	//returns true if string s is an operator
	private static boolean isOperator(String s) {
		return ((s=="*")||(s=="/")||(s=="-")||(s=="+"));
	}

	/*
	 * This helper method performs a traversal of the tree and adds all the values to valueStack.
	 */
	private static void valueHelper(Node<String> currentNode, int depth){
		if(currentNode == null){
			return;
		}else{
			//go to left child
			valueHelper(currentNode.children[0], depth+1);
			//now go to right child
			valueHelper(currentNode.children[1], depth+1);
			valueStack.push(currentNode.value);
		}
	}
	
	
	public static void main(String args[]) {
		GP gp = new GP("+");	
		
		Node<String> foo = gp.createBinaryTree();
		printTree(foo);
		System.out.println("value of tree: "+valueOfTree(foo));

		
	}
}
