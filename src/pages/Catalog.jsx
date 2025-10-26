import React from 'react';

const Catalog = () => {
  return (
    <div className="min-h-screen bg-white py-8">
      <div className="max-w-6xl mx-auto px-6">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-black mb-2">Catálogo</h1>
          <div className="flex gap-4 text-sm text-gray-600">
            <span>Início</span>
            <span>Cores</span>
            <span>Histórico</span>
            <span>Login</span>
          </div>
        </div>

        <div className="h-px bg-gray-200 mb-8"></div>

        <div>
          <h2 className="text-2xl font-semibold text-black mb-6">Catálogo de tintas</h2>
          
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {[...Array(8)].map((_, index) => (
              <div key={index} className="text-center cursor-pointer hover:scale-105 transition-transform">
                <div className="w-24 h-24 rounded-2xl bg-gradient-to-br from-blue-400 to-blue-600 mx-auto mb-3 border border-gray-200"></div>
                <span className="text-sm font-medium text-black">Cor {index + 1}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Catalog;