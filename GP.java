import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

//represents a tree.
public class GP {
	private static int maxdepth = 3;
	private static Stack<String> valueStack;
	private static Node<String> rootNode;

	public GP(String rootName) {
	}

	public static class Node<T> {
		private int numNodes;
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

	// returns a string representation of a random double between -5 and +5, or
	// x.
	private static String randNum() {
		Random r = new Random();
		if (r.nextDouble() < 0.5) {
			return "x";
		} else {
			return String.format("%.2f", r.nextInt(10) - 5 + r.nextDouble());
		}
	}

	public Node<String> createBinaryTree() {
		rootNode = new Node<String>();
		rootNode.depthFromRoot = 0;
		rootNode.value = randOperator();
		Node<String> curr = rootNode;
		treeHelper(curr, curr.depthFromRoot);
		int originalNumNodes = (int)Math.pow(2, maxdepth+1) - 1;
		rootNode = prune(rootNode);
		return rootNode;
	}

	//takes a uniform binary tree and "prunes" it creating a jagged binary tree.
	private static Node<String> prune(Node<String> n){
		Node<String> head = n;
		Random r = new Random();
		int depth = r.nextInt(maxdepth);
		int i = 0;
		while(i < depth -1){
			if(r.nextDouble()<.5) n = n.children[0];
			else n = n.children[1];
			i++;
		}
		//now, prune:
		if(r.nextDouble()<.5) {
			n.children[0].value = randNum();
			n.children[0].children[0] = null; n.children[0].children[1] = null;

		} 
		else {
			n.children[1].value= randNum();
			n.children[1].children[0] = null; n.children[1].children[1] = null;

		}
				
		return head;
	}
	
	public static void treeHelper(Node<String> currentNode, int depth) {
		Random rand = new Random();
		if (depth == maxdepth) {
			return;
		} else {
			Node<String> n1 = new Node<String>();
			if (depth == maxdepth - 1) {
				n1.value = randNum();
			} else {
				n1.value = randOperator();
			}
			n1.parent = currentNode;
			n1.depthFromRoot = currentNode.depthFromRoot + 1;
			currentNode.children[0] = n1;
			treeHelper(n1, n1.depthFromRoot);
			Node<String> n2 = new Node<String>();
			if (depth == maxdepth - 1) {
				n2.value = randNum();
			} else {
				n2.value = randOperator();
			}
			n2.parent = currentNode;
			n2.depthFromRoot = currentNode.depthFromRoot + 1;
			currentNode.children[1] = n2;
			
			treeHelper(n2, n2.depthFromRoot);
		}
	}

	// calls helper method (recursive method) to print tree.
	public static void printTree(Node<String> n) {
		// System.out.println("Current Tree: \n\n");
		printHelper(n, 0);
	}

	// performs inorder traversal, prints off (sideways).
	private static void printHelper(Node<String> currentNode, int depth) {
		if (currentNode == null) {
			return;
		} else {
			String spacing = "";
			for (int i = 0; i < depth; i++) {
				spacing += " ";
			}
			// print right child
			printHelper(currentNode.children[1], depth + 1);
			for (int i = 0; i < depth; i++) {
				spacing += " ";
			}
			System.out.println(spacing + currentNode.value);
			// now go to left child
			printHelper(currentNode.children[0], depth + 1);
		}
	}

	// This method actually calculates the value of a tree. It takes the head
	// node of the tree as input.
	// d is a placeholder for x
	private static double valueOfTree(Node<String> head, double d) {
		valueStack = new Stack<String>(); // resets the "valueStack"
		change_node(head, "x", Double.toString(d)); // change all the blank x's
													// to the string
													// representation of the
													// parameter for the
													// function.
		valueHelper(head, 0); // Now, "loads" the value stack.
		reverseStack(); // Now, reverses the value stack.
		double total = calculateStack(); // now, convert from postfix
											// (valueStack) to an infix and
											// calculate.\
		// printTree(head);
		change_node(head, Double.toString(d), "x"); // now, change all the
													// parameter values back to
													// x's (so that the same
													// tree can be called for
													// different values of the
													// paremeter).
		return total;
	}

	// let's go through tree and change all the "old values" to the
	// "new values".
	private static void change_node(Node<String> head, String oldValue,
			String newValue) {
		change_node_helper(head, oldValue, newValue);
	}

