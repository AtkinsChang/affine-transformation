package edu.nccu.plsm.gis.cp


/**
 * @version
 * @since
 */
class ControlPoints private[cp](private[gis] val cps: Seq[CP]) {
  lazy val n = cps.length

  lazy val x = cps.map(_.origin.x).sum
  lazy val xSqr = cps.map(_.origin.x.pow(2)).sum
  lazy val y = cps.map(_.origin.y).sum
  lazy val ySqr = cps.map(_.origin.y.pow(2)).sum
  lazy val xy = cps.map(cp => cp.origin.x * cp.origin.y).sum

  lazy val tX = cps.map(_.transformed.x).sum
  lazy val xtX = cps.map(cp => cp.origin.x * cp.transformed.x).sum
  lazy val ytX = cps.map(cp => cp.origin.y * cp.transformed.x).sum
  lazy val tY = cps.map(_.transformed.y).sum
  lazy val xtY = cps.map(cp => cp.origin.x * cp.transformed.y).sum
  lazy val ytY = cps.map(cp => cp.origin.y * cp.transformed.y).sum
}
