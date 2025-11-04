import { useState } from 'react';
import { Palette } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { signUp } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (password.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres');
      setLoading(false);
      return;
    }

    const { error: signUpError } = await signUp(name, email, password);

    if (signUpError) {
      setError(signUpError.message || 'Erro ao criar conta');
      setLoading(false);
    } else {
      navigate('/cores'); 
    }
  };

  return (
    <div className="h-screen w-screen bg-gradient-to-br from-gray-100 via-gray-300 to-gray-300 flex items-center justify-center p-4">
      <div className="w-full max-w-5xl grid md:grid-cols-2 gap-0 bg-white rounded-2xl overflow-hidden shadow-2xl min-h-[600px]">
        
        {/* Left Side - Brand */}
        <div className="bg-gray-200 p-12 flex flex-col justify-center items-center text-center">
          <div className="mb-8 flex flex-col items-center">
            <div className="w-16 h-16 rounded-full bg-gradient-to-br from-blue-500 via-green-500 to-yellow-500 flex items-center justify-center mb-4">
              <Palette className="w-10 h-10 text-white" />
            </div>
            <span className="text-3xl font-bold text-gray-900">PaintLab</span>
          </div>

          <div className="w-full max-w-xs">
            <h2 className="text-2xl font-bold text-gray-900 mb-3">
              Já possui uma conta?
            </h2>
            <p className="text-gray-700 mb-8 text-lg">
              Acesse agora mesmo.
            </p>
            <button
              onClick={() => navigate('/login')}
              className="bg-gray-900 text-white px-8 py-3 rounded-lg font-medium text-lg hover:bg-gray-800 transition-colors w-full"
            >
              Entrar
            </button>
          </div>
        </div>

        {/* Right Side - Form */}
        <div className="bg-gray-100 p-12 flex flex-col justify-center">
          <div className="mb-8 text-center">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">
              Crie sua conta
            </h1>
            <p className="text-gray-600 text-lg">Cadastre seus dados</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6 max-w-md mx-auto w-full">
            {error && (
              <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded-lg text-center">
                {error}
              </div>
            )}

            <div>
              <input
                type="text"
                placeholder="Nome"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="w-full px-4 py-3 rounded-lg bg-white border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 text-lg transition-all"
              />
            </div>

            <div>
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full px-4 py-3 rounded-lg bg-white border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 text-lg transition-all"
              />
            </div>

            <div>
              <input
                type="password"
                placeholder="Senha"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="w-full px-4 py-3 rounded-lg bg-white border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-blue-500 text-lg transition-all"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gray-900 text-white py-3 rounded-lg font-medium text-lg hover:bg-gray-800 transition-all duration-300 hover:scale-105 hover:shadow-xl disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100"
            >
              {loading ? 'Cadastrando...' : 'Cadastrar'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}