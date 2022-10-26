package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.stereotype.Component;
import tech.antoniosgarbi.desafiomvc.model.*;
import tech.antoniosgarbi.desafiomvc.repository.DeliveryRepository;
import tech.antoniosgarbi.desafiomvc.repository.ParticipantRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class DatabaseFillerComponent {
    private final ParticipantRepository participantRepository;
    private final DeliveryRepository deliveryRepository;
    private final EventService eventService;

    public DatabaseFillerComponent(ParticipantRepository participantRepository, DeliveryRepository deliveryRepository,
            EventService eventService) {
        this.participantRepository = participantRepository;
        this.deliveryRepository = deliveryRepository;
        this.eventService = eventService;

        if (this.eventService.findAll().isEmpty()) {
            this.initDatabase();
        }
    }

    private Date dateFrom(int day, int month, int year) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Delivery deliverFromParticipant(Participant participant, boolean late) {
        return new Delivery(null, participant, late);
    }

    private void initDatabase() {
        System.out.println("init database");
        List<Participant> participants = new LinkedList<>();

        Participant p1 = new Participant(null, "Robert Martin", "sênior", "robertmartin@mail.test", "ubob",
                "https://developeronfire.com/assets/images/UncleBobMartin.jpg");
        Participant p2 = new Participant(null, "Martin Fowler", "sênior", "martinfowler@mail.test", "fowl",
                "https://pbs.twimg.com/profile_images/79787739/mf-tg-sq_400x400.jpg");
        Participant p3 = new Participant(null, "Eric Evans", "sênior", "ericevans@mail.test", "eric",
                "https://res.cloudinary.com/value-object/image/upload/t_speaker19v2/v1541095170/dddeu19_site/eric_evans.jpg");
        Participant p4 = new Participant(null, "Kent Beck", "sênior", "kentbeck@mail.test", "kent",
                "https://avatars.githubusercontent.com/u/46154?v=4");
        Participant p5 = new Participant(null, "Grace Hopper", "sênior", "gracehopper@mail.test", "grac",
                "https://www.lambda3.com.br/wp-content/uploads//2017/03/grace-hopper-21406809-1-402.jpg");
        Participant p6 = new Participant(null, "James Grenning", "sênior", "jamesGrenning@mail.test", "jame",
                "https://wingman-sw.com/assets/james-grenning.jpg");
        Participant p7 = new Participant(null, "Mike Beedle", "sênior", "mikebeedle@mail.test", "mike",
                "https://www.agilealliance.org/wp-content/uploads/2018/03/mike_beedle.jpg");
        Participant p8 = new Participant(null, "Steve Mellor", "sênior", "stevemellor@mail.test", "stev",
                "https://pbs.twimg.com/profile_images/1075316455154032640/R3awrPPY_400x400.jpg");
        Participant p9 = new Participant(null, "Alistair Cockburn", "sênior", "AlistairCockburn@mail.test", "alis",
                "https://files.gotocon.com/uploads/portraits/97/square_medium/alistair_cockburn_1548158890.jpg");
        Participant p10 = new Participant(null, "Dave Thomas", "sênior", "davethomas@mail.test", "dave",
                "https://uploads.sitepoint.com/wp-content/uploads/2015/07/1436722772dave-thomas.jpg");
        Participant p11 = new Participant(null, "Loiane Groner", "sênior", "loianegroner@mail.test", "loia",
                "https://pbs.twimg.com/profile_images/1563898376533557253/iHDy1578_400x400.jpg");
        Participant p12 = new Participant(null, "Michelli Brito", "sênior", "michellibrito@mail.test", "mich",
                "https://pbs.twimg.com/profile_images/1563898376533557253/iHDy1578_400x400.jpg");
        Participant p13 = new Participant(null, "Fábio Akita", "sênio", "fabioakita@mail.test", "akit",
                "https://www.infoq.com/images/profiles/zDdlagNsRtArnva80YsOo70X0CrGzqVd.jpg");
        Participant p14 = new Participant(null, "Nélio Alves", "sênior", "nelioalves@mail.test", "neli",
                "https://pbs.twimg.com/profile_images/1276862257209716738/Ae7ZgOdl_400x400.jpg");
        Participant p15 = new Participant(null, "William Suane", "sênior", "williamsuane@mail.test", "dedo",
                "https://devdojo.academy/images/Mask-Group-4.png");
        Participant p16 = new Participant(null, "Elemar Junior", "sênior", "elemarjunior@mail.test", "elem",
                "https://eximia.co/wp-content/uploads/2022/02/elemar-eximia.png");
        Participant p17 = new Participant(null, "Lucas Montano", "sênior", "lucasmontano@mail.test", "luca",
                "https://miro.medium.com/max/2400/2*UbTtj9_xxzLJXTIuDh-qww.png");
        Participant p18 = new Participant(null, "Rodrigo Branas", "sênior", "rodrigobranas@mail.test", "bran",
                "https://pbs.twimg.com/profile_images/1422361113941975045/CCz99SUb_400x400.jpg");
        Participant p19 = new Participant(null, "Filipe Deschamps", "sênior", "filipedeschamps@mail.test", "desc",
                "https://miro.medium.com/fit/c/224/224/1*9Kr_4fcC_R2SzZkunC5Xwg.jpeg");
        Participant p20 = new Participant(null, "Wesley Willians", "sênior", "wesleywillians@mail.test", "fucy",
                "https://media-exp1.licdn.com/dms/image/C5603AQEe_i0cGgw5oA/profile-displayphoto-shrink_400_400/0/1654033409022?e=1668643200&v=beta&t=gsnV5r9HcH5pU7OrpLc2_VQ8jhpAJxHIMAT3QpEA-VM");

        participants = List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19,
                p20);
        participants.forEach(p -> p = this.participantRepository.save(p));

        {
            Event e1 = new Event(null, "Reunião Agil", dateFrom(10, 10, 2022), dateFrom(11, 12, 2022), "", null, null);

            Activity a1 = new Activity(null, "agil init", dateFrom(11, 10, 2022), dateFrom(17, 10, 2022), null);
            Activity a2 = new Activity(null, "agil mid", dateFrom(18, 10, 2022), dateFrom(25, 10, 2022), null);
            Activity a3 = new Activity(null, "agil end", dateFrom(26, 10, 2022), dateFrom(9, 11, 2022), null);

            {
                List<Delivery> deliveriesA1 = Arrays.asList(
                        deliverFromParticipant(p1, false),
                        deliverFromParticipant(p2, true),
                        deliverFromParticipant(p3, false),
                        deliverFromParticipant(p4, false),
                        deliverFromParticipant(p5, false),
                        deliverFromParticipant(p6, false),
                        deliverFromParticipant(p7, true),
                        deliverFromParticipant(p8, true),
                        deliverFromParticipant(p9, false));

                deliveriesA1.forEach(d -> d = this.deliveryRepository.save(d));
                a1.setDelivered(deliveriesA1);
            }
            {
                List<Delivery> deliveriesA2 = Arrays.asList(
                        deliverFromParticipant(p1, false),
                        deliverFromParticipant(p3, false),
                        deliverFromParticipant(p5, true),
                        deliverFromParticipant(p6, false),
                        deliverFromParticipant(p7, true),
                        deliverFromParticipant(p9, false));
                deliveriesA2.forEach(d -> d = this.deliveryRepository.save(d));
                a2.setDelivered(deliveriesA2);
            }
            {
                List<Delivery> deliveriesA3 = Arrays.asList(
                        deliverFromParticipant(p1, false),
                        deliverFromParticipant(p2, false),
                        deliverFromParticipant(p3, false),
                        deliverFromParticipant(p4, false),
                        deliverFromParticipant(p5, false),
                        deliverFromParticipant(p6, false),
                        deliverFromParticipant(p7, false),
                        deliverFromParticipant(p9, false));

                deliveriesA3.forEach(d -> d = this.deliveryRepository.save(d));
                a3.setDelivered(deliveriesA3);
            }
            e1.setActivities(Arrays.asList(a1, a2, a3));

            Group g1 = new Group(null, "grupo 1", List.of(p1, p2, p3));
            Group g2 = new Group(null, "grupo 2", List.of(p4, p5, p6));
            Group g3 = new Group(null, "grupo 3", List.of(p7, p8, p9));

            e1.setGroups(Arrays.asList(g1, g2, g3));

            eventService.save(e1);
        }

        // Activity a4 = new Activity(null, "name", dateFrom(0, 0, 0), dateFrom(0, 0,
        // 0), List.of());

        // Group g4 = new Group(null, "", List.of());
        // Group g5 = new Group(null, "", List.of());
        // Group g6 = new Group(null, "", List.of());
        // Group g7 = new Group(null, "", List.of());
        // Group g8 = new Group(null, "", List.of());
        // Group g9 = new Group(null, "", List.of());
        // Group g10 = new Group(null, "", List.of());
        // Group g11 = new Group(null, "", List.of());

    }

}
