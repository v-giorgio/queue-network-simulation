package model

data class JobEvent(
    val arrivalTime: Double,
    val action: () -> Unit
): Comparable<JobEvent> {

    override fun compareTo(other: JobEvent) = arrivalTime.compareTo(other.arrivalTime) // imitar a min heap (ascendente)
}
