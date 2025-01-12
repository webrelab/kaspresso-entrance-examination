package ru.webrelab.kie.cerealstorage

import kotlin.math.abs

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
        if (amount < 0) {
            throw IllegalArgumentException("Количество крупы не может быть отрицательным")
        }
        return if (storage.containsKey(cereal)) {
            val tempValue = storage[cereal]!! + amount
            putCereal(cereal, tempValue)
        } else {
            if ((storageCapacity - storage.count() * containerCapacity) >= containerCapacity) {
                putCereal(cereal, amount)
            } else {
                throw IllegalStateException("Недостаточно места в хранилище")
            }
        }
    }

    private fun putCereal(cereal: Cereal, amount: Float): Float {
        return if (amount >= containerCapacity) {
            storage[cereal] = containerCapacity
            amount - containerCapacity
        } else {
            storage[cereal] = amount
            0f
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0) {
            throw IllegalArgumentException("Количество крупы не может быть отрицательным")
        }
        return if (storage.containsKey(cereal)) {
            val tempValue = storage[cereal]!! - amount
            if (tempValue < 0) {
                storage[cereal] = 0f
                tempValue + amount
            } else {
                storage[cereal] = tempValue
                amount
            }
        } else {
            0f
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        if (!storage.containsKey(cereal)) return true
        return if (abs(storage[cereal]!!) <= 0.01) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return if (cereal in storage) {
            storage[cereal]!!
        } else {
            0f
        }
    }

    override fun getSpace(cereal: Cereal): Float {
        return if (cereal in storage) {
            containerCapacity - storage[cereal]!!
        } else {
            if ((storageCapacity - storage.count() * containerCapacity) >= containerCapacity) {
                containerCapacity
            } else {
                0f
            }
        }
    }

    override fun toString(): String {
        return "CerealStorageImpl(containerCapacity=$containerCapacity, storageCapacity=$storageCapacity)"
    }

}
