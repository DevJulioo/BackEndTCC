# ==================================
# ESTÁGIO 1: Build da Aplicação
# ==================================
# Usamos uma imagem oficial do Maven com Java 17 para compilar o projeto.
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar o cache do Docker.
# Se as dependências não mudarem, o Docker não vai baixá-las de novo.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte.
COPY src ./src

# Compila o projeto e cria o arquivo .jar, pulando os testes.
RUN mvn clean package -DskipTests


# ==================================
# ESTÁGIO 2: Runtime da Aplicação
# ==================================
# Usamos uma imagem JRE (Java Runtime Environment) super leve.
# Ela só tem o necessário para rodar Java, não para compilar.
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Copia o arquivo .jar que foi criado no Estágio 1 (builder).
# !!! ATENÇÃO: Verifique se o nome 'login-auth-api-0.0.1-SNAPSHOT.jar' está correto !!!
COPY --from=builder /app/target/login-auth-api-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 (a porta padrão do Tomcat).
EXPOSE 8080

# Comando final para iniciar a aplicação.
ENTRYPOINT ["java", "-jar", "app.jar"]