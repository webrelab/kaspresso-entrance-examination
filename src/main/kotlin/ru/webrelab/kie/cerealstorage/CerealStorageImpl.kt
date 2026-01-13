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
    require(amount >= 0) { "Количество добавляемой крупы не может быть отрицательным" }
    if (cereal !in storage) {
      check(storage.values.sum() + containerCapacity <= storageCapacity) {
        "В хранилище нет места для контейнера с новой крупой - $cereal"
      }
    }
    val currentAmount: Float = storage.getOrPut(cereal) { 0f }
    val totalContainerAmount: Float = currentAmount + amount
    return if (totalContainerAmount > containerCapacity) {
      val overflow = totalContainerAmount - containerCapacity
      storage[cereal] = containerCapacity
      overflow
    } else {
      storage[cereal] = totalContainerAmount
      0f
    }
  }

  override fun getCereal(cereal: Cereal, amount: Float): Float {
    require(amount >= 0) { "Количество извлекаемой крупы не может быть отрицательным" }
    require(cereal in storage) { "Крупы $cereal нет на складе. Доступные крупы: ${storage.keys}" }
    val currentAmount: Float = storage.getValue(cereal)
    return if (currentAmount < amount) {
      currentAmount
    } else {
      storage[cereal] = currentAmount - amount
      amount
    }
  }

  override fun removeContainer(cereal: Cereal): Boolean {
    val container = storage[cereal] ?: return false
    if (container != 0f) return false
    storage.remove(cereal)
    return true
  }

  override fun getAmount(cereal: Cereal): Float {
    return storage[cereal] ?: 0f
  }

  override fun getSpace(cereal: Cereal): Float {
    val currentAmount = storage[cereal] ?: throw IllegalStateException("На складе нет контейнера с крупой - $cereal")
    return containerCapacity - currentAmount
  }

  override fun toString(): String {
    return buildString {
      append("CerealStorage {\n")
      append("  containerCapacity: $containerCapacity \n")
      append("  storageCapacity: $storageCapacity \n")
      append("  Containers:\n")
      storage.forEach { (cereal, amount) ->
        append(
          "    - $cereal: ${"%.2f".format(amount)} " +
              "(free: ${"%.2f".format(getSpace(cereal))})\n"
        )
      }
      append("(total free space:${"%.2f".format(storageCapacity - storage.values.sum())} )\n")
      if (storage.isEmpty()) {
        append("    (no containers)\n")
      }
      append("}")
    }
  }
}