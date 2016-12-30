import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
	private Node root;
	private static boolean HORIZONTAL = true;
	private static boolean VERTICAL = false;

	public KdTree() {
		root = null;
	}

	private static class Node {
		private Point2D p;
		private RectHV rect;
		private Node lb;
		private Node rt;

		private Node() {
			p = null;
			rect = null;
			lb = null;
			rt = null;
		}

		private Node(Point2D point, RectHV r) {
			p = point;
			rect = r;
			lb = null;
			rt = null;
		}
	}

	// is the set empty?
	public boolean isEmpty() {
		return root == null;
	}

	// number of points in the set 
	public int size() {
		return size(root);
	}

	private int size (Node node) {
		if (node.p == null) return 0;

		int ret = 1;		
		if (node.lb != null) ret += size(node.lb);
		if (node.rt != null) ret += size(node.rt);

		return ret;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException();
		
		if (contains(p)) return;

		root = insert(root, new RectHV(0, 0, 1, 1), p, VERTICAL);
	}

	private Node insert(Node node, RectHV r, Point2D point, boolean direction) {
		System.out.println("point x = "+point.x()+ " y = "+point.y() + " direction "+direction);
		if (node == null) {
			return new Node(point, r);
		}

		double cmp = !direction ? (point.x() - node.p.x()) : (point.y() - node.p.y());
		RectHV next = null;
		if (cmp < 0) {
			if (direction)
				next = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
			else
				next = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
			System.out.println("insert left");
			node.lb = insert(node.lb, next, point, !direction);
		} else {
			if (direction)
				next = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
			else
				next = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
			System.out.println("insert right");
			node.rt = insert(node.rt, next, point, !direction);
		}

		return node;
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException();

		if (contains(root, p) == null)
			return false;

		return true;
	}

	private Node contains(Node node, Point2D point) {
		if (node == null) return null;

		int cmp = node.p.compareTo(point);

		if (cmp < 0)
			return contains(node.lb, point);
		else if (cmp > 0)
			return contains(node.rt, point);

		return node;
	}

	// draw all points to standard draw 
	public void draw() {
		draw(root, VERTICAL);
	}

	private void draw(Node node, boolean direction) {
		if (node == null) return;

		draw(node.lb, !direction);
		node.p.draw();
		node.rect.draw();
		draw(node.rt, !direction);
	}

	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new java.lang.NullPointerException();

		Queue<Point2D> ret = new Queue<Point2D>();

		collectPoints(ret, rect, root);

		return ret;
	}

	private void collectPoints(Queue<Point2D> q, RectHV rect, Node n) {
		if (n == null || !rect.intersects(n.rect)) return;

		if (rect.contains(n.p))
			q.enqueue(n.p);

		collectPoints(q, rect, n.lb);
		collectPoints(q, rect, n.rt);
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException();

		double max = Double.MAX_VALUE;
		Point2D ret = null;
		findNearest(root, p, ret, max);
		return ret;
	}

	private void findNearest(Node node, Point2D target, Point2D nearest, double max) {
		if (node == null) return;

		double dist = node.p.distanceSquaredTo(target);
		if (dist < max) {
			max = dist;
			nearest = node.p;
		}

		if (node.rect.contains(target))
			findNearest(node.lb, target, nearest, max);
		else
			findNearest(node.rt, target, nearest, max);
	}
}
