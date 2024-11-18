# Arquitetura e tecnologias

A arquitetura da aplicação é similar a de outros projetos que utilizam uma estrutura de diretórios baseados em Clean Architecture, mas a arquitetura seguida é a Model-View-ViewModel (MVVM), com distinções em subcamadas para os modelos. No lado do servidor, a arquitetura é monolítica, com um único banco de dados e acesso centralizado aos serviços de autenticação, armazenamento e etc.

Já as tecnologias utilizadas foram escolhidas com base nas seguintes restrições de domínio e de recursos:

- **Tempo limitado**: por isso utilizamos bibliotecas de interface de usuário com componentes e Design System já estruturados, e também utilizamos plataformas de BaaS (Back-End as a Service) para maior foco no desenvolvimento da aplicação móvel.
- **Relacionamento complexo entre entidades**: com agrupamento de múltiplas entidades por uma área temática, além da presença de formulários reutilizáveis em diferentes contextos, o que faz necessário que uma mesma alternativa de uma questão tenha diferentes valores para cada contexto.
- **Presença de dados que necessitam de atualizações assíncronas**: entre as funcionalidades descritas nas [Histórias de Usuário](software-requirements-specification.md) estão dicas personalizadas para usuários e rotinas semanais. Pensando na escalabilidade, uso em diferentes plataformas e experiência de usuário, existe a necessidade que atualizações nesses dados ocorram de forma assíncrona no lado do servidor.

#### Agrupamento de Funcionalidades

As principais funcionalidades da aplicação, definidas nas [Histórias de Usuário](software-requirements-specification.md) se dividem nos seguintes temas:

```mermaid
graph LR

subgraph Comparacao[Comparação]
    c1[Comparar consumo esperado com real] 
    c2[Comparar consumo com o de outros usuários]
end


subgraph Consumo
    
    c3[Estimar pegada de carbono]
    c4[Estimar consumo de energia]
    c5[Estimar consumo de água]
    c6[Controlar o consumo real de energia] --> c1
    c6 --> c2
    c7[Controlar o consumo real de água] --> c1
    c7 --> c2
end
subgraph Histórico
    h1[Gráficos de consumo de energia]
    h2[Gráfico de pegada de carbono]
    h3[Gráfico de consumo de água]
    h4[Histórico de consumo de água]
    h5[Histórico de consumo de energia]
    h6[Histórico de consumo de água]

    h1 --> h4
    h2 --> h5
    h3 --> h6
end

subgraph Rotina
    r[Controle de rotina]
    t[Tarefas semanais]

    t --> r
end

subgraph "Ação Coletiva"
    g[Gerenciar ações]
    p[Participar de ações]
end

subgraph Dicas
    d[Dicas personalizadas para usuários]
end
```

### Back-End

```mermaid
graph LR
    A["API REST"] --Armazena e manipula tabelas-->  B
    A --Adiciona e manipula imagens--> C
    D --Provê credenciais--> A

    subgraph Supabase
        direction LR

        C --Guarda referências às imagens--> B[("Database")]
        D --Provê permissões de linhas e colunas--> B
        C@{ shape: lin-cyl, label: "Supabase Storage" }
        D["Supabase Auth"]
    end
```

### Banco de dados

```mermaid
erDiagram
    user["Usuário"] {
        int id PK
        string name
        string email
        string password
    }
    action["Ação"] {
        int id PK
        datetime start_date
        datetime end_date
        string objective
    }
    action_media["Mídia de ação"] {
        int id PK
        blob file "Armazenado em um serviço diferente"
    }

    form["Formulário"] {
        int id PK
        date answered_date 
        blob file "Armazenado em um serviço diferente"
    }
    area["Área"] {
        int id PK
        string nome UK
    }
    question["Questão"] {
        int id PK
        string name
        string description
        blob file "Armazenado em um serviço diferente"
    }
    dependent_question["Questão Dependente"] {
        int id PK
    }
    question_value["Valor da Questão"] {
        int id pk
        enum unit 
        float quantity
        datetime time_period
    }
    question_answer["Resposta da Questão"] {
        int id pk
    }
    consumption["Consumo"] {
        int id PK
        float quantity
        string unit
        date measurement_date 
        datetime time_period
    }
    routine_meta["Dados da rotina"] {
        int id PK
    }
    routine["Rotina"] {
        datetime start_date
        datetime end_date
    }
    activity["Atividade"] {
        int id PK
        enum frequency 
        enum weekday
    }
    activity_meta["Dados da atividade"] {
        int id PK
        string name
        string description
    }
    tip["Dica"] {
        int id PK
        string name 
        string description
    }

    user ||--o{ action : Cria
    user }|--o{ action : Participa
    action ||--o{ action_media: Possui

    area ||--|{ consumption : "Relacionada a"
    consumption ||--|| form : "Calculado por" 
    user || --o{ consumption : "Mediu"
    question }|--|| area : "Relacionadas à"
    question }|--|{ form : "Estão em"
    question_answer ||--|{ question_value : "Possui"
    question ||--|{ question_value: "Possui"
    question ||--o{ dependent_question: "Possui"
    dependent_question }|--|{ form: "Estão em"
    dependent_question || -- |{ question_answer: "Depende de"
    form ||--|{ question_answer : "Possui"

    tip }|--o{ user : "Sugeridas para"
    tip }|--|{ area : "Pertencem a"

    user ||--|| routine_meta : "Possui"
    routine_meta ||--|{ activity_meta : "Possui"
    routine ||--|| routine_meta : "Descrita por"
    routine }|--|{ activity : "Possui várias"
    activity }|--|| activity_meta : "Descritas por"
    activity }|--|| area : "Situadas em"
```

