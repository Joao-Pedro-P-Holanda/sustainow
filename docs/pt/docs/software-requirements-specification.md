## Elicitação de requisitos

A elicitação de requisitos foi realizada, principalmente, com conversas com pessoas envolvidas na área de sustentabilidade, juntamente de um questionário de elicitação que, no momento da última alteração neste documento (24/11/2024) contava com 11 respostas.

## Histórias de Usuário

As histórias de usuário estão definidas com o formato 3C:

- **Cartão** - quem deseja a funcionalidade e por quê
- **Conversa** - detalhamento do motivo
- **Confirmação** - verificações que devem ser feitas para garantir que a história de usuário foi cumprida

### 1. Cálculo de Consumo

Funcionalidade principal da aplicação, relacionada aos fluxos onde o usuário responde questões para ter o seu consumo estimado e os fluxos onde ele informa seu consumo real que depois pode ser comparado.

#### 1.1 Estimar consumo

Estimativas do consumo são realizadas por meio de respostas em formulários, um mesmo formulário pode ser respondido várias vezes durante o mês, mas a última resposta desse mês é que deve ser considerada, nas estimativas de consumo devem haver diferentes questões que o usuário pode responder:

- **Questões de escolha única:** questões com duas ou mais alternativas onde só uma pode ser selecionada
- **Questões de múltipla escolha:** duas ou mais alternativas e mais de uma pode ser selecionada
- **Questões de valor numérico:** usuário pode responder com qualquer valor numérico aceito
- **Questões com campos aninhados:** questões onde o usuário pode inserir um valor geral para um grupo específico ou detalhar o valor individual de cada item nesse grupo (e.g. consumo médio de todas as lâmpadas ou o consumo de cada uma)

##### Estimar gasto de energia

  - **Cartão** usuário, eu quero estimar meu consumo com base nas informações fornecidas sobre meus eletrodomésticos e consumo para prever o valor na próxima conta.
  - **Conversa:** 81,8% das pessoas que responderam o formulário acompanham o valor da sua conta de energia e 27,3% destes também acompanham o consumo em kWh
  - **Confirmação**
      - [ ] Existe uma tela para iniciar o preenchimento das questões do formulário de consumo de energia
      - [ ] O usuário possui alguma forma de informar o preço do kWh na sua região durante o formulário
      - [ ] O usuário possui alguma forma de informar o consumo de cada grupo de eletrodomésticos, podendo informar em uma questão com campos aninhados: 1) o tempo de uso médio, quantidade e potência de todos os eletrodomésticos de um grupo; 2) o tempo de uso e a potência em Watts de cada eletrodoméstico separadamente
      - [ ] Durante a edição da potência ou tempo de uso de item de um grupo; a potência média e o tempo de uso médio de todos os itens não deve ser editável e deve atualizar de acordo com a média aritmética de cada valor.
      - [ ] Atualizar a potência média ou tempo de uso do grupo inteiro deve acrescentar ou remover do valor de cada item proporcionalmente para manter a média necessária
      - [ ] O usuário é informado do resultado mostrando seu gasto, em kWh, o valor estimado da sua conta e o percentil do seu gasto comparado com todos os outros usuários

      - **Desejável**
      - [ ] Repetir o formulário em um mesmo mês funciona como edição, pré-selecionando todas as questões para que o usuário possa apenas alterar alguma informação.
      - [ ] Deve ser possível reutilizar questões respondidas em um formulário de uma *área* diferente que também estejam presentes no formulário de energia (e.g. presença de energia renovável).

