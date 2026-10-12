import { useCallback, useEffect, useState } from "react";
import { mensagemDeErro } from "../utils/erros.js";

// Carrega dados de uma API ao abrir a tela e controla "carregando" e "erro".
// Uso: const { dados, carregando, erro, recarregar } = useDados(() => obterTrilha());
export function useDados(carregar, dependencias = []) {
  const [dados, setDados] = useState(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const recarregar = useCallback(async () => {
    setCarregando(true);
    setErro("");
    try {
      setDados(await carregar());
    } catch (e) {
      setErro(mensagemDeErro(e));
    } finally {
      setCarregando(false);
    }
  }, dependencias);

  useEffect(() => {
    recarregar();
  }, [recarregar]);

  return { dados, carregando, erro, recarregar };
}
