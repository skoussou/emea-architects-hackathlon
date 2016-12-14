package emea.summit.architects;

import java.util.Comparator;

public class RequestPayloadComparator implements Comparator<RequestPayload> {

	@Override
	public int compare(RequestPayload p1, RequestPayload p2) {
		String where = p1.getReindeerName().toUpperCase().compareTo(p2.getReindeerName().toUpperCase()) < 0 ? "BEFORE" : "AFTER";
		System.out.println("["+p1.getReindeerName().toUpperCase()+"] <"+where+"> ["+p2.getReindeerName().toUpperCase()+"]");
		return p1.getReindeerName().toUpperCase().compareTo(p2.getReindeerName().toUpperCase());
	}

}
