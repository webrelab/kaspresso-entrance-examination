package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is less than capacity of 1 container`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(4f, 1f)
        }
    }

    @Test
    fun `addCereal should throw if storage cannot contain more containers`() {
        storage.addCereal(Cereal.BULGUR, 10f)
        storage.addCereal(Cereal.PEAS, 10f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.RICE, 1f)
        }
    }

    @Test
    fun `addCereal should return excessive amount if container is full`() {
        assertAll(
            {
                storage.addCereal(Cereal.PEAS, 9f)
                val reminder = storage.addCereal(Cereal.PEAS, 2f)
                assertEquals(1f, reminder, "Reminder should be equal 1")
            },
            {
                val reminder = storage.addCereal(Cereal.PEAS, 1f)
                assertEquals(1f, reminder, "Reminder should be equal 1")
            },
            {
                val reminder = storage.addCereal(Cereal.MILLET, 20f)
                assertEquals(10f, reminder, "Reminder should be equal 10")
            }
        )
    }

    @Test
    fun `getCereal should throw if no such container`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getCereal(Cereal.PEAS, 10f)
        }
    }

    @Test
    fun `getCereal should throw if amount is negative`() {
        storage.addCereal(Cereal.PEAS, 10f)
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.PEAS, -1f)
        }
    }

    @Test
    fun `getCereal should return correct amount if there is enough cereal`() {
        storage.addCereal(Cereal.PEAS, 10f)
        val amount = storage.getCereal(Cereal.PEAS, 3f)
        assertAll(
            {
                assertEquals(3f, amount, "Obtained amount should be 3")
                assertEquals(7f, storage.getAmount(Cereal.PEAS), "Remaining amount in storage should be 7")
            }
        )
    }

    @Test
    fun `getCereal should return correct amount if there is not enough cereal`() {
        storage.addCereal(Cereal.PEAS, 10f)
        val amount = storage.getCereal(Cereal.PEAS, 13f)
        assertAll(
            {
                assertEquals(10f, amount, "Obtained amount should be 10")
                assertEquals(0f, storage.getAmount(Cereal.PEAS), "Remaining amount in storage should be 0")
            }
        )
    }

    @Test
    fun `getAmount should throw if there is no such container`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getAmount(Cereal.RICE)
        }
    }

    @Test
    fun `getAmount should return correct amount`() {
        storage.addCereal(Cereal.RICE, 0f)
        storage.addCereal(Cereal.PEAS, 10f)
        assertAll(
            {
                assertEquals(0f, storage.getAmount(Cereal.RICE), "Should return 0")
            },
            {
                assertEquals(10f, storage.getAmount(Cereal.PEAS), "Should return 10")
            }
        )
    }

    @Test
    fun `getSpace should return correct space`() {
        storage.addCereal(Cereal.RICE, 6f)
        assertEquals(4f, storage.getSpace(Cereal.RICE), "Should return 4")
    }

    @Test
    fun `getSpace should return correct space if container is full or empty`() {
        storage.addCereal(Cereal.RICE, 10f)
        storage.addCereal(Cereal.PEAS, 0f)
        assertAll(
            {
                assertEquals(0f, storage.getSpace(Cereal.RICE), "Should return 0")
            },
            {
                assertEquals(10f, storage.getSpace(Cereal.PEAS), "Should return 10")
            }
        )
    }

    @Test
    fun `getSpace should throw exception if there is no such container`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.RICE)
        }
    }

    @Test
    fun `removeContainer should return true and remove container if it is empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        assertTrue(storage.removeContainer(Cereal.BUCKWHEAT), "Should return true")
        assertThrows(IllegalStateException::class.java) {
            storage.getAmount(Cereal.BUCKWHEAT)
        }
    }

    @Test
    fun `removeContainer should return false if container is not empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 4f)
        assertFalse(storage.removeContainer(Cereal.BUCKWHEAT), "Should return false")
        assertEquals(4f, storage.getAmount(Cereal.BUCKWHEAT), "Should return 4, container exists")
    }

    @Test
    fun `removeContainer should throw exception if there is no such container`() {
        assertThrows(IllegalStateException::class.java) {
            storage.removeContainer(Cereal.RICE)
        }
    }
}