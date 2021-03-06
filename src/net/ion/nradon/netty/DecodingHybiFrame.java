package net.ion.nradon.netty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import net.ion.nradon.WebSocketHandler;
import net.ion.nradon.helpers.UTF8Output;
import net.ion.radon.core.except.AradonRuntimeException;

import org.jboss.netty.buffer.ChannelBuffer;

public class DecodingHybiFrame {

	private final int opcode;
	private final UTF8Output utf8Output;

	private List<ChannelBuffer> fragments = new ArrayList<ChannelBuffer>();
	private int length;

	public DecodingHybiFrame(int opcode, UTF8Output utf8Output, ChannelBuffer fragment) {
		this.opcode = opcode;
		this.utf8Output = utf8Output;
		append(fragment);
	}

	public void append(ChannelBuffer fragment) {
		fragments.add(fragment);
		length += fragment.readableBytes();
		if (opcode == Opcodes.OPCODE_TEXT || opcode == Opcodes.OPCODE_PONG) {
			utf8Output.write(fragment.array());
		}
	}

	private byte[] messageBytes() {
		byte[] result = new byte[length];
		int offset = 0;
		for (ChannelBuffer fragment : fragments) {
			byte[] array = fragment.array();
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	public void dispatchMessage(final WebSocketHandler handler, final NettyWebSocketConnection connection, final Executor executor, final Thread.UncaughtExceptionHandler exceptionHandler) {
		Thread.UncaughtExceptionHandler exceptionHandlerWithWebbitContext = exceptionHandlerWithConnectionForContext(connection, exceptionHandler);

		switch (opcode) {
		case Opcodes.OPCODE_TEXT: {
			final String messageValue = utf8Output.getStringAndRecycle();
			executor.execute(new CatchingRunnable(exceptionHandlerWithWebbitContext) {
				@Override
				protected void go() throws Throwable {
					handler.onMessage(connection, messageValue);
				}
			});
			return;
		}
		case Opcodes.OPCODE_BINARY: {
			final byte[] bytes = messageBytes();
			executor.execute(new CatchingRunnable(exceptionHandlerWithWebbitContext) {
				@Override
				public void go() throws Throwable {
					handler.onMessage(connection, bytes);
				}
			});
			return;
		}
		case Opcodes.OPCODE_PONG: {
			final String pongValue = utf8Output.getStringAndRecycle();
			executor.execute(new CatchingRunnable(exceptionHandlerWithWebbitContext) {
				@Override
				protected void go() throws Throwable {
					handler.onPong(connection, pongValue);
				}
			});
			return;
		}
		default:
			throw new IllegalStateException("Unexpected opcode:" + opcode);
		}
	}

	// Uncaught exception handler including the connection for context.
	private static Thread.UncaughtExceptionHandler exceptionHandlerWithConnectionForContext(final NettyWebSocketConnection connection, final Thread.UncaughtExceptionHandler exceptionHandler) {
		return new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				exceptionHandler.uncaughtException(t, AradonRuntimeException.fromException(e, connection.getChannel()));
			}
		};
	}

	private abstract class CatchingRunnable implements Runnable {
		private final Thread.UncaughtExceptionHandler exceptionHandler;

		public CatchingRunnable(Thread.UncaughtExceptionHandler exceptionHandler) {
			this.exceptionHandler = exceptionHandler;
		}

		public void run() {
			try {
				go();
			} catch (Throwable t) {
				exceptionHandler.uncaughtException(Thread.currentThread(), t);
			}
		}

		protected abstract void go() throws Throwable;
	}
}
