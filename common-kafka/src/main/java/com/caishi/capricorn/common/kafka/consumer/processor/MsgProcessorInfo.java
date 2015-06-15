package com.caishi.capricorn.common.kafka.consumer.processor;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by apple on 15/6/15.
 * consumer processor config
 */
public class MsgProcessorInfo {

	public static final int DEFAULT_THREAD_NUM = 1;

	private String topic;
	private String groupId;
	private int threadNum;

	public MsgProcessorInfo(String topic, String groupId) {
		this.topic = topic;
		this.groupId = groupId;
		this.threadNum = DEFAULT_THREAD_NUM;
	}

	public MsgProcessorInfo(String topic, String groupId, int threadNum) {
		this.topic = topic;
		this.groupId = groupId;
		this.threadNum = threadNum;
	}

	public void validate() {
		if (StringUtils.isBlank(topic)) {
			throw new IllegalArgumentException("kafka topic is null.");
		}
		if (StringUtils.isBlank(groupId)) {
			throw new IllegalArgumentException(String.format("kafka group id for %s is null.", topic));
		}
		if (threadNum < 0) {
			throw new IllegalArgumentException(String.format("kafka thread number for %s is less than 0.", topic));
		}
	}

	public static int getDefaultThreadNum() {
		return DEFAULT_THREAD_NUM;
	}

	public String getTopic() {
		return topic;
	}

	public String getGroupId() {
		return groupId;
	}

	public int getThreadNum() {
		return threadNum;
	}

	@Override
	public String toString() {
		return "MsgProcessorInfo{" +
				"topic='" + topic + '\'' +
				", groupId='" + groupId + '\'' +
				", threadNum=" + threadNum +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MsgProcessorInfo that = (MsgProcessorInfo) o;

		if (topic != null ? !topic.equals(that.topic) : that.topic != null) return false;
		return !(groupId != null ? !groupId.equals(that.groupId) : that.groupId != null);

	}

	@Override
	public int hashCode() {
		int result = topic != null ? topic.hashCode() : 0;
		result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
		return result;
	}
}
