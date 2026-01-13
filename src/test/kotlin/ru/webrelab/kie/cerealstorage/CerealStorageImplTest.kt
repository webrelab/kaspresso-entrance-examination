package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.webrelab.kie.cerealstorage.Cereal.BUCKWHEAT
import ru.webrelab.kie.cerealstorage.Cereal.BULGUR
import ru.webrelab.kie.cerealstorage.Cereal.MILLET
import ru.webrelab.kie.cerealstorage.Cereal.PEAS
import ru.webrelab.kie.cerealstorage.Cereal.RICE

class CerealStorageImplTest {

  private val storage = CerealStorageImpl(10f, 20f)
  private val delta = 0.01f

  private fun CerealStorage.fillWith(vararg cereals: Pair<Cereal, Float>) {
    cereals.forEach { (cereal, amount) -> addCereal(cereal, amount) }
  }

  @Test
  fun `should throw if containerCapacity is negative`() {
    assertThrows(IllegalArgumentException::class.java) {
      CerealStorageImpl(-4f, 10f)
    }
  }

  @Test
  fun `should throw if storageCapacity less containerCapacity`() {
    assertThrows(IllegalArgumentException::class.java) {
      CerealStorageImpl(10f, 9f)
    }
  }

  @Test
  fun `should throw if amount for add cereal is negative`() {
    assertThrows(IllegalArgumentException::class.java) {
      storage.addCereal(RICE, -2F)
    }
  }

