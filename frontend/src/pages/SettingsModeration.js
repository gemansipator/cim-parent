import React, { useState, useEffect } from 'react';
import { getAllUsers, approveUser, blockUser, deleteUser, createUser, getSettings, updateSettings } from '../services/api';
import { ShieldCheckIcon, UserPlusIcon, UserGroupIcon } from '@heroicons/react/24/outline';
import { toast } from 'react-toastify';
import '../styles/Dashboard.css';

/**
 * Компонент модуля "Настройки и модерация".
 * Отображает таблицу пользователей с кнопками модерации и переключатели глобальных настроек.
 */
const SettingsModeration = () => {
    const [users, setUsers] = useState([]);
    const [settings, setSettings] = useState(null);
    const [newUser, setNewUser] = useState({ username: '', password: '', roleNames: [] });
    const [availableRoles] = useState(['ADMIN', 'SUPERUSER', 'USER']);
    const [error, setError] = useState('');

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [usersData, settingsData] = await Promise.all([
                getAllUsers(),
                getSettings()
            ]);
            setUsers(usersData);
            setSettings(settingsData);
        } catch (err) {
            setError(err.message);
        }
    };

    const handleApproveUser = async (id) => {
        try {
            const updatedUser = await approveUser(id);
            setUsers(users.map(u => u.id === id ? updatedUser : u));
            toast.success('Пользователь одобрен');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleBlockUser = async (id) => {
        try {
            const updatedUser = await blockUser(id);
            setUsers(users.map(u => u.id === id ? updatedUser : u));
            toast.success('Пользователь заблокирован');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleDeleteUser = async (id) => {
        if (window.confirm('Удалить пользователя?')) {
            try {
                await deleteUser(id);
                setUsers(users.filter(u => u.id !== id));
                toast.success('Пользователь удалён');
            } catch (err) {
                toast.error(err.message);
            }
        }
    };

    const handleCreateUser = async () => {
        if (!newUser.username || !newUser.password) {
            toast.error('Заполните имя пользователя и пароль');
            return;
        }
        try {
            const newUserData = {
                user: {
                    username: newUser.username,
                    password: newUser.password,
                },
                roleNames: newUser.roleNames
            };
            const createdUser = await createUser(newUserData);
            setUsers([...users, createdUser]);
            setNewUser({ username: '', password: '', roleNames: [] });
            toast.success('Пользователь создан');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleUpdateSettings = async (updatedSettings) => {
        try {
            const newSettings = await updateSettings(updatedSettings);
            setSettings(newSettings);
            toast.success('Настройки обновлены');
        } catch (err) {
            toast.error(err.message);
        }
    };

    return (
        <div className="module-content-card">
            <h2 className="module-content-title">Настройки и модерация</h2>
            <div className="module-content-details">
                <h3>Глобальные настройки</h3>
                <div className="settings-row">
                    <label>Разрешить регистрацию</label>
                    <input
                        type="checkbox"
                        checked={settings?.registrationEnabled || false}
                        onChange={(e) => handleUpdateSettings({ ...settings, registrationEnabled: e.target.checked })}
                    />
                </div>
                <div className="settings-row">
                    <label>Автоодобрение новых пользователей</label>
                    <input
                        type="checkbox"
                        checked={settings?.autoApprovalEnabled || false}
                        onChange={(e) => handleUpdateSettings({ ...settings, autoApprovalEnabled: e.target.checked })}
                    />
                </div>
                <h3>Список пользователей</h3>
                <table className="users-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Имя пользователя</th>
                        <th>Статус</th>
                        <th>Роли</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map(u => (
                        <tr key={u.id}>
                            <td>{u.id}</td>
                            <td>{u.username}</td>
                            <td>{u.status}</td>
                            <td>{u.roles.map(r => r.name).join(', ')}</td>
                            <td>
                                {u.status === 'PENDING' && (
                                    <button onClick={() => handleApproveUser(u.id)} className="approve-btn">Одобрить</button>
                                )}
                                <button onClick={() => handleBlockUser(u.id)} className="block-btn">Заблокировать</button>
                                <button onClick={() => handleDeleteUser(u.id)} className="delete-btn">Удалить</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <h3>Ручное создание пользователя</h3>
                <div className="create-user-form">
                    <input
                        type="text"
                        placeholder="Имя пользователя"
                        value={newUser.username}
                        onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
                    />
                    <input
                        type="password"
                        placeholder="Пароль"
                        value={newUser.password}
                        onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
                    />
                    <select multiple value={newUser.roleNames} onChange={(e) => {
                        const selected = Array.from(e.target.selectedOptions, option => option.value);
                        setNewUser({ ...newUser, roleNames: selected });
                    }}>
                        {availableRoles.map(r => <option key={r} value={r}>{r}</option>)}
                    </select>
                    <button onClick={handleCreateUser} className="create-user-btn">Создать</button>
                </div>
            </div>
        </div>
    );
};

export default SettingsModeration;