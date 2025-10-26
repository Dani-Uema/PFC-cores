import React from 'react';

const History = () => {
  const searchHistory = [
    { id: 1, query: 'Azul Marinho', date: '15/11/2023 14:30' },
    { id: 2, query: 'Verde Floresta', date: '14/11/2023 10:15' },
    { id: 3, query: 'Vermelho Coral', date: '12/11/2023 16:45' },
  ];

  return (
    <div className="min-h-screen bg-white py-8">
      <div className="max-w-6xl mx-auto px-6">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-black mb-2">Histórico</h1>
          <div className="flex gap-4 text-sm text-gray-600">
            <span>Início</span>
            <span>Cores</span>
            <span className="text-black font-semibold">Histórico</span>
            <span>Login</span>
          </div>
        </div>

        <div className="h-px bg-gray-200 mb-8"></div>

        <div>
          <h2 className="text-2xl font-semibold text-black mb-6">Histórico de consultas</h2>
          
          <div className="space-y-4">
            {searchHistory.map((item) => (
              <div key={item.id} className="flex justify-between items-center p-6 border border-gray-200 rounded-2xl hover:bg-gray-50 transition-colors">
                <div>
                  <h3 className="font-semibold text-black text-lg">{item.query}</h3>
                  <p className="text-gray-500 text-sm">{item.date}</p>
                </div>
                <button className="bg-black text-white px-6 py-3 rounded-xl hover:bg-gray-800 transition-colors font-medium">
                  Consultar
                </button>
              </div>
            ))}
          </div>

          {searchHistory.length === 0 && (
            <div className="bg-gray-50 rounded-2xl p-8 text-center">
              <p className="text-gray-600">Nenhuma consulta no histórico</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default History;