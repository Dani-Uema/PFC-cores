import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext'; // ← MUDEI PARA ../

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { signIn } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const { error: signInError } = await signIn(email, password);
      
      if (signInError) {
        setError('Email ou senha inválidos');
      } else {
        window.location.href = '/cores';
      }
    } catch (err) {
      setError('Erro ao fazer login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="h-screen w-screen bg-gradient-to-br from-gray-100 to-gray-300 flex items-center justify-center p-4">
      <div className="w-full max-w-5xl grid md:grid-cols-2 gap-0 bg-white rounded-2xl overflow-hidden shadow-2xl">
        
        {/* Left Side - Brand */}
        <div className="bg-gradient-to-br from-gray-300 to-gray-400 p-12 flex flex-col justify-center items-center">
          <div className="mb-8">
            <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-500 via-green-500 to-yellow-500 flex items-center justify-center mb-4">
              <span className="text-2xl text-white">🎨</span>
            </div>
            <span className="text-3xl font-bold text-gray-900">PaintLab</span>
          </div>

          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-3">
              Ainda não possui uma conta?
            </h2>
            <p className="text-gray-700 mb-8 text-lg">
              Cadastre-se e comece agora!
            </p>
            <button
              onClick={() => window.location.href = '/registro'}
              className="bg-gray-900 text-white px-8 py-3 rounded-lg font-medium text-lg hover:bg-gray-800 hover:scale-105 transition-all duration-300"
            >
              Criar conta
            </button>
          </div>
        </div>

        {/* Right Side - Login Form */}
        <div className="bg-gray-200 p-12 flex flex-col justify-center">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">
              Bem-vindo de volta!
            </h1>
            <p className="text-gray-600 text-lg">Acesse sua conta.</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded-lg">
                {error}
              </div>
            )}

            <div>
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full px-4 py-3 rounded-lg bg-white border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-400 text-lg hover:scale-105 transition-transform duration-300"
              />
            </div>

            <div>
              <input
                type="password"
                placeholder="Senha"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="w-full px-4 py-3 rounded-lg bg-white border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-400 text-lg hover:scale-105 transition-transform duration-300"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gray-900 text-white py-3 rounded-lg font-medium text-lg hover:bg-gray-800 hover:scale-105 hover:shadow-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;