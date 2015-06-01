package edu.nccu.plsm.gis.transfomation

import com.typesafe.scalalogging.LazyLogging
import edu.nccu.plsm.gis.cp.ControlPoints

/**
 * @version
 * @since
 */
class Affine(private[gis] val cps: ControlPoints) extends LazyLogging {
  lazy val A = matrix(1)(0)
  lazy val B = matrix(2)(0)
  lazy val C = matrix(0)(0)
  lazy val D = matrix(1)(1)
  lazy val E = matrix(2)(1)
  lazy val F = matrix(0)(1)
  lazy val t = math.atan((D / A).doubleValue())
  lazy val sx: BigDecimal = math.sqrt((A.pow(2) + B.pow(2)).doubleValue())
  lazy val (k, sy) = {
    val sint = math.sin(t)
    val cost = math.cos(t)
    val BDivE = B / E
    val kResult = (-BDivE - sint) / (BDivE * sint - cost)
    val syResult = B / (kResult * cost - sint)
    (kResult, syResult)
  }
  lazy val tx = C
  lazy val ty = F
  //lazy val skewDegree = BigDecimal(math.asin(k.doubleValue()) / math.Pi * 180)
  lazy val skewDegree = BigDecimal(k.doubleValue() / math.Pi * 180)
  lazy val rotationDegree = BigDecimal(t / math.Pi * 180)
  lazy val residuals = {
    logger info s"calculating control points residuals..."
    cps.cps.map(cp => {
      val tp = transform(cp.origin)
      (tp.x - cp.transformed.x, tp.y - cp.transformed.y)
    })
  }
  lazy val RMSE = {
    logger info s"calculating RMSE..."
    BigDecimal(math.sqrt((residuals.map(e => e._1.pow(2) + e._2.pow(2)).sum / residuals.length).doubleValue()))
  }
  private[this] lazy val matrix = {
    logger info s"Estimating affine transformation coefficients..."
    val a = new Matrix(3, 3)
    a.set(0, 0)(cps.n)
    a.set(0, 1)(cps.x)
    a.set(0, 2)(cps.y)
    a.set(1, 0)(cps.x)
    a.set(1, 1)(cps.xSqr)
    a.set(1, 2)(cps.xy)
    a.set(2, 0)(cps.y)
    a.set(2, 1)(cps.xy)
    a.set(2, 2)(cps.ySqr)
    val b = new Matrix(3, 2)
    b.set(0, 0)(cps.tX)
    b.set(0, 1)(cps.tY)
    b.set(1, 0)(cps.xtX)
    b.set(1, 1)(cps.xtY)
    b.set(2, 0)(cps.ytX)
    b.set(2, 1)(cps.ytY)
    a.inverse * b
  }

  def transform(p: Point) = {
    val x = A * p.x + B * p.y + C
    val y = D * p.x + E * p.y + F
    logger info s"transformed (${p.x}, ${p.y}) => ($x, $y)"
    Point(x, y)
  }

}
