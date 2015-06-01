package edu.nccu.plsm.gis.gui

import java.io.{PrintWriter, StringWriter}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, Label, TextArea}
import javafx.scene.layout.{GridPane, Priority}

/**
 * @version
 * @since
 */
object ErrorDialog {
  def show(header: String, content: String, exception: Exception) = {
    val alert = new Alert(AlertType.ERROR)
    alert.setTitle("Error")
    alert.setHeaderText(header)
    alert.setContentText(content)

    val exceptionText = {
      val sw = new StringWriter()
      val pw = new PrintWriter(sw)
      exception.printStackTrace(pw)
      sw.toString
    }

    val label = new Label("The exception stacktrace was:")
    val textArea = {
      val ta = new TextArea(exceptionText)
      ta.setEditable(false)
      ta.setWrapText(true)
      ta.setMaxWidth(Double.MaxValue)
      ta.setMaxHeight(Double.MaxValue)
      ta
    }

    GridPane.setVgrow(textArea, Priority.ALWAYS)
    GridPane.setHgrow(textArea, Priority.ALWAYS)
    val expContent = {
      val grid = new GridPane()
      grid.setMaxWidth(Double.MaxValue)
      grid.add(label, 0, 0)
      grid.add(textArea, 0, 1)
      grid
    }

    alert.getDialogPane.setExpandableContent(expContent)
    alert.showAndWait()
  }
}
