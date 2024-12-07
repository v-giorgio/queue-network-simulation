package service

import model.constants.SimulationType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SimulatorFactoryTest {

    @Test
    fun `test if QueueHandler is created for DETERMINISTIC simulation`() {
        val simulationType = SimulationType.DETERMINISTIC
        val queueHandler = SimulatorFactory.create(simulationType)

        assertEquals(simulationType, queueHandler.type)

        assertEquals(2.0, queueHandler.lambda)
        assertEquals(10_000, queueHandler.warmUpJobs)
        assertEquals(10_000, queueHandler.testJobs)

        assertEquals(0.4, queueHandler.serviceTimes(1))
        assertEquals(0.6, queueHandler.serviceTimes(2))
        assertEquals(0.95, queueHandler.serviceTimes(3))
    }

    @Test
    fun `test if QueueHandler is created for UNIFORM_DISTRIBUTION simulation`() {
        val simulationType = SimulationType.UNIFORM_DISTRIBUTION
        val queueHandler = SimulatorFactory.create(simulationType)

        assertEquals(simulationType, queueHandler.type)

        assertEquals(2.0, queueHandler.lambda)
        assertEquals(10_000, queueHandler.warmUpJobs)
        assertEquals(10_000, queueHandler.testJobs)

        assertDoesNotThrow { queueHandler.serviceTimes(1) }
        assertDoesNotThrow { queueHandler.serviceTimes(2) }
        assertDoesNotThrow { queueHandler.serviceTimes(3) }
    }

    @Test
    fun `test if QueueHandler is created for EXPONENTIAL_DISTRIBUTION simulation`() {
        val simulationType = SimulationType.EXPONENTIAL_DISTRIBUTION
        val queueHandler = SimulatorFactory.create(simulationType)

        assertEquals(simulationType, queueHandler.type)

        assertEquals(2.0, queueHandler.lambda)
        assertEquals(10_000, queueHandler.warmUpJobs)
        assertEquals(10_000, queueHandler.testJobs)

        assertDoesNotThrow { queueHandler.serviceTimes(1) }
        assertDoesNotThrow { queueHandler.serviceTimes(2) }
        assertDoesNotThrow { queueHandler.serviceTimes(3) }
    }

    @ParameterizedTest
    @EnumSource(SimulationType::class)
    fun `test if exception is thrown for invalid server number in serviceTimes`(simulationType: SimulationType) {
        val queueHandler = SimulatorFactory.create(simulationType)

        assertFailsWith<IllegalArgumentException> {
            queueHandler.serviceTimes(99)
        }
    }
}