  @Test
  fun `add to container returns 0 when amount equals 0`() {
    storage.fillWith(RICE to 5f, BUCKWHEAT to 3f)
    val actualOverflow = storage.addCereal(RICE, 0f)
    assertEquals(
      0f, actualOverflow,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `add to container returns 0 when amount equals container capacity`() {
    storage.fillWith(RICE to 5f, MILLET to 3f)
    val actualOverflow = storage.addCereal(BUCKWHEAT, 10f)
    assertEquals(
      0f, actualOverflow,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `add to new container returns 0 when amount equals between  0 and container capacity`() {
    storage.fillWith(BUCKWHEAT to 3f, PEAS to 3f)
    val actualOverflow = storage.addCereal(MILLET, 5f)
    assertEquals(
      0f, actualOverflow,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `addCereal returns 0 when amount equals capacity plus delta`() {
    storage.fillWith(BUCKWHEAT to 2f, PEAS to 5f)
    val actualOverflow = storage.addCereal(MILLET, 10.001f)
    assertEquals(
      0f, actualOverflow, delta,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `addCereal returns overflow when amount by more than delta`() {
    storage.fillWith(BUCKWHEAT to 2f, PEAS to 5f)
    val actualOverflow = storage.addCereal(MILLET, 10.011f)
    assertEquals(
      0.01f, actualOverflow, delta,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `addCereal with multi plus returns 0 when amount equals capacity plus delta`() {
    storage.fillWith(BUCKWHEAT to 3f, PEAS to 3f, PEAS to 6f)
    val actualOverflow = storage.addCereal(PEAS, 1.001f)
    assertEquals(
      0f, actualOverflow, delta,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `addCereal with multi plus returns overflow when amount by more than delta`() {
    storage.fillWith(BUCKWHEAT to 3f, PEAS to 5f, MILLET to 2f, MILLET to 2f)
    val actualOverflow = storage.addCereal(MILLET, 6.011f)
    assertEquals(
      0.01f, actualOverflow, delta,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `add new container returns overflow`() {
    storage.fillWith(BULGUR to 9f)
    val actualOverflow = storage.addCereal(RICE, 15f)
    assertEquals(
      5f, actualOverflow,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `add to existing container amount returns overflow`() {
    storage.fillWith(RICE to 5f, BUCKWHEAT to 3f)
    val actualOverflow = storage.addCereal(RICE, 15f)
    assertEquals(
      10f, actualOverflow,
      "Переполнение не равно ожидаемому значению, а равно - $actualOverflow"
    )
  }

  @Test
  fun `should throw if lake of place for container`() {
    assertThrows(IllegalStateException::class.java) {
      storage.fillWith(RICE to 5f, BUCKWHEAT to 3f, MILLET to 3f)
      storage.addCereal(PEAS, 3f)
    }
  }

  @Test
  fun `should throw if amount for get cereal is negative`() {
    assertThrows(IllegalArgumentException::class.java) {
      storage.fillWith(BUCKWHEAT to 3f, MILLET to 2f, PEAS to 5f)
      storage.getCereal(PEAS, -2f)
    }
  }

  @Test
  fun `should throw if getting cereal is not in storage`() {
    assertThrows(IllegalArgumentException::class.java) {
      storage.fillWith(BUCKWHEAT to 3f, MILLET to 2f)
      storage.getCereal(PEAS, 2f)
    }
  }
  @Test
  fun `getAmount returns exact amount for existing cereal`() {
    storage.fillWith(BUCKWHEAT to 0.1f, MILLET to 2f, BULGUR to 5f)
    val actualAmount = storage.getAmount(BUCKWHEAT)
    assertEquals(
      0.1f, actualAmount,
      "Количество не равно ожидаемому результату, а равно - $actualAmount"
    )
  }

  @Test
  fun `getAmount returns 0 for non-existing cereal`() {
    storage.fillWith(BUCKWHEAT to 2f, MILLET to 2f, BULGUR to 5f)
    val actualAmount = storage.getAmount(RICE)
    assertEquals(
      0f, actualAmount,
      "Количество не равно ожидаемому результату, а равно - $actualAmount"
    )
  }

  @Test
  fun `should return getting amount cereal in case amount less current amount`() {
    storage.fillWith(BUCKWHEAT to 3f, MILLET to 2f, PEAS to 5f)
    val receivedAmount = storage.getCereal(PEAS, 3f)
    val currentAmount = storage.getAmount(PEAS)
    assertEquals(
      3f, receivedAmount,
      "Изъятие не равно ожидаемому значению, а равно - $receivedAmount"
    )
    assertEquals(
      2f, currentAmount,
      "Текущее количество не равно ожидаемому значению, а равно - $currentAmount"
    )
  }

  @Test
  fun `getCereal returns exact amount when taking exact available amount`() {
    storage.fillWith(RICE to 5f)
    val receivedAmount = storage.getCereal(RICE, 5f)
    val currentAmount = storage.getAmount(RICE)
    assertEquals(
      5f, receivedAmount, delta,
      "Изъятие не равно ожидаемому значению, а равно - $receivedAmount"
    )
    assertEquals(
      0f, currentAmount, delta,
      "Текущее количество не равно ожидаемому значению, а равно - $currentAmount"
    )
  }

  @Test
  fun `getCereal leaves small amount when taking almost all`() {
    storage.fillWith(MILLET to 5f)
    val receivedAmount = storage.getCereal(MILLET, 4.99f)
    val currentAmount = storage.getAmount(MILLET)
    assertEquals(
      4.99f, receivedAmount, delta,
      "Изъятие не равно ожидаемому значению, а равно - $receivedAmount"
    )
    assertEquals(
      0.01f, currentAmount, delta,
      "Текущее количество не равно ожидаемому значению, а равно - $currentAmount"
    )
  }

  @Test
  fun `getCereal handles float precision at boundary`() {
    storage.fillWith(MILLET to 0.3f)
    val receivedAmount = storage.getCereal(MILLET, 0.1f)
    val currentAmount = storage.getAmount(MILLET)
    assertEquals(
      0.1f, receivedAmount, delta,
      "Изъятие не равно ожидаемому значению, а равно - $receivedAmount"
    )
    assertEquals(
      0.2f, currentAmount, delta,
      "Текущее количество не равно ожидаемому значению, а равно - $currentAmount"
    )
  }

  @Test
  fun `should return getting amount cereal in case amount more current amount`() {
    storage.fillWith(BUCKWHEAT to 2f, MILLET to 2f, PEAS to 5f)
    val actualRemains = storage.getCereal(BUCKWHEAT, 2.01f)
    assertEquals(
      2f, actualRemains, delta,
      "Текущий остаток не равен ожидаемому значению, а равно - $actualRemains"
    )
  }

  @Test
  fun `removeContainer returns false for non-existent cereal`() {
    storage.fillWith(BUCKWHEAT to 2f, MILLET to 2f, BULGUR to 5f)
    assertFalse(
      storage.removeContainer(PEAS), "Для несуществующего контейнера должен возвращаться false"
    )
  }

  @Test
  fun `removeContainer returns false for non-empty container`() {
    storage.fillWith(BUCKWHEAT to 0.01f, MILLET to 2f, BULGUR to 5f)
    assertFalse(
      storage.removeContainer(BUCKWHEAT), "Для не пустого контейнера возвращаем - false"
    )
  }

  @Test
  fun `removeContainer returns true for empty container`() {
    storage.fillWith(BUCKWHEAT to 2f, MILLET to 2f, BULGUR to 0f)
    assertTrue(storage.removeContainer(BULGUR), "Для пустого контейнера должен возвращаем - true")
  }

  @Test
  fun `getSpace throws IllegalStateException for non-existent cereal`() {
    assertThrows(IllegalStateException::class.java) {
      storage.fillWith(BUCKWHEAT to 2f, PEAS to 2f, BULGUR to 5f, MILLET to 5f)
      storage.getSpace(RICE)
    }
  }

  @Test
  fun `getSpace returns positive value for existing cereal`() {
    storage.fillWith(BUCKWHEAT to 2f, PEAS to 2f, BULGUR to 5f, MILLET to 5f)
    val actualSpace = storage.getSpace(BUCKWHEAT)
    assertEquals(
      8f, actualSpace,
      "Свободное пространство не равно ожидаемому результату, а равно - $actualSpace"
    )
  }

  @Test
  fun `getSpace returns value within delta when container almost full`() {
    storage.fillWith( RICE to 9.99f, BUCKWHEAT to 9f)
    val actualSpace = storage.getSpace(RICE)
    assertEquals(
      0.01f, actualSpace, delta,
      "Свободное пространство не равно ожидаемому результату, а равно - $actualSpace"
    )
  }

  @Test
  fun `getSpace returns almost full capacity when container almost empty`() {
    storage.fillWith(BUCKWHEAT to 0.01f)
    val actualSpace = storage.getSpace(BUCKWHEAT)
    assertEquals(
      9.99f, actualSpace, delta,
      "Свободное пространство не равно ожидаемому результату, а равно - $actualSpace"
    )
  }

  @Test
  fun `toString returns string containing cereal information and capacities`() {
    storage.fillWith(BUCKWHEAT to 2f, PEAS to 4f)
    val actualString = storage.toString()
    val hasAnyCerealInfo =
      actualString.contains("PEAS") &&
          actualString.contains("BUCKWHEAT") &&
          actualString.contains("2") &&
          actualString.contains("4")
    assertTrue(hasAnyCerealInfo, "Строка должна содержать информацию о крупах: $actualString")
    assertTrue(
      actualString.contains(storage.storageCapacity.toString()),
      "Строка должна содержать информацию о емкости склада"
    )
    assertTrue(
      actualString.contains(storage.containerCapacity.toString()),
      "Строка должна содержать информацию о емкости контейнера"
    )
  }
}