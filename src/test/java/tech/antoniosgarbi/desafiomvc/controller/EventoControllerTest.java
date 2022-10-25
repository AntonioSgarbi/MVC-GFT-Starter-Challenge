package tech.antoniosgarbi.desafiomvc.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tech.antoniosgarbi.desafiomvc.service.EventService;

@SpringBootTest
public class EventoControllerTest {
    @Mock
    private EventService eventoService;
    @InjectMocks
    private EventController sobTest;

}
