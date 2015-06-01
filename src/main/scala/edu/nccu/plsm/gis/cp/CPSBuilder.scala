package edu.nccu.plsm.gis.cp

import com.typesafe.scalalogging.LazyLogging

import scala.collection._
import scala.collection.mutable.ArrayBuffer

/**
 * @version
 * @since
 */
class CPSBuilder extends LazyLogging {
  private val cps = ArrayBuffer.empty[CP]

  def ++=(xs: TraversableOnce[CP]): this.type = {
    cps ++= xs
    this
  }

  def build = {
    logger info s"Initializing control points..."
    new ControlPoints(cps)
  }

}

object CPSBuilder {
  def apply() = new CPSBuilder
}