	private static void change_node_helper(Node<String> currentNode,
			String oldValue, String newValue) {
		if (currentNode == null) {
			return;
		} else {
			// call method on right child.
			change_node_helper(currentNode.children[1], oldValue, newValue);
			// **make actual replacement** from old value to new value.
			if (currentNode.value.equals(oldValue)) {
				currentNode.value = newValue;
			}
			// now call method on left child
			change_node_helper(currentNode.children[0], oldValue, newValue);
		}
	}

	/*
	 * This helper method performs a traversal of the tree and adds all the
	 * values to valueStack.
	 */
	private static void valueHelper(Node<String> currentNode, int depth) {
		if (currentNode == null) {
			return;
		} else {
			// go to left child
			valueHelper(currentNode.children[0], depth + 1);
			// now go to right child
			valueHelper(currentNode.children[1], depth + 1);
			valueStack.push(currentNode.value);
		}
	}

	// reverses valueStack. very simple.
	private static void reverseStack() {
		Queue<String> tempQueue = new LinkedList<String>();
		int size = valueStack.size();
		for (int i = 0; i < size; i++) {
			tempQueue.add(valueStack.pop());
		}
		for (int i = 0; i < size; i++) {
			valueStack.push(tempQueue.poll());
		}
	}

	/*
	 * Here is where the bulk of the calculations take place. Essentially,you
	 * run through the valueStack (which contains everything in the tree).If you
	 * get to a number, you add it to a helper stack. If you get to an operator,
	 * you pop the top two values of the helper stack, perform the operation on
	 * thosetwo values, and the push that new value back on top of the helper
	 * stack. At theend of this algorithm, there will be only one value in the
	 * helper stack--the finalanswer.
	 */
	private static double calculateStack() {
		Stack<Double> values = new Stack<Double>(); // temp stack--only holds
													// numbers (no operators).
		int size = valueStack.size();
		for (int i = 0; i < size; i++) {
			String s = valueStack.pop();
			if (isOperator(s)) {
				double n1 = values.pop();
				double n2 = values.pop();
				double n3 = calc(n1, n2, s);
				values.add(n3);
			} else {
				values.add(Double.parseDouble(s));
			}
		}
		return values.peek();
	}

	// takes 2 double values, and a string representation of an operator, and
	// returns value of the equation (val1 __ val2)
	private static double calc(double val1, double val2, String op) {
		if (op == "+") {
			return val1 + val2;
		} else if (op == "-") {
			return val2 - val1;
		} else if (op == "*") {
			return val1 * val2;
		} else
			return val2 / val1;
	}

	// returns true if string s is an operator
	private static boolean isOperator(String s) {
		return ((s == "*") || (s == "/") || (s == "-") || (s == "+"));
	}

	// calculates the variance of our function from the mystery function
	private static double fitnessScore(Node<String> foo)
			throws FileNotFoundException {
		double x = -10;
		double score = -1;
		Scanner scan = new Scanner(new File("Generator1Runs.txt"));
		double sumOfVariancesSq = 0;
		while (scan.hasNext()) {
			double x1 = scan.nextDouble();
			double y1 = scan.nextDouble();

			double ourVal = valueOfTree(foo, x);
			sumOfVariancesSq += (y1 - ourVal) * (y1 - ourVal) * .01;
			x += .01;
		}
		score = sumOfVariancesSq;

		return score;
	}

	// takes 2 trees, and returns the 2 children created from them.
	private static Node<String>[] makeChildren(Node<String> parent1,
			Node<String> parent2) {

		Node<String>[] children = new Node[2];
		

		String val1 = parent1.value;
		String val2 = parent2.value;
		Node<String> newRoot1 = new Node<String>();
		Node<String> newRoot2 = new Node<String>();

		newRoot1.value = val1;
		newRoot1.children[0] = parent1.children[0];
		newRoot1.children[1] = parent2.children[1];
		parent1.children[0].parent = newRoot1;
		parent2.children[0].parent = newRoot1;

		newRoot2.value = val2;
		newRoot2.children[0] = parent2.children[0];
		newRoot2.children[1] = parent1.children[1];
		parent1.children[1].parent = newRoot2;
		parent2.children[1].parent = newRoot2;

		children[0] = newRoot1;
		children[1] = newRoot2;
		return children;
	}

