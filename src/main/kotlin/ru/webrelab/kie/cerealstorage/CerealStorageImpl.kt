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
        if (amount < 0) throw IllegalArgumentException("Cannot add negative amount of cereal")
        val cerealAmount = (storage[cereal] ?: 0f) + amount
        if ((cereal !in storage.keys)
            && (storageCapacity - storage.keys.count() * containerCapacity < containerCapacity)
        ) throw IllegalStateException("Cannot add another container")
        storage[cereal] = if (cerealAmount < containerCapacity) cerealAmount else containerCapacity
        var remainder = cerealAmount - containerCapacity
        remainder = if (remainder >= 0) remainder else 0f
        return remainder
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0) throw IllegalArgumentException("Cannot get negative amount")
        val amountObtained = storage[cereal]?.let {
            if (it >= amount) amount else storage[cereal]
        } ?: throw IllegalStateException("There is no such cereal in the storage")
        storage[cereal] = (storage[cereal] ?: 0f) - amountObtained
        return amountObtained
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        if (storage[cereal] == null) throw IllegalStateException("No such container")
        if (storage[cereal] == 0f) {
            val result = storage.remove(cereal)?.let {
                it == 0f
            } ?: throw IllegalStateException("No such container")
            return result
        }
        return false
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage[cereal] ?: throw IllegalStateException("There is no such container")
    }

    override fun getSpace(cereal: Cereal): Float {
        return storage[cereal]?.let {
            containerCapacity - it
        } ?: throw IllegalStateException("There is no such container")
    }

    override fun toString(): String {
        return storage.toString()
    }

}
