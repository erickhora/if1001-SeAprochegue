# IF1001-Programação 3
-----------------------------

# Projeto SeAprochegue

Aplicação Social voltada para criação e administração de grupos formados por pessoas com interesses em comum

## Implementação

Inicialmente decidimos seguir o fluxo do Mock Up como guia para nosso desenvolvimento.
Login -> Cadastrar -> Home -> Criar Grupo -> Perfil do Grupo -> Buscar

* **Tela de Login** 
  Contém dois Edittexts, entradas com formatação esperada para email e senha, ambas informações armzenadas no banco de dados do Firebase.
  Contém dois Butões, ambos respondendo ao comando de clique em que um deles redireciona para uma tela de cadastro,"Cadastre-se", 
  e o outro atentica as entradas passadas nos campos de textos com o Firebase e permite o login do usuário.
  
  Aqui captamos os valores para email e senha para um usuário que já tenha cadastro
  
  /---login_1---/
  
  Após isso fazemos a autenticação utilizando Firebase tratando os casos do Listener.Mal sucedido, informando
  através de ToastNotificattion incongruência nos valores passados em comparação ao que temos salvos no Banco de Dados.
  Bem sucedido, utilizando de intent para fazermos a transição de activity.Neste caso, indo para a "HomeActivity".
  
  /---Login_2---/
  
  Caso o usuário queira cadastrar-se, ao clicar o botão será redirecionado para a tela de Cadastro
  
  /---login_3---/
  
  
  
  
  **Tela de Cadastro**
  Contém um ImageView responsável para receber uma imagem provida pelo usuário advinda de qualquer uma das galerias de imagens existentes no dispositivo
  Contém quatro campos de input de texto ,Edittexts.Neles o usuário irá informar seu Nome, Telefone, E-mail e Senha para serem salvos no banco de dados
  Contém um Button, "Cadastrar" que serve para confirmar a inteção do usuário que criar uma conta no aplicativo
  Contém um TextView, clicável, que permite que um usuário já cadastrado possa efetuar login e acessar sua homepage
  
  
  Na função "onCreate", se o botão "btnSignUp" é selecionado então chamamos o método "registrar"
  Caso a opção, "Já tenho uma conta", seja selecionada a aplicação irá redirecionar o usuário para a tela de login
  Em "inputProfileImage" habilitamos o ImageView para seleção e salvamos e ele foi clicado ou não	
  
  /---register_1---/
  
  No método "registrar" atribuímos as variáveis de acordo com seus respectivos inputs, os convertendo para string
  Caso algum dos campos não seja preenchido será mostrado uma mensagem para que o usuário complete os campos faltantes
  Após completo isso, chamamos o método FirebaseAuth para criar um novo usuário.Caso tenha sido bem sucedido, também chamados o método "uploadImageToDatabase"
  
  /---register_2---/
  
  É gerado um ID aleatório para a imagem no Banco de dados e com isso chamamos o método "saveUserToDatabase" para terminar o processo de cadastrado
  São atribuídas variáveis com as instâncias do "uid" e "ref" do BD para usuário para que sejam por conseguinte inseridas na tabela do BD
  Concluído esses passos, o usuário é transferido para sua tela de Home na "HomeActivity"
  
  /---register_3---/
  
  
  **Home**
  
  /---home_1---/
  /---home_2---/
  /---home_3---/
  
  
  **CriarGrupo**
  /---cgroup_1---/
  /---cgroup_2---/
  /---cgroup_3---/
  
  
  **Grupo**
  /---group_1---/
  /---group_2---/
  /---group_3---/
  
  **Buscar**
  /---busca_1---/
  /---busca_2---/
  /---busca_3---/
	


## Construído com

* [Firebase](https://firebase.google.com/?hl=pt-br) - O banco de Dados
* [Picasso](https://github.com/square/picasso) - Biblioteca do github para downloading e caching de imagens
* [Groupie](https://github.com/lisawray/groupie) - Biblioteca do github que otimiza layouts de RecicleViews complexas

## Autores

* **Erick Nicolas** - (https://github.com/erickhora)
* **Raphael Tulyo** - (https://github.com/crvzybird)

## Reconhecimentos

* Um grande agradecimento ao professor responsável pela curso de Programação 3, Leopoldo Teixeira, pela compreensão e apoio no desenvolvimento deste projeto
* 
* 
* 

