/*************************************************************************
 *  Binary Search Tree class.
 *  Adapted from Sedgewick and Wayne.
 *
 *  @version 3.0 1/11/15 16:49:42
 *
 *  @author TODO
 *
 *************************************************************************/

import static org.junit.Assert.assertEquals;

import java.util.NoSuchElementException;

public class BST<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BST

    /**
     * Private node class.
     */
    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int N;             // number of nodes in subtree

        public Node(Key key, Value val, int N) {
            this.key = key;
            this.val = val;
            this.N = N;
        }
    }

    // is the symbol table empty?
    public boolean isEmpty() { return size() == 0; }

    // return number of key-value pairs in BST
    public int size() { return size(root); }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    /**
     *  Search BST for given key.
     *  Does there exist a key-value pair with given key?
     *
     *  @param key the search key
     *  @return true if key is found and false otherwise
     */
    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     *  Search BST for given key.
     *  What is the value associated with given key?
     *
     *  @param key the search key
     *  @return value associated with the given key if found, or null if no such key exists.
     */
    public Value get(Key key) { return get(root, key); }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

    /**
     *  Insert key-value pair into BST.
     *  If key already exists, update with new value.
     *
     *  @param key the key to insert
     *  @param val the value associated with key
     */
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Tree height.
     *
     * Asymptotic worst-case running time using Theta notation: O(h), where h is the height of the tree.
     * Justification: The function uses recursion to go through each node in the tree. In the worst case the function would have to go 
     * 				  through every node in the tree to get the longest path to the root that would indicate the height. In this case the 
     * 				  function would depend on the height of the tree therefore the worst case running time is O(h).
     *
     * @return the number of links from the root to the deepest leaf.
     *
     * Example 1: for an empty tree this should return -1.
     * Example 2: for a tree with only one node it should return 0.
     * Example 3: for the following tree it should return 2.
     *   B
     *  / \
     * A   C
     *      \
     *       D
     */
    public int height() {
    	if(isEmpty())
      	  return -1;
    	if(root.left == null && root.right == null)
    		return 0;
    	else
    		return height(root);
    }
    private int height(Node root)
    {
    	if(root == null)
    		return -1;
    	else
    	{
    		int left = height(root.left);
    		int right = height(root.right);
    		if(left > right)
    			return left + 1;
    		else 
    			return right + 1;
    	}
    }

    /**
     * Median key.
     * If the tree has N keys k1 < k2 < k3 < ... < kN, then their median key 
     * is the element at position (N+1)/2 (where "/" here is integer division)
     * @return the median key, or null if the tree is empty.
     * 
     * Justification of Theta(h) run time: The function calls the select function that retrieves the median key. 
     * 							 The size function is called in the median and in the select functions to get N in order to calculate and retrieve the median.
     * 							 Because of this, the function will always go through the entire tree in its run time. 
     * 							 Therefore the median function will always have a run time of Theta(h), where h is the height of the tree. 
     */
    public Key median() {
      if (isEmpty()) 
    	  return null;
      
      else
      	return median(root);
    }
    
    private Key median(Node root)
    {    	
    	int N = size(root);
    	int median = ((N - 1) / 2);
    	return select(median);
    }
    
    
    /* Select Key.
     * 
     * Selects a key when given its rank. In this case it would take in the position of the median 
     * and the function would start from 0 at the most left node and traverse through the tree
     * until it finds its match. This would return the median key.
     */
    public Key select(int rank)
    {
		return select(root, rank).key;
    }
    
    private Node select(Node root, int rank)
    {
    	int x = size(root.left);
    	if(x > rank)
    		return select(root.left, rank);
    	else if(x < rank)
    		return select(root.right, rank - x - 1);
    	else
    		return root;
    }
    
    /**
     * Print all keys of the tree in a sequence, in-order.
     * That is, for each node, the keys in the left subtree should appear before the key in the node.
     * Also, for each node, the keys in the right subtree should appear before the key in the node.
     * For each subtree, its keys should appear within a parenthesis.
     * 
     * !!The keys in each subtree should be contained in a pair of parentheses!!
     *
     * Example 1: Empty tree -- output: "()"
     * Example 2: Tree containing only "A" -- output: "(()A())"
     * Example 3: Tree:
     *   B
     *  / \
     * A   C
     *      \
     *       D
     *
     * output: "((()A())B(()C(()D())))"
     *
     * output of example in the assignment: (((()A(()C()))E((()H(()M()))R()))S(()X()))
     *
     * @return a String with all keys in the tree, in order, parenthesized.
     * 
     * 		
     */ 
    public String printKeysInOrder() {
      if (isEmpty()) return "()";
      else if(root.left == null && root.right == null)
    	  return "(()" + root.key + "())";
      else
      {
    	String s = "";
     	return printKeysInOrder(root,s);
      }
    }
    private String printKeysInOrder(Node root, String s)
    {
    	s += "(";
    	if(root != null)
    	{
    		s = printKeysInOrder(root.left, s);
    		s += root.key;
    		s = printKeysInOrder(root.right, s);
    	}
    	s += ")";
    	return s;
    }
    
    /**
     * Pretty Printing the tree. Each node is on one line -- see assignment for details.
     *
     * @return a multi-line string with the pretty ascii picture of the tree.
     */
    public String prettyPrintKeys() 
    {
    	if(isEmpty())
    		return "-null\n";
    	else
    		return prettyPrintKeys(root, "");
    }
    private String prettyPrintKeys(Node root, String s)
    {
    	if(root == null)
    		return s += "-null\n";
    	String p = s;
    	s += " ";
		p += "-" + root.key + "\n";
		p += prettyPrintKeys(root.left, s + "|");
		p += prettyPrintKeys(root.right, s + " ");
		return p;
    }

    /**
     * Deletes a key from a tree (if the key is in the tree).
     * Note that this method works symmetrically from the Hibbard deletion:
     * If the node to be deleted has two child nodes, then it needs to be
     * replaced with its predecessor (not its successor) node.
     *
     * @param key the key to delete
     */
    public void delete(Key key) 
    {
    	if(isEmpty() || key == null || contains(key) == false)
    		return;
    	if(key == root.key && root.left == null && root.right == null)
    		root = null;
    	else
    		root = delete(key, root);
    
    }
    private Node delete(Key key, Node root)
    {
    	if(root.key == key && root.left == null && root.right == null) 
    	{
    		root = null;
    		return null;
    	}
    	
    	int cmp = key.compareTo(root.key); 
    	
    	if(cmp < 0)  
    	{
    		root.left = delete(key, root.left);
    	}
    	
    	else if(cmp > 0) 
    	{
    		root.right = delete(key, root.right);
    	}
    	
    	else
    	{
    		if(cmp == 0 && root.right == null)  
        		return root.left;
        	
        	if(cmp == 0 && root.left == null) 
        		return root.right;
        	
        	Node t = root;
        	root = findPre(root.left);
        	root.left = deletePre(t.left);
        	root.right = t.right;
    	}
    	
    	root.N = size(root.left) + size(root.right) + 1;
    	return root;
    }
    
    /*
     * Find Predecessor
     * 
     * Finds and returns the root's predecessor
     * 
     */
    public Node findPre(Node x)
    {
    	return findPredecessor(x);
    }
    private Node findPredecessor(Node root) 
    {
    	if(root.right == null)
    		return root;
    	else
    		return findPre(root.right);
    }
    
    
    /*
     * Delete Predecessor
     * 
     * Predecessor node is passed in and deleted from the tree.
     * After the deletion the size is updated and the node deleted is returned. 
     * 
     */
    public Node deletePre(Node node)
    {
    	return deletePredecessor(node);
    }
    private Node deletePredecessor(Node node)
    {
    	if(node.right == null)
    		return node.left;
    	node.right = deletePredecessor(node.right);
    	node.N = 1 + size(node.left) + size(node.right);
    	return node;
    }
    
}