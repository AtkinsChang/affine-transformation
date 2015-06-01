package edu.nccu.plsm.gis.gui

import java.math.{MathContext, RoundingMode}
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.{ActionEvent, EventHandler}
import javafx.geometry.Insets
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.scene.control._
import javafx.scene.control.cell.{PropertyValueFactory, TextFieldTableCell}
import javafx.scene.layout.{GridPane, HBox, VBox}
import javafx.scene.text.{Font, FontWeight, Text}
import javafx.scene.{Group, Scene}
import javafx.stage.Stage
import javafx.util.Callback

import edu.nccu.plsm.gis.cp.{CPSBuilder, ControlPoints}
import edu.nccu.plsm.gis.transfomation.Affine

import scala.collection.JavaConverters._

/**
 * @version
 * @since
 */
class GUIMain extends Application {
  private[this] final val data = FXCollections.observableArrayList(
    new CPTableViewEntry("465.403", "2733.558", "518843.844", "5255910.5"),
    new CPTableViewEntry("5102.342", "2744.195", "528265.750", "5255948.5"),
    new CPTableViewEntry("5108.498", "465.302", "528288.063", "5251318.0"),
    new CPTableViewEntry("468.303", "455.048", "518858.719", "5251280.0"))
  private[this] final val table = {
    val tb = new TableView[CPTableViewEntry]
    tb.setItems(data)
    tb.setEditable(true)
    tb
  }
  private[this] final val addCPHBox = {
    val hBox = new HBox
    hBox.setSpacing(3)
    hBox
  }
  private[this] final val CPVBox = {
    val vBox = new VBox
    vBox.setSpacing(5)
    vBox.setPadding(new Insets(10, 0, 0, 10))
    vBox
  }
  private[this] final val resultVBox = {
    val vBox = new VBox
    vBox.setSpacing(5)
    vBox.setPadding(new Insets(20, 0, 0, 10))
    vBox
  }
  private[this] final val hBox = new HBox
  private[this] lazy val notEnoughCP = {
    val text = new Text("Not enough control points")
    text.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 30))
    text
  }

  override def start(stage: Stage) {
    val label: Label = new Label("Control Points")
    label.setFont(new Font("Arial", 20))

    createTable()
    update()

    CPVBox.getChildren.addAll(label, table, addCPHBox)
    hBox.getChildren.addAll(CPVBox, resultVBox)

    val scene = new Scene(new Group)
    stage.setTitle("Affine Transformation")
    stage.setWidth(1000)
    stage.setHeight(550)
    scene.getRoot.asInstanceOf[Group].getChildren.addAll(hBox)
    stage.setScene(scene)
    stage.show()
  }

  private[this] def createTable() = {
    val xCol = {
      val column = new TableColumn[CPTableViewEntry, String]("x")
      column.setMinWidth(100)
      column.setCellValueFactory(new PropertyValueFactory[CPTableViewEntry, String]("x"))
      column.setCellFactory(TextFieldTableCell.forTableColumn[CPTableViewEntry])
      column.setOnEditCommit(
        new EventHandler[CellEditEvent[CPTableViewEntry, String]] {
          override def handle(event: CellEditEvent[CPTableViewEntry, String]): Unit = {
            try {
              event.getTableView
                .getItems
                .get(event.getTablePosition.getRow)
                .setX(event.getNewValue)
              update()
            } catch {
              case e: NumberFormatException =>
                ErrorDialog.show(
                  "Input Error",
                  "Please insert numeric value in the input box",
                  e
                )
                event.getTableView.getColumns.get(0).setVisible(false)
                event.getTableView.getColumns.get(0).setVisible(true)
            }
          }
        }
      )
      column
    }
    val yCol = {
      val column = new TableColumn[CPTableViewEntry, String]("y")
      column.setMinWidth(100)
      column.setCellValueFactory(new PropertyValueFactory[CPTableViewEntry, String]("y"))
      column.setCellFactory(TextFieldTableCell.forTableColumn[CPTableViewEntry])
      column.setOnEditCommit(
        new EventHandler[CellEditEvent[CPTableViewEntry, String]] {
          override def handle(event: CellEditEvent[CPTableViewEntry, String]): Unit = {
            try {
              event.getTableView
                .getItems
                .get(event.getTablePosition.getRow)
                .setY(event.getNewValue)
              update()
            } catch {
              case e: NumberFormatException =>
                ErrorDialog.show(
                  "Input Error",
                  "Please insert numeric value in the input box",
                  e
                )
                event.getTableView.getColumns.get(0).setVisible(false)
                event.getTableView.getColumns.get(0).setVisible(true)
            }
          }
        })
      column
    }
    val txCol = {
      val column = new TableColumn[CPTableViewEntry, String]("X")
      column.setMinWidth(100)
      column.setCellValueFactory(new PropertyValueFactory[CPTableViewEntry, String]("tx"))
      column.setCellFactory(TextFieldTableCell.forTableColumn[CPTableViewEntry])
      column.setOnEditCommit(
        new EventHandler[CellEditEvent[CPTableViewEntry, String]] {
          override def handle(event: CellEditEvent[CPTableViewEntry, String]): Unit = {
            try {
              event.getTableView
                .getItems
                .get(event.getTablePosition.getRow)
                .setTx(event.getNewValue)
              update()
            } catch {
              case e: NumberFormatException =>
                ErrorDialog.show(
                  "Input Error",
                  "Please insert numeric value in the input box",
                  e
                )
                event.getTableView.getColumns.get(0).setVisible(false)
                event.getTableView.getColumns.get(0).setVisible(true)
            }
          }
        }
      )
      column
    }
    val tyCol = {
      val column = new TableColumn[CPTableViewEntry, String]("Y")
      column.setMinWidth(100)
      column.setCellValueFactory(new PropertyValueFactory[CPTableViewEntry, String]("ty"))
      column.setCellFactory(TextFieldTableCell.forTableColumn[CPTableViewEntry])
      column.setOnEditCommit(
        new EventHandler[CellEditEvent[CPTableViewEntry, String]] {
          override def handle(event: CellEditEvent[CPTableViewEntry, String]): Unit = {
            try {
              event.getTableView
                .getItems
                .get(event.getTablePosition.getRow)
                .setTy(event.getNewValue)
              update()
            } catch {
              case e: NumberFormatException =>
                ErrorDialog.show(
                  "Input Error",
                  "Please insert numeric value in the input box",
                  e
                )
                event.getTableView.getColumns.get(0).setVisible(false)
                event.getTableView.getColumns.get(0).setVisible(true)
            }
          }
        }
      )
      column
    }
    val actionCol = {
      val column = new TableColumn[CPTableViewEntry, String]("Delete")
      column.setCellFactory(
        new Callback[TableColumn[CPTableViewEntry, String], TableCell[CPTableViewEntry, String]]() {
          override def call(param: TableColumn[CPTableViewEntry, String]): TableCell[CPTableViewEntry, String] = {
            new TableCell[CPTableViewEntry, String] {
              val cellButton = new Button("Delete")
              cellButton.setOnAction(new EventHandler[ActionEvent]() {
                override def handle(event: ActionEvent): Unit = {
                  data.remove(getTableRow.getIndex)
                  update()
                }
              })

              override def updateItem(t: String, empty: Boolean) = {
                super.updateItem(t, empty)
                if (!empty) {
                  setGraphic(cellButton)
                } else {
                  setGraphic(null)
                }
              }
            }
          }
        })
      column
    }
    table.getColumns.addAll(xCol, yCol, txCol, tyCol, actionCol)

    val addX = {
      val text = new TextField
      text.setPromptText("Enter x")
      text.setMaxWidth(xCol.getPrefWidth)
      text
    }
    val addY = {
      val text = new TextField
      text.setMaxWidth(yCol.getPrefWidth)
      text.setPromptText("Enter y")
      text
    }
    val addTx = {
      val text = new TextField
      text.setMaxWidth(txCol.getPrefWidth)
      text.setPromptText("Enter X")
      text
    }
    val addTy = {
      val text = new TextField
      text.setMaxWidth(tyCol.getPrefWidth)
      text.setPromptText("Enter Y")
      text
    }
    val addButton = {
      val button = new Button("Add")
      button.setOnAction(
        new EventHandler[ActionEvent] {
          override def handle(event: ActionEvent): Unit = {
            try {
              data.add(
                new CPTableViewEntry(
                  addX.getText,
                  addY.getText,
                  addTx.getText,
                  addTy.getText)
              )
              addX.clear()
              addY.clear()
              addTx.clear()
              addTy.clear()
              update()
            } catch {
              case e: Exception => ErrorDialog.show(
                "Input Error",
                "Please insert numeric value in the input box",
                e
              )
            }
          }
        }
      )
      button
    }
    addCPHBox.getChildren.addAll(addX, addY, addTx, addTy, addButton)
  }

  private[this] def update() = {
    if (data.size >= 3) {
      val cps = {
        val builder = new CPSBuilder
        builder ++= data.asScala.map(cp => CP(
          Point(cp.getXValue, cp.getYValue),
          Point(cp.getTxValue, cp.getTyValue)
        ))
        builder.build
      }
      try {
        processResult(cps)
      } catch {
        case e: Exception =>
          ErrorDialog.show(
            "Control points error",
            "Please make sure the control point is well chosen to calculate inverse matrix",
            e
          )
      }
    } else {
      resultVBox.getChildren.clear()
      resultVBox.getChildren.add(notEnoughCP)
    }
  }

  private[this] def processResult(cps: ControlPoints) = {
    val affine = new Affine(cps)
    val mc = new MathContext(10, RoundingMode.HALF_UP)

    val title = {
      val text = new Text("Result")
      text.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 30))
      text
    }
    val rsmeBox = {
      val hbox = new HBox()
      hbox.setSpacing(3)
      val RSMETitle = new Text("RSME")
      RSMETitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 12))
      val RSME = new Text(affine.RMSE.round(mc).toString())
      RSME.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      hbox.getChildren.addAll(RSMETitle, RSME)
      hbox
    }
    val parametersGrid = {
      val grid = new GridPane
      grid.setHgap(10)
      grid.setVgap(10)
      grid.setPadding(new Insets(10, 10, 10, 10))

      val ATitle = new Text("A")
      ATitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(ATitle, 0, 0, 1, 1)
      val A = new Text(affine.A.round(mc).toString())
      grid.add(A, 1, 0, 1, 1)

      val BTitle = new Text("B")
      BTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(BTitle, 2, 0, 1, 1)
      val B = new Text(affine.B.round(mc).toString())
      grid.add(B, 3, 0, 1, 1)

      val CTitle = new Text("C")
      CTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(CTitle, 4, 0, 1, 1)
      val C = new Text(affine.C.round(mc).toString())
      grid.add(C, 5, 0, 1, 1)

      val DTitle = new Text("D")
      DTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(DTitle, 0, 1, 1, 1)
      val D = new Text(affine.D.round(mc).toString())
      grid.add(D, 1, 1, 1, 1)

      val ETitle = new Text("E")
      ETitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(ETitle, 2, 1, 1, 1)
      val E = new Text(affine.E.round(mc).toString())
      grid.add(E, 3, 1, 1, 1)

      val FTitle = new Text("F")
      FTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(FTitle, 4, 1, 1, 1)
      val F = new Text(affine.F.round(mc).toString())
      grid.add(F, 5, 1, 1, 1)
      grid
    }
    val infoGrid = {
      val grid = new GridPane
      grid.setHgap(10)
      grid.setVgap(10)
      grid.setPadding(new Insets(10, 10, 10, 10))

      val scaleTitle = new Text("Scale (X, Y) = ")
      scaleTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(scaleTitle, 0, 0, 1, 1)
      val scale = new Text(s"(${affine.sx.round(mc).toString()}, ${affine.sy.round(mc).toString()})")
      grid.add(scale, 1, 0, 1, 1)

      val skewTitle = new Text("Skew (degrees) = ")
      skewTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(skewTitle, 0, 1, 1, 1)
      val skew = new Text(s"(${affine.skewDegree.round(mc).toString()})")
      grid.add(skew, 1, 1, 1, 1)

      val rotationTitle = new Text("Rotation (degrees) = ")
      rotationTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(rotationTitle, 0, 2, 1, 1)
      val rotation = new Text(s"(${affine.rotationDegree.round(mc).toString()})")
      grid.add(rotation, 1, 2, 1, 1)

      val translationTitle = new Text("Translation = ")
      translationTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(translationTitle, 0, 3, 1, 1)
      val translation = new Text(s"(${affine.tx.round(mc).toString()}, ${affine.ty.round(mc).toString()})")
      grid.add(translation, 1, 3, 1, 1)

      grid
    }
    val cpGrid = {
      val grid = new GridPane
      grid.setHgap(10)
      grid.setVgap(10)
      grid.setPadding(new Insets(10, 10, 10, 10))

      val xTitle = new Text("x")
      xTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(xTitle, 0, 0, 1, 1)
      val yTitle = new Text("y")
      yTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(yTitle, 1, 0, 1, 1)
      val xtTitle = new Text("X")
      xtTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(xtTitle, 2, 0, 1, 1)
      val ytTitle = new Text("Y")
      ytTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(ytTitle, 3, 0, 1, 1)
      val xErrTitle = new Text("x error")
      xErrTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(xErrTitle, 4, 0, 1, 1)
      val yErrTitle = new Text("y error")
      yErrTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12))
      grid.add(yErrTitle, 5, 0, 1, 1)

      affine.cps.cps.indices.foreach(
        i => {
          val x = new Text(affine.cps.cps(i).origin.x.round(mc).toString())
          grid.add(x, 0, 1 + i, 1, 1)
          val y = new Text(affine.cps.cps(i).origin.y.round(mc).toString())
          grid.add(y, 1, 1 + i, 1, 1)
          val tx = new Text(affine.cps.cps(i).transformed.x.round(mc).toString())
          grid.add(tx, 2, 1 + i, 1, 1)
          val ty = new Text(affine.cps.cps(i).transformed.y.round(mc).toString())
          grid.add(ty, 3, 1 + i, 1, 1)
          val xerr = new Text(affine.residuals(i)._1.round(mc).toString())
          grid.add(xerr, 4, 1 + i, 1, 1)
          val yerr = new Text(affine.residuals(i)._2.round(mc).toString())
          grid.add(yerr, 5, 1 + i, 1, 1)
        }
      )
      grid
    }

    resultVBox.getChildren.clear()
    resultVBox.getChildren.addAll(title, rsmeBox, parametersGrid, infoGrid, cpGrid)
  }
}