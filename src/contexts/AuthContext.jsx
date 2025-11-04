import { createContext, useContext, useEffect, useState } from 'react';
import axios from 'axios';

const AuthContext = createContext(undefined);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userId = localStorage.getItem('userId');
    const email = localStorage.getItem('email');
    const role = localStorage.getItem('role');

    console.log('🔍 AuthContext - Carregando do localStorage:', { userId, email, role });

    if (token && userId && email && role) {
      setUser({ 
        id: userId,       
        email, 
        role 
      });
      console.log('✅ AuthContext - User carregado:', { id: userId, email, role });
    } else {
      console.log('❌ AuthContext - Dados incompletos no localStorage');
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
      console.log('🔍 AuthContext - Fazendo login para:', email);
      
      const response = await axios.post('http://localhost:8080/user/login', {
        email,
        password
      });

      console.log('✅ AuthContext - Resposta do login:', response.data);

      const { token, userId, email: userEmail, role } = response.data;

      localStorage.setItem('token', token);
      localStorage.setItem('userId', userId);
      localStorage.setItem('email', userEmail);
      localStorage.setItem('role', role);

      console.log('💾 AuthContext - Dados salvos no localStorage:', { userId, userEmail, role });

      const userData = { 
        id: userId,
        email: userEmail, 
        role: role 
      };
      
      setUser(userData);
      console.log('✅ AuthContext - User setado no state:', userData);

      return { error: null };
    } catch (error) {
      console.error('❌ AuthContext - Erro no login:', error.response?.data);
      return { error: error.response?.data || error.message };
    }
  };

  const signOut = async () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('email');
    localStorage.removeItem('role');
    setUser(null);
    console.log('✅ AuthContext - Logout realizado');
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