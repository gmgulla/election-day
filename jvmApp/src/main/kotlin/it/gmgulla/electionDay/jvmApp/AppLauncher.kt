package it.gmgulla.electionDay.jvmApp

import it.gmgulla.electionDay.jvmApp.ui.views.MainView
import it.gmgulla.electionDay.jvmApp.ui.views.Styles
import tornadofx.App

class AppLauncher : App(MainView::class, Styles::class)