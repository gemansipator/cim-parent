import axios from 'axios';

const api = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

api.interceptors.request.use(
    (config) => {
        const publicEndpoints = ['/users/register', '/users/login'];
        if (!publicEndpoints.includes(config.url)) {
            const token = localStorage.getItem('token');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export const registerUser = async (userData) => {
    try {
        const response = await api.post('/users/register', {
            user: {
                username: userData.username,
                password: userData.password,
            },
            roleNames: ['USER'],
        });
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('username', userData.username);
        } else {
            throw new Error('Токен не получен от сервера');
        }
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка регистрации');
    }
};

export const login = async (userData) => {
    try {
        const response = await api.post('/users/login', {
            username: userData.username,
            password: userData.password,
        });
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('username', userData.username);
        } else {
            throw new Error('Токен не получен от сервера');
        }
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка входа');
    }
};

export const getUserByUsername = async (username) => {
    try {
        const response = await api.get('/users/by-username', {
            params: { username },
        });
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка при получении пользователя');
    }
};

export const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
};

export const getModules = async () => {
    try {
        const response = await api.get('/modules');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения модулей');
    }
};

export const getSettingsModeration = async () => {
    try {
        const response = await api.get('/settings-moderation');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения настроек и модерации');
    }
};

export const getBimModels = async () => {
    try {
        const response = await api.get('/bim-models');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения BIM-моделей');
    }
};

export const getRequirements = async () => {
    try {
        const response = await api.get('/requirements');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения требований');
    }
};

export const getStatuses = async () => {
    try {
        const response = await api.get('/statuses');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения статусов');
    }
};

export const getBbbSessions = async () => {
    try {
        const response = await api.get('/bbb-sessions');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения BBB-сессий');
    }
};

// Добавлено: Методы для модерации пользователей
export const getAllUsers = async () => {
    try {
        const response = await api.get('/users');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения списка пользователей');
    }
};

export const approveUser = async (id) => {
    try {
        const response = await api.put(`/users/${id}/approve`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка одобрения пользователя');
    }
};

export const blockUser = async (id) => {
    try {
        const response = await api.put(`/users/${id}/block`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка блокировки пользователя');
    }
};

export const deleteUser = async (id) => {
    try {
        await api.delete(`/users/${id}`);
        return true;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка удаления пользователя');
    }
};

export const createUser = async (userData) => {
    try {
        const response = await api.post('/users/create', userData);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка создания пользователя');
    }
};

export const getSettings = async () => {
    try {
        const response = await api.get('/settings');
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка получения настроек');
    }
};

export const updateSettings = async (settings) => {
    try {
        const response = await api.put('/settings', settings);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.error || 'Ошибка обновления настроек');
    }
};