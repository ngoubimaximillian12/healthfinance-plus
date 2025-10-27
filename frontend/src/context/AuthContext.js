import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/api';
import {jwtDecode} from 'jwt-decode';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decoded = jwtDecode(token);
        const userData = authService.getCurrentUser();
        setUser(userData);
      } catch (error) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const response = await authService.login({ email, password });
    const { accessToken } = response.data;
    localStorage.setItem('token', accessToken);
    
    const decoded = jwtDecode(accessToken);
    const userData = { email: decoded.sub, roles: decoded.roles || ['PATIENT'] };
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const register = async (data) => {
    const response = await authService.register(data);
    const { accessToken } = response.data;
    localStorage.setItem('token', accessToken);
    
    const decoded = jwtDecode(accessToken);
    const userData = { email: decoded.sub, roles: decoded.roles || ['PATIENT'] };
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
    return userData;
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  const isAdmin = () => {
    return user?.roles?.includes('ADMIN') || user?.roles?.includes('DOCTOR');
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading, isAdmin }}>
      {children}
    </AuthContext.Provider>
  );
};
