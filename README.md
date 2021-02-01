# Envio de de Emails para autoridades.

## Modo de instalar

* #### Instale o java 15
  
    *    Linux :
            * Use o o gerenciador de pacotes instalando o openjdk-15-jdk
            - apt install openjdk-15-jdk
            - yum install java-15-openjdk-devel
    *    Windows / Mac :
            * Baixe do site: https://jdk.java.net/15/
    
* Faça o download do binário em : [Repositorio Binários](https://1drv.ms/u/s!Asl9Qoe_F0_VpektLMAC0qh5_1T_3w?e=nEzSwy)
    
* #### Ajuste as variáveis de ambiente :
  
    * JAVA_HOME com o caminho de instalação. Exemplo C:\Arquivos de Programas\java\jdk15\
    * Coloque o JAVA_HOME/bin no PATH do ambiente
        Dúvidas no windows : Acesse o [site](https://confluence.atlassian.com/confbr1/configurando-a-variavel-java_home-no-windows-933709538.html) 
      
## Execute o programa
  
    * Na linha de comando do terminal 
    * java -jar poder-direto-[versao].jar 
        * Exemplo : java -jar poder-direto-0.0.1.jar 
    
    * Acesso ao programa :
       * http://localhost:8080/
        * Leia a tela inicial e siga os passos indicados.

## Bases de dados de e-mail 
    
* #### Pastas
  
    * No home do usuário encontrará o diretório [[HOME_DIR]]/.config/poder-direto/*
    * Nessa pasta estará templates e planilhas para envio de e-mail
    * Ao executar a primeira vez ele criará a pasta a baixa da internet as listas de e-mail atualizadas. (Caso não exista)
    * Caso precise atualizar a lista de e-maisl existente, click na opção de "Contados" -> "Forçar Atualização"
    
## Outros detalhes ...
  
    # O template de exemplo se tem uma idéia de como  montar uma mensagem com as marcações "{{...}}"
    # O teste de envio enviará somente 1 e-mail para você com o formato/mensagem indicada
    # A simulação de envio, grava como o envio é feito (mensagens e destinatários) no arquivo de log - "poder-direto.log", mas não manda mensagem para ninguém.
    # O Gmail tem um limite de 500 e-mails por dia (com autenticação, sem autenticação é 100), por esse motivo as listas com mais de 500 autoridades é fracionada em listas com tamanho máximo de 400. (Caso da câmara dos deputados)
    # A sua senha de e-mail é utilizada durante o envio de e-mails, mas não é guardada em nenhum arquivo de configuração.

## Bom trabalho.
      
#### Dúvidas e/ou sugestões envie e-mail para "instrutor ponto edison no gmail ponto com'
    
