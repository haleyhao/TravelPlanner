package entity;

public class PlanIdGenerator {
	public static String getUsableId() {
		return String.valueOf(System.currentTimeMillis());
	}
}
