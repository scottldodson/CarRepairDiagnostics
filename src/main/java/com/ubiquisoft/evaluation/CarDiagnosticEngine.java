package com.ubiquisoft.evaluation;

import com.ubiquisoft.evaluation.domain.Car;
import com.ubiquisoft.evaluation.domain.ConditionType;
import com.ubiquisoft.evaluation.domain.Part;
import com.ubiquisoft.evaluation.domain.PartType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.Map;

public class CarDiagnosticEngine {

	public void executeDiagnostics(Car car) {
		/*
		 * Implement basic diagnostics and print results to console.
		 *
		 * The purpose of this method is to find any problems with a car's data or parts.
		 *
		 * Diagnostic Steps:
		 *      First   - Validate the 3 data fields are present, if one or more are
		 *                then print the missing fields to the console
		 *                in a similar manner to how the provided methods do.
		 *
		 *      Second  - Validate that no parts are missing using the 'getMissingPartsMap' method in the Car class,
		 *                if one or more are then run each missing part and its count through the provided missing part method.
		 *
		 *      Third   - Validate that all parts are in working condition, if any are not
		 *                then run each non-working part through the provided damaged part method.
		 *                A damaged part is one that has any condition other than NEW, GOOD, or WORN.
		 *
		 *      Fourth  - If validation succeeds for the previous steps then print something to the console informing the user as such.
		 *
		 * Important:
		 *      If any validation fails, complete whatever step you are actively on and end diagnostics early.
		 *
		 * Treat the console as information being read by a user of this application. Attempts should be made to ensure
		 * console output is as least as informative as the provided methods.
		 */

		// validate that the required attributes exist
		if (this.hasMissingRequiredAttributes(car)) {
			exitDiagnostics();
		}

		// validate that there are no missing parts
		if (this.hasMissingParts(car)) {
			exitDiagnostics();
		}

		// validate that there are no damaged parts
		if (this.hasDamagedParts(car)) {
			exitDiagnostics();
		}

		// all validations have passed
		System.out.println("Diagnostics passed successfully");
	}

	private boolean hasMissingRequiredAttributes(Car car) {
		boolean hasMissingRequiredAttributes = false;
		if (car.getYear() == null || car.getYear().isEmpty()) {
			printMissingYear();
			hasMissingRequiredAttributes = true;
		}
		
		if (car.getMake() == null || car.getMake().isEmpty()) {
			printMissingMake();
			hasMissingRequiredAttributes = true;
		}
		
		if (car.getModel() == null || car.getModel().isEmpty()) {
			printMissingModel();
			hasMissingRequiredAttributes = true;
		}
		
		return hasMissingRequiredAttributes;
	}

	private boolean hasMissingParts(Car car) {
		boolean hasMissingParts = false;
		Map<PartType, Integer> missingPartsMap = car.getMissingPartsMap();
		for (Map.Entry<PartType, Integer> missingPart : missingPartsMap.entrySet()) {
			printMissingPart(missingPart.getKey(), missingPart.getValue());
			hasMissingParts = true;
		}
		return hasMissingParts;
	}

	private boolean hasDamagedParts(Car car) {
		boolean hasDamagedParts = false;
		for (Part part : car.getParts()) {
			if ((part.getCondition() != ConditionType.NEW) &&
			    (part.getCondition() != ConditionType.GOOD) &&
			    (part.getCondition() != ConditionType.WORN))
			{
				printDamagedPart(part.getType(), part.getCondition());
				hasDamagedParts = true;
			}
		}
		return hasDamagedParts;
	}
		
	private void printMissingYear() {
		System.out.println("Missing Year Detected");
	}

	private void printMissingMake() {
		System.out.println("Missing Make Detected");
	}

	private void printMissingModel() {
		System.out.println("Missing Model Detected");
	}

	private void printMissingPart(PartType partType, Integer count) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (count == null || count <= 0) throw new IllegalArgumentException("Count must be greater than 0");

		System.out.println(String.format("Missing Part(s) Detected: %s - Count: %s", partType, count));
	}

	private void printDamagedPart(PartType partType, ConditionType condition) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (condition == null) throw new IllegalArgumentException("ConditionType must not be null");

		System.out.println(String.format("Damaged Part Detected: %s - Condition: %s", partType, condition));
	}

	private void exitDiagnostics() {
		System.out.println("Exiting...");
		System.exit(1);
	}
	
	public static void main(String[] args) throws JAXBException {
		// Load classpath resource
		InputStream xml = ClassLoader.getSystemResourceAsStream("SampleCar.xml");

		// Verify resource was loaded properly
		if (xml == null) {
			System.err.println("An error occurred attempting to load SampleCar.xml");

			System.exit(1);
		}

		// Build JAXBContext for converting XML into an Object
		JAXBContext context = JAXBContext.newInstance(Car.class, Part.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Car car = (Car) unmarshaller.unmarshal(xml);

		// Build new Diagnostics Engine and execute on deserialized car object.

		CarDiagnosticEngine diagnosticEngine = new CarDiagnosticEngine();

		diagnosticEngine.executeDiagnostics(car);

	}

}
