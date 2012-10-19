package ukkonen;

import java.util.TreeMap;


class Node {
	Node parent, suffixLink;
	int edgeStart, edgeEnd, parentDepth;
	// The edge that reaches this node contains the substring s[edgeStart, edgeEnd]
	TreeMap<Integer, Node> sons;

	public Node(){
		parent = suffixLink = null;
		edgeStart = edgeEnd = parentDepth = 0;
		sons = new TreeMap<Integer, Node>();
	}

	// Returns true if there is a path starting at root having length position + 1 that ends
	// in the edge that reaches this node.
	public boolean inEdge(int position){
		return parentDepth <= position && position < depth();
	}

	public int edgeLength(){
		return edgeEnd - edgeStart;
	}

	public int depth(){
		return parentDepth + edgeLength();
	}

	void link(Node son, int start, int end, int[] A){
		// Links the current node with the son. The edge will have substring s[start, end)
		son.parent = this;
		son.parentDepth = this.depth();
		son.edgeStart = start;
		son.edgeEnd = end;
		sons.put(A[start],son);
	}

	public boolean isLeaf(){
		return sons.size() == 0;
	}
}
