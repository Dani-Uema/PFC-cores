import React, { useState } from 'react';

const SearchBar = ({ onSearch, placeholder = "Pesquisar cor, código ou marca" }) => {
  const [query, setQuery] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(query);
  };

  return (
    <form onSubmit={handleSubmit} className="w-full">
      <div className="relative">
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder={placeholder}
          className="w-full px-6 py-4 border border-gray-300 rounded-2xl focus:ring-2 focus:ring-black focus:border-black outline-none transition-colors"
        />
        <button
          type="submit"
          className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-black text-white px-6 py-2 rounded-xl hover:bg-gray-800 transition-colors font-medium"
        >
          Pesquisar
        </button>
      </div>
    </form>
  );
};

export default SearchBar;