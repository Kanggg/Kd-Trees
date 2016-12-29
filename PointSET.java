import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;
import java.util.TreeSet;


public class PointSET {
	private TreeSet<Point2D> set;

	// construct an empty set of points
	public PointSET() {
		set = new TreeSet<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return set.isEmpty();
	}

	// number of points in the set 
	public int size() {
		return set.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		set.add(p);
	}

	// does the set contain point p? 
	public boolean contains(Point2D p) {
		return set.contains(p);
	}

	// draw all points to standard draw 
	public void draw() {

	}

	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		Queue<Point2D> all = new Queue<Point2D>();
		Queue<Point2D> ret = new Queue<Point2D>();

		Iterator<Point2D> iterate = set.iterator();
		while (iterate.hasNext()) {
			all.enqueue(iterate.next());
		}
		for (Point2D p : all) {
			if (rect.contains(p)) {
				ret.enqueue(p);
			}
		}
		return ret;
	}

	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {

	}

}
