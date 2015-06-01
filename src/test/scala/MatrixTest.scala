import edu.nccu.plsm.gis.transfomation.Matrix
import org.specs2._

class MatrixTest extends Specification {def is = s2"""

  This is a specification for the 'edu.nccu.plsm.gis.transfomation.Matrix'

  5 1 7
  2 7 2
  6 2 5

  This 'edu.nccu.plsm.gis.transfomation.Matrix' should
    has determinant equals to -109                    $t1
    inverse                                           $t2
    multiply                                          $t3
                                                      """

  val matrix = {
    val tmp = new Matrix(3, 3)
    tmp.set(0, 0)(5)
    tmp.set(1, 0)(2)
    tmp.set(2, 0)(6)
    tmp.set(0, 1)(1)
    tmp.set(1, 1)(7)
    tmp.set(2, 1)(2)
    tmp.set(0, 2)(7)
    tmp.set(1, 2)(2)
    tmp.set(2, 2)(5)
    tmp
  }

  def t1 = matrix.determinant must equalTo(-109)
  def t2 = matrix.inverse.apply(0)(0) must closeTo((-31: BigDecimal) / (109: BigDecimal) +/- 0.000001)
  def t3 = {
    ((matrix.inverse * matrix)(0)(0) must closeTo((1: BigDecimal) +/- 0.000001)) and
      ((matrix.inverse * matrix)(1)(1) must closeTo((1: BigDecimal) +/- 0.000001)) and
      ((matrix.inverse * matrix)(2)(2) must closeTo((1: BigDecimal) +/- 0.000001))
  }

}