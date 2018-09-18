package com.hc.util.verification.base;

public interface BaseTaskModifyFalg {
	boolean isWritabled(String csid, String ctid);
	
	void writeTrue(String csid, String ctid);
}