##### Estimar gasto de água

  - **Cartão** usuário, eu quero estimar meu consumo mensal de água em m³, com base nos meus hábitos de consumo, para prever o custo.
  - **Conversa:** O sistema exibe o consumo mensal de água e a projeção de custo.
  - **Confirmação:** O usuário visualiza o consumo estimado mensal de água.
      - [ ] Existe uma tela para iniciar o preenchimento das questões do formulário de consumo de água
      - [ ] O usuário possui alguma forma de informar o preço do m3 na sua região durante o formulário
      - [ ] O usuário responde a um grupo de questões e avança para o próximo grupo de questões
      - [ ] O usuário pode voltar a um grupo de questões anterior e alterar suas respostas
      - [ ] O usuário é informado do resultado mostrando seu gasto em m3, o valor estimado da sua conta e o percentil do seu gasto comparado com todos os outros usuários
      - **Desejável**
      - [ ] Repetir o formulário em um mesmo mês funciona como edição, pré-selecionando todas as questões para que o usuário possa apenas alterar alguma informação.
      - [ ] Questões que dependem de um valor específico em questões anteriores são descartadas se a alternativa não for um dos valores esperados (e.g. Nenhuma pergunta sobre o tamanho da máquina de lavar caso o usuário não possua uma)
      - [ ] Deve ser possível reutilizar questões respondidas em um formulário de uma *área* diferente que também estejam presentes no formulário de energia (e.g. presença de lava-louças).

##### Estimar minha pegada de carbono

  - **Cartão** usuário, eu quero estimar minha pegada de carbono para saber quanto carbono gerei em determinado período.
  - **Conversa:** O sistema faz uma estimativa da emissão de carbono do usuário.
  - **Confirmação**
      - [ ] Existe uma tela para iniciar o preenchimento de questões do formulário da pegada de carbono
      - [ ] O usuário responde a um grupo de questões e avança para o próximo grupo de questões
      - [ ] O usuário pode voltar a um grupo de questões anterior e alterar suas respostas
      - [ ] O usuário é informado do resultado mostrando sua pegada de carbono estimada em kg e o percentil da sua pegada de carbono comparada aos outros usuários
      - **Desejável**
      - [ ] Repetir o formulário em um mesmo mês funciona como edição, pré-selecionando todas as questões para que o usuário possa apenas alterar alguma informação.
      - [ ] Questões que dependem de um valor específico em questões anteriores são descartadas se a alternativa não for um dos valores esperados (e.g. nenhuma pergunta sobre o tipo de combustível do carro é feita se o usuário não possui um carro)
      - [ ] Deve ser possível reutilizar questões respondidas em um formulário de uma *área* diferente que também estejam presentes no formulário de energia (e.g. presença de lava-louças).

#### 1.2 Inserir consumo real

##### Consumo real de energia

  - **Cartão** usuário, eu quero visualizar meu consumo real de energia para saber quanto gastei ao longo do mês e estimar a conta.
  - **Conversa:** 54,5% dos usuários que responderam acompanham apenas o valor da conta, e outros 27,3% também acompanham o consumo em kWh, então um formulário com apenas dois campos beneficiaria os dois grupos para saberem se estão consumindo de acordo com o que foi estimado anteriormente nos formulários de estimativa
  - **Confirmação:**  
        - [ ] Existe uma página onde o usuário consegue adicionar o valor da sua conta de energia e o consumo em kWh a cada mês
        - [ ] Após adicionar um valor e consumo pela primeira vez no mês, qualquer novo envio nesse mês será uma atualização dos dados
        - [ ] Na página utilizada para inserir as informações, o valor do kWh está destacado com uma opção de ser alterado individualmente

##### Consumo real de água

  - **Cartão** usuário, eu quero visualizar meu consumo real de água para saber quanto gastei ao longo do mês e estimar a conta.
  - **Conversa:** 36,4% dos usuários que responderam acompanham apenas o valor da conta, e outros 18,2% também acompanham o consumo em m3, então um formulário com apenas dois campos beneficiaria os dois grupos para saberem se estão consumindo de acordo com o que foi estimado anteriormente nos formulários de estimativa, ainda que essa funcionalidade seja mais relevante para o consumo real de energia
  - **Confirmação:**
      - [ ] Existe uma página onde o usuário consegue adicionar o valor da sua conta de água e o consumo em m3 cúbicos a cada mês
      - [ ] Após adicionar um valor e consumo pela primeira vez no mês, qualquer novo envio nesse mês será uma atualização dos dados
      - [ ] Na página utilizada para inserir as informações, o valor do m3 está destacado com uma opção de ser alterado individualmente

