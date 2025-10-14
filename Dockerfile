# 1. Use uma imagem base oficial com Java 17
FROM eclipse-temurin:17-jdk-jammy

# 2. Argumento para o nome do arquivo JAR (ajuste se for diferente)
ARG JAR_FILE=target/login-auth-api-0.0.1-SNAPSHOT.jar

# 3. Crie um diretório de trabalho
WORKDIR /opt/app

# 4. Copie os arquivos de build do Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 5. Copie o código-fonte do projeto
COPY src ./src

# 6. Execute o build do Maven (pulando testes para acelerar)
RUN ./mvnw clean install -DskipTests

# 7. Copie o JAR construído para a raiz do diretório
COPY ${JAR_FILE} app.jar

# 8. Comando para iniciar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]