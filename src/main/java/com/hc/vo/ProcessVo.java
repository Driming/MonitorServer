package com.hc.vo;

public class ProcessVo {
	private String driverName;
	private String driverVersion;
	private Integer pid;
	private Float cpu;
	private Float memory;
	private Network network;
	private Io io;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public void setDriverVersion(String driverVersion) {
		this.driverVersion = driverVersion;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Float getCpu() {
		return cpu;
	}

	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	public Float getMemory() {
		return memory;
	}

	public void setMemory(Float memory) {
		this.memory = memory;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Io getIo() {
		return io;
	}

	public void setIo(Io io) {
		this.io = io;
	}

	@Override
	public String toString() {
		return "ProcessVo [driverName=" + driverName + ", driverVersion=" + driverVersion + ", pid=" + pid + ", cpu="
				+ cpu + ", memory=" + memory + ", network=" + network + ", io=" + io + "]";
	}

	public static class Io {
		private Integer read;
		private Integer write;

		public Integer getRead() {
			return read;
		}

		public void setRead(Integer read) {
			this.read = read;
		}

		public Integer getWrite() {
			return write;
		}

		public void setWrite(Integer write) {
			this.write = write;
		}

		@Override
		public String toString() {
			return "Io [read=" + read + ", write=" + write + "]";
		}

	}

	public static class Network {
		private Integer up;
		private Integer down;

		public Integer getUp() {
			return up;
		}

		public void setUp(Integer up) {
			this.up = up;
		}

		public Integer getDown() {
			return down;
		}

		public void setDown(Integer down) {
			this.down = down;
		}

		@Override
		public String toString() {
			return "Network [up=" + up + ", down=" + down + "]";
		}

	}
}
