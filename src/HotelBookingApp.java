import java.util.*;

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomInventory {

    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("DELUXE", 2);
        rooms.put("SUITE", 1);
        rooms.put("STANDARD", 3);
    }

    public boolean isAvailable(String roomType) {
        return rooms.containsKey(roomType) && rooms.get(roomType) > 0;
    }

    public void bookRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) - 1);
    }
}

class ReservationValidator {

    public void validate(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        roomType = roomType.toUpperCase();

        if (!inventory.isAvailable(roomType)) {
            throw new InvalidBookingException("Invalid or unavailable room type selected.");
        }
    }
}

public class HotelBookingApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();

        try {
            System.out.println("Booking Validation");

            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (DELUXE/SUITE/STANDARD): ");
            String roomType = scanner.nextLine();

            validator.validate(name, roomType, inventory);

            inventory.bookRoom(roomType.toUpperCase());

            System.out.println("Booking successful!");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}