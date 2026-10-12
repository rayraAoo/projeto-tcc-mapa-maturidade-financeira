// Ícones simples em SVG (sem biblioteca extra). "currentColor" faz o ícone seguir a cor do texto.
const propsBase = {
  width: 22,
  height: 22,
  viewBox: "0 0 24 24",
  fill: "none",
  stroke: "currentColor",
  strokeWidth: 2,
  strokeLinecap: "round",
  strokeLinejoin: "round",
  "aria-hidden": true,
};

export function IconeTrilha() {
  return (
    <svg {...propsBase}>
      <rect x="3" y="3" width="7" height="7" rx="1.5" />
      <rect x="14" y="3" width="7" height="7" rx="1.5" />
      <rect x="3" y="14" width="7" height="7" rx="1.5" />
      <rect x="14" y="14" width="7" height="7" rx="1.5" />
    </svg>
  );
}

export function IconeSimulador() {
  return (
    <svg {...propsBase}>
      <path d="M3 3v18h18" />
      <path d="M7 15l4-4 3 3 5-6" />
    </svg>
  );
}

export function IconeDiagnostico() {
  return (
    <svg {...propsBase}>
      <path d="M12 3l2.2 5.8L20 11l-5.8 2.2L12 19l-2.2-5.8L4 11l5.8-2.2z" />
    </svg>
  );
}

export function IconePerfil() {
  return (
    <svg {...propsBase}>
      <circle cx="12" cy="8" r="4" />
      <path d="M4 21c0-4.4 3.6-7 8-7s8 2.6 8 7" />
    </svg>
  );
}

export function IconeCheck() {
  return (
    <svg {...propsBase} width={16} height={16} strokeWidth={3} stroke="#ffffff">
      <path d="M20 6L9 17l-5-5" />
    </svg>
  );
}
