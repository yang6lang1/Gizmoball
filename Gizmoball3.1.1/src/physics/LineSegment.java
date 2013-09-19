package physics;

/**********************************************************************
 * Copyright (C) 2000 by the Massachusetts Institute of Technology,
 *                     Cambridge, Massachusetts.
 *
 *                        All Rights Reserved
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose and without fee is hereby
 * granted, provided that the above copyright notice appear in all
 * copies and that both that copyright notice and this permission
 * notice appear in supporting documentation, and that MIT's name not
 * be used in advertising or publicity pertaining to distribution of
 * the software without specific, written prior permission.
 *
 * THE MASSACHUSETTS INSTITUTE OF TECHNOLOGY DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE, INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS.  IN NO EVENT SHALL THE MASSACHUSETTS
 * INSTITUTE OF TECHNOLOGY BE LIABLE FOR ANY SPECIAL, INDIRECT OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 *********************************************************************/

import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * LineSegment is an immutable abstract data type which represents a line
 * segment in two dimensional Cartesian space.
 */
public final class LineSegment implements Serializable {

  private final Vect p1;
  private final Vect p2;

  // Rep. Invariant:
  //   p1 != null &&
  //   p2 != null

  // Abstraction Function:
  //   The line segment which starts at p1 and goes to p2

  /**
   * @requires <code>p1</code> and <code>p2</code> are not null
   * @effects constructs a new LineSegment between the two points
   * <code>p1</code> and <code>p2</code>.
   */
  public LineSegment(Vect p1, Vect p2) {
    if ((p1 == null) || (p2 == null)) {
      throw new IllegalArgumentException();
    }
    this.p1 = p1;
    this.p2 = p2;
  }

  /**
   * @requires l2d is not null
   * @effects constructs a new Line Segment with the same coordinates as
   * <code>l2d</code>
   */
  public LineSegment(Line2D l2d) {
    this(new Vect(l2d.getP1()), new Vect(l2d.getP2()));
  }

  /**
   * @effects constructs a new line segment from &lt;<code>x1</code>,
   * <code>y1</code>&gt; to &lt;<code>x2</code>, <code>y2</code>&gt;.
   */
  public LineSegment(double x1, double y1, double x2, double y2) {
    this(new Vect(x1, y1), new Vect(x2, y2));
  }

  /**
   * @return the first point of <code>this</code> line segment.
   */
  public Vect p1() {
    return p1;
  }

  /**
   * @return the second point of <code>this</code> line segment.
   */
  public Vect p2() {
    return p2;
  }

  /**
   * @return a new Line2D representing <code>this</code>
   */
  public Line2D.Double toLine2D() {
    return new Line2D.Double(p1.x(), p1.y(), p2.x(), p2.y());
  }

  /**
   * @return the angle from the x-axis of <code>this</code>.
   */
  public Angle angle() {
    return new Angle(p2.x() - p1.x(), p2.y() - p1.y());
  }

  /**
   * @return the length of <code>this</code>.
   */
  public double length() {
    return p2.minus(p1).length();
  }

  public String toString() {
    return "LineSegment(" + p1 + "-" + p2 + ")";
  }

  public boolean equals(LineSegment ls) {
    if (ls == null) return false;
    return (p1.equals(ls.p1) && p2.equals(ls.p2));
  }

  public boolean equals(Object o) {
    if (o instanceof LineSegment)
      return equals((LineSegment) o);
    else
      return false;
  }

  public int hashCode() {
    return p1.hashCode() + p2.hashCode();
  }
  
}