package Helpers;

import Helpers.PolygonApprox;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class MultiPolygonApprox extends PolygonApprox {
	private static final long serialVersionUID = 1L;
	byte[] pointtypes;
	
	public MultiPolygonApprox(List<? extends List<? extends Point2D>> rel) {
		int npoints = 0;
		for (List<?> l : rel) npoints += l.size();
		coords = new float[npoints<<1];
		pointtypes = new byte[npoints];
		Arrays.fill(pointtypes, (byte) PathIterator.SEG_LINETO);
		int coord = 0;
		int point = 0;
		for (List<? extends Point2D> l : rel) {
			/*Point2D start = new Point2D.Double(l.get(0).getX(), l.get(0).getY());
			Point2D end = new Point2D.Double(l.get(l.size()-1).getX(), l.get(l.size()-1).getY());

			if(coord > 1 && coords[coord - 1] != 0 && coords[coord] != 0){
				Point2D recent = new Point2D.Double(coords[coord-1], coords[coord]);
				double distToStart = Math.sqrt((recent.getX()-start.getX())*(recent.getX()-start.getX()) + (recent.getY()-start.getY())*(recent.getY()-start.getY()));
				double distToEnd = Math.sqrt((recent.getX()-end.getX())*(recent.getX()-end.getX()) + (recent.getY()-end.getY())*(recent.getY()-end.getY()));
				if(distToEnd < distToStart) Collections.reverse(l);
			}*/

			pointtypes[point] = (byte) PathIterator.SEG_MOVETO;
			point += l.size();
			for (int i = 0; i < l.size(); i++) {
				coords[coord++] = (float)l.get(i).getX();
				coords[coord++] = (float)l.get(i).getY();
			}
		}
		init();
	}
	
	public double distTo(Point2D p) {
		double dist = Double.MAX_VALUE;
		double px = p.getX();
		double py = p.getY();
		for (int i = 2 ; i < coords.length ; i += 2) {
			if (pointtypes[i >> i] != PathIterator.SEG_MOVETO)
				dist = Math.min(dist, Line2D.ptSegDist(coords[i-2], coords[i-1], coords[i], coords[i+1], px, py));
		}
		return dist;
	}

	public PathIterator getPathIterator(AffineTransform at, float pixelsq) {
		return new MultiPolygonApproxIterator(at, pixelsq);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new MultiPolygonApproxIterator(at, (float) (flatness * flatness));
	}
	
	class MultiPolygonApproxIterator extends PolygonApproxIterator {
		public MultiPolygonApproxIterator(AffineTransform _at, float _pixelsq) {
			super(_at, _pixelsq);
		}

		public void next() {
			float fx = coords[index];
			float fy = coords[index+1];
			index += 2;
			while (index < coords.length - 2 && pointtypes[(index >> 1) + 1] == PathIterator.SEG_LINETO && 
				distSq(fx, fy, coords[index], coords[index+1]) < approx) index += 2;
		}

		public int currentSegment(float[] c) {
			if (isDone()) {
	            throw new NoSuchElementException("poly approx iterator out of bounds");
	        }
	        c[0] = coords[index];
	        c[1] = coords[index+1];
	        if (at != null) {
	            at.transform(c, 0, c, 0, 1);
	        }
	        return pointtypes[index >> 1];
		}

		public int currentSegment(double[] coords) {
			throw new UnsupportedOperationException("Unexpected call to PolygonApprox.contains(Rectangle2D)");
		}
	}
}
