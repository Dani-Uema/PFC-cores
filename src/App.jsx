import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import Header from './components/Header';
import Home from "./pages/Home";
import Login from "./pages/Login";
import Cores from "./pages/Colors";
import Catalogo from "./pages/Catalog";
import Historico from "./pages/History";
import Register from "./pages/Register";

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen flex flex-col">
          <Header />
          <main className="flex-1">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/cores" element={<Cores />} />
              <Route path="/catalogo" element={<Catalogo />} />
              <Route path="/historico" element={<Historico />} />
              <Route path="/login" element={<Login />} />
              <Route path="/registro" element={<Register />} />

            </Routes>
          </main>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;