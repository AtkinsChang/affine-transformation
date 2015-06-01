package edu.nccu.plsm.gis

import edu.nccu.plsm.gis.cp.{CPGroup, Point2D}

/**
 * @version
 * @since
 */
package object transfomation {
  type Point = Point2D[BigDecimal]
  type CP = CPGroup[Point]
  val Point = Point2D[BigDecimal] _
  val CP = CPGroup[Point] _
}
