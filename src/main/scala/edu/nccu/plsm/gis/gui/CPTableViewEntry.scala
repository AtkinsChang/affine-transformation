package edu.nccu.plsm.gis.gui

import javafx.beans.property.SimpleStringProperty

/**
 * @version
 * @since
 */
class CPTableViewEntry(xStr: String, yStr: String, txStr: String, tyStr: String) {
  private[this] lazy val x = new SimpleStringProperty(xStr)
  private[this] lazy val y = new SimpleStringProperty(yStr)
  private[this] lazy val tx = new SimpleStringProperty(txStr)
  private[this] lazy val ty = new SimpleStringProperty(tyStr)
  private[this] var xValue = BigDecimal(x.get)
  private[this] var yValue = BigDecimal(y.get)
  private[this] var txValue = BigDecimal(tx.get)
  private[this] var tyValue = BigDecimal(ty.get)

  def getX = x.get

  def setX(fName: String) = {
    xValue = BigDecimal(fName)
    x.set(fName)
  }

  def getXValue = xValue

  def getY = y.get

  def setY(fName: String) = {
    yValue = BigDecimal(fName)
    y.set(fName)
  }

  def getYValue = yValue

  def getTx = tx.get

  def setTx(fName: String) = {
    txValue = BigDecimal(fName)
    tx.set(fName)
  }

  def getTxValue = txValue

  def getTy = ty.get

  def setTy(fName: String) = {
    tyValue = BigDecimal(fName)
    ty.set(fName)
  }

  def getTyValue = tyValue
}
