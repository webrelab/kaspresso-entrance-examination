package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество не может быть отрицательным" }

        val currentAmount = storage[cereal] ?: 0f

        if (currentAmount == 0f) {
            val usedCapacity = storage.values.sum()
            if (usedCapacity + containerCapacity > storageCapacity) {
                throw IllegalStateException("Хранилище не позволяет разместить ещё один контейнер для новой крупы")
            }

        }
        val availableSpace = containerCapacity - currentAmount

        if (amount <= availableSpace) {
            storage[cereal] = currentAmount + amount
            return 0f
        } else {
            storage[cereal] = containerCapacity
            return amount - availableSpace
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество не может быть отрицательным" }

        val currentAmount = storage[cereal] ?: 0f

        if (currentAmount >= amount) {
            val newAmount = currentAmount - amount
            if (newAmount > 0) {
                storage[cereal] = newAmount
            } else {
                storage.remove(cereal)
            }
            return amount
        } else {
            storage.remove(cereal)
            return currentAmount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage[cereal] ?: return false
        return if (currentAmount == 0f) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage[cereal] ?: throw IllegalStateException("Контейнер для $cereal отсутствует")
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        if (storage.isEmpty()) return "Хранилище пусто"

        return buildString {
            append("Хранилище (контейнеры: ${storage.size}):\n")
            storage.forEach { (cereal, amount) ->
                val percentage = (amount / containerCapacity * 100).toInt()
                append("${cereal.local}: $amount кг ($percentage%)\n")
            }
        }.trim()
    }
}
