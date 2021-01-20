package havis.net.ui.middleware.client.shared.trigger.model;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import havis.net.ui.middleware.client.utils.Utils;

public class RtcTrigger extends Trigger {

	private final static int GROUP_PERIOD = 1;
	private final static int GROUP_OFFSET = 2;
	private final static int GROUP_TIMEZONE = 4;
	private final static String pattern = "^urn:epcglobal:ale:trigger:rtc:(\\d+)\\.(\\d+)(\\.(Z|([+-]\\d{2}:\\d{2})))?$";
	private final static String base = "urn:epcglobal:ale:trigger:rtc:";

	private long periodHours;
	private long periodMinutes;
	private long periodSeconds;
	private long periodMilliseconds;
	private long offsetHours;
	private long offsetMinutes;
	private long offsetSeconds;
	private long offsetMilliseconds;
	private String timezone;

	public RtcTrigger() {
		this.scheme = TriggerScheme.EPC_GLOBAL;
		this.type = TriggerType.RTC;
	}

	public RtcTrigger(String uri) {
		this.scheme = TriggerScheme.EPC_GLOBAL;
		this.type = TriggerType.RTC;

		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(uri);
		if (result != null) {
			try {
				long period = Long.parseLong(result.getGroup(GROUP_PERIOD));
				this.periodHours = period / 3600000;
				period %= 3600000;
				this.periodMinutes = period / 60000;
				period %= 60000;
				this.periodSeconds = period / 1000;
				period %= 1000;
				this.periodMilliseconds = period;
			} catch (NumberFormatException e) {
			}
			try {
				long offset = Long.parseLong(result.getGroup(GROUP_OFFSET));
				this.offsetHours = offset / 3600000;
				offset %= 3600000;
				this.offsetMinutes = offset / 60000;
				offset %= 60000;
				this.offsetSeconds = offset / 1000;
				offset %= 1000;
				this.offsetMilliseconds = offset;
			} catch (NumberFormatException e) {
			}
			this.timezone = result.getGroup(GROUP_TIMEZONE);
		}
	}

	public long getPeriodHours() {
		return periodHours;
	}

	public void setPeriodHours(long periodHours) {
		this.periodHours = periodHours;
	}

	public long getPeriodMinutes() {
		return periodMinutes;
	}

	public void setPeriodMinutes(long periodMinutes) {
		this.periodMinutes = periodMinutes;
	}

	public long getPeriodSeconds() {
		return periodSeconds;
	}

	public void setPeriodSeconds(long periodSeconds) {
		this.periodSeconds = periodSeconds;
	}

	public long getPeriodMilliseconds() {
		return periodMilliseconds;
	}

	public void setPeriodMilliseconds(long periodMilliseconds) {
		this.periodMilliseconds = periodMilliseconds;
	}

	public long getOffsetHours() {
		return offsetHours;
	}

	public void setOffsetHours(long offsetHours) {
		this.offsetHours = offsetHours;
	}

	public long getOffsetMinutes() {
		return offsetMinutes;
	}

	public void setOffsetMinutes(long offsetMinutes) {
		this.offsetMinutes = offsetMinutes;
	}

	public long getOffsetSeconds() {
		return offsetSeconds;
	}

	public void setOffsetSeconds(long offsetSeconds) {
		this.offsetSeconds = offsetSeconds;
	}

	public long getOffsetMilliseconds() {
		return offsetMilliseconds;
	}

	public void setOffsetMilliseconds(long offsetMilliseconds) {
		this.offsetMilliseconds = offsetMilliseconds;
	}

	@Override
	public String toUri() {
		return base + getPeriod() + "." + getOffset() + (Utils.isNullOrEmpty(timezone) ? "" : "." + timezone);
	}

	public long getPeriod() {
		return periodHours * 3600000 + periodMinutes * 60000 + periodSeconds * 1000 + periodMilliseconds;
	}

	public long getOffset() {
		return offsetHours * 3600000 + offsetMinutes * 60000 + offsetSeconds * 1000 + offsetMilliseconds;
	}
}
