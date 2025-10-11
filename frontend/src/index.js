import React from 'react';
import ReactDOM from 'react-dom/client';
import { ToastContainer } from 'react-toastify'; // Добавлено для уведомлений
import 'react-toastify/dist/ReactToastify.css'; // Импорт стилей toast
import './index.css';
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App />
        <ToastContainer /> // Добавлено для уведомлений
    </React.StrictMode>
);