	private static Map<Double, Node<String>> makeProbabilityDistribution(
			LinkedList<Node<String>> population) throws FileNotFoundException {
		double totalScore = 0.0;
		Map<Double, Node<String>> reciprocalMap = new TreeMap<Double, Node<String>>();
		for (int i = 0; i < population.size(); i++) {
			GP gp = new GP("+");
			Node<String> tree = population.get(i);
			double score = 1.0 / fitnessScore(tree) + 1;

			totalScore += score;
			reciprocalMap.put(totalScore, tree);
		}

		return reciprocalMap;
	}

	private static double getMaxKey(Map<Double, Node<String>> m) {
		double max = 0;
		for (double d : m.keySet()) {
			if (d > max)
				max = d;
		}
		return max;

	}

	private static Node<String> getParent(double rand,
			Map<Double, Node<String>> distribution) {
		for (double d : distribution.keySet()) {
			if (d > rand)
				return distribution.get(d);
		}
		System.out
				.println("Error has occured. getParent has not found the parent to select for \"reproduction\"");

		return null;
	}

	private static Node<String> mutate(Node<String> n){
		Node<String> head = n;
		Random r = new Random();
		while ((n.children[0]!=null)||(n.children[1]!=null)){
			double mutateThisNode = r.nextDouble();
			if(mutateThisNode < .2) { //mutate this node
				n.value = randOperator();
				return head;
			}
			double whichChild = r.nextDouble();

			if(whichChild<.5){
				n = n.children[0];
			}else {
				n = n.children[1];
			}
		}
		n.value = randNum(); //if it's reached to here, it is at a leaf.
		return head;
	}
	
	
	private static double getFittestTree(LinkedList<Node<String>> l)
			throws FileNotFoundException {
		double min = 99999999;
		Node<String> fittest = new Node<String>();
		for (Node<String> n : l) {
			if (fitnessScore(n) < min) {
				min = fitnessScore(n);
				fittest = n;
			}
		}
		printTree(fittest);
		return min;
	}

	public static void main(String args[]) throws FileNotFoundException {

		int numberOfTrees = 500;
		LinkedList<Node<String>> parents = new LinkedList<Node<String>>();
		GP gp = new GP("+");
		
		Node<String> test = gp.createBinaryTree();
		//printTree(test);
		System.out.println(valueOfTree(test, 2));
		//System.exit(1);
		for (int i = 0; i < numberOfTrees; i++) {
			parents.add(gp.createBinaryTree());
		}

		Map<Double, Node<String>> probabilityDist = makeProbabilityDistribution(parents);
		// double totalScore = getMaxKey(probabilityDist);

		Random rn = new Random();

		LinkedList<Node<String>> children = new LinkedList<Node<String>>();

		for (int i = 0; i < 10; i++) {
			double totalScore = getMaxKey(probabilityDist);

			System.out.println("gen: " + (i + 1));
			children = new LinkedList<Node<String>>();

			while (children.size() <= numberOfTrees) {
				double num1 = rn.nextDouble() * totalScore;
				double num2 = rn.nextDouble() * totalScore;

				Node<String> parent1 = getParent(num1, probabilityDist);
				Node<String> parent2 = getParent(num2, probabilityDist);
				Node<String>[] childs = makeChildren(parent1, parent2);

				// System.out.println("Parent selected -- score: " +
				// fitnessScore(parent1));
				// System.out.println("Parent selected -- score: " +
				// fitnessScore(parent2));
				double mutateChance = .1;
				if(rn.nextDouble()<mutateChance){
					childs[0]=mutate(childs[0]);
				}
				if(rn.nextDouble()<mutateChance){
					childs[1]=mutate(childs[1]);
				}

				children.add(parent1);
				children.add(parent2);
				children.add(childs[0]);
				children.add(childs[1]);

			}
			double currFittest = getFittestTree(children);
			System.out.println("Current fittest: " + currFittest);
			if (currFittest < .05) {
				System.out.println("DONE.");
				System.exit(1);
			}
			probabilityDist = makeProbabilityDistribution(children);
		}
		System.out.printf("Fittest score of parents = %f\n",
				getFittestTree(parents));
		System.out.printf("Fittest score of children = %f\n\n",
				getFittestTree(children));

		// for(Node<String> n: children){
		// System.out.println("Score: " + fitnessScore(n));
		// }
		int x = 2; // **the parameter to our tree**.

		// Node<String> foo = gp.createBinaryTree();
		// //printTree(foo); //prints tree with x's.

	}
}
