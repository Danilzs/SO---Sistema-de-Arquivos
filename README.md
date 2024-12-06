# SO---Sistema-de-Arquivos
Simulador de um sistema de arquivos usando Journaling

#### Realizadores:
Cássio Thiago
Danilo Silva Pessoa De Araújo - 2218928

### Sobre:
O código implementa um simulador de sistema de arquivos em Java, permitindo o usuário interagir por meio de comandos em uma interface shell. O sistema trabalha com dois tipos principais de entidades: arquivos e diretórios. Ele oferece funcionalidades como criação, exclusão, renomeação, cópia e listagem, e usa o log journaling para registrar as operações.

### Introdução ao sistema de arquivos com Journaling:
O sistema de arquivos é um importante componente do sistema operacional, responsavel por armazenar, recuperar e organizar dados em dispositivos de armazenamentos, como disco rigido, SSD e entre outros, agindo como uma ponte entre os hardwares de armazenamento e os programas/aplicativos que precisam acessar os dadois. 
Alem disso, ele tambem define como os dados são estruturados no disco, como os metadados são gerenciados, como os nomes dos arquivos são atribuidos e etc. Tendo alguns componentes como: Arquivos, diretórios e estruturas de alocação.

O sistema de arquivos é importante tambem pela organização e estruturação dos dados armazenados, melhorando e facilitando o acesso dos usuarios e do proprio SO. Otimiza o uso do espaço e armazenamento, fornece mais segurança e controle de acesso, e permite o compartilhamento de dados entre diferentes dispositivos e sistemas operacionais.

##### Journaling:
O journaling é uma técnica usada em sistemas de arquivos para melhorar a confiabilidade e evitar corrupção de dados em caso de falhas inesperadas, como quedas de energia ou travamentos do sistema. Ele registra operações de escrita em um log especial chamado journal antes de aplicá-las ao armazenamento principal.
O objetivo principal do journaling é garantir a consistência do sistema de arquivos, mesmo se ocorrer uma interrupção no meio de uma operação.

Antes de qualquer modificação ser feita no disco, o sistema grava no journal o que será alterado, criando um registro das operações. Após registrar no journal, a operação é marcada como "pendente", e ai a modificação é então aplicada ao sistema de arquivos principal. Após a conclusão bem-sucedida, a entrada correspondente no journal é removida ou marcada como concluída.

##### Tipos de Journaling:
###### Write-Ahead Logging (WAL)
Grava todas as alterações no journal antes de aplicá-las ao disco principal. Mais confiável, mas introduz sobrecarga, pois cada dado é gravado duas vezes (no journal e no sistema principal).

###### Metadados-Only Journaling
Grava apenas os metadados das operações no journal, com os dados principais sendo escritos diretamente no sistema de arquivos, tem um menor impacto no desempenho, mas pode levar a inconsistências em casos extremos.

###### Data Journaling
Tanto os dados quanto os metadados são registrados no journal antes de serem escritos no disco. Oferece o maior nível de segurança, mas com maior custo de desempenho.

###### Log-Structured Journaling
Reorganiza os dados no journal como um log contínuo. É eficiente para gravações sequenciais e sistemas com alta taxa de escrita, mas pode ter dificuldades em sistemas de leitura intensiva.

### Implementação;
O código é feito em java, criando comandos interativos com o usuário, que vai poder escolher a operação a ser realizada com cada um deles através do runshell.

#### Função processCommand(String input)
Objetivo: Processa os comandos digitados pelo usuário.
Como funciona: O comando é extraído da entrada do usuário, o código usa um switch para identificar qual ação tomar com base no comando.
Cada comando chama uma função específica, como createFile(), deleteFile(), copy(), etc., para executar a ação correspondente.

#### Função logOperation(String operation)
Objetivo: Registra a operação realizada no simulador no arquivo de log (journal.log).
Como funciona: A operação é registrada tanto no console quanto no arquivo de log, o log é feito usando BufferedWriter para escrever no arquivo journal.log.

#### Função navigateToDirectory(String[] parts, boolean createIfNotExist)
Objetivo: Navega pelo sistema de arquivos até o diretório especificado pelo caminho dado.
Como funciona: Divide o caminho em partes e navega por cada diretório, criando diretórios automaticamente se o parâmetro createIfNotExist for true. Retorna null se algum diretório não for encontrado.

#### Classes File e Directory
Objetivo: Representam um arquivo e um diretório no sistema de arquivos simulado.
Como funciona: A classe File contém o nome e o conteúdo do arquivo.
A classe Directory contém o nome, os arquivos e subdiretórios, permitindo operações como adicionar, excluir e listar arquivos e diretórios.

#### Como o Sistema Funciona:
O código simula um sistema de arquivos básico onde você pode criar, deletar, renomear, copiar e listar arquivos e diretórios.
Cada operação é registrada em um arquivo de log (journal.log) para rastreamento. O sistema permite navegação hierárquica através de diretórios.

#### Comandos:
create file <path> [content]: Cria um arquivo com o conteúdo fornecido.
create dir <path>: Cria um diretório no caminho especificado.
delete file <path>: Exclui um arquivo.
delete dir <path>: Exclui um diretório.
rename <file|dir> <old path> <new path>: Renomeia um arquivo ou diretório.
copy <source> <destination>: Copia um arquivo de origem para o destino.
list <path>: Lista o conteúdo de um diretório ou exibe o arquivo no caminho especificado.
exit: Sai do simulador.

#### Forma recomendada de usar:

create dir /teste
create dir /teste2
create file /teste/olamundo.txt Ola mundo!
create file /teste2/welcome.txt Bem vindo!
view /teste
view /teste2
list dir
rename /teste/olamundo.txt /teste/testando.txt
delete file /teste2/welcome.txt
delete dir /teste
