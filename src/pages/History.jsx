import { useState, useEffect } from 'react';
import { Clock, Trash2 } from 'lucide-react';
import { historyService } from '../services/api';
import { useAuth } from '../contexts/AuthContext';

export default function History() {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  console.log('🔍 HISTORY - User:', user);
  console.log('🔍 HISTORY - User ID:', user?.id);

  useEffect(() => {
    console.log('🎯 HISTORY - useEffect triggered, user:', user);
    
    if (user && user.id) {
      console.log('✅ HISTORY - Carregando histórico para user:', user.id);
      loadHistory();
    } else {
      console.log('❌ HISTORY - User ou ID não disponível');
      setLoading(false);
    }
  }, [user]);

  const loadHistory = async () => {
  try {
    console.log('🎯 HISTORY - Chamando API com user ID:', user.id);
    
    const response = await historyService.getSearchHistory(user.id);
    
    console.log('✅ HISTORY - Dados recebidos:', response.data);
    
    if (response.data && response.data.length > 0) {
      const firstItem = response.data[0];
      console.log('🔍 ESTRUTURA COMPLETA DO PRIMEIRO ITEM:');
      
      console.log('   📋 CAMPOS DO ITEM:', Object.keys(firstItem));
      
      if (firstItem.color) {
        console.log('   🎨 CAMPOS DO COLOR:', Object.keys(firstItem.color));
        console.log('   🎨 COLOR COMPLETO:', firstItem.color);
      } else {
        console.log('   ❌ COLOR É:', firstItem.color);
      }
      
      console.log('   📅 consultationDate:', firstItem.consultationDate);
      console.log('   🆔 ID:', firstItem.id);
    }
    
    setHistory(response.data || []);
  } catch (error) {
    console.error('❌ HISTORY - Erro ao carregar histórico:', error);
    setHistory([]);
  } finally {
    setLoading(false);
  }
};

const deleteHistoryItem = async (historyId) => {
  console.log('🗑️ ========== INICIANDO DELETE ==========');
  console.log('📦 ID do item a ser deletado:', historyId);
  console.log('👤 User atual:', user);
  console.log('📋 Itens na lista antes:', history.map(item => ({ id: item.id, name: item.color?.name })));
  
  if (!user?.id) {
    console.log('❌ User não está logado!');
    return;
  }
  
  try {
    console.log('🎯 Chamando historyService.deleteHistoryItem...');
    
    await historyService.deleteHistoryItem(historyId);
    
    console.log('✅ API respondeu com sucesso!');
    
    const newHistory = history.filter(item => item.id !== historyId);
    console.log('🔄 Lista após filtro:', newHistory.map(item => ({ id: item.id, name: item.color?.name })));
    
    setHistory(newHistory);
    console.log('✅ Estado atualizado com sucesso!');
    console.log('🗑️ ========== DELETE CONCLUÍDO ==========');
    
  } catch (error) {
    console.error('❌ ========== ERRO NO DELETE ==========');
    console.error('❌ Erro completo:', error);
    console.error('❌ Mensagem:', error.message);
    console.error('❌ Response data:', error.response?.data);
    console.error('❌ Status:', error.response?.status);
    console.error('❌ ========== FIM DO ERRO ==========');
  }
};

  const formatDate = (dateString) => {
    console.log('🔍 Formatando data:', dateString);
    
    if (!dateString) {
      return 'Data não disponível';
    }
    
    try {
      const date = new Date(dateString);
      
      if (isNaN(date.getTime())) {
        console.log('❌ Data inválida:', dateString);
        return 'Data inválida';
      }
      
      const formatted = new Intl.DateTimeFormat('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      }).format(date);
      
      console.log('✅ Data formatada:', formatted);
      return formatted;
    } catch (error) {
      console.error('❌ Erro ao formatar data:', error, 'Data:', dateString);
      return 'Erro na data';
    }
  };

  if (!user) {
    return (
      <div className="min-h-[calc(100vh-5rem)] bg-gradient-to-br from-gray-100 via-gray-300 to-gray-300">
        <div className="bg-gray-600 py-12">
          <h1 className="text-4xl font-bold text-center text-gray-100">
            Histórico de consultas
          </h1>
        </div>
        <div className="max-w-6xl mx-auto px-4 py-12">
          <div className="bg-white rounded-2xl shadow-xl p-12 text-center">
            <p className="text-xl text-gray-600">
              Faça login para ver seu histórico de consultas
            </p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-[calc(100vh-5rem)] bg-gradient-to-br from-gray-100 via-gray-300 to-gray-300">
      <div className="bg-gray-600 py-12">
        <div className="max-w-6xl mx-auto px-4">
          <h1 className="text-4xl font-bold text-center text-gray-100">
            Histórico de consultas
          </h1>
        </div>
      </div>

      <div className="max-w-6xl mx-auto px-4 py-12">
        {loading && (
          <div className="text-center text-gray-600 text-lg">
            Carregando histórico...
          </div>
        )}

        {!loading && history.length === 0 && (
          <div className="bg-white rounded-2xl shadow-xl p-12 text-center">
            <Clock className="w-16 h-16 text-gray-400 mx-auto mb-4" />
            <p className="text-xl text-gray-600">
              Você ainda não realizou nenhuma consulta
            </p>
            <p className="text-gray-500 mt-2">
              Suas pesquisas aparecerão aqui automaticamente
            </p>
          </div>
        )}

        {!loading && history.length > 0 && (
          <div className="space-y-4">
            {history.map((item) => (
              <div
                key={item.id}
                className="bg-white rounded-xl shadow-lg p-6 hover:shadow-xl transition-shadow"
              >
                <div className="flex items-center gap-6">
                  <div
                    className="w-24 h-24 rounded-lg border-4 border-gray-200 flex-shrink-0"
                    style={{ backgroundColor: item.color?.hexCode || '#ccc' }}
                  />

                  <div className="flex-1">
                    <h3 className="text-2xl font-bold text-gray-900 mb-2">
                      {item.color?.name || 'Cor não disponível'}
                    </h3>
                    <div className="flex flex-wrap gap-4 text-gray-600">
                      {item.color?.colorCode && (
                        <span>Código: {item.color.colorCode}</span>
                      )}
                      {item.color?.brand && (
                        <span>Marca: {item.color.brand}</span>
                      )}
                    </div>
                  </div>

                  <div className="text-right text-gray-500 flex-shrink-0">
                    <div className="flex items-center gap-2 mb-2">
                      <Clock className="w-4 h-4" />
                      <span className="text-sm">
                        {formatDate(item.consultationDate)}
                      </span>
                    </div>
                    
                    <button
                      onClick={() => deleteHistoryItem(item.id)}
                      className="text-red-500 hover:text-red-700 text-sm flex items-center gap-1 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                      Remover
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}