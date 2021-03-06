/*
 * $Id$
 * 
 * Copyright (c) 2008-2014 David Muller <roxon@users.sourceforge.net>.
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package org.pwsafe.lib.crypto;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import org.pwsafe.lib.Log;

/**
 * Tries to create a key which is hidden from an occasional onlooker. Uses
 * direct allocated memory to distribute the key data.
 * <p>
 * It's unchecked whether this makes any difference for swapped out memory.
 * 
 * @author roxon
 */
public class InMemoryKey {
	private final static Log LOG = Log.getInstance(InMemoryKey.class);

	private final static int BUFFER_SIZE = 1024;
	final short[] access;
	ByteBuffer buffer;

	public InMemoryKey(short aSize) {
		access = new short[aSize];
	}

	public InMemoryKey(int aSize) {
		this((short) aSize);
	}

	public void init() {
		final byte[] accessBytes = new byte[access.length * 2];
		org.pwsafe.lib.Util.newRandBytes(accessBytes);
		final ShortBuffer accessShorts = ByteBuffer.wrap(accessBytes).asShortBuffer();
		for (int i = 0; i < access.length; i++) {
			access[i] = accessShorts.get(i);
		}

		buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		assert (buffer.isDirect());

		for (int i = 0; i < BUFFER_SIZE; i++) {
			buffer.put(org.pwsafe.lib.Util.newRand());
		}
		buffer.flip();
	}

	public byte[] getKey() {
		if (buffer == null) {
			throw new IllegalStateException("InMemoryKey has not been intialised or been disposed");
		}

		final byte[] content = new byte[8];

		// TODO: use higher bits of short value for content rotate
		for (int i = 0; i < 8; i++) {
			final short pos = access[i];
			content[i] = buffer.get(Math.abs(pos) % BUFFER_SIZE);
		}
		return content;
	}

	public void dispose() {
		if (buffer != null) {
			if (buffer.hasArray()) {
				final byte[] content = buffer.array();
				Arrays.fill(content, (byte) 0);
			}
			buffer = null;
		}
	}

	/**
	 * Can be used to rotate the bytes of the content buffer.
	 * 
	 * @param b
	 * @param distance
	 * @return
	 */
	private byte rotateRight(final byte b, final int distance) {
		return (byte) ((b >>> distance) | (b << -distance));
	}

}
