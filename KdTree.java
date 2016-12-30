import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
	private Node root;
	private static boolean HORIZONTAL = true;
	private static boolean VERTICAL = false;
	private double min;

	public KdTree() {
		root = null;
		min = Double.MAX_VALUE;
	}

	private static class Node {
		private Point2D p;
		private RectHV rect;
		private Node lb;
		private Node rt;
		private int size;

		private Node() {
			p = null;
			rect = null;
			lb = null;
			rt = null;
			size = 1;
		}

		private Node(Point2D point, RectHV r) {
			this();
			p = point;
			rect = r;
		}
	}

	// is the set empty?
	public boolean isEmpty() {
		return root == null;
	}

	// number of points in the set 
	public int size() {
		return (root == null) ? 0 : root.size;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException();

		if (contains(p)) return;

		root = insert(root, new RectHV(0, 0, 1, 1), p, VERTICAL);
	}

	private Node insert(Node node, RectHV r, Point2D point, boolean direction) {
		if (node == null) return new Node(point, r);

		double cmp = direction ? (point.x() - node.p.x()) : (node.p.y() - point.y());
		RectHV next = null;
		if (cmp < 0) {
			if (!direction)
				next = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
			else
				next = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
			node.lb = insert(node.lb, next, point, !direction);
		} else {
			if (!direction)
				next = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
			else
				next = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
			node.rt = insert(node.rt, next, point, !direction);
		}

		node.size = ((node.lb == null) ? 0 : node.lb.size) + ((node.rt == null) ? 0 : node.rt.size) + 1;
		return node;
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException();
		
		return contains(root, p, VERTICAL);
	}

	private boolean contains(Node node, Point2D point, boolean direction) {
		if (node == null) return false;

		if (point.equals(node.p)) return true;
		
		double cmp = direction ? (point.x() - node.p.x()) : (node.p.y() - point.y());

		if (cmp < 0) {
			if (contains(node.lb, point, !direction))
				return true;
		} else {
			if (contains(node.rt, point, !direction))
				return true;
		}

		return false;
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

		min = Double.MAX_VALUE;
		Point2D ret = null;
		ret = findNearest(root, p);
		return ret;
	}

	private Point2D findNearest(Node node, Point2D target) {
		if (node == null) return null;

		if (!node.rect.contains(target) && node.rect.distanceSquaredTo(target) > min)
			return null;
		
		Point2D ret = null, left = null, right = null;

		double dist = node.p.distanceSquaredTo(target);
		if (dist < min) {
			min = dist;
			ret = node.p;
		}

		left = findNearest(node.lb, target);
		if (left != null) ret = left;
		right = findNearest(node.rt, target);
		if (right != null) ret = right;
		
		return ret;

	}

	public static void main(String[] args) {
		Point2D p1 = new Point2D(0.7, 0.5);
		Point2D p2 = new Point2D(0.2, 0.5);
		Point2D p3 = new Point2D(0.1, 0.4);

		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLUE);

		RectHV r = new RectHV(0.05, 0.1, 0.15, 0.45);


		Iterable<Point2D> range = new Queue<Point2D> ();
		KdTree kdtree = new KdTree();
		kdtree.insert(p1);
		kdtree.insert(p2);
		kdtree.insert(p3);
		StdDraw.setPenRadius(0.02);
		StdDraw.setPenColor(StdDraw.RED);
		p1.draw();
		p2.draw();
		p3.draw();
		System.out.println("size = "+kdtree.size());
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		r.draw();
		range = kdtree.range(r);
		for (Point2D p : range) {
			System.out.println(p);
		}
	}
}
