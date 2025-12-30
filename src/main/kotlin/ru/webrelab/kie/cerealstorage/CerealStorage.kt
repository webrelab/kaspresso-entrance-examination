package ru.webrelab.kie.cerealstorage

interface CerealStorage {

    /**
     * Объём одного контейнера
     * @throws IllegalArgumentException если значение отрицательное при инициализации
     */
    val containerCapacity: Float

    /**
     * Совокупный объём хранилища
     * @throws IllegalArgumentException если значение меньше, чем [containerCapacity] при инициализации
     */
    val storageCapacity: Float

    /**
     * Добавляет крупу к существующему контейнеру соответствующего типа, либо добавляет новый контейнер
     * если его ещё не было в хранилище и добавляет в него предоставленную крупу.
     * Для одного вида крупы только один контейнер.
     * @param cereal крупа для добавления в контейнер
     * @param amount количество добавляемой крупы
     * @return количество оставшейся крупы если контейнер заполнился
     * @throws IllegalArgumentException если передано отрицательное значение
     * @throws IllegalStateException если хранилище не позволяет разместить ещё один контейнер для новой крупы
     */
    fun addCereal(cereal: Cereal, amount: Float): Float

    /**
     * Вынимает крупу из контейнера (после этого в контейнере крупы должно стать меньше)
     * @param cereal крупа, которую нужно взять из контейнера
     * @param amount количество крупы
     * @return количество полученной крупы или остаток содержимого контейнера, если крупы в нём было меньше
     * @throws IllegalArgumentException если передано отрицательное значение
     */
    fun getCereal(cereal: Cereal, amount: Float): Float

    /**
     * @param cereal уничтожает пустой контейнер
     * @return true если контейнер уничтожен и false если контейнер не пуст или отсутствует
     */
    fun removeContainer(cereal: Cereal): Boolean

    /**
     * @param cereal крупа, количество которой нужно узнать
     * @return количество крупы, которое хранится в контейнере или 0 если контейнера нет
     */
    fun getAmount(cereal: Cereal): Float

    /**
     * @param cereal крупа, для которой нужно проверить доступное место в контейнере
     * @return количество крупы, которое может вместить контейнер с учётом его текущей заполненности
     * @throws IllegalStateException если проверяемого контейнера нет
     */
    fun getSpace(cereal: Cereal): Float

    /**
     * @return текстовое представление
     */
    override fun toString(): String
}