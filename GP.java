import java.awt.List;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

//represents a tree.
public class GP {
	private Node<String> root;
	private static int maxdepth = 3;

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
			n1.value = randOperator();	
			n1.parent = currentNode;
			n1.depthFromRoot = currentNode.depthFromRoot+1;
			currentNode.children[0] = n1;
			treeHelper(n1, n1.depthFromRoot);
			
			Node<String> n2 = new Node<String>();
			n2.value = randOperator();
			n2.parent = currentNode;
			n2.depthFromRoot = currentNode.depthFromRoot+1;
			currentNode.children[1] = n2;
			treeHelper(n2, n2.depthFromRoot);
		
		}
	}

	
	public static void printTree(Node<String> n){
		printHelper(n, 0);
	
	}
	//performs inorder traversal.
	public static void printHelper(Node<String> currentNode, int depth){
		if(currentNode == null){
			return;
		}else{
			String spacing = "";
			for(int i = 0; i < depth; i++){spacing+=" ";}
			
			printHelper(currentNode.children[0], depth+1);
			for(int i = 0; i < depth; i++){spacing+=" ";}
			
			System.out.println(spacing + currentNode.value);
			printHelper(currentNode.children[1], depth+1);
		}
	}

	
	public static void main(String args[]) {
		GP gp = new GP("+");	
		
		Node<String> foo = gp.createBinaryTree();
		printTree(foo);

		
	}
}
