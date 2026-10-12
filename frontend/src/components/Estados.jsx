// Mensagens padrão de carregamento e erro, para não repetir em cada tela.
export function Carregando({ texto = "Carregando..." }) {
  return (
    <p className="subtitulo centralizado espaco-topo" role="status">
      {texto}
    </p>
  );
}

export function MensagemErro({ texto }) {
  if (!texto) return null;
  return (
    <div className="alerta alerta-erro" role="alert">
      {texto}
    </div>
  );
}

export function BarraProgresso({ percentual }) {
  const valor = Math.max(0, Math.min(100, percentual || 0));
  return (
    <div className="progresso" role="progressbar" aria-valuenow={Math.round(valor)} aria-valuemin={0} aria-valuemax={100}>
      <div style={{ width: `${valor}%` }} />
    </div>
  );
}
