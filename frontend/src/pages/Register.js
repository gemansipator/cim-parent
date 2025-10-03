import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { registerUser } from '../services/api';
import { useAuthStore } from '../context/authStore';
import { SunIcon, MoonIcon } from '@heroicons/react/24/outline';
import { toast } from 'react-toastify'; // Добавлено для уведомлений
import '../styles/Register.css';

/**
 * Компонент страницы регистрации.
 * Добавлена проверка глобальных настроек: если registrationEnabled = false, показать уведомление.
 */
const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
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
        setSuccess('');
        try {
            const response = await registerUser({ username, password });
            setAuth({ username, roles: response.user.roles });
            setSuccess(response.data.message || 'Регистрация успешна! Перейдите на страницу входа.');
            setUsername('');
            setPassword('');
            toast.success(response.data.message || 'Регистрация успешна!'); // Добавлено уведомление
        } catch (err) {
            setError(err.message || 'Ошибка регистрации');
            toast.error(err.message); // Добавлено уведомление
        }
    };

    const handleLoginRedirect = () => {
        navigate('/login');
    };

    return (
        <div className="register-container">
            <motion.div
                className="theme-toggle"
                onClick={() => setDarkMode(!darkMode)}
                title={darkMode ? 'Светлая тема' : 'Темная тема'}
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.9 }}
            >
                {darkMode ? <SunIcon className="theme-icon" /> : <MoonIcon className="theme-icon" />}
            </motion.div>
            <div className="register-card">
                <h2 className="register-title">Регистрация</h2>
                {error && <p className="register-error">{error}</p>}
                {success && (
                    <div>
                        <p className="register-success">{success}</p>
                        <button className="login-redirect-button" onClick={handleLoginRedirect}>
                            Перейти к входу
                        </button>
                    </div>
                )}
                {!success && (
                    <form onSubmit={handleSubmit}>
                        <div className="register-field">
                            <label className="register-label">Имя пользователя</label>
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="register-input"
                                required
                            />
                        </div>
                        <div className="register-field">
                            <label className="register-label">Пароль</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="register-input"
                                required
                            />
                        </div>
                        <button type="submit" className="register-button">
                            Зарегистрироваться
                        </button>
                    </form>
                )}
                <button
                    className="login-link-button"
                    onClick={() => navigate('/login')}
                >
                    Войти
                </button>
            </div>
        </div>
    );
};

export default Register;