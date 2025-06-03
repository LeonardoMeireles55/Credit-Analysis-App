# Aplicativo de Análise de Crédito

## Visão Geral

O Aplicativo de Análise de Crédito é um microsserviço Spring Boot projetado para avaliar propostas de crédito de forma assíncrona. A aplicação recebe propostas de crédito através de uma fila RabbitMQ, as analisa com base em várias estratégias de pontuação e envia os resultados de volta por meio de outro exchange RabbitMQ.

## Índice

- [Arquitetura](#arquitetura)
- [Principais Funcionalidades](#principais-funcionalidades)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Primeiros Passos](#primeiros-passos)
  - [Pré-requisitos](#pré-requisitos)
  - [Instalação](#instalação)
  - [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Documentação da API](#documentação-da-api)
- [Testes](#testes)
- [Suporte a Docker](#suporte-a-docker)

## Arquitetura

A aplicação segue o padrão de arquitetura de microsserviços, com foco no domínio de análise de crédito. Segue princípios SOLID e emprega uma abordagem de arquitetura limpa com:

- **Camada de Domínio**: Contém as entidades de negócio principais como `Proposal` e `User`
- **Camada de Serviço**: Contém a lógica de negócio com o `CreditAnalysisService` e implementações de estratégias
- **Camada de Infraestrutura**: Gerencia a comunicação com o RabbitMQ através de listeners e serviços de notificação

A aplicação utiliza o Padrão Strategy para calcular as pontuações de crédito, permitindo critérios de pontuação flexíveis que podem ser facilmente estendidos ou modificados.

## Principais Funcionalidades

- Processamento assíncrono de propostas de crédito via RabbitMQ
- Múltiplas estratégias de pontuação que analisam vários aspectos da capacidade de crédito
- Limite de aprovação configurável
- Tratamento de erros com mecanismos de retry
- Log abrangente
- Suporte a Docker para implantação em containers

## Tecnologias

- Java 21
- Spring Boot
- Spring AMQP (RabbitMQ)
- Lombok
- Maven
- Docker
- JUnit e Mockito (para testes)

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── leonardo/
│   │           └── creditanalysisapp/
│   │               ├── config/             # Classes de configuração
│   │               ├── domain/             # Entidades de domínio
│   │               ├── dto/                # Objetos de Transferência de Dados
│   │               ├── exception/          # Exceções personalizadas e handlers
│   │               ├── listener/           # Listeners de mensagens RabbitMQ
│   │               ├── mapper/             # Mapeadores de objetos
│   │               ├── service/            # Serviços de lógica de negócio
│   │               │   └── strategy/       # Estratégias de pontuação de crédito
│   │               └── statics/            # Constantes e mensagens estáticas
│   └── resources/
│       └── application.properties          # Configuração da aplicação
└── test/
    └── java/                               # Classes de teste
```

## Primeiros Passos

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Servidor RabbitMQ (executando localmente ou acessível remotamente)
- Docker (opcional, para implantação em containers)

### Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/yourusername/credit-analysis-app.git
   cd credit-analysis-app
   ```

2. Construa a aplicação:
   ```bash
   mvn clean package
   ```

### Configuração

Configure a aplicação através do `application.properties`:

```properties
# Nome da aplicação
spring.application.name=credit-analysis-app

# Configuração do RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Nomes das filas e exchanges
rabbitmq.queue.pending.proposal=pending-proposal.ms-credit-analysis
rabbitmq.completed-exchange.exchange=completed-proposal.ex

# Configuração de retry
spring.rabbitmq.listener.simple.retry.enabled=true
spring.rabbitmq.listener.simple.retry.max-attempts=3

# Configuração da análise de crédito
credit.analysis.approval.threshold=350
```

## Executando a Aplicação

### Usando Maven

```bash
mvn spring-boot:run
```

### Usando Java

```bash
java -jar target/credit-analysis-app.jar
```

### Usando Docker

```bash
docker build -t credit-analysis-app .
docker run -p 8080:8080 credit-analysis-app
```

Alternativamente, você pode usar Docker Compose:

```bash
docker-compose up
```

## Processo de Análise de Crédito

1. A aplicação escuta propostas de crédito na fila `pending-proposal.ms-credit-analysis`
2. Cada proposta é analisada usando múltiplas estratégias de pontuação:
   - Avaliação de compatibilidade de renda
   - Avaliação do prazo de pagamento
   - Pontuação baseada no nome
   - Verificação de registros negativos
   - Avaliação de empréstimos existentes
3. Os pontos de cada estratégia são somados e comparados ao limite de aprovação
4. A proposta analisada com o status de aprovação é enviada para o exchange `completed-proposal.ex`

## Testes

Execute testes unitários:

```bash
mvn test
```

Execute testes de integração e verifique o build:

```bash
mvn verify
```

## Suporte a Docker

A aplicação inclui um `Dockerfile` e um `docker-compose.yml` para implantação em containers:

- `Dockerfile`: Define a imagem do container para a aplicação
- `docker-compose.yml`: Orquestra a aplicação e suas dependências (RabbitMQ)

Para construir e executar com Docker Compose:

```bash
docker-compose up --build
```

## Contribuindo

1. Faça um fork do repositório
2. Crie um branch de funcionalidade (`git checkout -b feature/funcionalidade-incrivel`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona funcionalidade incrível'`)
4. Faça push para o branch (`git push origin feature/funcionalidade-incrivel`)
5. Abra um Pull Request

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).
