import { create } from 'zustand';

export const useAuthStore = create((set) => ({
    user: null,
    setAuth: (user) => set({ user }),
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        set({ user: null });
    },
}));