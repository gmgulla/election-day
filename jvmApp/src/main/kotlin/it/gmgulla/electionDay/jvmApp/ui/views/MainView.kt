package it.gmgulla.electionDay.jvmApp.ui.views

import tornadofx.*

class MainView : View("Hello TornadoFX") {
    override val root = hbox {
        label("$title This is Election-Day App") {
           addClass(Styles.heading)
        }
    }
}
