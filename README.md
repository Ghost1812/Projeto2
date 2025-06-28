# PackBee - Sistema de Gestão de Encomendas

## Visão Geral

Este projeto foi completamente reorganizado para usar **JavaFX com FXML** em vez de Swing, mantendo a arquitetura Spring Boot para o backend. Esta abordagem oferece uma interface mais moderna e uma melhor separação entre lógica de negócio e apresentação.

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/
│   │       ├── Projeto2Application.java          # Classe principal JavaFX
│   │       └── proj2/
│   │           ├── controllers/                  # Controllers JavaFX
│   │           │   ├── LoginController.java      # Controller de login
│   │           │   ├── MainMenuController.java   # Controller do menu principal
│   │           │   ├── ClienteController.java    # Controller de gestão de clientes
│   │           │   ├── EncomendaController.java  # Controller de gestão de encomendas
│   │           │   ├── FuncionarioController.java # Controller de gestão de funcionários
│   │           │   └── AuthController.java       # Controller REST (se necessário)
│   │           ├── models/                       # Entidades JPA
│   │           │   ├── Cliente.java
│   │           │   ├── Encomenda.java
│   │           │   ├── Funcionario.java
│   │           │   ├── Codpostal.java
│   │           │   ├── Entrega.java
│   │           │   ├── Estafeta.java
│   │           │   ├── Feedback.java
│   │           │   ├── Veiculo.java
│   │           │   └── UtilizadorAtual.java
│   │           ├── repositories/                 # Repositórios Spring Data
│   │           │   ├── ClienteRepository.java
│   │           │   ├── EncomendaRepository.java
│   │           │   ├── FuncionarioRepository.java
│   │           │   ├── CodPostalRepository.java
│   │           │   ├── EntregaRepository.java
│   │           │   ├── EstafetaRepository.java
│   │           │   ├── FeedbackRepository.java
│   │           │   └── VeiculoRepository.java
│   │           ├── services/                     # Lógica de negócio
│   │           │   ├── ClienteService.java
│   │           │   ├── EncomendaService.java
│   │           │   ├── FuncionarioService.java
│   │           │   ├── CodPostalService.java
│   │           │   ├── EntregaService.java
│   │           │   ├── EstafetaService.java
│   │           │   ├── FeedbackService.java
│   │           │   └── VeiculoService.java
│   │           └── ui/                           # Classes Swing antigas (para referência)
│   └── resources/
│       ├── fxml/                                 # Arquivos FXML (interface)
│       │   ├── login.fxml                       # Tela de login
│       │   ├── main-menu.fxml                   # Menu principal
│       │   ├── cliente.fxml                     # Gestão de clientes
│       │   ├── encomenda.fxml                   # Gestão de encomendas
│       │   └── funcionario.fxml                 # Gestão de funcionários
│       ├── css/                                  # Estilos CSS
│       │   ├── login.css                        # Estilos do login
│       │   ├── main-menu.css                    # Estilos do menu principal
│       │   ├── cliente.css                      # Estilos de clientes
│       │   ├── encomenda.css                    # Estilos de encomendas
│       │   └── funcionario.css                  # Estilos de funcionários
│       ├── images/                               # Recursos de imagem
│       │   └── img.png                          # Logo da aplicação
│       └── application.properties               # Configuração Spring
```

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.2.5**
- **JavaFX 21.0.2**
- **PostgreSQL**
- **Maven**

## Vantagens da Nova Arquitetura

### 1. **Separação de Responsabilidades**
- **FXML**: Define a estrutura da interface (XML)
- **CSS**: Define a aparência (estilos)
- **Controllers**: Controlam a lógica de apresentação
- **Services**: Contêm a lógica de negócio
- **Repositories**: Acessam a base de dados

### 2. **Interface Moderna**
- JavaFX oferece uma interface mais moderna que Swing
- Suporte a CSS para estilização avançada
- Melhor experiência do usuário
- Componentes mais responsivos

### 3. **Manutenibilidade**
- Código mais organizado e legível
- Fácil de modificar a interface sem alterar código Java
- Reutilização de componentes

## Funcionalidades Implementadas

### ✅ **Sistema de Login**
- Autenticação de funcionários
- Validação de credenciais
- Interface moderna com logo

### ✅ **Menu Principal**
- Navegação para todas as funcionalidades
- Interface responsiva
- Botões organizados por categoria

### ✅ **Gestão de Clientes**
- Criar, editar, eliminar clientes
- Pesquisa em tempo real
- Validação de dados
- Interface com formulário e tabela

### ✅ **Gestão de Encomendas**
- Criar, editar, eliminar encomendas
- Associação com clientes
- Estados e prioridades
- Pesquisa por número de rastreio

### ✅ **Gestão de Funcionários**
- Criar, editar, eliminar funcionários
- Diferentes cargos (admin, rececionista, etc.)
- Gestão segura de passwords
- Pesquisa por nome

## Como Executar

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL

### Configuração da Base de Dados
1. Crie uma base de dados PostgreSQL chamada `Projeto2`
2. Configure as credenciais em `application.properties`

### Execução
```bash
# Compilar e executar
mvn clean javafx:run