### Aplicação Mobile

O principal produto consiste de uma aplicação móvel nativa para Android. Essa aplicação atua como cliente do servidor descrito acima. Essa aplicação está sendo desenvolvida com a linguagem Kotlin, que é atualmente a recomendação da Google para o desenvolvimento Android. Abaixo estão incluídas descrições detalhadas das camadas da aplicação, da comunicação entre elas e as bibliotecas utilizadas em cada uma.

#### Camadas

```mermaid
graph LR
    subgraph presentation
        ui
        components --Combinados em páginas completas--> ui 
        theme --Define estilos--> ui
        theme --Define estilos-->components
        viewmodel --Mantém o estado de uma página--> ui

    end

    presentation --Representa objetos--> domainmodel

    repository --Controla os dados--> viewmodel
    service --Interage com interfaces externas --> viewmodel


    
    subgraph domain 
       domainmodel["model"]
    end

    subgraph service


    end

    subgraph repository
        repomodel["model"]
    end

    subgraph exceptions

    end

    exceptions --Define erros específicos--> repository 
    exceptions --Define erros específicos--> service

    subgraph utils
        mapper --Fornece conversões de--> domainmodel --Para--> repomodel
        mapper --Fornece conversões de--> repomodel --Para--> domainmodel

    end

    subgraph di["di (Dependency Injection)"]
        servicemodule
        repomodule
    end
        servicemodule --Fornece--> service
        repomodule --Fornece--> repository
```

### Tecnologias

#### Interface de Usuário (presentation)

A interface de usuário é criada com a biblioteca Jetpack Compose e outros pacotes auxiliares (e.g. navigation-compose), seguindo os padrões do Material 3. As telas completas são definidas no pacote subpacote `ui` e componentes individuais em `components` . O tema de cores foi criado com o plugin Material Theme Builder do Figma e é definido subpacote `theme`.

Dentro do subpacote `viewmodel` estão os ViewModels, responsáveis por armazenar os dados da tela e encapsular o contato destas com serviços e repositórios. Idealmente, cada tela está vinculada a um único ViewModel.

#### Serviços (service)

Os serviços são interfaces externas utilizadas pelo sistema, como autenticação e notificações, podem ser acessados por interfaces de Rede, como HTTP, ou por serviços do Android (Foreground, Background, Broadcast, etc).

Um serviço pode ser utilizado diretamente por um ViewModel, como no caso da autenticação, em que o estado do login e as ações realizadas são repassadas da interação do usuário para o ViewModel e depois para um serviço específico.

Atualmente, os serviços utilizados estão presentes no [Supabase](https://supabase.com), sendo a  autenticação o principal.

#### Repositórios (repository)

A camada de repositório é responsável por interfaces externas que representam/integram bancos de dados, como bancos de dados locais ou operações CRUD acessadas por APIs REST. No momento, a interface de banco de dados planejada será acessada através da API REST do Supbase, consumida através do Retrofit.

No subpacote `model` estão modelos específicos de uma implementação de banco de dados, que necessitam de conversão para modelos do domínio.

#### Utilitários (utils)

A camada de utilitários é compartilhada através de outras e é usada principalmente para processamentos locais ou mapeamento de entidades, no subpacote `mapper` estão as interfaces e classes responsáveis por converter modelos do domínio para modelos de outras camadas bidirecionalmente.

A serialização de objetos será implementada com a biblioteca Moshi e é utilizada nesse mapeamento, nos casos em que um objeto precise ser enviado em um formato JSON.

#### Injeção de Dependência (di)

A injeção de dependência na aplicação é gerenciada com a biblioteca Hilt, onde os módulos (objetos Kotlin) contendo os métodos para retornar implementações concretas das interfaces são definidos por camada, então todas as injeções da camada de repositórios estarão em um arquivo `RepositoryModule.kt`, da mesma forma com serviços e outras implementações.
