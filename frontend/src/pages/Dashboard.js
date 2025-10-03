import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useNavigate, Navigate } from 'react-router-dom';
import { useAuthStore } from '../context/authStore';
import {
    getUserByUsername,
    getModules,
    getBimModels,
    getRequirements,
    getStatuses,
    getBbbSessions,
    getSettingsModeration,
    getAllUsers,
    approveUser,
    blockUser,
    deleteUser,
    createUser,
    getSettings,
    updateSettings
} from '../services/api';
import {
    CogIcon,
    CubeIcon,
    ClipboardDocumentListIcon,
    CheckCircleIcon,
    VideoCameraIcon,
    ExclamationTriangleIcon,
    ShieldCheckIcon,
    SunIcon,
    MoonIcon,
    UserPlusIcon,
    UserGroupIcon
} from '@heroicons/react/24/outline';
import { toast } from 'react-toastify';
import '../styles/Dashboard.css';

/**
 * Компонент Dashboard — главный дашборд приложения.
 * Добавлен модуль "Настройки и модерация" с таблицей пользователей, переключателями и кнопками модерации.
 */
const Dashboard = () => {
    const [activeModule, setActiveModule] = useState('bimModels');
    const [modules, setModules] = useState([]);
    const [bimModels, setBimModels] = useState([]);
    const [requirements, setRequirements] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [bbbSessions, setBbbSessions] = useState([]);
    const [settingsModeration, setSettingsModeration] = useState(null);
    const [users, setUsers] = useState([]); // Добавлено для списка пользователей
    const [settings, setSettings] = useState(null); // Добавлено для глобальных настроек
    const [newUser, setNewUser] = useState({ username: '', password: '', roleNames: [] }); // Добавлено для ручного создания
    const [availableRoles] = useState(['ADMIN', 'SUPERUSER', 'USER']); // Добавлено для ролей
    const [error, setError] = useState('');

    // Тема: при инициализации читаем значение из localStorage
    const [darkMode, setDarkMode] = useState(() => {
        return localStorage.getItem('darkMode') === 'true';
    });

    const { user, logout } = useAuthStore();
    const navigate = useNavigate();

    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');

    useEffect(() => {
        if (!token || !username) {
            navigate('/login');
            return;
        }

        const fetchUser = async () => {
            try {
                const userData = await getUserByUsername(username);
                useAuthStore.getState().setAuth(userData);
            } catch (err) {
                setError(err.message);
                logout();
                navigate('/login');
            }
        };

        const fetchData = async () => {
            try {
                const [modulesData, bimModelsData, requirementsData, statusesData, bbbSessionsData] = await Promise.all([
                    getModules(),
                    getBimModels(),
                    getRequirements(),
                    getStatuses(),
                    getBbbSessions(),
                ]);
                setModules(modulesData);
                setBimModels(bimModelsData);
                setRequirements(requirementsData);
                setStatuses(statusesData);
                setBbbSessions(bbbSessionsData);
            } catch (err) {
                setError(err.message);
            }
        };

        const fetchSettingsModeration = async () => {
            if (user?.roles?.some(r => ['ADMIN', 'SUPERUSER'].includes(r.name))) {
                try {
                    const data = await getSettingsModeration();
                    setSettingsModeration(data);
                } catch (err) {
                    setSettingsModeration({ message: 'Данные недоступны' });
                }
            }
        };

        // Добавлено: Загрузка пользователей и настроек для админа
        const fetchAdminData = async () => {
            if (user?.roles?.some(r => ['ADMIN', 'SUPERUSER'].includes(r.name))) {
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
            }
        };

        fetchUser();
        fetchData();
        fetchSettingsModeration();
        fetchAdminData();
    }, [navigate, token, username, user, logout]);

    // Добавлено: Обработчики для модерации пользователей
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

    // Добавлено: Обработчик для ручного создания пользователя
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

    // Добавлено: Обработчик для обновления настроек
    const handleUpdateSettings = async (updatedSettings) => {
        try {
            const newSettings = await updateSettings(updatedSettings);
            setSettings(newSettings);
            toast.success('Настройки обновлены');
        } catch (err) {
            toast.error(err.message);
        }
    };

    const moduleList = [
        { id: 'modules', name: 'Модули', icon: CogIcon },
        { id: 'bimModels', name: 'BIM Модели', icon: CubeIcon },
        { id: 'requirements', name: 'Требования', icon: ClipboardDocumentListIcon },
        { id: 'statuses', name: 'Статусы', icon: CheckCircleIcon },
        { id: 'bbbSessions', name: 'BBB Сессии', icon: VideoCameraIcon },
        { id: 'settingsModeration', name: 'Настройки и модерация', icon: ShieldCheckIcon, roles: ['ADMIN', 'SUPERUSER'] },
        { id: 'nocodb', name: 'NocoDB', icon: ShieldCheckIcon, roles: ['ADMIN', 'SUPERUSER', 'USER'] }
    ].filter(module => !module.roles || module.roles.some(role => user?.roles?.map(r => r.name).includes(role)));

    const noData = () => (
        <div className="module-empty">
            <ExclamationTriangleIcon className="module-empty-icon" />
            <p>Данные недоступны</p>
        </div>
    );

    const moduleContent = {
        modules: modules.length ? modules.map(m => <p key={m.id}>{m.name}: {m.description}</p>) : noData(),
        bimModels: bimModels.length ? bimModels.map(m => <p key={m.id}>{m.name} ({m.filePath})</p>) : noData(),
        requirements: requirements.length ? requirements.map(r => <p key={r.id}>{r.name}: {r.description}</p>) : noData(),
        statuses: statuses.length ? statuses.map(s => <p key={s.id}>{s.name}: {s.description}</p>) : noData(),
        bbbSessions: bbbSessions.length ? bbbSessions.map(s => <p key={s.id}>{s.name} ({s.meetingId})</p>) : noData(),
        settingsModeration: settingsModeration ? (
            <div className="module-content-details">
                <p>{settingsModeration.message}</p>
            </div>
        ) : (
            <div className="module-empty">
                <ExclamationTriangleIcon className="module-empty-icon" />
                <p>Загрузка...</p>
            </div>
        ),
        nocodb: (
            <iframe
                src="/nocodb/dashboard/#/"
                title="NocoDB Interface"
                style={{ width: '100%', height: '80vh', border: 'none', borderRadius: '8px' }}
            />
        ),
        // Добавлено: Модуль модерации пользователей
        userModeration: user?.roles?.some(r => ['ADMIN', 'SUPERUSER'].includes(r.name)) ? (
            <div className="module-content-card">
                <h2 className="module-content-title">Модерация пользователей</h2>
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
        ) : (
            <div className="module-empty">
                <ExclamationTriangleIcon className="module-empty-icon" />
                <p>Доступ только для администратора</p>
            </div>
        )
    };

    useEffect(() => {
        if (darkMode) {
            document.body.classList.add('dark-mode');
        } else {
            document.body.classList.remove('dark-mode');
        }
        localStorage.setItem('darkMode', darkMode);
    }, [darkMode]);

    if (!token) {
        return <Navigate to="/login" />;
    }

    return (
        <div className="dashboard-container">
            <motion.header className="dashboard-header" initial={{ opacity: 0, y: -20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }}>
                <div className="header-content">
                    <div className="header-theme-toggle" onClick={() => setDarkMode(!darkMode)} title={darkMode ? 'Светлая тема' : 'Темная тема'}>
                        {darkMode ? <SunIcon className="theme-icon" /> : <MoonIcon className="theme-icon" />}
                    </div>
                    <div className="header-title-wrapper">
                        <h1 className="dashboard-title">
                            Система управления создания цифровых информационных моделей (ЦИМ)
                        </h1>
                    </div>
                    <div className="header-user-controls">
                        <span className="user-greeting">{user?.username}</span>
                        <motion.button onClick={logout} className="logout-button" whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                            Выйти
                        </motion.button>
                    </div>
                </div>
            </motion.header>

            {error && <p className="error">{error}</p>}

            <div className="dashboard-grid">
                <div className="module-selector">
                    {moduleList.map(module => (
                        <motion.div
                            key={module.id}
                            className={`module-selector-card ${activeModule === module.id ? 'module-selector-card-active' : ''}`}
                            onClick={() => setActiveModule(module.id)}
                            whileHover={{ scale: 1.03 }}
                            whileTap={{ scale: 0.97 }}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.3 }}
                        >
                            <module.icon className="module-icon" />
                            <span className="module-selector-title">{module.name}</span>
                        </motion.div>
                    ))}
                </div>
                <motion.div className="module-content-container" initial={{ opacity: 0 }} animate={{ opacity: 1 }} transition={{ duration: 0.5 }}>
                    {activeModule && (
                        <AnimatePresence mode="wait">
                            <motion.div
                                key={activeModule}
                                className="module-content-card"
                                initial={{ opacity: 0, x: 50 }}
                                animate={{ opacity: 1, x: 0 }}
                                exit={{ opacity: 0, x: -50 }}
                                transition={{ duration: 0.4, ease: 'easeInOut' }}
                            >
                                {activeModule !== 'nocodb' && (
                                    <h2 className="module-content-title">
                                        {moduleList.find(m => m.id === activeModule)?.name}
                                    </h2>
                                )}
                                <div className="module-content-details">
                                    {moduleContent[activeModule]}
                                </div>
                            </motion.div>
                        </AnimatePresence>
                    )}
                </motion.div>
            </div>
        </div>
    );
};

export default Dashboard;