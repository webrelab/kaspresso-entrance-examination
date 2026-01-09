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
        require(amount >= 0F) {
            "Количество добавляемой крупы не может быть отрицательной"
        }
        require(containerCapacity >= amount) {
            "Количество добавляемой крупы не может быть больше ёмкости одного контейнера"
        }

        val currentAmount: Float? = storage[cereal]
        var addedAmount: Float
        if (currentAmount != null) {
            check(currentAmount != containerCapacity) {
                "Хранилище не позволяет разместить ещё один контейнер"
            }

            if (getSpace(cereal) < amount) {
                addedAmount = amount - getSpace(cereal)
                storage[cereal] = containerCapacity
            } else {
                storage[cereal] = currentAmount + amount
                addedAmount = amount
            }
        } else {
            storage.put(cereal, amount)
            addedAmount = amount
        }

        return addedAmount
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0F) {
            "Количество получаемой крупы не может быть отрицательной"
        }
        require(containerCapacity >= amount) {
            "Количество получаемой крупы не может быть больше ёмкости одного контейнера"
        }
        require(storage[cereal] != null) {
            "Контейнер с крупой должен существовать"
        }

        val currentAmount: Float = storage[cereal]!!
        if (currentAmount < amount) {
            return currentAmount
        } else {
            storage[cereal] = currentAmount - amount
            return amount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage[cereal] ?: return false
        if (currentAmount != 0f) return false

        storage.remove(cereal)
        return true
    }

    override fun getAmount(cereal: Cereal): Float =
        storage[cereal] ?: 0f

    override fun getSpace(cereal: Cereal): Float {
        check(storage[cereal] != null) {
            "Контейнер с крупой должен существовать"
        }
        return containerCapacity - storage[cereal]!!
    }

    override fun toString(): String {
        return "containerCapacity = $containerCapacity, storageCapacity = $storageCapacity"
    }
}
