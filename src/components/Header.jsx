import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Palette } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext'; 

const Header = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, signOut } = useAuth(); 
  const navItems = [
    { path: '/', label: 'Início' },
    { path: '/cores', label: 'Cores' },
    { path: '/catalogo', label: 'Catálogo' },
    { path: '/historico', label: 'Histórico' }
  ];

  const handleLogout = async () => {
    try {
      await signOut();
      navigate('/');
    } catch (error) {
      console.error('Erro ao fazer logout:', error);
    }
  };

  return (
    <header className="bg-white/95 backdrop-blur-md border-b border-gray-200 shadow-sm">
      <div className="max-w-7xl mx-auto px-6">
        <div className="flex justify-between items-center h-20">
          <Link to="/" className="flex items-center gap-3 hover:scale-105 transition-transform duration-300">
            <div className="w-12 h-12 rounded-full bg-gradient-to-br from-blue-500 via-green-500 to-yellow-500 flex items-center justify-center">
              <Palette className="w-6 h-6 text-white" />
            </div>
            <span className="text-3xl font-bold text-gray-900">
              PaintLab
            </span>
          </Link>

          <nav className="flex items-center gap-x-8">
            {navItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={`text-lg font-medium transition-all duration-300 py-2 border-b-2 ${
                  location.pathname === item.path
                    ? 'text-black border-black'
                    : 'text-gray-600 border-transparent hover:text-black hover:scale-105'
                }`}
              >
                {item.label}
              </Link>
            ))}
            
            {user ? (
              <button
                onClick={handleLogout}
                className="text-lg font-medium text-gray-600 hover:text-black hover:scale-105 transition-all duration-300 py-2 border-b-2 border-transparent"
              >
                Sair
              </button>
            ) : (
              <Link
                to="/login"
                className={`text-lg font-medium transition-all duration-300 py-2 border-b-2 ${
                  location.pathname === '/login'
                    ? 'text-black border-black'
                    : 'text-gray-600 border-transparent hover:text-black hover:scale-105'
                }`}
              >
                Login
              </Link>
            )}
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;