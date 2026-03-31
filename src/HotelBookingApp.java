import java.util.*;
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            confirmedReservations.add(reservation);
        }
    }

    public List<Reservation> getReservations() {
        return confirmedReservations;
    }
}

class BookingReportService {

    public void generateReport(BookingHistory history) {

        List<Reservation> reservations = history.getReservations();

        System.out.println("\nBooking History Report");
        System.out.println("----------------------");

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }

        System.out.println("\nTotal Bookings: " + reservations.size());
    }
}

public class HotelBookingApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("Alice", "Deluxe", 2));
        history.addReservation(new Reservation("Bob", "Suite", 3));
        history.addReservation(new Reservation("Charlie", "Standard", 1));

        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}