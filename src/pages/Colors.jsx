import { useState, useEffect } from 'react';
import { Search } from 'lucide-react';
import { colorService } from '../services/api';
import { historyService } from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function Colors() {
  const [searchTerm, setSearchTerm] = useState('');
  const [colors, setColors] = useState([]);
  const [filteredColors, setFilteredColors] = useState([]);
  const [selectedColor, setSelectedColor] = useState(null);
  const [pigments, setPigments] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    loadColors();
  }, []);

  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredColors([]);
      setSelectedColor(null);
      setPigments([]);
    } else {
      const filtered = colors.filter(color =>
        color.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        color.colorCode?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        color.brand?.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredColors(filtered);
    }
  }, [searchTerm, colors]);

  const loadColors = async () => {
    try {
      const response = await colorService.getAllColors(); 
      setColors(response.data || []);
    } catch (error) {
      console.error('Erro ao carregar cores:', error);
      setColors([]);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (searchTerm.trim() === '') return;

    try {
      const response = await colorService.searchColors(searchTerm);
      const searchResults = response.data || [];

      if (searchResults.length > 0) {
        await selectColor(searchResults[0]);
        setFilteredColors(searchResults);
      } else {
        setFilteredColors([]);
        setSelectedColor(null);
        setPigments([]);
      }
    } catch (error) {
      console.error('Erro na busca:', error);
      const filtered = colors.filter(color =>
        color.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        color.colorCode?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        color.brand?.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredColors(filtered);

      if (filtered.length > 0) {
        await selectColor(filtered[0]);
      }
    }
  };

  const selectColor = async (color) => {
    console.log('🎯 COLORS - selectColor chamado');
    console.log('🔍 COLORS - User no momento do clique:', user);
    console.log('🔍 COLORS - User ID no momento do clique:', user?.id);

    setSelectedColor(color);

    try {
      const response = await colorService.getColorComposition(color.id);
      setPigments(response.data || []);
    } catch (error) {
      console.error('Erro ao carregar composição:', error);
      setPigments([]);
    }

    if (user && user.id) {
      try {
        console.log('🎯 Salvando busca no histórico...');
        await historyService.addToHistory({
          userId: user.id,
          colorId: color.id
        });
        console.log('✅ Histórico salvo com sucesso!');
      } catch (error) {
        console.error('❌ Erro ao salvar histórico:', error);
      }
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-200 via-gray-300 to-gray-400">
      <div className="bg-gray-600 py-12">
        <h1 className="text-4xl font-bold text-center text-gray-100">
          Pesquisa de tintas
        </h1>
      </div>

      <div className="max-w-6xl mx-auto px-4 py-12">
        <div className="mb-12">
          <h2 className="text-2xl font-bold text-gray-900 mb-6">Pesquise uma cor</h2>
          <div className="flex gap-4">
            <input
              type="text"
              placeholder="Pesquisar cor, código ou marca"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              className="flex-1 px-6 py-4 rounded-lg bg-gray-200 border border-gray-300 text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-500 text-lg"
            />
            <button
              onClick={handleSearch}
              className="bg-gray-900 text-white px-8 py-4 rounded-lg font-medium text-lg hover:bg-gray-800 transition-colors flex items-center gap-2"
            >
              <Search className="w-5 h-5" />
              Buscar
            </button>
          </div>
        </div>

        {loading && (
          <div className="text-center text-gray-600 text-lg">Carregando cores...</div>
        )}

        {!loading && searchTerm && filteredColors.length === 0 && (
          <div className="text-center text-gray-600 text-lg">
            Nenhuma cor encontrada para "{searchTerm}"
          </div>
        )}

        {selectedColor && (
          <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
            <div className="p-8">
              <h2 className="text-3xl font-bold text-gray-900 mb-8">
                {selectedColor.name}
              </h2>

              <div className="grid md:grid-cols-3 gap-8">
                <div className="md:col-span-1">
                  <div
                    className="w-full aspect-square rounded-2xl border-4 border-gray-200 shadow-lg flex items-center justify-center"
                    style={{ backgroundColor: selectedColor.hexCode }}
                  >
                    <span className="text-gray-900 font-bold text-xl px-4 py-2 bg-white/80 rounded-lg backdrop-blur-sm">
                      cor da tinta
                    </span>
                  </div>
                  {selectedColor.colorCode && (
                    <p className="text-center text-gray-600 mt-4 text-lg">
                      Código: {selectedColor.colorCode}
                    </p>
                  )}
                  {selectedColor.brand && (
                    <p className="text-center text-gray-600 text-lg">
                      Marca: {selectedColor.brand}
                    </p>
                  )}
                </div>

                <div className="md:col-span-2">
                  <h3 className="text-2xl font-bold text-gray-900 mb-6">Composição</h3>
                  {pigments.length > 0 ? (
                    <div className="space-y-4">
                      {pigments.map((pigment, index) => (
                        <div key={index} className="flex items-center gap-4">
                          <div className="flex-1">
                            <div className="flex justify-between items-center mb-2">
                              <span className="text-gray-700 font-medium text-lg">
                                {pigment.name}
                              </span>
                              <span className="text-gray-600 font-medium">
                                {pigment.proportion}%
                              </span>
                            </div>
                            <div className="h-3 bg-gray-200 rounded-full overflow-hidden">
                              <div
                                className="h-full bg-gradient-to-r from-blue-500 to-blue-600 rounded-full transition-all"
                                style={{
                                  width: `${pigment.proportion}%`
                                }}
                              />
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <p className="text-gray-600 text-lg">
                      Informações de composição não disponíveis
                    </p>
                  )}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Lista de resultados da busca */}
        {!loading && filteredColors.length > 1 && (
          <div className="mt-8">
            <h3 className="text-xl font-bold text-gray-900 mb-4">
              Resultados da busca ({filteredColors.length})
            </h3>
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredColors.map((color) => (
                <div
                  key={color.id}
                  onClick={() => selectColor(color)}
                  className="bg-white rounded-lg shadow-md p-4 cursor-pointer hover:shadow-lg transition-shadow"
                >
                  <div className="flex items-center gap-4">
                    <div
                      className="w-12 h-12 rounded border-2 border-gray-200"
                      style={{ backgroundColor: color.hexCode }}
                    />
                    <div>
                      <h4 className="font-medium text-gray-900">{color.name}</h4>
                      {color.colorCode && (
                        <p className="text-sm text-gray-600">Código: {color.colorCode}</p>
                      )}
                      {color.brand && (
                        <p className="text-sm text-gray-600">Marca: {color.brand}</p>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}