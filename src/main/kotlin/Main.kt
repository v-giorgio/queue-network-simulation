import model.constants.SimulationType
import service.SimulatorFactory
import utils.Loggable

fun main() {
    val simulationOne = SimulatorFactory.create(SimulationType.DETERMINISTIC).execute()
    val simulationTwo = SimulatorFactory.create(SimulationType.UNIFORM_DISTRIBUTION).execute()
    val simulationThree = SimulatorFactory.create(SimulationType.EXPONENTIAL_DISTRIBUTION).execute()

    listOf(simulationOne, simulationTwo, simulationThree).forEach {
        Loggable.info(it.type, it.meanResponseTime, it.standardDeviationResponseTime)
    }
}