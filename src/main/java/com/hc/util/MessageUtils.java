package com.hc.util;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {
	private static Map<String, Object> mess = null;

	public static Map<String, Object> returnState(final int code, final String message, final Object data) {
		mess = new HashMap<String, Object>() {
			private static final long serialVersionUID = 7682459266377451105L;
			{
				put("stateCode", code);
				put("message", message);
				put("data", data);
			}
		};
		return mess;
	}

	public static Map<String, Object> deleteSuccess() {
		return returnState(0, "Delete success", null);
	}

	public static Map<String, Object> updateSuccess() {
		return returnState(0, "Update success", null);
	}

	public static Map<String, Object> insertSuccess() {
		return returnState(0, "Add success", null);
	}

	public static Map<String, Object> returnSuccess(final Object data) {
		return returnState(0, "Request success", data);
	}

	public static Map<String, Object> operationSuccess() {
		return returnState(0, "Operation success", null);
	}
	
	public static Map<String, Object> logoutSuccess() {
		return returnState(0, "Logout success", null);
	}
	
	public static Map<String, Object> loginSuccess() {
		return returnState(0, "Login success", null);
	}

	public static Map<String, Object> timeError() {
		return returnState(-1, "The time format does not match", null);
	}

	public static Map<String, Object> parameterNullError() {
		return returnState(-2, "Parameter can not be null", null);
	}

	public static Map<String, Object> parameterNotStandardValueError() {
		return returnState(-3, "Some parameters need to pass the specified value", null);
	}

	public static Map<String, Object> fileNotFountError() {
		return returnState(-4, "File not found", null);
	}

	public static Map<String, Object> parserFileDatetimeError() {
		return returnState(-5, "There was an error parsing the file time", null);
	}

	public static Map<String, Object> pageOutOfRange() {
		return returnState(-6, "Request page number is out of range", null);
	}

	public static Map<String, Object> imgNotFountError() {
		return returnState(-7, "Img not found, or Img is wrong", null);
	}

	public static Map<String, Object> dataFormatError() {
		return returnState(-8, "Data format error", null);
	}

	public static Map<String, Object> operationFailedError() {
		return returnState(-9, "Operation failed", null);
	}

	public static Map<String, Object> dataHasExistError() {
		return returnState(-10, "Data Has exist", null);
	}

	public static Map<String, Object> notLoginError() {
		return returnState(-11, "Not login error", null);
	}

	public static Map<String, Object> requestPageError() {
		return returnState(-12, "Request page not in range", null);
	}
	
	public static Map<String, Object> loginError() {
		return returnState(-13, "Wrong user name or password", null);
	}
	
	public static Map<String, Object> permissionDeniedError() {
		return returnState(-14, "Permission denied", null);
	}
	
	public static Map<String, Object> verificationCodeFailureError() {
		return returnState(-15, "Verification code failure", null);
	}
	
	public static Map<String, Object> captchaCodeFailureError() {
		return returnState(-16, "Capatcha failure", null);
	}
	
	public static Map<String, Object> phoneMessageTooFrequentError() {
		return returnState(-16, "Phone message too frequent", null);
	}
	
	public static Map<String, Object> otherUserWorkingThisTaskError() {
		return returnState(-17, "Other users are working on this task", null);
	}
	
	public static Map<String, Object> jarFileNotFoundError() {
		return returnState(-18, "Jar file not found", null);
	}

	public static Map<String, Object> notDataError() {
		return returnState(-99, "No data", null);
	}

	public static Map<String, Object> writeMessage(final String key, final Object value) {
		mess = new HashMap<String, Object>() {
			private static final long serialVersionUID = -6588484225065440446L;
			{
				put(key, value);
			}
		};
		return mess;
	}

	public static Map<String, Object> getMessage(final String message, final int code) {
		mess = new HashMap<String, Object>() {
			private static final long serialVersionUID = 8355750238341828550L;
			{
				put("stateCode", code);
				put("desc", message);
			}
		};
		return mess;
	}

	public static Map<String, Object> getMessageWithEntity(final String message, final int code, final Object entity) {
		mess = new HashMap<String, Object>() {
			private static final long serialVersionUID = 8355750238341828550L;
			{
				put("stateCode", code);
				put("desc", message);
				put("data", entity);
			}
		};
		return mess;
	}

}
