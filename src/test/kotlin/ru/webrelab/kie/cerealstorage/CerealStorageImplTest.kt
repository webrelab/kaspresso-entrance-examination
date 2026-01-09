package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    val storage = CerealStorageImpl(1000F, 2000F)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if amount is negative for addCereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, -10F)
        }
    }

    @Test
    fun `should throw if amount is bigger than containerCapacity for addCereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, 2000F)
        }
    }

    @Test
    fun `should create new container if not exist for addCereal`() {
        storage.addCereal(Cereal.RICE, 500F)
        assertEquals(500F, storage.getAmount(Cereal.RICE), 0.01F)
    }

    @Test
    fun `should return amount for addCereal`() {
        val result = storage.addCereal(Cereal.RICE, 600F)
        assertEquals(600F, result, 0.01F)
    }

    @Test
    fun `should return added part of amount if no enough space for amount in container for addCereal`() {
        storage.addCereal(Cereal.RICE, 500F)
        val result = storage.addCereal(Cereal.RICE, 600F)
        assertEquals(100F, result, 0.01F)
    }

    @Test
    fun `should add cereal if container exists and amount is correct`() {
        storage.addCereal(Cereal.BUCKWHEAT, 500F)
        storage.addCereal(Cereal.BUCKWHEAT, 200F)
        assertEquals(700F, storage.getAmount(Cereal.BUCKWHEAT), 0.01F)
    }

    @Test
    fun `should return amounts for several cereals for addCereal`() {
        storage.addCereal(Cereal.MILLET, 500F)
        storage.addCereal(Cereal.PEAS, 500F)

        val resul1 = storage.addCereal(Cereal.MILLET, 200F)
        val resul2 = storage.addCereal(Cereal.PEAS, 300F)

        assertEquals(200F, resul1, 0.01F)
        assertEquals(300F, resul2, 0.01F)
    }

    @Test
    fun `should throw if amount is negative for getCereal`() {
        storage.addCereal(Cereal.RICE, 500F)
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, -10F)
        }
    }

    @Test
    fun `should throw if not possible to add one more container for addCereal`() {
        storage.addCereal(Cereal.RICE, 1000F)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.RICE, 100F)
        }
    }

    @Test
    fun `should throw if amount is bigger than containerCapacity for getCereal`() {
        storage.addCereal(Cereal.RICE, 500F)
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, 2000F)
        }
    }

    @Test
    fun `should throw if container not exists for getCereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, 2000F)
        }
    }

    @Test
    fun `should return amount for getCereal`() {
        storage.addCereal(Cereal.RICE, 500F)
        val result = storage.getCereal(Cereal.RICE, 200F)
        assertEquals(200F, result, 0.01F)
    }

    @Test
    fun `should return current amount if try to get more for getCereal`() {
        storage.addCereal(Cereal.RICE, 500F)
        val result = storage.getCereal(Cereal.RICE, 700F)
        assertEquals(500F, result, 0.01F)
    }

    @Test
    fun `should return true if container was removed`() {
        storage.addCereal(Cereal.RICE, 0F)
        val result = storage.removeContainer(Cereal.RICE)
        assertTrue(result)
    }

    @Test
    fun `should return false if container was not removed because it's not empty`() {
        storage.addCereal(Cereal.RICE, 500F)
        val result = storage.removeContainer(Cereal.RICE)
        assertFalse(result)
    }

    @Test
    fun `should return false if container was not removed because it was not exist`() {
        val result = storage.removeContainer(Cereal.RICE)
        assertFalse(result)
    }

    @Test
    fun `should throw if container not exists for getSpace`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.RICE)
        }
    }

    @Test
    fun `should return space if container exists for getSpace`() {
        storage.addCereal(Cereal.RICE, 100F)
        val result = storage.getSpace(Cereal.RICE)
        assertEquals(900F, result, 0.01F)
    }
}