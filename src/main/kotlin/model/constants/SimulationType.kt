package model.constants

enum class SimulationType(val description: String) {
    DETERMINISTIC("Os tempos de serviço são determinísticos"),
    UNIFORM_DISTRIBUTION("Os tempos de serviço são VAs uniformes nos intervalos dados"),
    EXPONENTIAL_DISTRIBUTION("Os tempos de serviço são VAs exponenciais"),
}