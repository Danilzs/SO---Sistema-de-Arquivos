# SO---Sistema-de-Arquivos
Simulador de um sistema de arquivos usando Journaling

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
