package utils

import kotlin.math.ln
import kotlin.random.Random.Default.nextDouble

object GetSamples {

    fun getExponentialInverseCdfSample(lambda: Double) = -(ln(1.0 - nextDouble(0.0, 1.0)) / lambda)

    fun getUniformSample(min: Double, max: Double) = nextDouble(min, max)
}