---

### 2. Histórico de Consumo

#### Histórico de pegada de carbono estimada

  - **Cartão** usuário, eu quero visualizar meu histórico de estimativa de pegada de carbono para saber aproximadamente quanto de carbono emiti ao longo do tempo.
  - **Conversa:** O sistema mostra o histórico de estimativas de pegada de carbono.
  - **Confirmação**
      - [ ] Existe uma tela onde o usuário pode visualizar o seu histórico de pegadas de carbono medidas
      - [ ] Existem filtros para mostrar apenas valores entre datas (mês e ano) iniciais e finais
      - [ ] A visualização de cada item individual do histórico mostra a quantidade em Kg/mês estimada
      - [ ] Caso o usuário não tenha preenchido nenhum formulário até o momento, a tela mostra uma opção de navegar para um novo formulário
      - **Desejável**
      - [ ] A visualização de um item individual possui uma opção de mostrar as respostas do formulário nesse mês

#### Histórico de consumo de água

  - **Cartão** usuário, eu quero visualizar meu histórico de consumo de água para saber em qual período gastei mais água.
  - **Conversa:** O sistema exibe o histórico de consumo de água em uma linha do tempo ou gráfico.
  - **Confirmação:** O usuário visualiza o histórico de consumo de água.
      - [ ]  Existe uma tela onde o usuário pode visualizar o seu histórico de consumos de água estimados e informados
      - [ ] Existem filtros para mostrar apenas valores entre datas (mês e ano) iniciais e finais
      - [ ]  A visualização de cada item individual mostra o consumo esperado e real, em m3, e o preço do m3 nesse mês.
      - [ ] Caso o usuário não tenha preenchido nenhum formulário até o momento, a tela mostra uma opção de navegar para um novo formulário
      - **Desejável**
      - [ ] A visualização de um item individual possui uma opção de mostrar as respostas do formulário nesse mês

#### Histórico de consumo de energia

  - **Cartão** usuário, eu quero visualizar meu histórico de consumo de energia para saber em qual período gastei mais energia.
  - **Conversa:** O sistema exibe o histórico de consumo de energia com detalhamento mensal ou semanal.
  - **Confirmação:** O usuário visualiza o histórico de consumo de energia.
      - [ ]  Existe uma tela onde o usuário pode visualizar o seu histórico de consumos de energia estimados e informados
      - [ ] Existem filtros para mostrar apenas valores entre datas (mês e ano) iniciais e finais
      - [ ] A visualização de cada item individual mostra o consumo esperado e real, em kWh, e o preço do kWh nesse mês
      - [ ] Caso o usuário não tenha preenchido nenhum formulário até o momento, a tela mostra uma opção de navegar para um novo formulário
      - **Desejável**
      - [ ] A visualização de um item individual possui uma opção de mostrar os detalhes de cada eletrodoméstico informado

---

### 3. Comparação

#### Comparar consumo com outros usuários

  - **Cartão** usuário, eu quero comparar meu consumo com o de outros usuários para saber o quanto posso economizar.
  - **Conversa:** Nenhuma das alternativas, analisadas até o momento (Pawprint, Klima, Decarbon, theplanetapp e CERO) possui uma forma de comparar o consumo em relação a outros usuários detalhamente, somente mostrando ao fim do formulário. A exceção é o app Cool the Globe que sempre mostra as economias e o consumo médio de todos os usuários.
  - **Confirmação**
      - [ ] Existe uma tela onde o usuário pode visualizar detalhes do seu consumo real comparado ao de outros usuários, mostrando o percentil ocupado pelo consumo dele
      - [ ] Os consumos reais de água e energia e a pegada de carbono estimada são mostrados separadamente
      - [ ] Existem filtros para que o usuário selecione um período específico de comparação
      - [ ] Uma das informações mostradas é a redução/aumento de consumo pelos usuários em comparação a um mês anterior
      - **Desejáveis**
      - [ ] O usuário consegue filtrar por outros usuários de alguma localização definida, ou selecionar uma área específica para comparar

---

### 4. Rotinas de Tarefas

