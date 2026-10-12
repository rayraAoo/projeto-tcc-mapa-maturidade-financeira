# Avaliação de riscos — uso por adolescentes (ECA Digital e LGPD)

> Documento técnico da equipe. **Não é parecer jurídico.** A leitura das normas deve ser validada com a orientadora.
> Fontes consultadas: Lei nº 15.211/2025 (ECA Digital), Decreto nº 12.880/2026 e a matéria do Ministério dos Direitos
> Humanos e da Cidadania sobre a lei.

## Público e enquadramento

A plataforma é indicada para **12 anos ou mais** (adolescentes e adultos). Ao aceitar adolescentes, ela deve ser tratada como
serviço de acesso provável por menores de 18 anos. Não é rede social (não há compartilhamento entre usuários nem conexões) e
não oferece conteúdo ou produto proibido para menores (armas, álcool, tabaco, apostas, conteúdo pornográfico etc.).

## Riscos e medidas

| Risco | Medida adotada | Referência |
|---|---|---|
| Faixa etária não informada | Faixa indicada (12+) exibida no cadastro e nos termos, via `GET /api/publico/termos` | Lei, art. 8º, V; Decreto, art. 12, §4º |
| Menor usando sem conhecimento dos responsáveis | Declaração de ciência obrigatória de 12 a 17 anos, registrada com data, hora e versão do texto | Lei, art. 3º, parágrafo único |
| Responsáveis sem informação | Página pública "Para pais e responsáveis" (texto servido pela API): dados coletados, finalidade, IA, exclusão | Lei, art. 16 |
| Privacidade menos protetiva por padrão | Coleta mínima; sem publicidade; sem compartilhamento entre usuários; IA só com autorização a cada uso | Lei, art. 7º |
| Envio de dados a terceiro (IA) além do necessário | Prompt **sem nome e sem e-mail**; autorização explícita a cada geração; contingência sem IA | Lei, art. 16, parágrafo único |
| Perfilamento | O questionário classifica o usuário apenas para personalizar a trilha; não há publicidade nem uso comercial | Lei, arts. 2º, V, 22 e 26 |
| Estímulo a uso excessivo | XP por aprendizado (módulos, acertos, notas), sem recompensa por tempo de uso, sequência diária ou notificações | Decreto, art. 9º |
| Práticas manipulativas | Exclusão de conta simples e direta; sem urgência artificial | Decreto, art. 10 |
| Conteúdo gerado por IA | Rótulo "gerado por inteligência artificial" em toda resposta; regra de não recomendar produtos | Decreto, art. 11 |
| Recomendação financeira indevida | Conteúdo estático revisado para não indicar produto; prompt proíbe recomendação | Escopo do projeto (RNF06) |

## Limitações assumidas

1. **A declaração de ciência é autodeclarada**; a plataforma não verifica parentesco nem idade.
2. **Não há conta de responsável nem controles parentais** (limite de tempo, restrições). A lei os prevê para serviços de acesso
   provável por menores (arts. 17 e 18), com obrigações graduadas pela natureza e porte do serviço (art. 39). Fica como trabalho futuro.
3. **Plano gratuito do Gemini:** o conteúdo enviado pode ser usado pelo provedor para melhorar seus produtos. Em uma versão pública,
   usar plano pago ou outro provedor.
4. Sem verificação de e-mail e sem limite de tentativas de login.

## Trabalhos futuros

Contas de responsável com vínculo verificado e controles parentais; verificação de idade proporcional ao risco;
relatório de impacto completo e publicação de versão resumida (Decreto, art. 47); política de privacidade formal.
