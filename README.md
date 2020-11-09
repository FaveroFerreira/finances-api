# Finances API

For fun

## Dependências (Principais)

1. Git
2. Maven
3. Java 8
4. Spring-Boot 2.3.5.RELEASE
5. Flyway
6. JUnit
7. Docker
8. Docker-Compose

## Configuração do projeto para execução local

Para executar o projeto localmente, é necessário Docker e Docker-Compose instalados na máquina,
também é necessário que as portas 8080 e 3306 estejam livres no sistema.

Para clonar o projeto:
```
$ git clone https://github.com/FaveroFerreira/finances-api.git
```
Para acessar o diretório do projeto:
```
$ cd finances-api
```
Para preparar o ambiente docker:
```
$ docker-compose build
```
Para rodar o projeto em ambiente docker:
```
$ docker-compose up
```
Para acessar a URL do Swagger:
```
$ http://localhost:8080/swagger-ui.html#/
```