# Ou usando o plugin Spring Boot
mvn spring-boot:run
```

## Migração de Swing para JavaFX

### Estrutura Anterior (Swing)
```java
// LoginFrame.java - Tudo em uma classe
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        // Código de interface e lógica misturados
    }
}
```

### Nova Estrutura (JavaFX)
```xml
<!-- login.fxml - Interface em XML -->
<VBox>
    <TextField fx:id="emailField" />
    <PasswordField fx:id="passwordField" />
    <Button onAction="#handleLogin" />
</VBox>
```

```java
// LoginController.java - Lógica separada
@Component
public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    
    @FXML
    private void handleLogin() {
        // Lógica de login
    }
}
```

## Arquitetura de Controllers

### Padrão Implementado
Cada controller segue o mesmo padrão:

1. **Injeção de Dependências**: Services e ApplicationContext
2. **Inicialização**: Setup de tabelas, comboboxes e eventos
3. **Operações CRUD**: Create, Read, Update, Delete
4. **Validação**: Validação de formulários
5. **Feedback**: Alertas para o usuário

### Exemplo de Controller
```java
@Component
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    
    @FXML
    public void initialize() {
        setupTable();
        loadClientes();
        setupEventHandlers();
    }
    
    @FXML
    private void handleCreate() {
        // Lógica de criação
    }
}
```

## Estilos CSS

### Sistema de Cores
- **Primária**: #e6b43c (Dourado)
- **Secundária**: #4CAF50 (Verde)
- **Perigo**: #f44336 (Vermelho)
- **Neutra**: #808080 (Cinza)

### Classes CSS Reutilizáveis
- `.primary-button`: Botões principais
- `.secondary-button`: Botões secundários
- `.danger-button`: Botões de eliminação
- `.text-field`: Campos de texto
- `.table-view`: Tabelas

## Próximos Passos

### 1. **Completar Funcionalidades**
- Implementar triagem de encomendas
- Implementar entrega de encomendas
- Implementar gestão de feedback
- Implementar relatórios

### 2. **Melhorar a Experiência**
- Adicionar validações em tempo real
- Implementar feedback visual
- Adicionar animações
- Implementar temas escuro/claro

### 3. **Funcionalidades Avançadas**
- Sistema de notificações
- Relatórios em PDF
- Integração com APIs externas
- Sistema de auditoria

## Benefícios da Mudança

- ✅ **Interface mais moderna e profissional**
- ✅ **Código mais organizado e manutenível**
- ✅ **Melhor separação entre lógica e apresentação**
- ✅ **Facilidade para adicionar novos recursos**
- ✅ **Melhor experiência do desenvolvedor**
- ✅ **Reutilização de componentes**
- ✅ **Estilização avançada com CSS**

## Suporte

Para dúvidas ou problemas, consulte a documentação do JavaFX e Spring Boot, ou entre em contacto com a equipa de desenvolvimento.

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request 