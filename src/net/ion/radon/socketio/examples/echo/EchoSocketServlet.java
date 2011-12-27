/**
 * The MIT License
 * Copyright (c) 2010 Tad Glines
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.ion.radon.socketio.examples.echo;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.ion.radon.socketio.common.DisconnectReason;
import net.ion.radon.socketio.server.SocketIOInbound;
import net.ion.radon.socketio.server.SocketIOServlet;


public class EchoSocketServlet extends SocketIOServlet {
	private static final long serialVersionUID = 1L;
	private Set<EchoConnection> connections = new HashSet<EchoConnection>();

	private class EchoConnection implements SocketIOInbound {
		private SocketIOOutbound outbound = null;

		public String getProtocol() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onConnect(SocketIOOutbound outbound) {
			this.outbound = outbound;
			synchronized (connections) {
				connections.add(this);
			}
		}

		public void onDisconnect(DisconnectReason reason, String errorMessage) {
			synchronized(this) {
				this.outbound = null;
			}
			synchronized (connections) {
				connections.remove(this);
			}
		}

		public void onMessage(int messageType, String message) {
			try {
				outbound.sendMessage(messageType, message);
			} catch (IOException e) {
				outbound.disconnect();
			}
		}
	}

	@Override
	protected SocketIOInbound doSocketIOConnect(HttpServletRequest request) {
		return new EchoConnection();
	}

}