#### Gerenciar Rotina

  - **Cartão** como usuário, eu quero registrar minha rotina para estabelecer metas de economia de energia, água e redução da pegada de carbono para me organizar melhor.
  - **Conversa:** 63,7% dos usuários responderam que utilizariam um aplicativo que pudessem criar rotinas de atividades para reduzir consumo de energia e água com certa frequência ou frequentemente.
  - **Confirmação:**
        - [ ] Existe uma tela onde o usuário pode gerenciar sua rotina ou criar uma nova caso ainda não tenha
        - [ ] O usuário pode estabelecer uma meta de redução do seu consumo em um período da rotina (mês inteiro ou para cada semana)
        - **Desejável**
        - [ ] O usuário consegue controlar a frequência e horário em que recebe as notificações de tarefas

#### Gerenciar Tarefas das rotinas

  - **Cartão** como usuário, eu quero registrar ações dentro das rotinas para organizar metas individuais e coletivas.
  - **Conversa:** necessário para efetivamente implementar a rotina é necessário que exista uma forma de controlar as tarefas dela
  - **Confirmação:**
        - [ ] O usuário consegue adicionar novas tarefas à sua rotina, incluindo os dias da semana em que ela será realizada, e opcionalmente o horário.
        - [ ] O usuário consegue remover tarefas da sua rotina

### Relatório da Rotina

  - **Cartão** como usuário, eu quero poder acessar um relatório mensal da minha rotina para poder ver como evolui no meu consumo em cada semana e quantas atividades consegui realizar.
  - **Conversa** entre os usuários que responderam que utilizariam um sistema com rotina de atividades, 90% respondeu que uma das funcionalidades desejadas era um quadro com a evolução do consumo e 60% consideravam a comparação com metas estabelecidas pelo usuário como algo importante
  - **Comparação**
        - [ ] Existe uma tela onde o usuário pode visualizar seu histórico de tarefas em um dado período da rotina, agrupando por mês ou ano, mostrando as atividades que foram concluídas, as atividades que ficaram em aberto e a meta do usuário para esse período
        - [ ] Na visualização por mês cada semana deve poder ser visualizada em detalhes individualmente
        - [ ] Tarefas já removidas da rotina devem continuar acessíveis no histórico

---

### 5. Dicas

#### Dicas de economia

  - **Cartão** como usuário, eu quero visualizar dicas de economia no aplicativo para ajudar a atingir minhas metas de economia.
  - **Conversa:** 36,4% dos usuários que responderam, relataram que as informações sobre redução de consumo são escassas ou difíceis de encontrar, outros 45,5% relataram que existe muita informação, mas difícil de ser encontrada.
  - **Confirmação:**
      - [ ] Dicas são fornecidas ao final de cada formulário e possuem uma opção de serem adicionadas à rotina
      - **Desejável**
      - [ ] O usuário recebe dicas personalizadas de acordo com suas respostas e atividades da rotina na home page e na página principal das rotinas

---

### 6. Ações Conjuntas

#### Registrar e manter ações conjuntas

  - **Cartão** usuário, eu quero criar ações conjuntas com outros usuários para colaborar em iniciativas de economia e sustentabilidade.
  - **Conversa:** O sistema guia o usuário para convidar outros usuários para uma ação conjunta.
  - **Confirmação:**
      - [ ] Existe uma tela onde um usuário cria uma ação conjunta com imagem de capa, mome, descrição, objetivo, data e hora de início, data e hora de encerramento , a disponibilidade para novos participantes (booleano) e o número máximo de participantes
      - [ ] Existe uma tela para visualizar e editar os detalhes de uma ação conjunta já criada
      - [ ] Existe uma tela para pesquisar todas as ações conjuntas criadas pelo usuário, filtrando por nome, data de início e fim e se estão concluídas ou não
      - [ ] Somente o usuário que criou a ação com consegue editar as informações delas
      - **Desejável**
      - [ ] O criador da ação conjunta pode enviar convites para outros usuários

