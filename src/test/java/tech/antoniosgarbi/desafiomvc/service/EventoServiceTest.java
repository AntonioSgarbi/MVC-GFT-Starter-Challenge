package tech.antoniosgarbi.desafiomvc.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tech.antoniosgarbi.desafiomvc.repository.EventRepository;

@SpringBootTest
public class EventoServiceTest {
    @Mock
    private EventRepository eventoRepository;
    @InjectMocks
    private EventService sobTest;

}
