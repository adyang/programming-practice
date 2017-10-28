package binarytree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

public class SuperbalancedBinaryTreeTest {
	private static final int VALUE = 1;

	@Test
	public void rootOnly() {
		BinaryTreeNode root = new BinaryTreeNode(VALUE);
		assertTrue(isSuperbalanced(root));
	}

	@Test
	public void oneLeafDepthOne() {
		BinaryTreeNode root = new BinaryTreeNode(VALUE);
		root.insertLeft(VALUE);
		assertTrue(isSuperbalanced(root));
	}

	@Test
	public void oneLeafLinkedList() {
		BinaryTreeNode root = new BinaryTreeNode(VALUE);
		root.insertLeft(VALUE);
		root.left.insertLeft(VALUE);
		assertTrue(isSuperbalanced(root));
	}

	@Test
	public void twoLeafDepthDifferenceWithinOne() {
		BinaryTreeNode root = new BinaryTreeNode(VALUE);
		root.insertLeft(VALUE);
		root.left.insertLeft(VALUE);
		root.left.left.insertLeft(VALUE);
		root.insertRight(VALUE);
		root.right.insertRight(VALUE);
		assertTrue(isSuperbalanced(root));
	}

	@Test
	public void twoLeafDepthDifferenceExceedOne() {
		BinaryTreeNode root = new BinaryTreeNode(VALUE);
		root.insertLeft(VALUE);
		root.left.insertLeft(VALUE);
		root.left.left.insertLeft(VALUE);
		root.insertRight(VALUE);
		assertFalse(isSuperbalanced(root));
	}

	@Test
	public void rootWithSuperbalancedLeftRightButNotSuperbalancedItself() {
		BinaryTreeNode root = new BinaryTreeNode(VALUE);
		root.insertLeft(VALUE);
		root.left.insertLeft(VALUE);
		root.left.left.insertLeft(VALUE);
		root.left.left.insertRight(VALUE);
		root.left.left.left.insertLeft(VALUE);
		root.insertRight(VALUE);
		root.right.insertRight(VALUE);
		assertFalse(isSuperbalanced(root));
	}

	private boolean isSuperbalanced(BinaryTreeNode root) {
		if (root == null) {
			return true;
		}

		List<Integer> depths = new ArrayList<>();
		Stack<NodeDepthPair> stack = new Stack<>();
		stack.push(new NodeDepthPair(root, 0));

		while (!stack.isEmpty()) {
			NodeDepthPair pair = stack.pop();
			BinaryTreeNode node = pair.node;
			int depth = pair.depth;
			if (isLeaf(node)) {
				addDepthIfAbsent(depths, depth);
				if (isUnbalancedTree(depths)) {
					return false;
				}
			} else {
				pushIfNodePresent(node.left, depth, stack);
				pushIfNodePresent(node.right, depth, stack);
			}
		}

		return true;
	}

	private boolean isLeaf(BinaryTreeNode node) {
		return node.left == null && node.right == null;
	}

	private void addDepthIfAbsent(List<Integer> depths, int depth) {
		if (!depths.contains(depth)) {
			depths.add(depth);
		}
	}

	private boolean isUnbalancedTree(List<Integer> depths) {
		return depths.size() > 2 || (depths.size() == 2 && Math.abs(depths.get(0) - depths.get(1)) > 1);
	}

	private void pushIfNodePresent(BinaryTreeNode node, int depth, Stack<NodeDepthPair> stack) {
		if (node != null) {
			stack.push(new NodeDepthPair(node, depth + 1));
		}
	}

	private static class NodeDepthPair {
		BinaryTreeNode node;
		int depth;

		NodeDepthPair(BinaryTreeNode node, int depth) {
			this.node = node;
			this.depth = depth;
		}
	}

	private static class BinaryTreeNode {
		public int value;
		public BinaryTreeNode left;
		public BinaryTreeNode right;

		public BinaryTreeNode(int value) {
			this.value = value;
		}

		public BinaryTreeNode insertLeft(int leftValue) {
			this.left = new BinaryTreeNode(leftValue);
			return this.left;
		}

		public BinaryTreeNode insertRight(int rightValue) {
			this.right = new BinaryTreeNode(rightValue);
			return this.right;
		}
	}
}