#### Juntar-se a ação conjunta

  - **Cartão** usuário, eu quero me juntar a uma ação conjunta para contribuir com práticas de redução de consumo.
  - **Conversa:** O sistema envia uma solicitação para o criador autorizar a entrada do usuário como colaborador.
  - **Confirmação:**
      - [ ] Existe uma tela para pesquisar todas as ações conjuntas, filtrando por nome, data de início e fim e se estão concluídas ou não
      - [ ] Um usuário pode solicitar adesão em uma ação conjunta que esteja aceitando novos participantes
      - **Desejável**
      - [ ] Um usuário convidado é notificado por email ou por uma notificação do celular
      - [ ] Um usuário pode aceitar convites enviados pelo criador de uma ação
      - [ ] Um usuário pode fazer comentários de texto e mídia em uma ação conjunta

---

### 7. Gráficos

#### Gráfico de pegada de carbono estimada

  - **Cartão** usuário, eu quero visualizar um gráfico com minhas medições da pegada de carbono ao longo do tempo para que eu possa analisar minha evolução ao longo de um período de tempo
  - **Conversa:** a aplicação gira em torno principalmente da pegada de carbono e 63,6% das pessoas que responderam o questionário consideram útil o acompanhamento do seu consumo
  - ***Confirmação:**
      - [ ] Um gráfico de linhas com a quantidade de carbono, em kg, estimada é acessível para o usuário dentro dos detalhes da pegada de carbono
      - [ ] O usuário possui opções para filtrar a data e mês de início e fim utilizadas no gráfico
      - [ ] O gráfico atualiza automaticamente ao selecionar novas datas de filtragem
      - [ ] Existe uma opção de reconfigurar as datas para o valor padrão: fim no mês atual e início 12 meses atrás ou no primeiro mês que o usuário respondeu caso não existam dados mais antigos

#### Gráfico de consumo de energia esperado e real

  - **Cartão** usuário, eu quero visualizar um gráfico com minhas informações de consumo de energia para que eu possa analisar detalhadamente meu consumo.
  - **Conversa:** Todos os usuários que responderam as ações realizadas na verificação do consumo de energia incluíram o acompanhamento ao longo do tempo
  - **Confirmação**
      - [ ] Um gráfico de linhas com o consumo em um período determinado de tempo é mostrado, com uma linha sendo o consumo real e outra sendo o consumo esperado
      - [ ] Um gráfico de pizza com a participação de cada eletrodoméstico no consumo é mostrado
      - [ ] O usuário consegue selecionar novas datas iniciais e finais para o período de tempo
      - [ ] O gráfico atualiza automaticamente ao selecionar novas datas de filtragem
      - [ ] A data final do consumo real é limitada pela data atual, datas posteriores mostram apenas o consumo estimado
      - [ ] A data inicial é a menor data entre o primeiro consumo real e o primeiro consumo esperado
      - [ ] Existe uma opção de reconfigurar as datas para o valor padrão: fim no mês atual e início 12 meses atrás ou no primeiro mês que o usuário respondeu caso não existam dados mais antigos

---

#### Gráfico de consumo de água esperado e real

  - **Cartão** usuário, eu quero visualizar um gráfico com minhas informações de consumo de água para que eu possa analisar detalhadamente meu consumo.
  - **Conversa:** O sistema exibe um gráfico com o histórico de consumo de água.  
  - **Confirmação**
      - [ ] Um gráfico de linhas com a quantidade de água estimada e real, em m3, é acessível para o usuário dentro dos detalhes do consumo de água, com uma linha sendo o consumo real e outra sendo o consumo esperado
      - [ ] O usuário possui opções para filtrar a data e mês de início e fim utilizadas no gráfico
      - [ ] A data final do consumo real é limitada pela data atual, datas posteriores mostram apenas o consumo estimado
      - [ ] A data inicial é a menor data entre o primeiro consumo real e o primeiro consumo esperado
      - [ ] O gráfico atualiza automaticamente ao selecionar novas datas de filtragem
      - [ ] Existe uma opção de reconfigurar as datas para o valor padrão: fim no mês atual e início 12 meses atrás ou no primeiro mês que o usuário respondeu caso não existam dados mais antigos
