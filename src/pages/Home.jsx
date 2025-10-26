import React from 'react';

const Home = () => {
  const features = [
    { icon: '🔍', title: 'Busca Avançada', desc: 'Pesquise cores por nome, código ou marca.' },
    { icon: '💧', title: 'Composição Detalhada', desc: 'Veja a composição completa de pigmentos.' },
    { icon: '⏰', title: 'Histórico', desc: 'Mantenha registro das suas consultas.' }
  ];

  const navigateTo = (path) => {
    window.location.href = path;
  };

  return (
    <div className="h-screen w-screen bg-gradient-to-br from-blue-50 to-purple-100 flex items-center justify-center p-4">
      <div className="text-center">
        
        {/* Hero Section */}
        <div className="mb-16">
          <div className="flex items-center justify-center gap-4 mb-6">
            <div className="w-20 h-20 rounded-full bg-gradient-to-br from-blue-500 via-green-500 to-yellow-500 flex items-center justify-center shadow-2xl hover:scale-110 transition-transform duration-300">
              <span className="text-2xl text-white">🎨</span>
            </div>
            <h1 className="text-6xl font-bold text-gray-900 hover:scale-105 transition-transform duration-300">
              PaintLab
            </h1>
          </div>
          <p className="text-2xl text-gray-700 max-w-3xl mx-auto">
            Seu catálogo profissional de tintas e cores.
          </p>
        </div>

        {/* Features Grid */}
        <div className="grid md:grid-cols-3 gap-8 mb-16">
          {features.map((feature, index) => (
            <div 
              key={index} 
              className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl hover:scale-105 transition-all duration-300 cursor-pointer"
              onClick={() => navigateTo('/cores')}
            >
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6 mx-auto hover:scale-110 transition-transform duration-300">
                <span className="text-xl">{feature.icon}</span>
              </div>
              <h3 className="text-2xl font-bold text-gray-900 mb-4">{feature.title}</h3>
              <p className="text-gray-600 text-lg">{feature.desc}</p>
            </div>
          ))}
        </div>

        {/* Button */}
        <button
          onClick={() => navigateTo('/cores')}
          className="bg-gray-900 text-white px-12 py-4 rounded-xl font-bold text-xl hover:bg-gray-800 hover:scale-105 hover:shadow-xl transition-all duration-300 shadow-lg"
        >
          Explorar Catálogo
        </button>

      </div>
    </div>
  );
};

export default Home;