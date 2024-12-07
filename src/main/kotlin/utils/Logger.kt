package utils

import model.constants.SimulationType

object Loggable {
    fun info(simulationType: SimulationType, meanResponseTime: Double, stdDevResponseTime: Double) {
        println("$simulationType: ${simulationType.description}")
        println("Tempo médio no sistema: $meanResponseTime")
        println("Desvio padrão do tempo no sistema: $stdDevResponseTime")
        println("\n")
    }
}