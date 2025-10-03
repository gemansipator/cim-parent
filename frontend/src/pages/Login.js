import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/api';
import { useAuthStore } from '../context/authStore';
import { SunIcon, MoonIcon } from '@heroicons/react/24/outline';
import '../styles/Login.css';

/**
 * Компонент страницы входа.
 */
const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [darkMode, setDarkMode] = useState(() => {
        return localStorage.getItem('darkMode') === 'true';
    });
    const { setAuth } = useAuthStore();
    const navigate = useNavigate();

    useEffect(() => {
        if (darkMode) {
            document.body.classList.add('dark-mode');
        } else {
            document.body.classList.remove('dark-mode');
        }
        localStorage.setItem('darkMode', darkMode);
    }, [darkMode]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const response = await login({ username, password });
            setAuth({ username, roles: response.user.roles });
            navigate('/dashboard');
        } catch (err) {
            setError(err.message || 'Ошибка входа');
        }
    };

    return (
        <div className="login-container">
            <motion.div
                className="theme-toggle"
                onClick={() => setDarkMode(!darkMode)}
                title={darkMode ? 'Светлая тема' : 'Темная тема'}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
            >
                {darkMode ? <SunIcon className="theme-icon" /> : <MoonIcon className="theme-icon" />}
            </motion.div>
            <div className="login-card">
                <h2 className="login-title">Вход</h2>
                {error && <p className="login-error">{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div className="login-field">
                        <label className="login-label">Имя пользователя</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="login-input"
                            required
                        />
                    </div>
                    <div className="login-field">
                        <label className="login-label">Пароль</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="login-input"
                            required
                        />
                    </div>
                    <button type="submit" className="login-button">
                        Войти
                    </button>
                </form>
                <button
                    className="register-link-button"
                    onClick={() => navigate('/register')}
                >
                    Зарегистрироваться
                </button>
            </div>
        </div>
    );
};

export default Login;