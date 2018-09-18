package com.hc.entity.collection.history.extra;

public class TaskSchedule {
	private Integer node;
	private Integer nodeResult;

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public Integer getNodeResult() {
		return nodeResult;
	}

	public void setNodeResult(Integer nodeResult) {
		this.nodeResult = nodeResult;
	}

	@Override
	public String toString() {
		return "TaskSchedule [node=" + node + ", nodeResult=" + nodeResult + "]";
	}

}
