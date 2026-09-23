import java.util.Scanner;

// ==========================================
// Vehicle Class
// ==========================================
class Vehicle {

    // Encapsulated attributes
    private String plateNumber;
    private String ownerName;
    private String type;

    // Constructor
    public Vehicle(String plateNumber, String ownerName, String type) {
        this.plateNumber = plateNumber;
        this.ownerName = ownerName;
        this.type = type;
    }

    // Getters
    public String getPlateNumber() {
        return plateNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getType() {
        return type;
    }
}


// ==========================================
// ParkingSlot Class
// ==========================================
class ParkingSlot {

    private int slotNumber;
    private String slotType;
    private boolean occupied;
    private Vehicle parkedVehicle;

    // Constructor
    public ParkingSlot(int slotNumber, String slotType) {
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.occupied = false;
        this.parkedVehicle = null;
    }

    // Getters
    public int getSlotNumber() {
        return slotNumber;
    }

    public String getSlotType() {
        return slotType;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    // Check if vehicle can use this slot
    public boolean isCompatible(Vehicle vehicle) {

        if (vehicle.getType().equals("MOTORCYCLE")) {

            // Motorcycle can use M or C
            return slotType.equals("M");

        } else if (vehicle.getType().equals("CAR")) {

            // Car can only use C
            return slotType.equals("C");
        }

        return false;
    }

    // Park vehicle in this slot
    public void parkVehicle(Vehicle vehicle) {
        parkedVehicle = vehicle;
        occupied = true;
    }

    // Remove vehicle from slot
    public void removeVehicle() {
        parkedVehicle = null;
        occupied = false;
    }
}


// ==========================================
// Garage Class
// ==========================================
class Garage {

    // Has-a relationship:
    // Garage has ParkingSlot objects
    private ParkingSlot[] slots;

    // Static field
    private static int vehicleCount = 0;

    // Constructor
    public Garage(ParkingSlot[] slots) {
        this.slots = slots;
    }

    // ==========================================
    // Park Vehicle
    // ==========================================
    public void park(Vehicle vehicle) {

        // Check duplicate plate number
        for (int i = 0; i < slots.length; i++) {

            if (slots[i].isOccupied()
                    && slots[i].getParkedVehicle()
                    .getPlateNumber()
                    .equalsIgnoreCase(vehicle.getPlateNumber())) {

                System.out.println(
                    "Parking rejected: vehicle is already parked."
                );
                return;
            }
        }

        // Search for first compatible free slot
        for (int i = 0; i < slots.length; i++) {

            if (!slots[i].isOccupied()
                    && slots[i].isCompatible(vehicle)) {

                slots[i].parkVehicle(vehicle);

                vehicleCount++;

                System.out.println(
                    vehicle.getPlateNumber()
                    + " -> slot "
                    + slots[i].getSlotNumber()
                );

                return;
            }
        }

        // No compatible slot found
        System.out.println(
            "Parking rejected: no compatible slot."
        );
    }

    // ==========================================
    // Exit Vehicle
    // ==========================================
    public void exit(String plate, int hours) {

        // Hours must be at least 1
        if (hours < 1) {
            System.out.println(
                "Exit rejected: hours must be at least 1."
            );
            return;
        }

        // Search for vehicle
        for (int i = 0; i < slots.length; i++) {

            if (slots[i].isOccupied()
                    && slots[i].getParkedVehicle()
                    .getPlateNumber()
                    .equalsIgnoreCase(plate)) {

                Vehicle vehicle = slots[i].getParkedVehicle();

                double fee = calculateFee(
                    vehicle.getType(),
                    hours
                );

                System.out.printf(
                    "%s exited. Fee = PHP %.2f%n",
                    vehicle.getPlateNumber(),
                    fee
                );

                // Free the slot
                slots[i].removeVehicle();

                vehicleCount--;

                return;
            }
        }

        // Vehicle not found
        System.out.println(
            "Exit rejected: vehicle not found."
        );
    }

    // ==========================================
    // Calculate Parking Fee
    // ==========================================
    private double calculateFee(String type, int hours) {

        if (type.equals("MOTORCYCLE")) {

            // First hour = 20
            // Additional hour = 10
            return 20 + (hours - 1) * 10;

        } else if (type.equals("CAR")) {

            // First hour = 40
            // Additional hour = 20
            return 40 + (hours - 1) * 20;
        }

        return 0;
    }

    // ==========================================
    // Get Occupancy
    // ==========================================
    public static int getVehicleCount() {
        return vehicleCount;
    }

    // ==========================================
    // Display Final Occupancy
    // ==========================================
    public void displayOccupancy() {

        System.out.println();
        System.out.println("===== FINAL OCCUPANCY =====");

        System.out.println(
            "Vehicles parked: " + vehicleCount
        );

        System.out.println();
        System.out.println("===== OCCUPIED SLOTS =====");

        boolean hasOccupiedSlot = false;

        // Search through all slots
        for (int i = 0; i < slots.length; i++) {

            if (slots[i].isOccupied()) {

                hasOccupiedSlot = true;

                Vehicle vehicle = slots[i].getParkedVehicle();

                System.out.println(
                    "Slot " + slots[i].getSlotNumber()
                    + " (" + slots[i].getSlotType() + ")"
                    + " -> "
                    + vehicle.getPlateNumber()
                    + " - "
                    + vehicle.getOwnerName()
                    + " - "
                    + vehicle.getType()
                );
            }
        }

        if (!hasOccupiedSlot) {
            System.out.println("No occupied slots.");
        }
    }
}


// ==========================================
// Main Class
// ==========================================
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ==========================================
        // Create Parking Slots
        // ==========================================

        System.out.print("Enter number of parking slots: ");
        int slotCount = input.nextInt();

        ParkingSlot[] slots = new ParkingSlot[slotCount];

        // Loop to create slots
        for (int i = 0; i < slotCount; i++) {

            System.out.print("Enter slot number: ");
            int slotNumber = input.nextInt();

            System.out.print("Enter slot type (M/C): ");
            String slotType = input.next().toUpperCase();

            slots[i] = new ParkingSlot(
                slotNumber,
                slotType
            );
        }

        // Create Garage
        Garage garage = new Garage(slots);

        // ==========================================
        // Operations
        // ==========================================

        System.out.print("Enter number of operations: ");
        int operations = input.nextInt();

        for (int i = 0; i < operations; i++) {

            System.out.println();
            System.out.print("Enter operation (P/E): ");
            String operation = input.next().toUpperCase();

            // ======================================
            // PARK
            // ======================================
            if (operation.equals("P")) {

                System.out.print("Enter plate number: ");
                String plate = input.next();

                System.out.print("Enter owner name: ");
                String owner = input.next();

                System.out.print(
                    "Enter vehicle type (MOTORCYCLE/CAR): "
                );
                String type = input.next().toUpperCase();

                Vehicle vehicle = new Vehicle(
                    plate,
                    owner,
                    type
                );

                garage.park(vehicle);
            }

            // ======================================
            // EXIT
            // ======================================
            else if (operation.equals("E")) {

                System.out.print("Enter plate number: ");
                String plate = input.next();

                System.out.print("Enter hours parked: ");
                int hours = input.nextInt();

                garage.exit(plate, hours);
            }

            // ======================================
            // INVALID OPERATION
            // ======================================
            else {
                System.out.println(
                    "Invalid operation."
                );
            }
        }

        // ==========================================
        // Final Output
        // ==========================================

        garage.displayOccupancy();

        input.close();
    }
}
