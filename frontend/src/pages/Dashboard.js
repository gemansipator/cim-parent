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
    getSettingsModeration
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
    MoonIcon
} from '@heroicons/react/24/outline';
import '../styles/Dashboard.css';

/**
 * Компонент Dashboard — главный дашборд приложения.
 * Здесь происходит:
 * - загрузка и отображение данных из API,
 * - выбор активного модуля для просмотра,
 * - переключение между светлой и темной темами,
 * - показ имени пользователя и кнопки выхода,
 * - встроенный iframe с NocoDB для админов.
 */
const Dashboard = () => {
    const [activeModule, setActiveModule] = useState('bimModels');
    const [modules, setModules] = useState([]);
    const [bimModels, setBimModels] = useState([]);
    const [requirements, setRequirements] = useState([]);
    const [statuses, setStatuses] = useState([]);
    const [bbbSessions, setBbbSessions] = useState([]);
    const [settingsModeration, setSettingsModeration] = useState(null);
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

        fetchUser();
        fetchData();
        fetchSettingsModeration();
    }, [navigate, token, username, user, logout]);

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
