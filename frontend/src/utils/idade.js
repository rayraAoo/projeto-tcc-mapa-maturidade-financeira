// Calcula a idade completa a partir de uma data no formato AAAA-MM-DD (valor do input type="date").
export function calcularIdade(dataIso) {
  if (!dataIso) return null;

  const [ano, mes, dia] = dataIso.split("-").map(Number);
  if (!ano || !mes || !dia) return null;

  const hoje = new Date();
  let idade = hoje.getFullYear() - ano;

  const mesAtual = hoje.getMonth() + 1;
  const jaFezAniversario = mesAtual > mes || (mesAtual === mes && hoje.getDate() >= dia);
  if (!jaFezAniversario) idade -= 1;

  return idade;
}

export function hojeIso() {
  const hoje = new Date();
  const mes = String(hoje.getMonth() + 1).padStart(2, "0");
  const dia = String(hoje.getDate()).padStart(2, "0");
  return `${hoje.getFullYear()}-${mes}-${dia}`;
}
