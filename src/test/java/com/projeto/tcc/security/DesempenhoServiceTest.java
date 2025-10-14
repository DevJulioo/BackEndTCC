package com.projeto.tcc.security; // Deve ser o mesmo pacote do seu Service

import com.projeto.tcc.domain.avaliacao.Avaliacao;
import com.projeto.tcc.domain.user.User;
import com.projeto.tcc.dto.DesempenhoDTO;
import com.projeto.tcc.repository.AvaliacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class DesempenhoServiceTest {

    @Mock // Cria uma versão "fake" do repositório, que não acessa o banco de dados
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks // Cria uma instância real do DesempenhoService, usando o repositório fake acima
    private DesempenhoService desempenhoService;

    @Test // Marca este método como um caso de teste
    void deveCalcularMediaGeralCorretamente() {
        // 1. Arrange (Preparação)
        // Criamos os dados falsos que nosso teste usará
        User alunoFalso = new User();
        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setNota(10.0);
        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setNota(8.0);
        List<Avaliacao> listaDeAvaliacoesFalsas = List.of(avaliacao1, avaliacao2);

        // "Ensinamos" o repositório fake: quando o método findAllByAluno for chamado,
        // ele deve retornar nossa lista de avaliações falsas.
        Mockito.when(avaliacaoRepository.findAllByAluno(alunoFalso)).thenReturn(listaDeAvaliacoesFalsas);

        // 2. Act (Ação)
        // Executamos o método que queremos testar
        DesempenhoDTO resultado = desempenhoService.getDesempenhoDoAluno(alunoFalso);

        // 3. Assert (Verificação)
        // Verificamos se o resultado foi o esperado
        Assertions.assertEquals(9.0, resultado.mediaGeral(), "A média de 10.0 e 8.0 deve ser 9.0");
        Assertions.assertEquals(2, resultado.atividadesConcluidas(), "Deve haver 2 atividades concluídas");
    }
}