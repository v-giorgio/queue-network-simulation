package service

import model.JobEvent
import model.constants.SimulationType
import utils.GetSamples
import java.util.PriorityQueue
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextDouble

class QueueHandler(
    val type: SimulationType,
    val lambda: Double,
    val serviceTimes: (Int) -> Double,
    val warmUpJobs: Int,
    val testJobs: Int
) {
    private val priorityQueue = PriorityQueue<JobEvent>()

    private var currentTime = 0.0
    private var amountJobsProcessedSoFar = 0
    private var responseTimes = mutableListOf<Double>()

    private val serverOneQueue = mutableListOf<Double>()
    private val serverTwoQueue = mutableListOf<Double>()
    private val serverThreeQueue = mutableListOf<Double>()

    private var meanResponseTime = 0.0
    private var standardDeviationResponseTime = 0.0

    fun execute(): Results {
        addNewArrival()

        while (amountJobsProcessedSoFar < warmUpJobs + testJobs) {
            val jobEvent = priorityQueue.poll()
            currentTime = jobEvent.arrivalTime
            jobEvent.action()
        }

        meanResponseTime = responseTimes.average()
        standardDeviationResponseTime = responseTimes.map { (it - meanResponseTime).pow(2) }
            .average().let { sqrt(it) }

        return Results(type, meanResponseTime, standardDeviationResponseTime)
    }

    private fun addNewArrival() {
        val arrivalTime = currentTime + GetSamples.getExponentialInverseCdfSample(lambda)
        priorityQueue.add(JobEvent(arrivalTime) { // somente primeiro job inicialmente adicionado
            serverOneQueue.add(arrivalTime)
            addOnServer(1)
            addNewArrival()
        })
    }

    private fun addOnServer(server: Int): Any? =
        when (server) {
            1 -> {
                val arrivalTime = serverOneQueue.removeAt(0)
                val serviceTime = serviceTimes(1)
                val accTime = max(currentTime, arrivalTime) + serviceTime // não começar antes da hora

                priorityQueue.add(JobEvent(accTime) {
                    if (nextDouble() < 0.5) {
                        serverTwoQueue.add(accTime)
                        addOnServer(2)
                    } else {
                        serverThreeQueue.add(accTime)
                        addOnServer(3)
                    }
                })
            }
            2 -> {
                if (serverTwoQueue.isNotEmpty()) {
                    val arrivalTime = serverTwoQueue.removeAt(0)
                    val serviceTime = serviceTimes(2)
                    val completionTime = max(currentTime, arrivalTime) + serviceTime

                    priorityQueue.add(JobEvent(completionTime) {
                        if (nextDouble() < 0.2) {
                            serverTwoQueue.add(completionTime)
                            addOnServer(2)
                        } else {
                            if (amountJobsProcessedSoFar >= warmUpJobs) {
                                responseTimes.add(completionTime - arrivalTime)
                            }
                            amountJobsProcessedSoFar++
                        }
                    })
                } else null
            }
            3 -> {
                if (serverThreeQueue.isNotEmpty()) {
                    val arrivalTime = serverThreeQueue.removeAt(0)
                    val serviceTime = serviceTimes(3)
                    val completionTime = max(currentTime, arrivalTime) + serviceTime

                    priorityQueue.add(JobEvent(completionTime) {
                        if (amountJobsProcessedSoFar >= warmUpJobs) {
                            responseTimes.add(completionTime - arrivalTime)
                        }
                        amountJobsProcessedSoFar++
                    })
                } else null
            }
            else -> throw IllegalArgumentException("Número de servidor inválido")
        }

    class Results(
        val type: SimulationType,
        val meanResponseTime: Double,
        val standardDeviationResponseTime: Double
    )
}
