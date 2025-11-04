import { useState, useEffect } from 'react';
import { Search, Filter, Grid, List, Palette } from 'lucide-react';
import { colorService } from '../services/api';

export default function Catalog() {
  const [colors, setColors] = useState([]);
  const [filteredColors, setFilteredColors] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedBrand, setSelectedBrand] = useState('');
  const [selectedColor, setSelectedColor] = useState(null);
  const [pigments, setPigments] = useState([]);
  const [viewMode, setViewMode] = useState('grid');
  const [loading, setLoading] = useState(true);
  const [brands, setBrands] = useState([]);
  const [pigmentsLoading, setPigmentsLoading] = useState(false);

  useEffect(() => {
    loadColors();
  }, []);

  useEffect(() => {
    filterColors();
  }, [searchTerm, selectedBrand, colors]);

  const loadColors = async () => {
    try {
      const response = await colorService.getAllColors();
      const colorsData = response.data || [];
      console.log('Cores carregadas:', colorsData); 
      
      setColors(colorsData);
      
      const uniqueBrands = [...new Set(colorsData
        .map(color => color.brand || color.marca) 
        .filter(Boolean)
      )];
      setBrands(uniqueBrands.sort());
    } catch (error) {
      console.error('Erro ao carregar cores:', error);
      setColors([]);
      setBrands([]);
    } finally {
      setLoading(false);
    }
  };

  const loadPigments = async (colorName) => {
    setPigmentsLoading(true);
    try {
      const response = await colorService.getColorComposition(colorName);
      setPigments(response.data || []);
    } catch (error) {
      console.error('Erro ao carregar pigmentos:', error);
      setPigments([]);
    } finally {
      setPigmentsLoading(false);
    }
  };

  const filterColors = () => {
    let filtered = colors;

    if (searchTerm) {
      filtered = filtered.filter(color =>
        (color.name || color.nome || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
        (color.code || color.codigo || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
        (color.brand || color.marca || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
        (color.hexCode || color.hex || color.hexColor || '').toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (selectedBrand) {
      filtered = filtered.filter(color => 
        (color.brand || color.marca) === selectedBrand
      );
    }

    setFilteredColors(filtered);
  };

  const handleColorSelect = async (color) => {
    setSelectedColor(color);
    if (color.name || color.nome) {
      await loadPigments(color.name || color.nome);
    }
  };

  const clearFilters = () => {
    setSearchTerm('');
    setSelectedBrand('');
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-200 via-gray-300 to-gray-400">
      <div className="bg-gray-600 py-12">
        <div className="max-w-6xl mx-auto px-4">
          <h1 className="text-4xl font-bold text-center text-white mb-4">
            Catálogo de Cores
          </h1>
          <p className="text-gray-200 text-center text-lg">
            Explore todas as cores disponíveis
          </p>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Filtros e Busca */}
        <div className="bg-white rounded-2xl shadow-lg p-6 mb-8">
          <div className="flex flex-col lg:flex-row gap-4 mb-6">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder="Pesquisar cor, código, marca ou hex..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500"
              />
            </div>

            <div className="flex gap-4">
              {brands.length > 0 && (
                <select
                  value={selectedBrand}
                  onChange={(e) => setSelectedBrand(e.target.value)}
                  className="px-4 py-3 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500"
                >
                  <option value="">Todas as marcas</option>
                  {brands.map((brand) => (
                    <option key={brand} value={brand}>
                      {brand}
                    </option>
                  ))}
                </select>
              )}

              <div className="flex bg-gray-100 rounded-lg p-1">
                <button
                  onClick={() => setViewMode('grid')}
                  className={`p-2 rounded-md transition-colors ${
                    viewMode === 'grid' ? 'bg-white shadow-sm' : 'hover:bg-gray-200'
                  }`}
                >
                  <Grid className="w-5 h-5" />
                </button>
                <button
                  onClick={() => setViewMode('list')}
                  className={`p-2 rounded-md transition-colors ${
                    viewMode === 'list' ? 'bg-white shadow-sm' : 'hover:bg-gray-200'
                  }`}
                >
                  <List className="w-5 h-5" />
                </button>
              </div>
            </div>
          </div>

          <div className="flex justify-between items-center">
            <div className="text-gray-600">
              {filteredColors.length} {filteredColors.length === 1 ? 'cor encontrada' : 'cores encontradas'}
              {selectedBrand && ` • Marca: ${selectedBrand}`}
            </div>
            {(searchTerm || selectedBrand) && (
              <button
                onClick={clearFilters}
                className="text-gray-600 hover:text-gray-800 underline text-sm"
              >
                Limpar filtros
              </button>
            )}
          </div>
        </div>

        {/* Grid de Cores */}
        {loading ? (
          <div className="text-center py-12">
            <div className="text-gray-600 text-lg">Carregando cores...</div>
          </div>
        ) : filteredColors.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl shadow-lg">
            <Filter className="w-16 h-16 text-gray-400 mx-auto mb-4" />
            <h3 className="text-xl font-bold text-gray-900 mb-2">
              Nenhuma cor encontrada
            </h3>
            <p className="text-gray-600">
              {searchTerm || selectedBrand 
                ? 'Tente ajustar os filtros de busca' 
                : 'Nenhuma cor disponível no momento'
              }
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4">
            {filteredColors.map((color) => (
              <div
                key={color.id}
                className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all duration-300 hover:scale-105 cursor-pointer group"
                onClick={() => handleColorSelect(color)}
              >
                {/* Bloco de Cor */}
                <div
                  className="h-32 w-full relative group-hover:brightness-110 transition-all"
                  style={{ 
                    backgroundColor: color.hexCode || color.hex || color.hexColor || color.corHex || '#ccc' 
                  }}
                >
                  <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-10 transition-all duration-300" />
                  <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                    <Palette className="w-8 h-8 text-white drop-shadow-lg" />
                  </div>
                </div>

                {/* Informações */}
                <div className="p-3">
                  <h3 className="font-semibold text-gray-900 text-sm truncate">
                    {color.name || color.nome || 'Sem nome'}
                  </h3>
                  {(color.code || color.codigo) && (
                    <p className="text-gray-600 text-xs truncate">
                      {color.code || color.codigo}
                    </p>
                  )}
                  {(color.brand || color.marca) && (
                    <p className="text-gray-500 text-xs truncate">
                      {color.brand || color.marca}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Modal de Detalhes */}
        {selectedColor && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
            <div className="bg-white rounded-2xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
              <div className="p-6">
                <div className="flex justify-between items-start mb-4">
                  <h2 className="text-2xl font-bold text-gray-900">
                    {selectedColor.name || selectedColor.nome || 'Sem nome'}
                  </h2>
                  <button
                    onClick={() => {
                      setSelectedColor(null);
                      setPigments([]);
                    }}
                    className="text-gray-400 hover:text-gray-600 text-xl"
                  >
                    ✕
                  </button>
                </div>

                {/* Amostra da Cor */}
                <div
                  className="w-full h-48 rounded-xl mb-6 border-4 border-gray-200 shadow-inner"
                  style={{ 
                    backgroundColor: selectedColor.hexCode || selectedColor.hex || selectedColor.hexColor || selectedColor.corHex || '#ccc' 
                  }}
                />

                <div className="grid md:grid-cols-2 gap-6">
                  {/* Informações Básicas */}
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-3">Informações</h3>
                    <div className="space-y-2">
                      {(selectedColor.code || selectedColor.codigo) && (
                        <div className="flex justify-between">
                          <span className="font-medium text-gray-700">Código:</span>
                          <span className="text-gray-900">{selectedColor.code || selectedColor.codigo}</span>
                        </div>
                      )}
                      {(selectedColor.brand || selectedColor.marca) && (
                        <div className="flex justify-between">
                          <span className="font-medium text-gray-700">Marca:</span>
                          <span className="text-gray-900">{selectedColor.brand || selectedColor.marca}</span>
                        </div>
                      )}
                      <div className="flex justify-between">
                        <span className="font-medium text-gray-700">Hex:</span>
                        <span className="text-gray-900 font-mono">
                          {selectedColor.hexCode || selectedColor.hex || selectedColor.hexColor || selectedColor.corHex || 'N/A'}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Composição de Pigmentos */}
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-3">
                      Composição
                      {pigmentsLoading && <span className="text-sm text-gray-500 ml-2">(carregando...)</span>}
                    </h3>
                    {pigments.length > 0 ? (
                      <div className="space-y-2">
                        {pigments.map((pigment, index) => (
                          <div key={index} className="flex justify-between items-center">
                            <span className="text-gray-700">{pigment.name || pigment.nome}</span>
                            <span className="text-gray-900 font-medium">
                              {pigment.percentage || pigment.percentual}%
                            </span>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p className="text-gray-600">
                        {pigmentsLoading ? 'Carregando...' : 'Informações de composição não disponíveis'}
                      </p>
                    )}
                  </div>
                </div>

                <button
                  onClick={() => {
                    setSelectedColor(null);
                    setPigments([]);
                  }}
                  className="w-full mt-6 bg-gray-900 text-white py-3 rounded-lg font-medium hover:bg-gray-800 transition-colors"
                >
                  Fechar
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}