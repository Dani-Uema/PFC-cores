import React, { useState } from 'react';

const Colors = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [colorData, setColorData] = useState(null);

  const mockColorData = {
    name: 'Azul Celeste',
    composition: [
      { pigment: 'Pigmento X', percentage: '20%' },
      { pigment: 'Pigmento Y', percentage: '30%' },
      { pigment: 'Pigmento Z', percentage: '15%' },
      { pigment: 'Base Acrílica', percentage: '35%' }
    ]
  };

  const handleSearch = () => {
    setColorData(mockColorData);
  };

  return (
    <div className="min-h-screen bg-white py-8">
      <div className="max-w-6xl mx-auto px-6">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-black mb-2">Início</h1>
          <div className="flex gap-4 text-sm text-gray-600">
            <span>Cores</span>
            <span>Histórico</span>
            <span>Login</span>
          </div>
        </div>

        <div className="h-px bg-gray-200 mb-8"></div>

        <div className="mb-12">
          <h2 className="text-2xl font-semibold text-black mb-6">Pesquise uma cor</h2>
          <div className="relative max-w-2xl">
            <input
              type="text"
              placeholder="Pesquisar cor, código ou marca"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full px-6 py-4 border border-gray-300 rounded-2xl focus:ring-2 focus:ring-black focus:border-black outline-none transition-colors"
            />
            <button 
              onClick={handleSearch}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-black text-white px-6 py-2 rounded-xl hover:bg-gray-800 transition-colors font-medium"
            >
              Pesquisar
            </button>
          </div>
        </div>

        {colorData && (
          <div className="bg-white border border-gray-200 rounded-2xl p-8">
            <h3 className="text-2xl font-semibold text-black mb-6">{colorData.name}</h3>
            
            <div className="grid md:grid-cols-2 gap-8">
              <div>
                <div className="w-full h-64 rounded-2xl border-2 border-gray-300 bg-gradient-to-br from-blue-400 to-blue-600"></div>
              </div>
              
              <div>
                <h4 className="text-lg font-semibold text-black mb-4">Composição</h4>
                <div className="border border-gray-200 rounded-2xl overflow-hidden">
                  <table className="w-full">
                    <thead>
                      <tr className="bg-gray-50 border-b border-gray-200">
                        <th className="text-left py-4 px-6 font-semibold text-black">cor da tinta</th>
                        <th className="text-left py-4 px-6 font-semibold text-black">Composição</th>
                      </tr>
                    </thead>
                    <tbody>
                      {mockColorData.composition.map((item, index) => (
                        <tr key={index} className="border-b border-gray-100 last:border-b-0">
                          <td className="py-4 px-6"></td>
                          <td className="py-4 px-6">{item.pigment} - {item.percentage}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Colors;