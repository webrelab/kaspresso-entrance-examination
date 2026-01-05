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
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }
        val currentAmount = storage[cereal] ?: 0f
        val availableSpace = if (currentAmount == 0f) {
            val totalContainers = storage.size
            if ((totalContainers + 1) * containerCapacity > storageCapacity) {
                throw IllegalStateException("Нет места для нового контейнера")
            }
            containerCapacity
        } else {
            containerCapacity - currentAmount
        }
        val toAdd = amount.coerceAtMost(availableSpace)
        storage[cereal] = currentAmount + toAdd
        return amount - toAdd
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }
        val currentAmount = storage[cereal] ?: return 0f
        val taken = amount.coerceAtMost(currentAmount)
        storage[cereal] = currentAmount - taken
        return taken
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
        val currentAmount = storage[cereal] ?: throw IllegalStateException("Контейнер отсутствует")
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        return storage.entries.joinToString(separator = "\n") {
            "${it.key}: ${it.value} / $containerCapacity"
        }
    }

}
