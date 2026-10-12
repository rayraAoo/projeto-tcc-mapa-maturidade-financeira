import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { cadastrar, entrarNaConta, obterTermos } from "../api/servicos.js";
import { useAuth } from "../auth/AuthContext.jsx";
import { useDados } from "../hooks/useDados.js";
import { calcularIdade, hojeIso } from "../utils/idade.js";
import { mensagemDeErro } from "../utils/erros.js";
import Layout from "../components/Layout.jsx";
import { MensagemErro } from "../components/Estados.jsx";

export default function Cadastro() {
  const navegar = useNavigate();
  const { entrar } = useAuth();
  const { dados: termos } = useDados(() => obterTermos());

  const [form, setForm] = useState({ nome: "", email: "", senha: "", dataNascimento: "" });
  const [ciencia, setCiencia] = useState(false);
  const [erro, setErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  // Os limites vêm do backend; os números abaixo só valem enquanto os termos não carregam.
  const idadeMinima = termos?.idadeMinima ?? 12;
  const maioridade = termos?.maioridade ?? 18;

  const idade = calcularIdade(form.dataNascimento);
  const abaixoDoMinimo = idade !== null && idade < idadeMinima;
  const precisaCiencia = idade !== null && idade >= idadeMinima && idade < maioridade;

  function alterar(evento) {
    setForm({ ...form, [evento.target.name]: evento.target.value });
  }

  async function enviar(evento) {
    evento.preventDefault();
    setErro("");

    if (abaixoDoMinimo) {
      setErro(`A plataforma é indicada para pessoas com ${idadeMinima} anos ou mais.`);
      return;
    }
    if (precisaCiencia && !ciencia) {
      setErro("Para menores de 18 anos, marque a declaração de ciência dos pais ou responsáveis.");
      return;
    }

    setEnviando(true);
    try {
      await cadastrar({
        ...form,
        confirmaCienciaResponsavel: precisaCiencia ? ciencia : undefined,
      });
      // Conta criada: já entra automaticamente e segue para o questionário.
      const sessao = await entrarNaConta(form.email, form.senha);
      entrar({ token: sessao.token, nome: sessao.nome, email: sessao.email });
      navegar("/questionario");
    } catch (e) {
      setErro(mensagemDeErro(e));
    } finally {
      setEnviando(false);
    }
  }

  return (
    <Layout comNavegacao={false}>
      <p className="marca">Mapa da Maturidade Financeira</p>

      <h1 className="espaco-topo">Crie sua conta</h1>
      <p className="subtitulo">Comece sua jornada rumo à maturidade financeira. Leva menos de um minuto.</p>

      <form className="espaco-topo-g" onSubmit={enviar} noValidate>
        <div className="campo">
          <label htmlFor="nome">Nome completo</label>
          <input id="nome" name="nome" type="text" value={form.nome} onChange={alterar} placeholder="Como podemos te chamar?" required />
        </div>

        <div className="campo">
          <label htmlFor="email">E-mail</label>
          <input id="email" name="email" type="email" value={form.email} onChange={alterar} placeholder="voce@exemplo.com" required />
        </div>

        <div className="campo">
          <label htmlFor="senha">Senha</label>
          <input id="senha" name="senha" type="password" value={form.senha} onChange={alterar} placeholder="Mínimo de 8 caracteres" required />
        </div>

        <div className="campo">
          <label htmlFor="dataNascimento">Data de nascimento</label>
          <input
            id="dataNascimento"
            name="dataNascimento"
            type="date"
            value={form.dataNascimento}
            onChange={alterar}
            max={hojeIso()}
            required
          />
          <span className="dica">Indicado para {termos?.faixaEtariaIndicada || "12 anos ou mais"}.</span>
        </div>

        {abaixoDoMinimo && (
          <div className="alerta alerta-erro" role="alert">
            A plataforma é indicada para pessoas com {idadeMinima} anos ou mais. Por enquanto, não é possível criar a conta.
          </div>
        )}

        {precisaCiencia && (
          <label className="caixa-marcar">
            <input type="checkbox" checked={ciencia} onChange={(e) => setCiencia(e.target.checked)} />
            <span>
              {termos?.textoCiencia ||
                "Declaro que sou menor de 18 anos e que meus pais ou responsáveis estão cientes de que utilizo esta plataforma."}{" "}
              <Link to="/responsaveis">Saiba mais</Link>
            </span>
          </label>
        )}

        <MensagemErro texto={erro} />

        <button type="submit" className="btn btn-primario" disabled={enviando || abaixoDoMinimo}>
          {enviando ? "Criando conta..." : "Criar conta"}
        </button>
      </form>

      <p className="centralizado nota espaco-topo-g">
        Já tem conta? <Link to="/login">Entrar</Link>
      </p>
    </Layout>
  );
}
