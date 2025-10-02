# 🌤️ CityMet - Weather App

App Android de previsão do tempo desenvolvido com as tecnologias mais modernas do ecossistema Android.

![Gravação-de-Tela-2025-10-02-às-11 33 05](https://github.com/user-attachments/assets/2e2ea31b-8b7f-4df7-817d-170ee923600a)

## 📱 Sobre o Projeto

CityMet é um aplicativo que permite consultar informações meteorológicas de cidades do estado de SP, utilizando geolocalização e busca por nome da cidade.

## ✨ Funcionalidades

- 🔍 Busca de cidades com filtro em tempo real
- 📍 Detecção de localização atual
- 🌡️ Informações detalhadas do clima (temperatura, condição, ícone)
- 🎨 Interface adaptativa (tema claro/escuro baseado no horário)
- 📊 Lista completa de cidades do estado de SP

## 🛠️ Tecnologias Utilizadas

### Linguagem e Framework
- **Kotlin** - Linguagem principal
- **Jetpack Compose** - UI moderna e declarativa
- **Material Design 3** - Design system

### Arquitetura
- **MVVM** (Model-View-ViewModel)
- **Clean Architecture** - Separação de camadas
- **Single Source of Truth** - Estado unificado com StateFlow
- **Repository Pattern** - Abstração de dados

### Bibliotecas Principais
- **Coroutines & Flow** - Programação assíncrona
- **Hilt** - Injeção de dependências
- **Ktor** - Cliente HTTP
- **OkHttp** - Interceptors e logging
- **Lifecycle** - Gerenciamento de ciclo de vida

### Testes
- **MockK** - Mocking para Kotlin
- **Turbine** - Testes de Flow
- **Truth** - Assertions fluentes
- **Coroutines Test** - Testes de coroutines

## 🌐 APIs Utilizadas

Este projeto utiliza as seguintes APIs públicas:

- **[OpenWeatherMap API](https://openweathermap.org/api)** - Dados meteorológicos em tempo real
  - Endpoint: Current Weather Data
  - Informações: temperatura, condição climática, ícones

- **[BrasilAPI](https://brasilapi.com.br/)** - Dados sobre cidades brasileiras
  - Endpoint: Municípios
  - Informações: lista de cidades, coordenadas geográficas

> **Nota:** É necessário obter uma chave de API gratuita no OpenWeatherMap para executar o projeto.
> O app realiza um cruzamento de dados, pegando a informação de lat e long da api brasil de acordo com a seleção de usuário e repassando isso para a api openWeather.

## 🚀 Como Executar

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 17
- SDK Android 24+ (Android 7.0)

### Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/citymet.git
cd citymet
```

2. Obtenha uma chave de API gratuita:
   - Acesse [OpenWeatherMap](https://openweathermap.org/api)
   - Crie uma conta e gere uma API Key

3. Configure a chave de API:

Crie um arquivo `local.properties` na raiz do projeto:
```properties
WEATHER_API_KEY=sua_chave_aqui
```

Ou adicione no `build.gradle.kts`:
```kotlin
android {
    defaultConfig {
        buildConfigField("String", "WEATHER_API_KEY", "\"sua_chave_aqui\"")
    }
}
```

4. Sincronize o projeto e execute!

## 🎯 Aprendizados

Este projeto foi desenvolvido para demonstrar:

- ✅ Arquitetura MVVM com Clean Architecture
- ✅ Jetpack Compose moderno com State Management
- ✅ Testes unitários com Turbine para StateFlow
- ✅ Injeção de dependências com Hilt
- ✅ Consumo de APIs REST
- ✅ Boas práticas de código Kotlin
- ✅ Material Design 3

## 👨‍💻 Autor

**[Breno Carvalho]**
- LinkedIn: [seu-linkedin](https://linkedin.com/in/breno-carvalho-63221b174)
- GitHub: [@seu-usuario](https://github.com/brenimsc)
- Email: brenocarvs@gmail.com

---

⭐ Se este projeto te ajudou de alguma forma, considere dar uma estrela!

## 🙏 Agradecimentos

- [OpenWeatherMap](https://openweathermap.org/) pelos dados meteorológicos
- [BrasilAPI](https://brasilapi.com.br/) pelos dados de cidades brasileiras
- Comunidade Android pela documentação e suporte
