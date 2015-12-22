package core.hibernate.services;


import core.configurations.SSHConfiguration;

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

}
