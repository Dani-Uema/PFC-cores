import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Header = () => {
  const location = useLocation();

  const navItems = [
    { path: '/', label: 'Início' },
    { path: '/cores', label: 'Cores' },
    { path: '/catalogo', label: 'Catálogo' },
    { path: '/historico', label: 'Histórico' },
    { path: '/login', label: 'Login' }
  ];

  return (
    <header className="bg-white/95 backdrop-blur-md border-b border-gray-200 shadow-sm">
      <div className="max-w-7xl mx-auto px-6">
        <div className="flex justify-between items-center h-20">
          <Link to="/" className="text-3xl font-bold text-gray-900 hover:scale-105 transition-transform duration-300">
            PaintLab
          </Link>
          
          <nav className="flex space-x-8">
            {navItems.map((item) => (
              <Link
                key={item.path}
                to={item.path}
                className={`text-lg font-medium transition-all duration-300 ${
                  location.pathname === item.path
                    ? 'text-black font-bold border-b-2 border-black'
                    : 'text-gray-600 hover:text-black hover:scale-105'
                }`}
              >
                {item.label}
              </Link>
            ))}
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;