package service

import model.constants.SimulationType
import utils.GetSamples

class SimulatorFactory {

    companion object {

        fun create(simulationType: SimulationType) : QueueHandler =
            when (simulationType) {
                SimulationType.DETERMINISTIC -> QueueHandler(
                    type = SimulationType.DETERMINISTIC,
                    lambda = 2.0,
                    serviceTimes = { when (it) {
                        1 -> 0.4
                        2 -> 0.6
                        3 -> 0.95
                        else -> throw IllegalArgumentException("Número de servidor inválido")
                    } },
                    warmUpJobs = 10_000,
                    testJobs = 10_000
                )
                SimulationType.UNIFORM_DISTRIBUTION -> QueueHandler(
                    type = SimulationType.UNIFORM_DISTRIBUTION,
                    lambda = 2.0,
                    serviceTimes = { when (it) {
                        1 -> GetSamples.getUniformSample(0.1, 0.7)
                        2 -> GetSamples.getUniformSample(0.1, 1.1)
                        3 -> GetSamples.getUniformSample(0.1, 1.8)
                        else -> throw IllegalArgumentException("Número de servidor inválido")
                    } },
                    warmUpJobs = 10_000,
                    testJobs = 10_000
                )
                SimulationType.EXPONENTIAL_DISTRIBUTION -> QueueHandler(
                    type = SimulationType.EXPONENTIAL_DISTRIBUTION,
                    lambda = 2.0,
                    serviceTimes = { when (it) {
                        1 -> GetSamples.getExponentialInverseCdfSample(1 / 0.4) // 1/E[S] = λ
                        2 -> GetSamples.getExponentialInverseCdfSample(1 / 0.6)
                        3 -> GetSamples.getExponentialInverseCdfSample(1 / 0.95)
                        else -> throw IllegalArgumentException("Número de servidor inválido")
                    } },
                    warmUpJobs = 10_000,
                    testJobs = 10_000
                )
            }
    }
}