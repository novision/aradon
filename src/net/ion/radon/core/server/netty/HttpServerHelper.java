/**
 * Copyright 2005-2011 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package net.ion.radon.core.server.netty;

import net.ion.radon.core.server.netty.internal.HttpServerPipelineFactory;

import org.jboss.netty.channel.ChannelPipelineFactory;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Netty HTTP server connector. 
 * 
 * @see <a href="http://jboss.org/netty/">Netty home page</a>
 * @author Gabriel Ciuloaica (gciuloaica@gmail.com)
 */
public class HttpServerHelper extends NettyServerHelper {

    /**
     * Constructor.
     * 
     * @param server
     *            The helped server instance.
     */
    public HttpServerHelper(Server server) {
        super(server);
        getProtocols().add(Protocol.HTTP);

    }

    @Override
    public ChannelPipelineFactory getPipelineFatory() {
        return new HttpServerPipelineFactory(this);
    }

}
