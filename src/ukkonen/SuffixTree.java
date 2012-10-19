package ukkonen;

import java.util.ArrayList;

class SuffixTree {
	ArrayList<Node> nodes;
	Node root, needSuffix;
	int currentNode;
	int length;
	int TERMINATORS_RANGE = 5;
	int termi=0; //terminal number
	String generalized; //when inserting many strings into a generalized tree, all the strings are appended into a generalized string in the form of GS="S1"+"$1"+"S2"+"$2"+...


	public SuffixTree(int[] sb) {
		nodes = new ArrayList<Node>();
		currentNode = 0;
		root = newNode();
		length = sb.length;
	    build(root, sb);	
	}

	public SuffixTree() {
		nodes = new ArrayList<Node>();
		currentNode = 0;
		root = newNode();
	}
	
	int nofnodes() {
		return currentNode;
	}
	
	Node newNode(){
		nodes.add(currentNode,new Node());
		currentNode++;
		return new Node();
	}

	Node walkDown(Node c, int j, int i, int[] str) {
		int k = j + c.depth();
		if (i - j + 1 > 0){
			while (!c.inEdge(i - j)){
				c = c.sons.get(str[k]);
				k += c.edgeLength();
			}
		}
		return c;
	}

	void addSuffixLink(Node current){
		if (needSuffix != null){
			needSuffix.suffixLink = current;
		}
		needSuffix = null;
	}

	void build(Node root, int[] s) {
		
		Node c = newNode();
		needSuffix = null;
		root.link(c, 0, length, s);

		// Indicates if at the beginning of the phase we need to follow the suffix link of the current node 
		//and then walk down the tree using the skip and count trick.
		boolean needWalk = true;

		for (int i=0, j=1; i<length-1; ++i){ //loops all the generalized string
			int nc = s[i+1];
			while (j <= i + 1){
				if (needWalk){
					if (c.suffixLink == null && c.parent != null) c = c.parent;
					c = (c.suffixLink == null ? root : c.suffixLink);
					c = walkDown(c, j, i, s);
				}

				needWalk = true;
				// Here c == the highest node below s[j...i] and we will add char s[i+1]
				int m = i - j + 1; // Length of the string s[j..i].
				if (m == c.depth()){
					// String s[j...i] ends exactly at node c (explicit node).
					addSuffixLink(c);
					if (c.sons.containsKey(nc)){
						c = c.sons.get(nc);
						needWalk = false;
						break;
					}else{
						Node leaf = newNode();
						c.link(leaf, i+1, length, s);
					}
				}else{
					// String s[j...i] ends at some place in the edge that reaches node c.
					int where = c.edgeStart + m - c.parentDepth;
					// The next character in the path after string s[j...i] is s[where]
					if (s[where] == nc){ //Either rule 3 or rule 1
						addSuffixLink(c);
						if (!c.isLeaf() || j != c.edgeStart - c.parentDepth){
							// Rule 3
							needWalk = false;
							break;
						}
					}else{
						Node split = newNode();
						c.parent.link(split, c.edgeStart, where, s);
						split.link(c, where, c.edgeEnd, s);
						split.link(newNode(), i+1, length, s);
      
						addSuffixLink(split);
      
						if (split.depth() == 1){
							//The suffix link is the root because we remove the only character and end with an empty string.
							split.suffixLink = root;
						}else{
							needSuffix = split;
						}
						c = split;
					}
				}
				j++;
			}
		}
	}
}

