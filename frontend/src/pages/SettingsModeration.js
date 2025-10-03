import React, { useState, useEffect } from 'react';
import { getAllUsers, approveUser, blockUser, unblockUser, deleteUser, updateRole, createUser, getSettings, updateSettings } from '../services/api';
import { ChevronDownIcon, ChevronUpIcon } from '@heroicons/react/24/outline';
import { toast } from 'react-toastify';
import { useAuthStore } from '../context/authStore';
import '../styles/Dashboard.css';

/**
 * Компонент модуля "Настройки и модерация".
 * Отображает таблицу пользователей с кнопками модерации и переключатели глобальных настроек.
 * Добавлена свернутость секций (accordion), защита от самоудаления/блокировки, single select для роли, кнопка разблокировки.
 * Добавлена смена роли в таблице.
 */
const SettingsModeration = () => {
    const [users, setUsers] = useState([]);
    const [settings, setSettings] = useState(null);
    const [newUser, setNewUser] = useState({ username: '', password: '', roleName: '' }); // Single role
    const [availableRoles] = useState(['SUPERUSER', 'USER']); // Скрыта 'ADMIN'
    const [error, setError] = useState('');
    const [expandedSections, setExpandedSections] = useState({ settings: true, users: true, create: true }); // Для accordion
    const [roleChangeId, setRoleChangeId] = useState(null); // Для открытия select смены роли
    const [newRole, setNewRole] = useState(''); // Для новой роли

    const { user } = useAuthStore(); // Для текущего пользователя

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

    const toggleSection = (section) => {
        setExpandedSections(prev => ({ ...prev, [section]: !prev[section] }));
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
        if (id === user.id) {
            toast.error('Нельзя заблокировать самого себя');
            return;
        }
        try {
            const updatedUser = await blockUser(id);
            setUsers(users.map(u => u.id === id ? updatedUser : u));
            toast.success('Пользователь заблокирован');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleUnblockUser = async (id) => {
        if (id === user.id) {
            toast.error('Нельзя разблокировать самого себя');
            return;
        }
        try {
            const updatedUser = await unblockUser(id);
            setUsers(users.map(u => u.id === id ? updatedUser : u));
            toast.success('Пользователь разблокирован');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleDeleteUser = async (id) => {
        if (id === user.id) {
            toast.error('Нельзя удалить самого себя');
            return;
        }
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

    const handleRoleChange = async (id) => {
        if (!newRole) {
            toast.error('Выберите роль');
            return;
        }
        if (id === user.id) {
            toast.error('Нельзя изменить свою роль');
            return;
        }
        try {
            const updatedUser = await updateRole(id, newRole);
            setUsers(users.map(u => u.id === id ? updatedUser : u));
            setRoleChangeId(null);
            setNewRole('');
            toast.success('Роль изменена');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const handleCreateUser = async () => {
        if (!newUser.username || !newUser.password || !newUser.roleName) {
            toast.error('Заполните все поля');
            return;
        }
        try {
            const newUserData = {
                user: {
                    username: newUser.username,
                    password: newUser.password,
                },
                roleNames: [newUser.roleName]
            };
            const createdUser = await createUser(newUserData);
            setUsers([...users, createdUser]);
            setNewUser({ username: '', password: '', roleName: '' });
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
                {/* Секция глобальных настроек */}
                <div className="accordion-section">
                    <button className="accordion-header" onClick={() => toggleSection('settings')}>
                        <h3>Глобальные настройки</h3>
                        {expandedSections.settings ? <ChevronUpIcon className="accordion-icon" /> : <ChevronDownIcon className="accordion-icon" />}
                    </button>
                    {expandedSections.settings && (
                        <div className="accordion-content">
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
                        </div>
                    )}
                </div>

                {/* Секция списка пользователей */}
                <div className="accordion-section">
                    <button className="accordion-header" onClick={() => toggleSection('users')}>
                        <h3>Список пользователей</h3>
                        {expandedSections.users ? <ChevronUpIcon className="accordion-icon" /> : <ChevronDownIcon className="accordion-icon" />}
                    </button>
                    {expandedSections.users && (
                        <div className="accordion-content">
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
                                            {u.status === 'BLOCKED' && (
                                                <button onClick={() => handleUnblockUser(u.id)} className="unblock-btn">Разблокировать</button>
                                            )}
                                            {u.id !== user.id && (
                                                <>
                                                    {u.status !== 'BLOCKED' && (
                                                        <button onClick={() => handleBlockUser(u.id)} className="block-btn">Заблокировать</button>
                                                    )}
                                                    <button onClick={() => handleDeleteUser(u.id)} className="delete-btn">Удалить</button>
                                                    <button onClick={() => setRoleChangeId(u.id)} className="change-role-btn">Изменить роль</button>
                                                </>
                                            )}
                                            {roleChangeId === u.id && u.id !== user.id && (
                                                <div className="role-change">
                                                    <select value={newRole} onChange={(e) => setNewRole(e.target.value)}>
                                                        <option value="">Выберите роль</option>
                                                        {availableRoles.map(r => <option key={r} value={r}>{r}</option>)}
                                                    </select>
                                                    <button onClick={() => handleRoleChange(u.id)} className="save-role-btn">Сохранить</button>
                                                    <button onClick={() => setRoleChangeId(null)} className="cancel-role-btn">Отмена</button>
                                                </div>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>

                {/* Секция ручного создания */}
                <div className="accordion-section">
                    <button className="accordion-header" onClick={() => toggleSection('create')}>
                        <h3>Ручное создание пользователя</h3>
                        {expandedSections.create ? <ChevronUpIcon className="accordion-icon" /> : <ChevronDownIcon className="accordion-icon" />}
                    </button>
                    {expandedSections.create && (
                        <div className="accordion-content">
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
                                <select value={newUser.roleName} onChange={(e) => setNewUser({ ...newUser, roleName: e.target.value })}>
                                    <option value="">Выберите роль</option>
                                    {availableRoles.map(r => <option key={r} value={r}>{r}</option>)}
                                </select>
                                <button onClick={handleCreateUser} className="create-user-btn">Создать</button>
                            </div>
                        </div>
                    )}
                </div>

                {error && <p className="error">{error}</p>}
            </div>
        </div>
    );
};

export default SettingsModeration;