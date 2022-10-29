package tech.antoniosgarbi.desafiomvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "tb_attendance_list")
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceList implements Comparable<AttendanceList> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    
    public boolean isInThePast() {
        return this.date.compareTo(new Date()) < 0;
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
    }

    @Override
    public int compareTo(AttendanceList attendanceList) {
        return this.date.compareTo(attendanceList.getDate());
    }

}
