/*******************************************************************************
 * Copyright (c) 2013, Regents of the University of California
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 * 
 * None of the name of the Regents of the University of California, or the names of its
 * contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package calico.utils;

import java.awt.font.*;
import java.awt.*;
import java.awt.geom.*;

public class RoundPolygon implements Shape {
	GeneralPath path;
	public RoundPolygon(Polygon p, int arcWidth) {
		path=new GeneralPath();
		transform(p, arcWidth, path);
	}
	public Rectangle getBounds() {
		return path.getBounds();
	}

	public Rectangle2D getBounds2D() {
		return path.getBounds2D();
	}

	public boolean contains(double x, double y) {
		return path.contains(x,y);
	}

	public boolean contains(Point2D p) {
		return path.contains(p);
	}

	public boolean intersects(double x, double y, double w, double h) {
		return path.intersects(x,y,w,h);
	}

	public boolean intersects(Rectangle2D r) {
		return path.intersects(r);
	}

	public boolean contains(double x, double y, double w, double h) {
		return path.contains(x,y,w,h);
	}

	public boolean contains(Rectangle2D r) {
		return path.contains(r) ;
	}

	public PathIterator getPathIterator(AffineTransform at) {
		return path.getPathIterator(at);
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return path.getPathIterator(at, flatness);
	}

	protected static void transform(Polygon shape, int arcWidth, GeneralPath path) {
		PathIterator pIter = shape.getPathIterator(new AffineTransform());

		Point2D.Float pointFirst=new Point2D.Float(0,0);
		Point2D.Float pointSecond=null;

		Point2D.Float pointLast=new Point2D.Float(0,0);
		Point2D.Float pointCorner=null;
		Point2D.Float pointNext=null;

		float [] coor = new float[6];
		while (!pIter.isDone()) {
			int type = pIter.currentSegment(coor);
			float x1 = coor[0];
			float y1 = coor[1];
			float x2 = coor[2];
			float y2 = coor[3];
			float x3 = coor[4];
			float y3 = coor[5];

			switch (type) {
			case PathIterator.SEG_CLOSE:
				//                        path.closePath();
				break;
			case PathIterator.SEG_CUBICTO:
				path.curveTo(x1, y1, x2, y2, x3, y3);
				break;
			case PathIterator.SEG_LINETO:
				if (pointCorner==null) {
					pointCorner=new Point2D.Float(x1,y1);
					if (pointNext==null) {
						//first move
						pointSecond=new Point2D.Float(x1,y1);
						Point2D.Float arcStartPoint=getArcPoint(pointSecond, pointFirst, arcWidth);

						path.moveTo(arcStartPoint.x,arcStartPoint.y);
					}
				}
				else {
					pointNext=new Point2D.Float(x1,y1);
					add(path, pointLast, pointCorner, pointNext, arcWidth);
					pointLast=pointCorner;
					pointCorner=pointNext;
				}
				//                        res.lineTo(x1, y1);
				break;
			case PathIterator.SEG_MOVETO:
				pointLast.x=x1;
				pointLast.y=y1;
				pointFirst.x=x1;
				pointFirst.y=y1;
				//                        path.moveTo(x1, y1);
				break;
			case PathIterator.SEG_QUADTO:
				path.quadTo(x1, y1, x2, y2);
				break;
			}
			pIter.next();
		}

		add(path, pointLast, pointCorner, pointFirst, arcWidth);
		add(path, pointCorner, pointFirst, pointSecond, arcWidth);
		//            path.lineTo(pointStart.x, pointStart.y);
		path.closePath();

	}

	protected static void add(GeneralPath path, Point2D.Float last, Point2D.Float corner, Point2D.Float next, float w) {
		Point2D.Float arcStartPoint=getArcPoint(last, corner, w);
		Point2D.Float arcEndPoint=getArcPoint(next, corner, w);

		path.lineTo(arcStartPoint.x, arcStartPoint.y);
		path.quadTo(corner.x, corner.y, arcEndPoint.x, arcEndPoint.y);
	}

	protected static Point2D.Float getArcPoint(Point2D.Float p1, Point2D.Float p2, float w) {
		Point2D.Float res=new Point2D.Float();
		float d=Math.round(Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y)));

		if (p1.x<p2.x) {
			res.x = p2.x - w * Math.abs(p1.x - p2.x) / d;
		}
		else {
			res.x = p2.x + w * Math.abs(p1.x - p2.x) / d;
		}

		if (p1.y<p2.y) {
			res.y = p2.y - w * Math.abs(p1.y - p2.y) / d;
		}
		else {
			res.y = p2.y + w * Math.abs(p1.y - p2.y) / d;
		}

		return res;
	}

	protected static void transformPrint(Polygon shape) {
		PathIterator pIter = shape.getPathIterator(new AffineTransform());
		GeneralPath res = new GeneralPath();

		float [] coor = new float[6];
		while (!pIter.isDone()) {
			int type = pIter.currentSegment(coor);
			float x1 = coor[0];
			float y1 = coor[1];
			float x2 = coor[2];
			float y2 = coor[3];
			float x3 = coor[4];
			float y3 = coor[5];

			switch (type) {
			case PathIterator.SEG_CLOSE:
				res.closePath();
				System.out.println("SEG_CLOSE");
				break;
			case PathIterator.SEG_CUBICTO:
				res.curveTo(x1, y1, x2, y2, x3, y3);
				System.out.println("SEG_CUBICTO");
				break;
			case PathIterator.SEG_LINETO:
				res.lineTo(x1, y1);
				System.out.println("SEG_LINETO");
				break;
			case PathIterator.SEG_MOVETO:
				res.moveTo(x1, y1);
				System.out.println("SEG_MOVETO");
				break;
			case PathIterator.SEG_QUADTO:
				res.quadTo(x1, y1, x2, y2);
				System.out.println("SEG_QUADTO");
				break;
			}
			pIter.next();
		}

	}

	public static void main(String[] args) {
		Polygon p=new Polygon(new int[] {0,100,0}, new int[] {0,0,100}, 3);
		transformPrint(p);

		System.out.println("mid="+getArcPoint(new Point2D.Float(0,0),new Point2D.Float(50,100),10));
		System.out.println("mid="+getArcPoint(new Point2D.Float(100,100),new Point2D.Float(0,0),10));
	}
}
