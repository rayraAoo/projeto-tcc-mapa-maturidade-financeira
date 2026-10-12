import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";

// Só deixa abrir a tela quem estiver com sessão ativa; os demais vão para o login.
export default function RotaProtegida({ children }) {
  const { autenticado } = useAuth();
  const local = useLocation();

  if (!autenticado) {
    return <Navigate to="/login" replace state={{ de: local.pathname }} />;
  }
  return children;
}
