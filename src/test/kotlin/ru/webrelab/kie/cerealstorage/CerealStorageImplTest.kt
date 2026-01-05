package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    private val rice = Cereal.RICE
    private val buckwheat = Cereal.BUCKWHEAT
    private val millet = Cereal.MILLET
    private val peas = Cereal.PEAS
    private val bulgur = Cereal.BULGUR

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 5f)
        }
    }

    @Test
    fun `addCereal should add cereal if space available`() {
        val leftover = storage.addCereal(rice, 5f)
        assertEquals(0f, leftover)
        assertEquals(5f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `addCereal should return leftover if amount exceeds container capacity`() {
        val leftover = storage.addCereal(rice, 15f)
        assertEquals(5f, leftover, 0.01f)
        assertEquals(10f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `addCereal should throw if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(rice, -1f)
        }
    }

    @Test
    fun `addCereal should throw if no space for new container`() {
        storage.addCereal(rice, 10f)
        storage.addCereal(buckwheat, 10f)

        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(millet, 5f)
        }
    }

    @Test
    fun `getCereal should return available amount if container has less than requested`() {
        storage.addCereal(rice, 5f)
        val taken = storage.getCereal(rice, 10f)
        assertEquals(5f, taken, 0.01f)
        assertEquals(0f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `getCereal should return requested amount if enough in container`() {
        storage.addCereal(rice, 8f)
        val taken = storage.getCereal(rice, 5f)
        assertEquals(5f, taken, 0.01f)
        assertEquals(3f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `getCereal should return 0 if container does not exist`() {
        val taken = storage.getCereal(bulgur, 5f)
        assertEquals(0f, taken, 0.01f)
    }

    @Test
    fun `getCereal should throw if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(rice, -2f)
        }
    }

    @Test
    fun `removeContainer should remove empty container`() {
        storage.addCereal(rice, 5f)
        storage.getCereal(rice, 5f)
        val removed = storage.removeContainer(rice)
        assertTrue(removed)
        assertEquals(0f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `removeContainer should not remove non-empty container`() {
        storage.addCereal(rice, 5f)
        val removed = storage.removeContainer(rice)
        assertFalse(removed)
        assertEquals(5f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `removeContainer should return false if container does not exist`() {
        val removed = storage.removeContainer(bulgur)
        assertFalse(removed)
    }

    @Test
    fun `getAmount should return correct amount`() {
        storage.addCereal(rice, 7f)
        assertEquals(7f, storage.getAmount(rice), 0.01f)
    }

    @Test
    fun `getAmount should return 0 if container does not exist`() {
        assertEquals(0f, storage.getAmount(peas), 0.01f)
    }

    @Test
    fun `getSpace should return remaining space in container`() {
        storage.addCereal(rice, 6f)
        assertEquals(4f, storage.getSpace(rice), 0.01f)
    }

    @Test
    fun `getSpace should throw if container does not exist`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(peas)
        }
    }

    @Test
    fun `toString should contain cereal names and amounts`() {
        storage.addCereal(rice, 3f)
        storage.addCereal(buckwheat, 4f)
        val output = storage.toString()
        assertTrue(output.contains("RICE"))
        assertTrue(output.contains("BUCKWHEAT"))
        assertTrue(output.contains("3.0") || output.contains("3.00"))
        assertTrue(output.contains("4.0") || output.contains("4.00"))
    }
}