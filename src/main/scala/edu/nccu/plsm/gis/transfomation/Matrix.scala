package edu.nccu.plsm.gis.transfomation

/**
 * @version
 * @since
 */
class Matrix(private val m: Int, private val n: Int) {
  private val data = Array.fill[BigDecimal](m, n)(0)

  def inverse = {
    assert(m == n)
    val det = determinant
    val matrix = new Matrix(m, n)
    for (a <- 0 until m) {
      for (b <- 0 until n) {
        if ((a + b) % 2 == 0) {
          matrix.set(a, b)(adjugate(b, a).determinant / det)
        } else {
          matrix.set(a, b)(-adjugate(b, a).determinant / det)
        }
      }
    }
    matrix
  }

  def set(m: Int, n: Int)(value: BigDecimal) = {
    data(m)(n) = value
  }

  def determinant = {
    assert(m == n)
    if (m <= 2) {
      data(0)(0) * data(1)(1) - data(1)(0) * data(0)(1)
    } else {
      (0 until n).map {
        k => {
          (0 until n).map(l => apply((k + l) % n)(l)).product - (0 until n).map(l => apply((k + n - l) % n)(l)).product
        }
      }.sum
    }
  }

  def apply(m: Int)(n: Int) = {
    data(m)(n)
  }

  private[this] def adjugate(k: Int, l: Int) = {
    val matrix = new Matrix(m - 1, n - 1)
    var c = 0
    for (a <- 0 until m if a != k) {
      var r = 0
      for (b <- 0 until n if b != l) {
        matrix.set(c, r)(data(a)(b))
        r += 1
      }
      c += 1
    }
    matrix
  }

  def *(matrix: Matrix) = multiply(matrix)

  def multiply(matrix: Matrix) = {
    val result = new Matrix(m, matrix.n)
    for (a <- 0 until m) {
      for (b <- 0 until matrix.n) {
        val v1 = data(a)
        val v2 = matrix.data.map(_.apply(b))
        result.set(a, b)(v1.zip(v2).map(a => a._1 * a._2).sum)
      }
    }
    result
  }

}
