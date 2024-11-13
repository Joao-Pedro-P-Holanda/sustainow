# Histórias de Usuário

---

## 1. Cálculo de Consumo

### Estimar gasto de energia
  - **Como** usuário, eu quero estimar meu consumo com base nas informações fornecidas sobre meus eletrodomésticos e consumo para prever o valor na próxima conta.
  - **Conversa:** O sistema exibe o consumo mensal em kWh e a estimativa de custo.
  - **Confirmação:** O usuário visualiza o gráfico com o consumo estimado.

### Estimar gasto de água
  - **Como** usuário, eu quero estimar meu consumo mensal de água em m³, com base nos meus hábitos de consumo, para prever o custo.
  - **Conversa:** O sistema exibe o consumo mensal de água e a projeção de custo.
  - **Confirmação:** O usuário visualiza o consumo estimado mensal de água.

### Estimar minha pegada de carbono
  - **Como** usuário, eu quero estimar minha pegada de carbono para saber quanto carbono gerei em determinado período.
  - **Conversa:** O sistema faz uma estimativa da emissão de carbono do usuário.
  - **Confirmação:** O usuário visualiza a estimativa de sua pegada de carbono.


### Consumo real de água 
  - **Como** usuário, eu quero visualizar meu consumo real de água para saber quanto gastei ao longo do mês e estimar a conta.
  - **Conversa:** O sistema exibe o consumo mensal de água.
  - **Confirmação:** O usuário visualiza o consumo mensal de água em m³.

  
### Consumo real de energia
  - **Como** usuário, eu quero visualizar meu consumo real de energia para saber quanto gastei ao longo do mês e estimar a conta.
  - **Conversa:** O sistema exibe o consumo mensal de energia.
  - **Confirmação:** O usuário visualiza o consumo mensal de energia em kWh.

---

## 2. Histórico de Consumo


### Histórico de pegada de carbono estimada
  - **Como** usuário, eu quero visualizar meu histórico de estimativa de pegada de carbono para saber aproximadamente quanto de carbono emiti ao longo do tempo.
  - **Conversa:** O sistema mostra o histórico de estimativas de pegada de carbono.
  - **Confirmação:** O usuário visualiza seu histórico de estimativas.

### Histórico de consumo de água
  - **Como** usuário, eu quero visualizar meu histórico de consumo de água para saber em qual período gastei mais água.
  - **Conversa:** O sistema exibe o histórico de consumo de água em uma linha do tempo ou gráfico.
  - **Confirmação:** O usuário visualiza o histórico de consumo de água.

### Histórico de consumo de energia
  - **Como** usuário, eu quero visualizar meu histórico de consumo de energia para saber em qual período gastei mais energia.
  - **Conversa:** O sistema exibe o histórico de consumo de energia com detalhamento mensal ou semanal.
  - **Confirmação:** O usuário visualiza o histórico de consumo de energia.

---

## 3. Comparação

### Comparar consumo com outros usuários
  - **Como** usuário, eu quero comparar meu consumo com o de outros usuários para saber o quanto posso economizar.
  - **Conversa:** O sistema exibe uma comparação visual do consumo do usuário com o de outros usuários.
  - **Confirmação:** O usuário visualiza gráficos comparativos de consumo.

### Comparar consumo esperado e consumo real
  - **Como** usuário, eu quero visualizar meu consumo real de energia comparado com o consumo esperado para verificar se cumpri minha meta de economia.
  - **Conversa:** O sistema exibe a comparação entre consumo real e esperado em gráficos.
  - **Confirmação:** O usuário visualiza o gráfico de consumo real e esperado.

---

## 4. Rotinas de Tarefas

### Rotinas
  - **Como** usuário, eu quero registrar rotinas para estabelecer metas de economia de energia, água e redução da pegada de carbono para me organizar melhor.
  - **Conversa:** O sistema permite que o usuário defina uma rotina e adicione ações.
  - **Confirmação:** O sistema exibe "Rotina registrada com sucesso".

### Ações dentro das rotinas
  - **Como** usuário, eu quero registrar ações dentro das rotinas para organizar metas individuais e coletivas.
  - **Conversa:** O sistema pede que o usuário defina uma ação e sua data de realização.
  - **Confirmação:** O sistema exibe "Ação registrada na rotina com sucesso".

---

## 5. Dicas

### Dicas de economia
  - **Como** usuário, eu quero visualizar dicas de economia no aplicativo para ajudar a atingir minhas metas de economia.
  - **Conversa:** O sistema apresenta dicas baseadas no consumo e nas metas do usuário.
  - **Confirmação:** O usuário visualiza dicas exibidas em cards.

---

## 6. Ações Conjuntas

### Registrar ações conjuntas
  - **Como** usuário, eu quero criar ações conjuntas com outros usuários para colaborar em iniciativas de economia e sustentabilidade.
  - **Conversa:** O sistema guia o usuário para convidar outros usuários para uma ação conjunta.
  - **Confirmação:** O sistema exibe "Ação conjunta registrada com sucesso".

### Juntar-se a ação conjunta
  - **Como** usuário, eu quero me juntar a uma ação conjunta para contribuir com práticas de redução de consumo.
  - **Conversa:** O sistema envia uma solicitação para o criador autorizar a entrada do usuário como colaborador.
  - **Confirmação:** O usuário convidado passa a visualizar a ação conjunta.

---

## 7. Gráficos

### Gráfico de consumo de energia esperado e real
**Como** usuário,  
**Eu quero** visualizar um gráfico com minhas informações de consumo de energia  
**Para que** eu possa analisar detalhadamente meu consumo.

**Conversa:** O sistema exibe um gráfico de consumo ao longo de períodos selecionados pelo usuário.  
**Confirmação:**

- [ ] Um gráfico de linhas com o consumo em um período determinado de tempo é mostrado, com uma linha sendo o consumo real e outra sendo o consumo esperado
- [ ] O usuário consegue selecionar novas datas iniciais e finais para o período de tempo
- [ ] A data final do consumo real é limitada pela data atual, datas posteriores mostram apenas o consumo estimado
- [ ] A data inicial é a menor data entre o primeiro consumo real e o primeiro consumo esperado

---

### Gráfico de consumo de água esperado e real
**Como** usuário,  
**Eu quero** visualizar um gráfico com minhas informações de consumo de água  
**Para que** eu possa analisar detalhadamente meu consumo.

**Conversa:** O sistema exibe um gráfico com o histórico de consumo de água.  
**Confirmação:** O usuário deve ser capaz de visualizar gráfico de consumo de água.
