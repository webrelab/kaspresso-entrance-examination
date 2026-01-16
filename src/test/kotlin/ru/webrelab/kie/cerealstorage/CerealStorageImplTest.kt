package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `getAmount should return 0 for non-existent cereal`() {
        assertEquals(0f, storage.getAmount(Cereal.PEAS), 0.01f)
    }

    @Test
    fun `getAmount should return correct amount for existing cereal`() {
        storage.addCereal(Cereal.BULGUR, 7.5f)
        assertEquals(7.5f, storage.getAmount(Cereal.BULGUR), 0.01f)
    }

    @Test
    fun `getAmount should return updated amount after operations`() {
        storage.addCereal(Cereal.RICE, 5f)
        storage.getCereal(Cereal.RICE, 2f)
        assertEquals(3f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `addCereal should add cereal to empty storage`() {
        val remaining = storage.addCereal(Cereal.BUCKWHEAT, 5f)
        assertEquals(0f, remaining, 0.01f)
        assertEquals(5f, storage.getAmount(Cereal.BUCKWHEAT), 0.01f)
    }

    @Test
    fun `addCereal should return remaining when container overflows`() {
        storage.addCereal(Cereal.RICE, 8f)
        val remaining = storage.addCereal(Cereal.RICE, 5f)
        assertEquals(3f, remaining, 0.01f)
        assertEquals(10f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `addCereal should throw when adding negative amount`() {
        val exception = assertThrows<IllegalArgumentException> {
            storage.addCereal(Cereal.BUCKWHEAT, -2f)
        }
        assertEquals("Количество не может быть отрицательным", exception.message)
    }

    @Test
    fun `addCereal should throw when no space for new container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        storage.addCereal(Cereal.RICE, 10f)

        val exception = assertThrows<IllegalStateException> {
            storage.addCereal(Cereal.MILLET, 1f)
        }
        assertEquals("Хранилище не позволяет разместить ещё один контейнер для новой крупы", exception.message)
    }

    @Test
    fun `addCereal should add to existing container without overflow`() {
        storage.addCereal(Cereal.PEAS, 3f)
        val remaining = storage.addCereal(Cereal.PEAS, 4f)
        assertEquals(0f, remaining, 0.01f)
        assertEquals(7f, storage.getAmount(Cereal.PEAS), 0.01f)
    }

    @Test
    fun `addCereal should handle multiple additions to same container`() {
        storage.addCereal(Cereal.BULGUR, 2f)
        storage.addCereal(Cereal.BULGUR, 3f)
        storage.addCereal(Cereal.BULGUR, 4f)

        assertEquals(9f, storage.getAmount(Cereal.BULGUR), 0.01f)
        assertEquals(1f, storage.getSpace(Cereal.BULGUR), 0.01f)
    }

    @Test
    fun `removeContainer should return true for empty container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        val result = storage.removeContainer(Cereal.BUCKWHEAT)
        assertTrue(result)
        assertEquals(0f, storage.getAmount(Cereal.BUCKWHEAT), 0.01f)
    }

    @Test
    fun `removeContainer should return false for non-empty container`() {
        storage.addCereal(Cereal.RICE, 5f)
        val result = storage.removeContainer(Cereal.RICE)
        assertFalse(result)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `removeContainer should return false for non-existent container`() {
        val result = storage.removeContainer(Cereal.MILLET)
        assertFalse(result)
    }

    @Test
    fun `getCereal should return requested amount when enough cereal`() {
        storage.addCereal(Cereal.PEAS, 8f)
        val taken = storage.getCereal(Cereal.PEAS, 3f)
        assertEquals(3f, taken, 0.01f)
        assertEquals(5f, storage.getAmount(Cereal.PEAS), 0.01f)
    }

    @Test
    fun `getCereal should return remaining when not enough cereal`() {
        storage.addCereal(Cereal.BULGUR, 3f)
        val taken = storage.getCereal(Cereal.BULGUR, 5f)
        assertEquals(3f, taken, 0.01f)
        assertEquals(0f, storage.getAmount(Cereal.BULGUR), 0.01f)
    }

    @Test
    fun `getCereal should throw when getting negative amount`() {
        val exception = assertThrows<IllegalArgumentException> {
            storage.getCereal(Cereal.BUCKWHEAT, -1f)
        }
        assertEquals("Количество не может быть отрицательным", exception.message)
    }

    @Test
    fun `getCereal should return 0 for non-existent cereal`() {
        val taken = storage.getCereal(Cereal.RICE, 5f)
        assertEquals(0f, taken, 0.01f)
    }

    @Test
    fun `getCereal should remove container when emptied`() {
        storage.addCereal(Cereal.MILLET, 3f)
        storage.getCereal(Cereal.MILLET, 3f)
        assertEquals(0f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `getCereal should not remove container when some cereal remains`() {
        storage.addCereal(Cereal.RICE, 7f)
        storage.getCereal(Cereal.RICE, 2f)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `getSpace should return available space for existing container`() {
        storage.addCereal(Cereal.RICE, 4f)
        val space = storage.getSpace(Cereal.RICE)
        assertEquals(6f, space, 0.01f)
    }

    @Test
    fun `getSpace should throw IllegalStateException for non-existent container`() {
        val exception = assertThrows<IllegalStateException> {
            storage.getSpace(Cereal.MILLET)
        }
        assertEquals("Контейнер для MILLET отсутствует", exception.message)
    }

    @Test
    fun `getSpace should return full capacity for empty container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        val space = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(10f, space, 0.01f)
    }

    @Test
    fun `getSpace should return 0 for full container`() {
        storage.addCereal(Cereal.BULGUR, 10f)
        val space = storage.getSpace(Cereal.BULGUR)
        assertEquals(0f, space, 0.01f)
    }

    @Test
    fun `toString should show empty storage message`() {
        assertEquals("Хранилище пусто", storage.toString())
    }

    @Test
    fun `toString should show storage with one container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        val result = storage.toString()
        assertTrue(result.contains("Хранилище (контейнеры: 1):"))
        assertTrue(result.contains("Гречка: 5.0 кг (50%)"))
    }

    @Test
    fun `toString should show storage with multiple containers`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        storage.addCereal(Cereal.RICE, 10f)

        val result = storage.toString()
        assertTrue(result.contains("Хранилище (контейнеры: 2):"))
        assertTrue(result.contains("Гречка: 5.0 кг (50%)"))
        assertTrue(result.contains("Рис: 10.0 кг (100%)"))
    }

    @Test
    fun `toString should handle empty containers`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        val result = storage.toString()
        assertTrue(result.contains("Гречка: 0.0 кг (0%)"))
    }
}