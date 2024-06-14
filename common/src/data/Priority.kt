package data

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

fun Priority.toCount() = when (this) {
    Priority.LOW -> 1
    Priority.MEDIUM -> 2
    Priority.HIGH -> 3
}
