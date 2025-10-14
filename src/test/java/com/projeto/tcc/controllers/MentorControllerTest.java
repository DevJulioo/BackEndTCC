package com.projeto.tcc.controllers;

import com.projeto.tcc.domain.avaliacao.Avaliacao;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.domain.user.UserRoles;
import com.projeto.tcc.repository.AvaliacaoRepository;
import com.projeto.tcc.repository.UseRepository; // Usando o nome do seu repositório
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MentorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UseRepository useRepository; // Usando o nome do seu repositório

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanupDatabase() {
        // Limpa os repositórios em uma ordem que respeita as chaves estrangeiras
        avaliacaoRepository.deleteAll();
        // É importante limpar os relacionamentos antes de deletar os usuários
        // (Esta parte pode precisar de ajuste dependendo da sua entidade User)
        useRepository.findAll().forEach(user -> {
            user.getAlunos().clear();
            user.getMentores().clear();
        });
        useRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "mentor@email.com", roles = "MENTOR")
    @Transactional 
    void deveRetornarAlunosComNotasSeparadasPorMateria() throws Exception {
        // 1. Arrange (Preparação dos Dados)
        
        // Cria mentor e aluno com senhas criptografadas
        User mentor = new User("Mentor", "mentor@email.com", passwordEncoder.encode("senhaMentor"), UserRoles.MENTOR);
        User aluno = new User("Julio", "julio@email.com", passwordEncoder.encode("senhaAluno"), UserRoles.ALUNO);
        
        // Salva ambos no banco primeiro
        useRepository.save(mentor);
        useRepository.save(aluno);

        // Cria a associação bidirecional entre eles
        mentor.getAlunos().add(aluno);
        aluno.getMentores().add(mentor);
        
        // Salva novamente para persistir a relação
        useRepository.save(mentor);
        useRepository.save(aluno);

        // Cria a avaliação para o aluno
        Avaliacao avaliacaoIOT = new Avaliacao();
        avaliacaoIOT.setAluno(aluno);
        avaliacaoIOT.setCursoId("IOT"); // Supondo que o DTO usa o cursoId como "matéria"
        avaliacaoIOT.setNota(10.0);
        avaliacaoRepository.save(avaliacaoIOT);

        // 2. Act (Ação) & Assert (Verificação)
        mockMvc.perform(get("/api/mentor/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()) // Garante que a resposta é uma lista
                .andExpect(jsonPath("$[0].name").value("Julio"))
                // A verificação abaixo depende da estrutura do seu AlunoDTO.
                // Ajustei para o formato que seu MentorController parecia estar criando: (id, nome, nota)
                .andExpect(jsonPath("$[0].score").value(10.0));
    }
}