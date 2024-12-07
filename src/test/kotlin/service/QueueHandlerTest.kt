package service

import model.constants.SimulationType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class QueueHandlerTest {

    private lateinit var queueHandler: QueueHandler
    private lateinit var serviceTimesMock: (Int) -> Double

    @BeforeEach
    fun setup() {
        serviceTimesMock = mock()
        whenever(serviceTimesMock(1)).thenReturn(0.4)
        whenever(serviceTimesMock(2)).thenReturn(0.6)
        whenever(serviceTimesMock(3)).thenReturn(0.95)

        queueHandler = QueueHandler(
            type = SimulationType.DETERMINISTIC,
            lambda = 2.0,
            serviceTimes = serviceTimesMock,
            warmUpJobs = 1000,
            testJobs = 1000
        )
    }

    @Test
    fun `test if execute runs without errors`() {
        val result = queueHandler.execute()

        assertEquals(SimulationType.DETERMINISTIC, result.type)

        assert(result.meanResponseTime > 0.0)
        assert(result.standardDeviationResponseTime > 0.0)
    }

    @Test
    fun `test if mean response time is calculated correctly`() {
        whenever(serviceTimesMock(1)).thenReturn(0.5)
        whenever(serviceTimesMock(2)).thenReturn(0.8)
        whenever(serviceTimesMock(3)).thenReturn(1.0)

        val result = queueHandler.execute()

        assert(result.meanResponseTime > 0.0)
    }

    @Test
    fun `test if standard deviation is calculated correctly`() {
        whenever(serviceTimesMock(1)).thenReturn(0.4)
        whenever(serviceTimesMock(2)).thenReturn(0.6)
        whenever(serviceTimesMock(3)).thenReturn(0.95)

        val result = queueHandler.execute()

        assert(result.standardDeviationResponseTime > 0.0)
    }
}
