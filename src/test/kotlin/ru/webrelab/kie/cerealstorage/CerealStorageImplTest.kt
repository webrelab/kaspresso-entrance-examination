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
    fun `should throw if storageCapacity less then containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(40f, 10f)
        }
        assertDoesNotThrow {
            CerealStorageImpl(40f, 40f)
        }
    }

    @Test fun addCereal() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f,20f).addCereal(Cereal.PEAS, -1f)
        }
        assertDoesNotThrow{
            CerealStorageImpl(10f,20f).addCereal(Cereal.PEAS, 0f)
        }
        assertDoesNotThrow{
            CerealStorageImpl(10f,20f).addCereal(Cereal.PEAS, 1f)
        }

        assertThrows(IllegalStateException::class.java) {
            val cerealStorageImpl = CerealStorageImpl(10f,15f)
            cerealStorageImpl.addCereal(Cereal.PEAS, 10f)
            cerealStorageImpl.addCereal(Cereal.RICE, 10f)
        }

        assertEquals(
            CerealStorageImpl(10f, 20f).addCereal(Cereal.PEAS, 11f),
            1f,
            0.01f
        )

        assertEquals(
            CerealStorageImpl(10f, 20f).addCereal(Cereal.PEAS, 9f),
            0f,
            0.01f
        )

        val cerealStorageImpl = CerealStorageImpl(10f, 10f)
        cerealStorageImpl.addCereal(Cereal.PEAS, 10f)
        assertEquals(
            cerealStorageImpl.addCereal(Cereal.PEAS, 9f),
            9f,
            0.01f
        )
    }

    @Test fun getCereal() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f,20f).getCereal(Cereal.PEAS, -1f)
        }
        assertDoesNotThrow{
            CerealStorageImpl(10f,20f).getCereal(Cereal.PEAS, 0f)
        }
        assertDoesNotThrow{
            CerealStorageImpl(10f,20f).getCereal(Cereal.PEAS, 1f)
        }

        assertEquals(
            CerealStorageImpl(10f, 20f).getCereal(Cereal.PEAS, 5f),
            0f,
            0.01f
        )

        val cerealStorageImpl = CerealStorageImpl(10f, 20f)
        cerealStorageImpl.addCereal(Cereal.PEAS, 10f)
        assertEquals(
            cerealStorageImpl.getCereal(Cereal.PEAS, 9f),
            9f,
            0.01f
        )

        val cerealStorageImpl2 = CerealStorageImpl(10f, 20f)
        cerealStorageImpl2.addCereal(Cereal.PEAS, 5f)
        assertEquals(
            cerealStorageImpl2.getCereal(Cereal.PEAS, 9f),
            5f,
            0.01f
        )
    }

    @Test fun removeContainer() {
        val cerealStorageImpl = CerealStorageImpl(10f, 20f)
        cerealStorageImpl.addCereal(Cereal.PEAS, 1f)
        assertFalse(
            cerealStorageImpl.removeContainer(Cereal.PEAS)
        )

        val cerealStorageImpl2 = CerealStorageImpl(10f, 20f)
        cerealStorageImpl2.addCereal(Cereal.RICE, 1f)
        cerealStorageImpl2.getCereal(Cereal.RICE, 1f)
        assertTrue(
            cerealStorageImpl2.removeContainer(Cereal.RICE)
        )

        val cerealStorageImpl3 = CerealStorageImpl(10f, 20f)
        assertTrue(
            cerealStorageImpl3.removeContainer(Cereal.RICE)
        )
    }

    @Test fun getAmount() {
        assertEquals(
            CerealStorageImpl(10f, 20f).getAmount(Cereal.PEAS),
            0f,
            0.01f
        )

        val cerealStorageImpl = CerealStorageImpl(10f, 20f)
        cerealStorageImpl.addCereal(Cereal.PEAS, 1f)
        assertEquals(
            cerealStorageImpl.getAmount(Cereal.PEAS),
            1f,
            0.01f
        )
    }

    @Test fun getSpace() {
        val cerealStorageImpl = CerealStorageImpl(10f, 20f)
        cerealStorageImpl.addCereal(Cereal.PEAS, 1f)
        assertEquals(
            cerealStorageImpl.getSpace(Cereal.PEAS),
            9f,
            0.01f
        )

        val cerealStorageImpl2 = CerealStorageImpl(10f, 20f)
        assertEquals(
            cerealStorageImpl2.getSpace(Cereal.PEAS),
            10f,
            0.01f
        )

        val cerealStorageImpl3 = CerealStorageImpl(10f, 15f)
        cerealStorageImpl3.addCereal(Cereal.PEAS, 10f)
        assertEquals(
            cerealStorageImpl3.getSpace(Cereal.BULGUR),
            0f,
            0.01f
        )

    }

    @Test
    fun `toString should return correct representation`() {
        val cerealStorageImpl = CerealStorageImpl(10f, 20f)
        assertEquals(
            "CerealStorageImpl(containerCapacity=10.0, storageCapacity=20.0)",
            cerealStorageImpl.toString()
        )

        assertNotEquals(
            "CerealStorageImpl(containerCapacity=10.0)",
            cerealStorageImpl.toString()
        )
    }
}