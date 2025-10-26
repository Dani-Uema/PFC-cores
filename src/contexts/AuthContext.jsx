import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

const AuthContext = createContext(undefined);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const email = localStorage.getItem('email');
    const role = localStorage.getItem('role');

    if (token && email && role) {
      setUser({ email, role });
    }

    setLoading(false);
  }, []);

  const signUp = async (name, email, password, role = 'USER') => {
    try {
      await axios.post('http://localhost:8080/user/register', {
        name,
        email,
        password,
        role
      });
      return { error: null };
    } catch (error) {
      return { error: error.response?.data || error.message };
    }
  };

  const signIn = async (email, password) => {
    try {
      const response = await axios.post('http://localhost:8080/user/login', {
        email,
        password
      });

      const { token } = response.data;

      localStorage.setItem('token', token);
      localStorage.setItem('email', email);
      localStorage.setItem('role', 'USER'); // ajuste manual por enquanto

      setUser({ email, role: 'USER' });
      return { error: null };
    } catch (error) {
      return { error: error.response?.data || error.message };
    }
  };

  const signOut = async () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('role');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, signUp, signIn, signOut }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}