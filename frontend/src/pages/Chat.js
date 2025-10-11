/**
 * Компонент страницы чата.
 * Реализует общий чат с полем ввода, отправкой по Enter/кнопке, отображением ника, времени, смайликов,
 * активных ссылок, ответов на сообщения, анимацией сообщений и списком пользователей с статусами.
 * Добавлена поддержка выбора смайликов через emoji-picker-react.
 */
import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import EmojiPicker from 'emoji-picker-react';
import { useAuthStore } from '../context/authStore';
import { toast } from 'react-toastify';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
// Заменили ReplyIcon -> ArrowUturnLeftIcon для @heroicons/react v2
import { ChatBubbleLeftIcon, TrashIcon, ArrowUturnLeftIcon, ShieldCheckIcon, FaceSmileIcon } from '@heroicons/react/24/outline';
import { getAllUsers, approveUser, blockUser, unblockUser, deleteUser } from '../services/api';
import '../styles/Chat.css';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [users, setUsers] = useState([]);
    const [userStatuses, setUserStatuses] = useState({});
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [selectedMessage, setSelectedMessage] = useState(null);
    const [replyTo, setReplyTo] = useState(null);
    const [showEmojiPicker, setShowEmojiPicker] = useState(false);
    const { user } = useAuthStore();
    const chatRef = useRef(null);
    const stompClient = useRef(null);

    useEffect(() => {
        const initializeChat = async () => {
            // Загрузка начальных сообщений
            try {
                const response = await fetch(`/api/chat/messages?page=${page}&size=100`);
                const data = await response.json();
                setMessages(data.content.reverse());
                setHasMore(!data.last);
            } catch (err) {
                toast.error('Ошибка загрузки сообщений');
            }

            // Загрузка пользователей и статусов
            try {
                const usersData = await getAllUsers();
                setUsers(usersData);
                const statuses = await fetch('/api/user-statuses').then(res => res.json());
                setUserStatuses(statuses.reduce((acc, status) => ({
                    ...acc,
                    [status.userId]: status.online
                }), {}));
            } catch (err) {
                toast.error('Ошибка загрузки пользователей');
            }

            // Настройка WebSocket
            const socket = new SockJS('/chat');
            stompClient.current = new Client({
                webSocketFactory: () => socket,
                reconnectDelay: 5000,
                onConnect: () => {
                    stompClient.current.subscribe('/topic/public', (message) => {
                        const msg = JSON.parse(message.body);
                        setMessages(prev => [...prev, msg]);
                        scrollToBottom();
                    });
                    // Отправка статуса онлайн
                    fetch('/api/user-statuses', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}` },
                        body: JSON.stringify({ userId: user.id, online: true })
                    });
                }
            });
            stompClient.current.activate();

            return () => {
                if (stompClient.current) {
                    // Отправка статуса оффлайн
                    fetch('/api/user-statuses', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}` },
                        body: JSON.stringify({ userId: user.id, online: false })
                    });
                    stompClient.current.deactivate();
                }
            };
        };

        initializeChat();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page, user]);

    const scrollToBottom = () => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    };

    const handleSendMessage = () => {
        if (!newMessage.trim()) return;
        const message = {
            content: newMessage,
            senderUsername: user.username,
            replyToId: replyTo?.id || null
        };
        try {
            stompClient.current.publish({
                destination: '/app/chat.sendMessage',
                body: JSON.stringify(message)
            });
        } catch (err) {
            // Если stompClient ещё не активен, можно fallback'ить на fetch (если у тебя есть endpoint)
            console.error('Ошибка отправки через STOMP', err);
            toast.error('Ошибка отправки сообщения');
            return;
        }
        setNewMessage('');
        setReplyTo(null);
        setShowEmojiPicker(false);
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSendMessage();
        }
    };

    const handleDeleteMessage = async (messageId) => {
        try {
            await stompClient.current.publish({
                destination: '/app/chat.deleteMessage',
                body: JSON.stringify({ id: messageId, senderUsername: user.username })
            });
            toast.success('Сообщение удалено');
        } catch (err) {
            toast.error('Ошибка удаления сообщения');
        }
    };

    const handleLoadMore = async () => {
        setPage(prev => prev + 1);
        try {
            const response = await fetch(`/api/chat/messages?page=${page + 1}&size=100`);
            const data = await response.json();
            setMessages(prev => [...data.content.reverse(), ...prev]);
            setHasMore(!data.last);
        } catch (err) {
            toast.error('Ошибка загрузки сообщений');
        }
    };

    const handleUserAction = async (action, userId) => {
        try {
            if (action === 'approve') {
                await approveUser(userId);
                toast.success('Пользователь одобрен');
            } else if (action === 'block') {
                await blockUser(userId);
                toast.success('Пользователь заблокирован');
            } else if (action === 'unblock') {
                await unblockUser(userId);
                toast.success('Пользователь разблокирован');
            } else if (action === 'delete') {
                if (window.confirm('Удалить пользователя?')) {
                    await deleteUser(userId);
                    toast.success('Пользователь удалён');
                    setUsers(users.filter(u => u.id !== userId));
                }
            }
            const usersData = await getAllUsers();
            setUsers(usersData);
        } catch (err) {
            toast.error(err.message);
        }
    };

    const renderMessageContent = (content) => {
        const urlRegex = /(https?:\/\/[^\s]+)/g;
        return content.replace(urlRegex, '<a href="$1" target="_blank" rel="noopener noreferrer">$1</a>');
    };

    const onEmojiClick = (emojiObject) => {
        // emoji-picker-react v4: onEmojiClick(emojiObject) — emojiObject.emoji содержит символ
        setNewMessage(prev => prev + emojiObject.emoji);
        setShowEmojiPicker(false);
    };

    return (
        <div className="chat-container">
            <div className="chat-main">
                <h2 className="chat-title">Общий чат</h2>
                {hasMore && (
                    <button className="load-more-btn" onClick={handleLoadMore}>Загрузить старые сообщения</button>
                )}
                <div className="chat-messages" ref={chatRef}>
                    <AnimatePresence>
                        {messages.map(msg => (
                            !msg.deleted && (
                                <motion.div
                                    key={msg.id}
                                    className={`chat-message ${msg.senderUsername === user.username ? 'own-message' : ''} ${selectedMessage === msg.id ? 'selected' : ''}`}
                                    initial={{ opacity: 0, y: 20 }}
                                    animate={{ opacity: 1, y: 0 }}
                                    exit={{ opacity: 0, height: 0 }}
                                    transition={{ duration: 0.3 }}
                                    onClick={() => setSelectedMessage(msg.id)}
                                >
                                    <div className="message-header">
                                        <span className="message-sender">{msg.senderUsername}</span>
                                        <span className="message-time">{msg.timestamp ? new Date(msg.timestamp).toLocaleTimeString() : ''}</span>
                                    </div>
                                    {msg.replyToId && (
                                        <div className="message-reply">
                                            <ArrowUturnLeftIcon className="reply-icon" />
                                            <span>В ответ на сообщение #{msg.replyToId}</span>
                                        </div>
                                    )}
                                    <div className="message-content" dangerouslySetInnerHTML={{ __html: renderMessageContent(msg.content) }} />
                                    {(msg.senderUsername === user.username || user.roles.some(r => r.name === 'ADMIN')) && (
                                        <div className="message-actions">
                                            {msg.senderUsername === user.username && (
                                                <button
                                                    className="reply-btn"
                                                    onClick={() => setReplyTo(msg)}
                                                    title="Ответить"
                                                >
                                                    <ArrowUturnLeftIcon />
                                                </button>
                                            )}
                                            {(msg.senderUsername === user.username || user.roles.some(r => r.name === 'ADMIN')) && (
                                                <button
                                                    className="delete-btn"
                                                    onClick={() => handleDeleteMessage(msg.id)}
                                                    title="Удалить"
                                                >
                                                    <TrashIcon />
                                                </button>
                                            )}
                                        </div>
                                    )}
                                </motion.div>
                            )
                        ))}
                    </AnimatePresence>
                </div>
                {replyTo && (
                    <div className="reply-preview">
                        <span>В ответ на: {replyTo.content}</span>
                        <button onClick={() => setReplyTo(null)}>Отмена</button>
                    </div>
                )}
                <div className="chat-input">
                    <textarea
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        onKeyPress={handleKeyPress}
                        placeholder="Введите сообщение..."
                    />
                    <motion.button
                        onClick={() => setShowEmojiPicker(!showEmojiPicker)}
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                        className="emoji-btn"
                        type="button"
                        title="Открыть выбор смайлов"
                    >
                        <FaceSmileIcon />
                    </motion.button>
                    <motion.button
                        onClick={handleSendMessage}
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                        className="send-btn"
                        type="button"
                    >
                        Отправить
                    </motion.button>
                </div>
                {showEmojiPicker && (
                    <div className="emoji-picker">
                        <EmojiPicker onEmojiClick={onEmojiClick} />
                    </div>
                )}
            </div>
            <div className="chat-users">
                <h3>Пользователи</h3>
                <div className="user-list">
                    {users.map(u => (
                        <div key={u.id} className={`user-item ${userStatuses[u.id] ? 'online' : 'offline'}`}>
                            <span>{u.username}</span>
                            {user.roles.some(r => r.name === 'ADMIN') && u.id !== user.id && (
                                <div className="user-actions">
                                    {u.status === 'PENDING' && (
                                        <button onClick={() => handleUserAction('approve', u.id)} className="approve-btn">Одобрить</button>
                                    )}
                                    {u.status === 'BLOCKED' && (
                                        <button onClick={() => handleUserAction('unblock', u.id)} className="unblock-btn">Разблокировать</button>
                                    )}
                                    {u.status !== 'BLOCKED' && (
                                        <button onClick={() => handleUserAction('block', u.id)} className="block-btn">Заблокировать</button>
                                    )}
                                    <button onClick={() => handleUserAction('delete', u.id)} className="delete-btn">Удалить</button>
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Chat;
