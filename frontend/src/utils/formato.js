const formatadorMoeda = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });

export function moeda(valor) {
  return formatadorMoeda.format(valor ?? 0);
}

export function dataHora(iso) {
  if (!iso) return "";
  return new Date(iso).toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" });
}
