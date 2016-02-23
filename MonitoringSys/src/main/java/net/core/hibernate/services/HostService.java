package net.core.hibernate.services;


import net.core.configurations.SSHConfiguration;

import java.util.List;

/**
 * Сервис для работы с базой данных мультимедийной библиотеки
 */
public interface HostService {

    /**
     * Сохраняет элемент
     * @param sshConfiguration
     */
    public void save(SSHConfiguration sshConfiguration);

    /**
     * Возвращает все элементы
     * @return Список элементов
     */
    public List<SSHConfiguration> getAll();

    public void remove(SSHConfiguration content);

    public void update(SSHConfiguration content);

    public SSHConfiguration get(int id);
}
