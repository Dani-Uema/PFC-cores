import { Palette, Search, Clock, Droplet } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function Home() {
  const navigate = useNavigate();

  return (
    <div className="h-screen w-screen bg-gradient-to-br from-black-200 via-gray-300 to-white-100 flex items-center justify-center p-4">
      <div className="max-w-7xl mx-auto px-4 py-16">
        <div className="text-center mb-16">
          <div className="inline-flex items-center justify-center gap-4 mb-6">
            <div className="w-20 h-20 rounded-full bg-gradient-to-br from-blue-500 via-green-500 to-yellow-500 flex items-center justify-center">
              <Palette className="w-12 h-12 text-white" />
            </div>
            <h1 className="text-6xl font-bold text-gray-900">PaintLab</h1>
          </div>
          <p className="text-2xl text-gray-700 max-w-3xl mx-auto leading-relaxed">
            Seu catálogo profissional de tintas e cores. Encontre a cor perfeita,
            consulte composições e mantenha histórico das suas pesquisas.
          </p>
        </div>

        <div className="grid md:grid-cols-3 gap-8 mb-16">
          <div
            onClick={() => navigate('/cores')}
            className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl transition-shadow cursor-pointer hover:scale-105 transition-transform duration-300"
          >
            <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mb-6">
              <Search className="w-8 h-8 text-blue-600" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900 mb-4">
              Busca Avançada
            </h3>
            <p className="text-gray-600 text-lg leading-relaxed">
              Pesquise cores por nome, código ou marca. Sistema de busca
              inteligente para encontrar exatamente o que precisa.
            </p>
          </div>

          <div
            onClick={() => navigate('/catalogo')}
            className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl transition-shadow cursor-pointer hover:scale-105 transition-transform duration-300"
          >
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mb-6">
              <Droplet className="w-8 h-8 text-green-600" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900 mb-4">
              Catálogo Completo
            </h3>
            <p className="text-gray-600 text-lg leading-relaxed">
              Navegue por todo nosso catálogo de cores organizado por categorias,
              marcas e tipos de aplicação.
            </p>
          </div>

          <div
            onClick={() => navigate('/historico')}
            className="bg-white rounded-2xl shadow-xl p-8 hover:shadow-2xl transition-shadow cursor-pointer hover:scale-105 transition-transform duration-300"
          >
            <div className="w-16 h-16 bg-orange-100 rounded-full flex items-center justify-center mb-6">
              <Clock className="w-8 h-8 text-orange-600" />
            </div>
            <h3 className="text-2xl font-bold text-gray-900 mb-4">
              Histórico de Consultas
            </h3>
            <p className="text-gray-600 text-lg leading-relaxed">
              Mantenha registro de todas as suas consultas e acesse
              rapidamente cores já pesquisadas.
            </p>
          </div>
        </div>

        <div className="text-center">
          <button
            onClick={() => navigate('/login')}
            className="bg-gray-900 text-white px-12 py-4 rounded-xl font-bold text-xl hover:bg-gray-800 transition-colors shadow-lg hover:shadow-xl hover:scale-105"
          >
            Começar Agora
          </button>
        </div>
      </div>
    </div>
  );
}