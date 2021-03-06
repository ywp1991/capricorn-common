/*
 * Copyright 2013 sohu.com All right reserved. This software is the
 * confidential and proprietary information of sohu.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with sohu.com.
 */
package com.caishi.capricorn.common.filter;


import com.caishi.capricorn.common.filter.hash.Hash;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.BitSet;

/**
 * 参考自org.apache.hadoop.util.bloom.BloomFilter
 *
 * @see <a href="http://portal.acm.org/citation.cfm?id=362692&dl=ACM&coll=portal">Space/Time Trade-Offs in Hash Coding with Allowable Errors</a>
 */
public class BloomFilter extends Filter {
	private static final byte[] bitvalues = new byte[]{
			(byte) 0x01,
			(byte) 0x02,
			(byte) 0x04,
			(byte) 0x08,
			(byte) 0x10,
			(byte) 0x20,
			(byte) 0x40,
			(byte) 0x80
	};

	/**
	 * The bit vector.
	 */
	BitSet bits;

	/**
	 * Default constructor - use with readFields
	 */
	public BloomFilter() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param vectorSize The vector size of <i>this</i> filter.
	 * @param nbHash     The number of hash function to consider.
	 * @param hashType   type of the hashing function (see
	 *                   {@link Hash}).
	 */
	public BloomFilter(int vectorSize, int nbHash, int hashType) {
		super(vectorSize, nbHash, hashType);

		bits = new BitSet(this.vectorSize);
	}

	@Override
	public void add(byte[] key) {
		if (key == null) {
			throw new NullPointerException("key cannot be null");
		}

		int[] h = hash.hash(key);
		hash.clear();

		for (int i = 0; i < nbHash; i++) {
			bits.set(h[i]);
		}
	}

	@Override
	public void and(Filter filter) {
		if (filter == null
				|| !(filter instanceof BloomFilter)
				|| filter.vectorSize != this.vectorSize
				|| filter.nbHash != this.nbHash) {
			throw new IllegalArgumentException("filters cannot be and-ed");
		}

		this.bits.and(((BloomFilter) filter).bits);
	}

	@Override
	public boolean membershipTest(byte[] key) {
		if (key == null) {
			throw new NullPointerException("key cannot be null");
		}

		int[] h = hash.hash(key);
		hash.clear();
		for (int i = 0; i < nbHash; i++) {
			if (!bits.get(h[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void not() {
		bits.flip(0, vectorSize - 1);
	}

	@Override
	public void or(Filter filter) {
		if (filter == null
				|| !(filter instanceof BloomFilter)
				|| filter.vectorSize != this.vectorSize
				|| filter.nbHash != this.nbHash) {
			throw new IllegalArgumentException("filters cannot be or-ed");
		}
		bits.or(((BloomFilter) filter).bits);
	}

	@Override
	public void xor(Filter filter) {
		if (filter == null
				|| !(filter instanceof BloomFilter)
				|| filter.vectorSize != this.vectorSize
				|| filter.nbHash != this.nbHash) {
			throw new IllegalArgumentException("filters cannot be xor-ed");
		}
		bits.xor(((BloomFilter) filter).bits);
	}

	@Override
	public String toString() {
		return bits.toString();
	}

	/**
	 * @return size of the the bloomfilter
	 */
	public int getVectorSize() {
		return this.vectorSize;
	}

	// Writable

	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		byte[] bytes = new byte[getNBytes()];
		for (int i = 0, byteIndex = 0, bitIndex = 0; i < vectorSize; i++, bitIndex++) {
			if (bitIndex == 8) {
				bitIndex = 0;
				byteIndex++;
			}
			if (bitIndex == 0) {
				bytes[byteIndex] = 0;
			}
			if (bits.get(i)) {
				bytes[byteIndex] |= bitvalues[bitIndex];
			}
		}
		out.write(bytes);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		bits = new BitSet(this.vectorSize);
		byte[] bytes = new byte[getNBytes()];
		in.readFully(bytes);
		for (int i = 0, byteIndex = 0, bitIndex = 0; i < vectorSize; i++, bitIndex++) {
			if (bitIndex == 8) {
				bitIndex = 0;
				byteIndex++;
			}
			if ((bytes[byteIndex] & bitvalues[bitIndex]) != 0) {
				bits.set(i);
			}
		}
	}

	/* @return number of bytes needed to hold bit vector */
	private int getNBytes() {
		return (vectorSize + 7) / 8;
	}


}
