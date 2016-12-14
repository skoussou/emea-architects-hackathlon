package emea.summit.architects;

import java.util.Comparator;

public class RequestPayloadComparator implements Comparator<RequestPayload> {

	@Override
	public int compare(RequestPayload p1, RequestPayload p2) {
		return p1.getReindeerName().compareTo(p2.getReindeerName());
	}

}
