package com.parking_lot;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

class Car{
	private String registration_number;
	private String color_of_car;
	
	public Car(String registrationNumber , String colorOfCar) {
		this.registration_number = registrationNumber;
		this.color_of_car = colorOfCar;
	}
	
	public String getColorOfCar() {
		return this.color_of_car;
	}
	
	public String getRegistrationNumber() {
		return this.registration_number;
	}
}

public class ParkingLot {
	private static Scanner sc = new Scanner(System.in);
	private static String input;
	private static String[] inputBreakdown;
	private static int numberOfSlots;
	private static String registrationNumber;
	private static String colorOfCar;
	private static int slotNumber;
	private static TreeMap<Integer,Car> parkingSlots;
	
	
	public static void main(String []args) {
		runCommands();
	}

	private static void runCommands() {
		
		
		input = sc.nextLine();
		inputBreakdown = input.split(" ");
		switch (inputBreakdown[0]) {
		
		case "create_parking_lot": 
								   if(parkingSlots == null) {
									   numberOfSlots = Integer.parseInt(inputBreakdown[1]);
									   createParkingLot();
									   runCommands();
								   }else {
									   System.out.println("Parking Slot Already Created With "+numberOfSlots+" Slots");
									   runCommands();
								   }
			break;
			
		case "park" : if(parkingSlots == null) {
						System.out.println("Parking Slot not created yet.... ");
						runCommands();
					 }else {
						 registrationNumber = inputBreakdown[1];
						 colorOfCar = inputBreakdown[2];
						 park();
						 runCommands();
					 }
		    break;
		
		
		case "leave" :  if(parkingSlots == null) {
							System.out.println("Parking Slot not created yet.... ");
							runCommands();
						}else {
							slotNumber = Integer.parseInt(inputBreakdown[1]);
							leave();
							runCommands();
						}	
		    break;
		
		
		case "status" : if(parkingSlots == null) {
							System.out.println("Parking Slot not created yet.... ");
							runCommands();
						}else {
							status();
							runCommands();
						}
		    break;
		
		
		case "registration_numbers_for_cars_with_colour" : if(parkingSlots == null) {
																System.out.println("Parking Slot not created yet.... ");
																runCommands();
															}else {
																colorOfCar = inputBreakdown[1];
																registrationNumberForCarsWithcolour();
																runCommands();
															}
		    break;
		
		
		case "slot_numbers_for_cars_with_colour" : if(parkingSlots == null) {
														System.out.println("Parking Slot not created yet.... ");
														runCommands();
													}else {
														colorOfCar = inputBreakdown[1];
														slotNumbersForCarsWithColour();
														runCommands();
													}
		    break;
		
		
		case "slot_number_for_registration_number" : if(parkingSlots == null) {
															System.out.println("Parking Slot not created yet.... ");
															runCommands();
													}else {
														registrationNumber = inputBreakdown[1];
														SlotNumberForRegistrationNumber();
														runCommands();
													}
			
		    break;
		 
		
		case "exit"  : System.out.println("Exiting the program.....");
			break;
		
		
		default: System.out.println("Wrong command use proper commands");
				 runCommands();
			break;
		}
	}

	
	// TO print slot of a given Registration Number
	private static void SlotNumberForRegistrationNumber() {
		
		Set<Integer> slotNumbers = parkingSlots.keySet();
		boolean foundRegistrationNumber = false;
		for (Integer slotNumber : slotNumbers) {
			
			if(parkingSlots.get(slotNumber).getRegistrationNumber().equals(registrationNumber)) {
				System.out.println(slotNumber);
				foundRegistrationNumber = true;
				break;
			}
		}
		
		if(!foundRegistrationNumber) {
			System.out.println("Not found");
		}
	}

	// TO print slots of a given color car
	private static void slotNumbersForCarsWithColour() {
		
		Set<Integer> slotNumbers = parkingSlots.keySet();
		String slotNum = "";
		for (Integer slotNumber : slotNumbers) {
			
			if(parkingSlots.get(slotNumber).getColorOfCar().equals(colorOfCar)) {				
				slotNum+=(slotNumber+", ");
			}
		}
		slotNum = slotNum.substring(0, slotNum.length()-2);
		System.out.println(slotNum);
	}

	
	// To print registration numbers using color of car
	private static void registrationNumberForCarsWithcolour() {
		Set<Integer> slotNumbers = parkingSlots.keySet();
		String registrationNumbers = "";
		for (Integer slotNumber : slotNumbers) {
			
			if(parkingSlots.get(slotNumber).getColorOfCar().equals(colorOfCar)) {				
				registrationNumbers+=(parkingSlots.get(slotNumber).getRegistrationNumber()+", ");
			}
		}
		registrationNumbers = registrationNumbers.substring(0, registrationNumbers.length()-2);
		System.out.println(registrationNumbers);
	}

	
	// To vacate the parking lot using slot number
	private static void leave() {
		if(slotNumber<0 && slotNumber>numberOfSlots) {
			System.out.println("Enter  appropriate command with right values");
		}else {
			parkingSlots.remove(slotNumber);
			System.out.println("Slot number "+ slotNumber +" is free");
		}
	}

	
	// To print the status of parking lot
	private static void status() {
		
		System.out.printf("%-10s%-20s%-15s","Slot No.","Registration No.","Color");
		System.out.println();
		Set <Integer> slotNumbers = parkingSlots.keySet();
		for (Integer slotNumber : slotNumbers) {
			System.out.printf("%-10s%-20s%-15s",slotNumber,registrationNumber,colorOfCar);
			System.out.println();
		}
			
		
	}

	
	// To park the car to nearest empty parking lot
	private static void park() {
		//checking if parking lot is full
		if(parkingSlots.size()==numberOfSlots) {   
			System.out.println("Sorry, parking lot is full");   
		}else {
			
			Car car  = new Car(registrationNumber , colorOfCar);
			for(int slotNumber=1;  slotNumber<=numberOfSlots;  slotNumber++) {
				
				//checking slots which are empty from the start
				if(!parkingSlots.containsKey(slotNumber)) {
					
					//inserting the car object in the empty parking lot
					parkingSlots.put(slotNumber, car);
					System.out.println("Allocated slot number: "+slotNumber);
					break;
				}
					
			}
			
		}
	}

	
	// Method to create parking lot with slot length greater than zero 
	private static void createParkingLot() {
		if(numberOfSlots == 0) {
			System.out.println("Can't create a Parking slot with " +numberOfSlots+ " slot. Enter  appropriate command with right values");
		}else {
			parkingSlots = new TreeMap<Integer,Car>();
			System.out.println("Created a parking lot with "+numberOfSlots+" slots");
		}
	}
}
