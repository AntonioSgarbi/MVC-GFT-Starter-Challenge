package tech.antoniosgarbi.desafiomvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceList implements Comparable<AttendanceList> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @ManyToMany
    private List<Participant> participantsWerePresent;
    @ManyToMany
    private List<Participant> participantsArrivedLate;

    public String getTitle() {
        return ViewHelper.getFormattedDate(this.date);
    }

    public String getDayInPortuguese() {
        return String.valueOf(ViewHelper.getDayInPortuguese(ViewHelper.getDayOfWeek(date)));
    }

    static class ViewHelper {

        static int getDayOfWeek(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal.get(Calendar.DAY_OF_WEEK);
        }

        static int getDayOfMonth(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal.get(Calendar.DAY_OF_MONTH);
        }

        static int getMonth(Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal.get(Calendar.MONTH);
        }

        static String getFormattedDate(Date date) {
            int month = getMonth(date) + 1;
            return String.valueOf(getDayOfMonth(date)).concat("/").concat(String.valueOf(month));
        }

        static private String getDayInPortuguese(int day) {
            String dayInPortuguese = "";
            switch (day) {
                case 1 -> dayInPortuguese = "Domingo";
                case 2 -> dayInPortuguese = "Segunda";
                case 3 -> dayInPortuguese = "Terça";
                case 4 -> dayInPortuguese = "Quarta";
                case 5 -> dayInPortuguese = "Quinta";
                case 6 -> dayInPortuguese = "Sexta";
                case 7 -> dayInPortuguese = "Sábado";
            }
            return dayInPortuguese;
        }

        static private String monthInPortuguese(int month) {
            String monthInPortuguese = "";
            switch (month) {
                case 1 -> monthInPortuguese = "Janeiro";
                case 2 -> monthInPortuguese = "Fevereiro";
                case 3 -> monthInPortuguese = "Março";
                case 4 -> monthInPortuguese = "Abril";
                case 5 -> monthInPortuguese = "Maio";
                case 6 -> monthInPortuguese = "Junho";
                case 7 -> monthInPortuguese = "Julho";
                case 8 -> monthInPortuguese = "Agosto";
                case 9 -> monthInPortuguese = "Setembro";
                case 10 -> monthInPortuguese = "Outubro";
                case 11 -> monthInPortuguese = "Novembro";
                case 12 -> monthInPortuguese = "Dezembro";
            }
            return monthInPortuguese;
        }
    }

    @Override
    public int compareTo(AttendanceList attendanceList) {
        return this.date.compareTo(attendanceList.getDate());
    }

}
