import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext.jsx";
import RotaProtegida from "./components/RotaProtegida.jsx";
import Cadastro from "./pages/Cadastro.jsx";
import Diagnostico from "./pages/Diagnostico.jsx";
import Landing from "./pages/Landing.jsx";
import Login from "./pages/Login.jsx";
import Modulo from "./pages/Modulo.jsx";
import Perfil from "./pages/Perfil.jsx";
import Questionario from "./pages/Questionario.jsx";
import Quiz from "./pages/Quiz.jsx";
import Responsaveis from "./pages/Responsaveis.jsx";
import Simulador from "./pages/Simulador.jsx";
import SimuladorCenario from "./pages/SimuladorCenario.jsx";
import Trilha from "./pages/Trilha.jsx";

// Páginas privadas ficam dentro de <RotaProtegida>: sem login, o usuário é levado para /login.
function Privada({ children }) {
  return <RotaProtegida>{children}</RotaProtegida>;
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Públicas */}
          <Route path="/" element={<Landing />} />
          <Route path="/responsaveis" element={<Responsaveis />} />
          <Route path="/cadastro" element={<Cadastro />} />
          <Route path="/login" element={<Login />} />

          {/* Privadas */}
          <Route path="/questionario" element={<Privada><Questionario /></Privada>} />
          <Route path="/trilha" element={<Privada><Trilha /></Privada>} />
          <Route path="/modulo/:id" element={<Privada><Modulo /></Privada>} />
          <Route path="/quiz/:moduloId" element={<Privada><Quiz /></Privada>} />
          <Route path="/simulador" element={<Privada><Simulador /></Privada>} />
          <Route path="/simulador/:id" element={<Privada><SimuladorCenario /></Privada>} />
          <Route path="/diagnostico" element={<Privada><Diagnostico /></Privada>} />
          <Route path="/perfil" element={<Privada><Perfil /></Privada>} />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
