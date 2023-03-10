package tech.antoniosgarbi.desafiomvc.service;

import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.antoniosgarbi.desafiomvc.config.WebSecurityConfig;
import tech.antoniosgarbi.desafiomvc.model.*;
import tech.antoniosgarbi.desafiomvc.repository.ParticipantRepository;
import tech.antoniosgarbi.desafiomvc.repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class DatabaseFillerComponent {
        private final UserRepository userRepository;
        private final ParticipantRepository participantRepository;
        private final EventService eventService;
        private String password;
        private String username;

        public DatabaseFillerComponent(UserRepository userRepository, ParticipantRepository participantRepository,
                        EventService eventService, Environment environment) {
                this.userRepository = userRepository;
                this.participantRepository = participantRepository;
                this.eventService = eventService;

                this.username = environment.getProperty("personal.username");
                this.password = environment.getProperty("personal.password");

                if (this.eventService.findAll(Pageable.unpaged()).isEmpty()) {
                        this.initDatabase();
                }
        }

        private Date dateFrom(int day, int month, int year) {
                return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        private void initDatabase() {
                System.out.printf("%n****** init database ******%n%n");

                {
                        UserModel userModel = new UserModel();
                        userModel.setUsername(username);
                        userModel.setEmail("mail");
                        String encripted = WebSecurityConfig.passwordEncoder().encode(password);
                        userModel.setPassword(encripted);
                        this.userRepository.save(userModel);
                }

                List<Participant> participants = new LinkedList<>();

                Participant p1 = new Participant(null, "Robert Martin", "s??nior", "robertmartin@mail.test", "ubob",
                                "https://developeronfire.com/assets/images/UncleBobMartin.jpg");

                Participant p2 = new Participant(null, "Martin Fowler", "s??nior", "martinfowler@mail.test", "fowl",
                                "https://pbs.twimg.com/profile_images/79787739/mf-tg-sq_400x400.jpg");

                Participant p3 = new Participant(null, "Eric Evans", "s??nior", "ericevans@mail.test", "eric",
                                "https://res.cloudinary.com/value-object/image/upload/t_speaker19v2/v1541095170/dddeu19_site/eric_evans.jpg");

                Participant p4 = new Participant(null, "Kent Beck", "s??nior", "kentbeck@mail.test", "kent",
                                "https://avatars.githubusercontent.com/u/46154?v=4");

                Participant p5 = new Participant(null, "Grace Hopper", "s??nior", "gracehopper@mail.test", "grac",
                                "https://www.lambda3.com.br/wp-content/uploads//2017/03/grace-hopper-21406809-1-402.jpg");

                Participant p6 = new Participant(null, "James Grenning", "s??nior", "jamesGrenning@mail.test", "jame",
                                "https://wingman-sw.com/assets/james-grenning.jpg");

                Participant p7 = new Participant(null, "Mike Beedle", "s??nior", "mikebeedle@mail.test", "mike",
                                "https://www.agilealliance.org/wp-content/uploads/2018/03/mike_beedle.jpg");

                Participant p8 = new Participant(null, "Steve Mellor", "s??nior", "stevemellor@mail.test", "stev",
                                "https://pbs.twimg.com/profile_images/1075316455154032640/R3awrPPY_400x400.jpg");

                Participant p9 = new Participant(null, "Alistair Cockburn", "s??nior", "AlistairCockburn@mail.test",
                                "alis",
                                "https://files.gotocon.com/uploads/portraits/97/square_medium/alistair_cockburn_1548158890.jpg");

                Participant p10 = new Participant(null, "Dave Thomas", "s??nior", "davethomas@mail.test", "dave",
                                "https://uploads.sitepoint.com/wp-content/uploads/2015/07/1436722772dave-thomas.jpg");

                Participant p11 = new Participant(null, "Loiane Groner", "s??nior", "loianegroner@mail.test", "loia",
                                "https://pbs.twimg.com/profile_images/1563898376533557253/iHDy1578_400x400.jpg");

                Participant p12 = new Participant(null, "Michelli Brito", "s??nior", "michellibrito@mail.test", "mich",
                                "https://pbs.twimg.com/profile_images/1563898376533557253/iHDy1578_400x400.jpg");

                Participant p13 = new Participant(null, "F??bio Akita", "s??nio", "fabioakita@mail.test", "akit",
                                "https://www.infoq.com/images/profiles/zDdlagNsRtArnva80YsOo70X0CrGzqVd.jpg");

                Participant p14 = new Participant(null, "N??lio Alves", "s??nior", "nelioalves@mail.test", "neli",
                                "https://pbs.twimg.com/profile_images/1276862257209716738/Ae7ZgOdl_400x400.jpg");

                Participant p15 = new Participant(null, "William Suane", "s??nior", "williamsuane@mail.test", "dedo",
                                "https://devdojo.academy/images/Mask-Group-4.png");

                Participant p16 = new Participant(null, "Elemar Junior", "s??nior", "elemarjunior@mail.test", "elem",
                                "https://eximia.co/wp-content/uploads/2022/02/elemar-eximia.png");

                Participant p17 = new Participant(null, "Lucas Montano", "s??nior", "lucasmontano@mail.test", "luca",
                                "https://miro.medium.com/max/2400/2*UbTtj9_xxzLJXTIuDh-qww.png");

                Participant p18 = new Participant(null, "Rodrigo Branas", "s??nior", "rodrigobranas@mail.test", "bran",
                                "https://pbs.twimg.com/profile_images/1422361113941975045/CCz99SUb_400x400.jpg");

                Participant p19 = new Participant(null, "Filipe Deschamps", "s??nior", "filipedeschamps@mail.test",
                                "desc",
                                "https://miro.medium.com/fit/c/224/224/1*9Kr_4fcC_R2SzZkunC5Xwg.jpeg");

                Participant p20 = new Participant(null, "Wesley Willians", "s??nior", "wesleywillians@mail.test", "fucy",
                                "https://media-exp1.licdn.com/dms/image/C5603AQEe_i0cGgw5oA/profile-displayphoto-shrink_400_400/0/1654033409022?e=1668643200&v=beta&t=gsnV5r9HcH5pU7OrpLc2_VQ8jhpAJxHIMAT3QpEA-VM");

                Participant p21 = new Participant(null, "Phill Collins", "s??nior", "phillcollins@mail.test", "phil",
                                "https://upload.wikimedia.org/wikipedia/commons/3/3b/Phil_Collins_1_%28cropped%29.jpg");

                Participant p22 = new Participant(null, "Bryan Adams", "s??nior", "bryanadams@mail.test", "brya",
                                "https://midias.correiobraziliense.com.br/_midias/jpg/2021/11/26/675x450/1_8e6a7961dc34f72fc9193154d9a95213-7121290.jpg?20211126145324?20211126145324");

                Participant p23 = new Participant(null, "James Hatfield", "s??nior", "jameshatfield@mail.test", "jame",
                                "https://pbs.twimg.com/profile_images/563802377644290048/lwaZrs6j_400x400.jpeg");

                Participant p24 = new Participant(null, "Justin Timberlake", "s??nior", "phillcollins@mail.test", "just",
                                "https://www.vagalume.com.br/justin-timberlake/images/justin-timberlake.jpg");

                Participant p25 = new Participant(null, "Adam Levine", "s??nior", "adamlevine@mail.test", "adam",
                                "https://www.google.com/url?sa=i&url=https%3A%2F%2Ffotografia.folha.uol.com.br%2Fgalerias%2F20710-adam-levine&psig=AOvVaw3DJj_OOsLcd6gZDTuIZOfH&ust=1667259844838000&source=images&cd=vfe&ved=0CA0QjRxqFwoTCNDYwYiRifsCFQAAAAAdAAAAABAO");

                Participant p26 = new Participant(null, "Nelly Furtado", "s??nior", "nellyfurtado@mail.test", "nely",
                                "https://www.perfildosfamosos.com/nelly-furtado/foto-perfil-nelly-furtado.jpg?v=54b13499cee1c276806d9c8786134315");

                Participant p27 = new Participant(null, "Madonna", "s??nior", "madonna@mail.test", "mado",
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0d/Rebel_Heart_WTG_%283%29.jpg/200px-Rebel_Heart_WTG_%283%29.jpg");

                Participant p28 = new Participant(null, "Rihanna", "s??nior", "rihanna@mail.test", "riha",
                                "https://f.i.uol.com.br/fotografia/2022/05/19/16529839176286886de359a_1652983917_3x4_md.jpg");

                Participant p29 = new Participant(null, "Beyonce", "s??nior", "beyonce@mail.test", "beyo",
                                "https://ichef.bbci.co.uk/news/640/cpsprodpb/16398/production/_126123019_gettyimages-621520826.jpg");

                Participant p30 = new Participant(null, "Jessie J", "s??nior", "jessiej@mail.test", "jesi",
                                "https://s.yimg.com/ny/api/res/1.2/3xIeaAe8LR2VJQ4KNHrDzQ--/YXBwaWQ9aGlnaGxhbmRlcjt3PTY0MA--/https://s.yimg.com/os/creatr-uploaded-images/2021-11/c0290ea0-4d63-11ec-a7fd-323a93d28803");

                participants = List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18,
                                p19, p20, p21, p22, p23, p24, p25, p26, p27, p28, p29, p30);

                participants.forEach(p -> p = this.participantRepository.save(p));

                {
                        Event e1 = new Event(null, "Reuni??o Agil", dateFrom(10, 10, 2022), dateFrom(11, 12, 2022), "",
                                        null, null, null, false);

                        Activity a1 = new Activity(null, "agil init", dateFrom(11, 10, 2022), dateFrom(17, 10, 2022),
                                        new ArrayList<>(), new ArrayList<>());
                        Activity a2 = new Activity(null, "agil mid", dateFrom(18, 10, 2022), dateFrom(25, 10, 2022),
                                        new ArrayList<>(), new ArrayList<>());
                        Activity a3 = new Activity(null, "agil end", dateFrom(26, 10, 2022), dateFrom(9, 11, 2022),
                                        new ArrayList<>(), new ArrayList<>());

                        {
                                List<Participant> deliveriesA1 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
                                a1.setDelivered(deliveriesA1);
                        }

                        {
                                List<Participant> deliveriesA2 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
                                a2.setDelivered(deliveriesA2);
                        }
                        {
                                List<Participant> deliveriesA3 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
                                a3.setDelivered(deliveriesA3);
                        }
                        e1.setActivities(Arrays.asList(a1, a2, a3));

                        Group g1 = new Group(null, "grupo 1", 0, List.of(p1, p2, p3));
                        Group g2 = new Group(null, "grupo 2", 0, List.of(p4, p5, p6));
                        Group g3 = new Group(null, "grupo 3", 0, List.of(p7, p8, p9));

                        e1.setGroups(Arrays.asList(g1, g2, g3));

                        eventService.save(e1);
                }
                
                {
                        Event e1 = new Event(null, "Reuni??o Tdd", dateFrom(28, 02, 2023), dateFrom(05, 05, 2023), "",
                                        null, null, null, false);

                        Activity a1 = new Activity(null, "tdd planing", dateFrom(1, 03, 2023), dateFrom(15, 04, 2023),
                                        new ArrayList<>(), new ArrayList<>());
                        Activity a2 = new Activity(null, "tdd execution", dateFrom(16, 04, 2023),
                                        dateFrom(25, 04, 2023),
                                        new ArrayList<>(), new ArrayList<>());
                        Activity a3 = new Activity(null, "tdd finalization", dateFrom(26, 04, 2023),
                                        dateFrom(03, 05, 2023),
                                        new ArrayList<>(), new ArrayList<>());

                        {
                                List<Participant> deliveriesA1 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
                                a1.setDelivered(deliveriesA1);
                        }

                        {
                                List<Participant> deliveriesA2 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
                                a2.setDelivered(deliveriesA2);
                        }
                        {
                                List<Participant> deliveriesA3 = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9);
                                a3.setDelivered(deliveriesA3);
                        }
                        e1.setActivities(Arrays.asList(a1, a2, a3));

                        Group g1 = new Group(null, "grupo 1", 0, List.of(p1, p2, p3));
                        Group g2 = new Group(null, "grupo 2", 0, List.of(p4, p5, p6));
                        Group g3 = new Group(null, "grupo 3", 0, List.of(p7, p8, p9));

                        e1.setGroups(Arrays.asList(g1, g2, g3));

                        eventService.save(e1);
                }

        }

}
