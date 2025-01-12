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
        TODO("Not yet implemented")
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        TODO("Not yet implemented")
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAmount(cereal: Cereal): Float {
        TODO("Not yet implemented")
    }

    override fun getSpace(cereal: Cereal): Float {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        TODO("Not yet implemented")
    }

}
